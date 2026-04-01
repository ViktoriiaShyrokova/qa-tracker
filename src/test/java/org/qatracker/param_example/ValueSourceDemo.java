package org.qatracker.param_example;

import org.qatracker.model.Product;
import org.qatracker.util.HttpUtils;
import org.qatracker.validation.InputValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ValueSourceDemo {

    // String
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void blankStringShouldBeInvalid(String input) {
        assertFalse(
                InputValidator.isValidName(input),
                "Expected '" + input + "' to be invalid");
    }

    // int
    @ParameterizedTest
    @ValueSource(ints = {200, 201, 204, 404})
    void httpStatusAreKnown(int code) {
        assertNotEquals("Unknown Status", HttpUtils.getStatusDescription(code));
    }

    // double
    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.5, -0.01, -999.99})
    void negativePriceShouldBeInvalid(double price) {
        assertThrows(IllegalArgumentException.class, () -> new Product("X", price, 5));
    }

    // boolean
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void productCanBeCreatedWithAnyStockStatus(boolean inStock) {
        int stock = inStock ? 10 : 0;

        Product p = new Product("Test", 100.0, stock);
        assertEquals(inStock, p.isInStock());
    }

    @ParameterizedTest
    @CsvSource({
            "'user@example.com',    true",
            "'non-an-email',        false",
            "'',                    false",
            "'test@test.org',       true",
    })
    void emailValidation(String email, boolean expectedValid) {
        assertEquals(expectedValid, InputValidator.isValidEmail(email));
    }

    @ParameterizedTest(name = "discount {0}% on price {1} -> expected {2}")
    @CsvSource({
            "10, 100.0, 90.0",
            "25, 200.0, 150.0",
            "50, 80.0, 40.0"
    })
    void discountWithDisplayName(double percent, double price, double expected) {
        Product product = new Product("Item", price, 1);
        product.applyDiscount(percent);
        assertEquals(expected, product.getPrice(), 0.001);
    }
}

