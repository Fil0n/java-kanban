package com.yandex.app.api;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHandler {
    public static void sendText(HttpExchange exchange, String text) throws IOException {
        if (text.equals("null")) {
            sendNotFound(exchange);
        } else {
            sendResponse(exchange, 200, text);
        }
    }

    public static void sendNotFound(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 404, "{\"message\":\"Not Found\"}");
    }

    public static void sendDeleted(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200, "{\"message\":\"Deleted\"}");
    }

    public static void sendBadRequest(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 400, "{\"message\":\"Bad Request\"}");
    }

    public static void sendServerError(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 500, "{\"message\":\"Server Error\"}");
    }

    public static void sendCreated(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 201, "{\"message\":\"Created\"}");
    }

    public static void sendUpdated(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 202, "{\"message\":\"Updated\"}");
    }

    public static void sendNonAcceptable(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 406, "{\"message\":\"Not Acceptable\"}");
    }

    private static void sendResponse(HttpExchange exchange, int status, String text) throws IOException{
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(status, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
