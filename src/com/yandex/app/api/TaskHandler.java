package com.yandex.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TaskHandler extends BaseHandler implements HttpHandler {
    private final TaskManager manager;

    TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, Adapters.localDateTimeAdapter)
                .serializeNulls()
                .create();

        String type = pathParts.length >= 2 ? pathParts[1] : null;
        Integer id = pathParts.length >= 3 ? Integer.parseInt(pathParts[2]) : null;
        Boolean getEpicSubtasks = type.equals("epics") && pathParts.length == 4 && pathParts[3].equals("subtasks");

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
            }
            case "DELETE": {
                if(id == null) {
                    BaseHandler.sendNotFound(exchange);
                    return;
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
            }
            case "POST": {
                switch (type) {
                    case "tasks": {
                        try {
                            String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Task task = gson.fromJson(request, Task.class);
                            if (task.getId() == 0) {
                                if (manager.addTask(task) != null) {
                                    BaseHandler.sendCreated(exchange);
                                } else {
                                    BaseHandler.sendNonAcceptable(exchange);
                                }
                            } else {
                                manager.updateTask(task);
                                BaseHandler.sendCreated(exchange);
                            }
                        } catch (IOException e) {
                            System.out.printf("Что-то пошло не так\n\t%s%n", e.getMessage());
                        }
                        return;
                    }
                    case "epics": {
                        try {
                            String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Epic epic = gson.fromJson(request, Epic.class);
                            if (epic.getId() == 0) {
                                if (manager.addEpic(epic) != null) {
                                    BaseHandler.sendCreated(exchange);
                                } else {
                                    BaseHandler.sendNonAcceptable(exchange);
                                }
                            } else {
                                manager.updateEpic(epic);
                                BaseHandler.sendCreated(exchange);
                            }
                        } catch (IOException e) {
                            System.out.printf("Что-то пошло не так\n\t%s%n", e.getMessage());
                        }
                        return;
                    }
                    case "subtasks": {
                        try {
                            String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Subtask subtask = gson.fromJson(request, Subtask.class);
                            if (subtask.getId() == 0) {
                                if (manager.addSubtask(subtask) != null) {
                                    BaseHandler.sendCreated(exchange);
                                } else {
                                    BaseHandler.sendNonAcceptable(exchange);
                                }
                            } else {
                                manager.updateSubtask(subtask);
                                BaseHandler.sendCreated(exchange);
                            }
                        } catch (IOException e) {
                            System.out.printf("Что-то пошло не так\n\t%s%n", e.getMessage());
                        }
                        return;
                    }
                    default: {
                        BaseHandler.sendNotFound(exchange);
                        return;
                    }
                }
            }
            default: {
                BaseHandler.sendNotFound(exchange);
            }
        }
    }
}
