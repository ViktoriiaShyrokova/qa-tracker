package org.qatracker.validation;

import java.util.regex.Pattern;

public class InputValidator {

    // Регулярное выражение: разрешаем только буквы (латиница/кириллица), пробелы и дефисы
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\s\\-]+$");

    // Практичное регулярное выражение для email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Вариант 1: Продвинутая валидация имени (пройдет твой тест + проверит символы)
     * @param input строка для проверки
     * @return true, если имя валидно
     */
    public static boolean isValidName(String input) {
        // 1. Проверяем на null и на то, состоит ли строка только из пробелов/табов/переносов
        // Метод isBlank() появился в Java 11. Если у тебя Java 8, используй: input.trim().isEmpty()
        if (input == null || input.isBlank()) {
            return false;
        }

        // 2. Проверяем длину (например, имя не может состоять из 1 буквы)
        if (input.trim().length() < 2) {
            return false;
        }

        // 3. Проверяем, что в имени нет цифр и спецсимволов
        return NAME_PATTERN.matcher(input).matches();
    }

    /**
     * Вариант 2: Простейшая валидация (только чтобы пройти твой конкретный тест)
     * Если тебе пока не нужны проверки на цифры и длину.
     */
    public static boolean isSimpleValidName(String input) {
        return input != null && !input.isBlank();
    }

    /**
     * Email
     */
    public static boolean isValidEmail(String email) {
        // Проверяем на null и пустоту, чтобы избежать NullPointerException
        if (email == null || email.isBlank()) {
            return false;
        }

        // Проверяем соответствие регулярному выражению
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Age
     */
    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 67;
    }

    /**
     * Password
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasSpec = password.chars()
                .anyMatch(ch -> !Character.isLetterOrDigit(ch));
        return hasUpper && hasDigit && hasLower && hasSpec;
    }

    /**
     * Clear
     */
    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim().toLowerCase();
    }


}