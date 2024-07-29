package dev.fedichkin;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        final var server = new Server(9999);

        // добавление хендлеров (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {

            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                String path = request.getPath();
                String paramValue = request.getQueryParam("paramName");
                // Обработка запроса
                System.out.println("Path: " + path + ", Param: " + paramValue);
                responseStream.write(("Path: " + path + ", Param: " + paramValue).getBytes());
                responseStream.flush();
            }
        });

        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                String path = request.getPath();
                String paramValue = request.getQueryParam("paramName");
                // Обработка запроса
                System.out.println("Path: " + path + ", Param: " + paramValue);
                responseStream.write(("Path: " + path + ", Param: " + paramValue).getBytes());
                responseStream.flush();
            }
        });

        server.startServer();
    }
}