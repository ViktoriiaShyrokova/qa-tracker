package org.qatracker.util;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    // Хранилище кодов и их текстовых описаний
    private static final Map<Integer, String> STATUS_DESCRIPTIONS = new HashMap<>();

    static {
        // 1xx Informational (Информационные)
        STATUS_DESCRIPTIONS.put(100, "Continue");
        STATUS_DESCRIPTIONS.put(101, "Switching Protocols");

        // 2xx Success (Успешно)
        STATUS_DESCRIPTIONS.put(200, "OK");
        STATUS_DESCRIPTIONS.put(201, "Created");
        STATUS_DESCRIPTIONS.put(202, "Accepted");
        STATUS_DESCRIPTIONS.put(204, "No Content");

        // 3xx Redirection (Перенаправление)
        STATUS_DESCRIPTIONS.put(301, "Moved Permanently");
        STATUS_DESCRIPTIONS.put(302, "Found");
        STATUS_DESCRIPTIONS.put(304, "Not Modified");

        // 4xx Client Error (Ошибка клиента)
        STATUS_DESCRIPTIONS.put(400, "Bad Request");
        STATUS_DESCRIPTIONS.put(401, "Unauthorized");
        STATUS_DESCRIPTIONS.put(403, "Forbidden");
        STATUS_DESCRIPTIONS.put(404, "Not Found");
        STATUS_DESCRIPTIONS.put(405, "Method Not Allowed");
        STATUS_DESCRIPTIONS.put(408, "Request Timeout");
        STATUS_DESCRIPTIONS.put(409, "Conflict");

        // 5xx Server Error (Ошибка сервера)
        STATUS_DESCRIPTIONS.put(500, "Internal Server Error");
        STATUS_DESCRIPTIONS.put(501, "Not Implemented");
        STATUS_DESCRIPTIONS.put(502, "Bad Gateway");
        STATUS_DESCRIPTIONS.put(503, "Service Unavailable");
        STATUS_DESCRIPTIONS.put(504, "Gateway Timeout");
    }

    // Приватный конструктор, чтобы предотвратить создание объектов утилитного класса
    private HttpUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Получить текстовое описание по HTTP коду.
     * @param statusCode HTTP код ответа
     * @return Описание статуса или "Unknown Status", если код не найден
     */
    public static String getStatusDescription(int statusCode) {
        return STATUS_DESCRIPTIONS.getOrDefault(statusCode, "Unknown Status");
    }

    /**
     * Проверяет, является ли статус успешным (2xx).
     */
    public static boolean isSuccessful(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Проверяет, является ли статус ошибкой на стороне клиента (4xx).
     */
    public static boolean isClientError(int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Проверяет, является ли статус ошибкой на стороне сервера (5xx).
     */
    public static boolean isServerError(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }

    /**
     * Проверяет, является ли статус перенаправлением (3xx).
     */
    public static boolean isRedirect(int statusCode) {
        return statusCode >= 300 && statusCode < 400;
    }
}