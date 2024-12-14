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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskHandleTest {
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
        final Integer taskId = manager.addTask(new Task("Таск 1", "Описание 1", 30, "01.01.2025 00:00"));
        manager.addTask(new Task("Таск 2", "Описание 2", 30, "01.01.2025 01:00"));
        manager.addTask(new Task("Таск 3", "Описание 3", 30, "01.01.2025 02:00"));

        HttpResponse<String> response = TestUtils.get(client, "/tasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getTasks()), response.body());

        final Integer epicId  = manager.addEpic(new Epic("Епик 1", "Описание 1"));

        final Integer subtaskId = manager.addSubtask(new Subtask("Сабтаск 1", "Описание 1", 30, "01.01.2025 03:00",  epicId));
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

        response = TestUtils.get(client, "/epics/" + epicId + "/subtasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicSubtasks(epicId)), response.body());

        response = TestUtils.get(client, "/subtasks/"+subtaskId);
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getSubtaskById(subtaskId)), response.body());

        response = TestUtils.get(client, "/epics/"+epicId);
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicById(epicId)), response.body());

        response = TestUtils.get(client, "/tasks/"+taskId);
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getTaskById(taskId)), response.body());

        response = TestUtils.get(client, "/history");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getHistory()), response.body());

        response = TestUtils.get(client, "/prioritized");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body());
    }


    @Test
    void getWithoutTimeAndDuration(){
        final Integer taskId = manager.addTask(new Task("Таск 1", "Описание 1" ));
        manager.addTask(new Task("Таск 2", "Описание 2"));
        manager.addTask(new Task("Таск 3", "Описание 3"));

        HttpResponse<String> response = TestUtils.get(client, "/tasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getTasks()), response.body());

        final Integer epicId  = manager.addEpic(new Epic("Епик 1", "Описание 1"));

        final Integer subtaskId = manager.addSubtask(new Subtask("Сабтаск 1", "Описание 1",  epicId));
        manager.addSubtask(new Subtask("Сабтаск 2", "Описание 2",  epicId));
        manager.addSubtask(new Subtask("Сабтаск 3", "Описание 3",  epicId));

        response = TestUtils.get(client, "/epics");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpics()), response.body());

        response = TestUtils.get(client, "/subtasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getSubtasks()), response.body());

        response = TestUtils.get(client, "/epics/" + epicId + "/subtasks");
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicSubtasks(epicId)), response.body());

        response = TestUtils.get(client, "/subtasks/"+subtaskId);
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getSubtaskById(subtaskId)), response.body());

        response = TestUtils.get(client, "/epics/"+epicId);
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicById(epicId)), response.body());

        response = TestUtils.get(client, "/tasks/"+taskId);
        assertNotEquals(response, null);
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getTaskById(taskId)), response.body());
    }

    @Test
    void getWithWrongIDs(){
        final Integer taskId = manager.addTask(new Task("Таск 1", "Описание 1", 30, "01.01.2025 00:00"));

        final Integer epicId  = manager.addEpic(new Epic("Епик 1", "Описание 1"));

        final Integer subtaskId = manager.addSubtask(new Subtask("Сабтаск 1", "Описание 1", 30, "01.01.2025 03:00",  epicId));

        HttpResponse<String> response = TestUtils.get(client, "/tasks/" + taskId + 1);
        assertNotEquals(response, null);
        assertEquals(404, response.statusCode());
        assertEquals("{\"message\":\"Not Found\"}", response.body());

        response = TestUtils.get(client, "/epics/" + taskId + 1);
        assertNotEquals(response, null);
        assertEquals(404, response.statusCode());
        assertEquals("{\"message\":\"Not Found\"}", response.body());

        response = TestUtils.get(client, "/epics/" + taskId + 1 + "/subtasks");
        assertNotEquals(response, null);
        assertEquals(404, response.statusCode());
        assertEquals("{\"message\":\"Not Found\"}", response.body());

        response = TestUtils.get(client, "/subtasks" + subtaskId + 1);
        assertNotEquals(response, null);
        assertEquals(404, response.statusCode());
        assertEquals("{\"message\":\"Not Found\"}", response.body());
    }


}


