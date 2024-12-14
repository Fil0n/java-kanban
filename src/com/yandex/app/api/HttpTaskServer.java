package com.yandex.app.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer = null;
    private static final TaskManager manager = Managers.getDefault();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            httpServer = HttpServer.create();

            httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
            httpServer.createContext("/", new TaskHandler(manager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static void stop() {
        httpServer.stop(0);
    }

    public TaskManager getManager() {
        return manager;
    }
}
