package dev.fedichkin;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(socket));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void handleConnection(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.split(" ").length != 3) {
                socket.close();
                return;
            }

            String[] requestParts = requestLine.split(" ")[1].split("\\?", 2);
            String path = requestParts[0];
            Request request = new Request(path);
            if (requestParts.length > 1) {
                String[] queryParams = requestParts[1].split("&");
                for (String param : queryParams) {
                    String[] keyValue = param.split("=", 2);
                    if (keyValue.length == 2) {
                        request.addQueryParam(keyValue[0], keyValue[1]);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void sendNotFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void handleClassicPage(Path filePath, BufferedOutputStream out) throws IOException {
        String template = Files.readString(filePath);
        String content = template.replace("{time}", LocalDateTime.now().toString());
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + content.getBytes().length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content.getBytes());
        out.flush();
    }

    private void sendFile(Path filePath, BufferedOutputStream out) throws IOException {
        String mimeType = Files.probeContentType(filePath);
        long length = Files.size(filePath);
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }

    public void addHandler(String method, String path, Handler handler) {

    }
}