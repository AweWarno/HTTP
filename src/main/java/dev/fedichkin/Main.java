package dev.fedichkin;

public class Main {
    public static void main(String[] args) {
        int port = 9999;
        Server server = new Server(port);
        server.startServer();
        System.out.println("Сервер запущен");
    }
}

