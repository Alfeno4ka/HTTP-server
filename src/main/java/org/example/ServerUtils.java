package org.example;



import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

public class ServerUtils {

    /**
     * Получить перечень квери-параметров из пути.
     *
     * @param path путь, переданный в запросе
     * @return список квери-параметров
     * @throws URISyntaxException
     */
    public static List<NameValuePair> getQueryParams(String path) throws URISyntaxException {
        return URLEncodedUtils.parse(new URI(path), Charset.defaultCharset());
    }

    /**
     * Получить заданный квери-параметр из путии запроса.
     *
     * @param path путь, переданный в запросе
     * @param name наименование квери-параметра
     * @return искомый кверипараметр
     * @throws URISyntaxException
     */
    public static Optional<NameValuePair> getQueryParam(String path, String name) throws URISyntaxException {
        List<NameValuePair> params = getQueryParams(path);
        return params.stream()
                .filter(param -> param.getName().equals(name))
                .findFirst();
    }

    public static String createOkResponse(String mimeType, long length) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + mimeType + "\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
    }

    public static String createNotFoundResponse() {
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 0\r\n" +
                "Connection: close\r\n" +
                "\r\n";
    }
}
