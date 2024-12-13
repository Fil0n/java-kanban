package com.yandex.app.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;

public class HttpTaskServer {
    private static final int PORT = 9080;
    private static HttpServer httpServer = null;

    public static void main(String[] args){
        serverStart();
    }

    public static void serverStart() {
        try {
            httpServer = HttpServer.create();
            TaskManager manager = Managers.getDefault();

            Task task = new Task("Таск 1", "Описание 1", 60, "31.12.2024 00:00");
            final Integer taskId = manager.addTask(task);
            final Task savedTask = manager.getTaskById(taskId);

            Task task2 = new Task("Таск 2", "Описание 2", 60, "31.12.2024 03:00");
            final Integer task2Id = manager.addTask(task2);
            final Task savedTask2 = manager.getTaskById(task2Id);

            Task task3 = new Task("Таск 3", "Описание 3", 60, "31.12.2024 00:30");
            final Integer task3Id = manager.addTask(task3);
            final Task savedTask3 = task3Id != null ? manager.getTaskById(task3Id) : null; //Этот таск будет исключен

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

    class TaskListTypeToken extends TypeToken<List<Task>> {}

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime LocalDateTime) throws IOException {
            if(LocalDateTime == null){
                jsonWriter.value("");
                return;
            }
            jsonWriter.value(LocalDateTime.format(timeFormatter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.toString(), timeFormatter);
        }
    }
}
