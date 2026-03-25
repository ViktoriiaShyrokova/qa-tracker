package org.qatracker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Smoke — маркер для smoke-тестов (быстрая проверка основного функционала).
// RetentionPolicy.RUNTIME — аннотация видна во время выполнения (через рефлексию).
// Target(METHOD) — можно применять только к методам.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Smoke {
    // Маркер без элементов - просто метка
}
