package com.yandex.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TskHandleTest {
    HttpTaskServer server;
    Gson gson;
    TaskManager manager;
    HttpClient client;

    @BeforeEach
    void startServer() {
        server = new HttpTaskServer();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, Adapters.localDateTimeAdapter)
                .serializeNulls()
                .create();
        server.start();

        client = HttpClient.newHttpClient();
        manager = server.getManager();
        manager.removeTasks();
    }

    @AfterEach
    void stopServer(){
        server.stop();
    }

    @Test
    void GET(){

        manager.addTask(new Task("Таск 1", "Описание 1", 30, "01.01.2025 00:00"));
        manager.addTask(new Task("Таск 2", "Описание 2", 30, "01.01.2025 01:00"));
        manager.addTask(new Task("Таск 3", "Описание 3", 30, "01.01.2025 02:00"));

        HttpResponse<String> response = TestUtils.get(client, "/tasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getTasks()), response.body());

        final Integer epicId  = manager.addEpic(new Epic("Епик 1", "Описание 1"));

        LocalDateTime l = LocalDateTime.parse("01.01.2025 03:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        manager.addSubtask(new Subtask("Сабтаск 1", "Описание 1", 30, "01.01.2025 03:00",  epicId));
        manager.addSubtask(new Subtask("Сабтаск 2", "Описание 2", 30, "01.01.2025 04:00", epicId));
        manager.addSubtask(new Subtask("Сабтаск 3", "Описание 3", 30, "01.01.2025 05:00", epicId));

        response = TestUtils.get(client, "/epics");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpics()), response.body());

        response = TestUtils.get(client, "/subtasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getSubtasks()), response.body());
    }
}


