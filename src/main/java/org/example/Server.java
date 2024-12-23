package org.example;



import org.example.handler.Handler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ServerSocket serverSocket;
    private final ExecutorService workerThreadPool;
    private final List<Handler> handlers = new ArrayList<>();

    public Server(int port, int threadsNumber) {
        try {
            serverSocket = new ServerSocket(port);
            workerThreadPool = Executors.newFixedThreadPool(threadsNumber);
        } catch (IOException e) {
            throw new IllegalStateException("Port " + port + " is already used");
        }
    }

    public void listen() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                CompletableFuture.runAsync(() -> processRequest(socket), workerThreadPool);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Добавить обработчик.
     *
     * @param handler обработчик http-запроса
     */
    public void addHandler(Handler handler) {
        this.handlers.add(handler);
    }

    /**
     * Обработать конкретное подключение
     *
     * @param socket сокет подключения
     */
    private void processRequest(Socket socket) {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())) {
            String requestLine = in.readLine();
            String[] parts = requestLine.split(" ");
            if (parts.length != 3) {
                return;
            }

            //Выделяем из запроса HTTP-метод и путь, и определяем хендлер
            String method = parts[0];
            String path = parts[1];

            Optional<Handler> optionalHandler = resolveHandler(method, path);
            if (optionalHandler.isEmpty()) {
                //Если не нашли хендлер - откидываем 404 и завершаем обработку соединения
                out.write((ServerUtils.createNotFoundResponse()).getBytes());
                out.flush();
                return;
            }

            //Вызываем найденный хендлер
            optionalHandler.get().handle(method, path, out);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Определить обработчик запроса по его методу и пути
     * @param method метод запроса
     * @param path путь запроса
     * @return обарботчик
     */
    private Optional<Handler> resolveHandler(String method, String path) {
        return this.handlers.stream()
                .filter(handler -> handler.getMethod().equals(method)
                        && path.matches(handler.getPath()))
                .findFirst();
    }
}
