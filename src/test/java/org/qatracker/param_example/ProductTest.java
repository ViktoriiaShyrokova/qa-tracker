package org.qatracker.param_example;

import org.qatracker.model.Product;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

    // Плохо - дублирование кода
/*

    @Test void negativePrice1() {
        assertThrows("....", () -> new Product("X", -1.0, 5));
    }
    @Test void negativePrice2() {
        assertThrows("....", () -> new Product("X", -100.0, 5));
    }
    @Test void negativePrice3() {
        assertThrows("....", () -> new Product("X", -0.01, 5));
    }
*/

public class ProductTest {

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.5, -0.01, -999.99})
    void negativePriceShouldThrowException(double price) {
        assertThrows(IllegalArgumentException.class, () -> new Product("X", price, 5));
    }
}
