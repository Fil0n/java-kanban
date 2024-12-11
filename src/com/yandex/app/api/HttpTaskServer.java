package com.yandex.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.model.Task;
import com.yandex.app.service.FileBackedTaskManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer = null;

    public static void main(String[] args){
        serverStart();
    }

    public static void serverStart() {
        try {
            httpServer = HttpServer.create();
            FileBackedTaskManager manager = new FileBackedTaskManager();

            httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
            httpServer.createContext("/", new TaskHandler(manager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static void stopServer(){
        httpServer.stop(0);
    }

    static class HelloHandler implements HttpHandler {


        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /hello запроса от клиента.");

            String response = "Hey! Glad to see you on our server.";
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class TaskHandler implements HttpHandler {
        private final FileBackedTaskManager manager;

        TaskHandler(FileBackedTaskManager manager) {
            this.manager = manager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /day запроса от клиента.");
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            Gson gson = new GsonBuilder().create();
            String response = "";

            switch (exchange.getRequestMethod()) {
                case "GET": {
                    switch (pathParts.length) {
                        case 2: {
                            switch (pathParts[1]) {
                                case "tasks": {
                                    response = gson.toJson(manager.getTasks());
                                    break;
                                }
                                case "epics": {
                                    response = gson.toJson(manager.getEpics());
                                    break;
                                }
                                case "subtasks": {
                                    response = gson.toJson(manager.getSubtasks());
                                    break;
                                }
                                case "history": {
                                    response = gson.toJson(manager.getHistory());
                                    break;
                                }
                                case "prioritized": {
                                    response = gson.toJson(manager.());
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                        }
                    }
                    if(pathParts.length == 2) {
                        manager.getTasks();
                    } else if (pathParts.length == 3) {
                        manager.getTaskById(Integer.parseInt(pathParts[2]));
                    } else {
                        //Ошибка!!! 404
                    }
                    break;
                }
                case "POST": {
                    if(pathParts.length == 2) {
                        String body;
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                            body = reader.lines().collect(Collectors.joining());
                        }
                        Gson gson = new GsonBuilder().create();
                        Task task = gson.fromJson(body, Task.class);
                        manager.addTask(task);
                    } else if (pathParts.length == 3) {
                        manager.getTaskById(Integer.parseInt(pathParts[2]));
                    } else {
                        //Ошибка!!! 404
                    }

                    break;
                }
                case "DELETE": {
                    if(pathParts.length == 3) {
                        manager.removeTaskById(Integer.parseInt(pathParts[2]));
                    } else {
                        //Ошибка!!! 404
                    }
                    break;
                }
                default: {
                    //Ошибка!!! 404
                    break;
                }
            }
            exchange.sendResponseHeaders(200, 0);
            String response = "Hey! Glad to see you on our server.";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
