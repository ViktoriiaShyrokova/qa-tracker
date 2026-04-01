package org.qatracker.util;

import java.util.Map;
import java.util.Optional;

public class TypeUtils {

    private TypeUtils() {}

    /**
     * Безопасный cast: возвращает Optional.empty() вместо ClassCastException.
     */
    public static <T> Optional<T> safeCast(Object obj, Class<T> type) {
        if (type.isInstance(obj)) {
            return Optional.of(type.cast(obj));
        }
        return Optional.empty();
    }

    /**
     * Получить значение из Map<String, Object> нужного типа.
     * Возвращает defaultValue если ключа нет или тип не совпадает.
     */
    public static <T> T getOrDefault(Map<String, Object> map, String key,
                                      Class<T> type, T defaultValue) {
        return safeCast(map.get(key), type).orElse(defaultValue);
    }

    /**
     * Безопасное извлечение int из Object: Integer, Long, Double или String.
     */
    public static int extractInt(Object obj, int defaultValue) {
        if (obj instanceof Integer i) return i;
        if (obj instanceof Long    l) return l.intValue();
        if (obj instanceof Double  d) return d.intValue();
        if (obj instanceof String  s) {
            try { return Integer.parseInt(s.trim()); }
            catch (NumberFormatException e) { return defaultValue; }
        }
        return defaultValue;
    }
}
