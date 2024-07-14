package dev.fedichkin;

import com.sun.net.httpserver.Request;

import java.io.BufferedOutputStream;


public class Main {
    public static void main(String[] args) {
        final var server = new Server(9999);

        // добавление хендлеров (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {

            }
        });
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {

            }
        });

        server.startServer();
    }
}


