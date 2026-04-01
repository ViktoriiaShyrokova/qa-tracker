package org.qatracker.stream_example;

import org.qatracker.model.Product;
import org.qatracker.validation.InputValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MethodSourceDemo {
    // Источник данных - статический метод
    static Stream<Arguments> validEmailProvider() {
        return Stream.of(
                Arguments.of("user@example.com",    true, "standard email"),
                Arguments.of("user+tag@mail.com",   true, "email with plus"),
                Arguments.of("non-an-email",        false, "missing @"),
                Arguments.of("",                    false, "empty string"),
                Arguments.of("test@",               false, "missing domain")
        );
    }
    @ParameterizedTest(name = "{2}: {0} -> {1}")
    @MethodSource("validEmailProvider")
    void emailValidationTest(String email, boolean expectedValid, String description) {
        assertEquals(expectedValid, InputValidator.isValidEmail(email));
    }

    // Источник данных - объекты
    static Stream<Arguments> productProvider() {
        return Stream.of(
                Arguments.of(new Product("Laptop", 1000.0, 5), true, 1000.0),
                Arguments.of(new Product("Phone", 500.0, 0), false, 500.0),
                Arguments.of(new Product("Watch", 200.0, 1), true, 200.0)
        );
    }

    @ParameterizedTest(name = "Product {0}")
    @MethodSource("productProvider")
    void productTest(Product product, boolean inStock, double price) {
        assertEquals(inStock, product.isInStock());
        assertEquals(price, product.getPrice(), 0.001);
    }
}
