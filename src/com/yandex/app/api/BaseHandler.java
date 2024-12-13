package com.yandex.app.api;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHandler {
    public static void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"status\":404,\"message\":\"Not Found\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendDeleted(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"message\":\"Deleted\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendBadRequest(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"message\":\"Bad Request\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(400, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendServerError(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"message\":\"Server Error\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(500, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendCreated(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"message\":\"Created\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(201, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendUpdated(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"message\":\"Updated\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(202, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public static void sendNonAcceptable(HttpExchange exchange) throws IOException {
        byte[] resp = ("{\"message\":\"Not Acceptable\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
