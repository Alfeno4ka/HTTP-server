package org.example.handler;



import org.apache.http.NameValuePair;
import org.example.ServerUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageHandler extends Handler {

    private static final String PARAM_QUANTITY = "quantity";
    private static final String PARAM_ORDER = "order";
    private static final String PARAM_ORDER_REVERSE_VALUE = "reversed";
    private static final List<String> MESSAGES = List.of(
            "MESSAGE 1",
            "MESSAGE 2",
            "MESSAGE 3",
            "MESSAGE 4",
            "MESSAGE 5",
            "MESSAGE 6",
            "MESSAGE 7",
            "MESSAGE 8",
            "MESSAGE 9",
            "MESSAGE 10",
            "MESSAGE 11",
            "MESSAGE 12",
            "MESSAGE 13",
            "MESSAGE 14",
            "MESSAGE 15");

    public MessageHandler() {
        this.method = "GET";
        this.path = "^(/messages)\\??([a-zA-Z]+\\=[a-zA-Z0-9]+(\\&)?)*$"; //messages с квери-параметрами
    }

    @Override
    public void handle(String method, String path, BufferedOutputStream out) throws URISyntaxException, IOException {
        //Извлекаем квери-параметры из запроса, преобразуем их в Map для удобства
        List<NameValuePair> queryParamsParsed = ServerUtils.getQueryParams(path);
        Map<String, String> params = queryParamsParsed.stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

        //Из квери-параметра "quantity" получаем количкество сообщений, которые надо отобразить. Если не задано, возвращаем все.
        int messagesToReturn = Objects.nonNull(params.get(PARAM_QUANTITY))
                ? Integer.parseInt(params.get(PARAM_QUANTITY))
                : MESSAGES.size();

        //Из квери-параметра "order" получаем порядок сортировки. Если "reversed", то возвращем в обратном порядке.
        boolean isReversed = Objects.equals(params.get(PARAM_ORDER), PARAM_ORDER_REVERSE_VALUE);

        //Создаем стрим. Лимитируем сообщения по количеству
        Stream<String> contentStream = MESSAGES.stream()
                .limit(messagesToReturn);

        //Если задан обратный порядок - сортируем
        if (isReversed) {
            contentStream = contentStream.sorted(Comparator.reverseOrder());
        }

        //Добавляем переносы - сводим в одну строку
        byte[] content = contentStream
                .map(message -> message + "\r\n")
                .collect(Collectors.joining()).getBytes();

        out.write((ServerUtils.createOkResponse("text/plain", content.length)).getBytes());
        out.write(content);
        out.flush();
    }
}
