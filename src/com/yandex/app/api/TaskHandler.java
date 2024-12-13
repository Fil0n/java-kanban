package com.yandex.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {
    private final TaskManager manager;

    TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
                .create();

        String type = pathParts.length >= 2 ? pathParts[1] : null;
        Integer id = pathParts.length >= 3 ? Integer.parseInt(pathParts[2]) : null;
        Boolean getEpicSubtasks = type == "epics" && pathParts.length == 4 && pathParts[3].equals("subtasks");

        switch (exchange.getRequestMethod()) {
            case "GET": {
                switch (type) {
                    case "tasks": {
                        BaseHandler.sendText(exchange, gson.toJson(id == null ? manager.getTasks() : manager.getTaskById(id)));
                        return;
                    }
                    case "epics": {
                        BaseHandler.sendText(exchange, gson.toJson(id == null ? manager.getEpics() : getEpicSubtasks ? manager.getEpicSubtasks(id) : manager.getEpicById(id)));
                        return;
                    }
                    case "subtasks": {
                        BaseHandler.sendText(exchange, gson.toJson(id == null ? manager.getSubtasks() : manager.getSubtaskById(id)));
                        return;
                    }
                    case "history": {
                        BaseHandler.sendText(exchange, gson.toJson(manager.getHistory()));
                        return;
                    }
                    case "prioritized": {
                        BaseHandler.sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));
                        return;
                    }
                    default: {
                        BaseHandler.sendNotFound(exchange);
                        return;
                    }
                }
                break;
            }
            case "DELETE": {
                if(id == null) {
                    BaseHandler.sendNotFound(exchange);
                }

                switch (type) {
                    case "tasks": {
                        manager.removeTaskById(id);
                        BaseHandler.sendDeleted(exchange);
                        return;
                    }
                    case "epics": {
                        manager.removeEpicById(id);
                        BaseHandler.sendDeleted(exchange);
                        return;
                    }
                    case "subtasks": {
                        manager.removeSubtaskById(id);
                        BaseHandler.sendDeleted(exchange);
                        return;
                    }
                    default: {
                        BaseHandler.sendNotFound(exchange);
                        return;
                    }
                }
                break;
            }
            case "POST": {
                if(pathParts.length == 2) {
                    String body;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                        body = reader.lines().collect(Collectors.joining());
                    }
                    gson = new GsonBuilder().create();
                    Task task = gson.fromJson(body, Task.class);
                    manager.addTask(task);
                } else if (pathParts.length == 3) {
                    manager.getTaskById(Integer.parseInt(pathParts[2]));
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
    }
}
