package org.example;


import org.example.handler.ClassicHandler;
import org.example.handler.FileHandler;
import org.example.handler.MessageHandler;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        int threadsNumber = 64;

        Server server = new Server(port, threadsNumber);
        server.addHandler(new ClassicHandler());
        server.addHandler(new FileHandler());
        server.addHandler(new MessageHandler());

        server.listen();
    }
}