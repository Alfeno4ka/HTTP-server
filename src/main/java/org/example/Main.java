package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> validPaths = List.of(
                "/index.html",
                "/spring.svg",
                "/spring.png",
                "/resources.html",
                "/styles.css",
                "/app.js",
                "/links.html",
                "/forms.html",
                "/classic.html",
                "/events.html",
                "/events.js");
        int port = 8080;
        int threadsNumber = 64;

        Server server = new Server(port, threadsNumber, validPaths);
        server.listen();
    }
}