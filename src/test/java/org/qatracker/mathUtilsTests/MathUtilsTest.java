package org.qatracker.mathUtilsTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathUtilsTest {

    static Stream<Arguments> clamParametersProvider() {
        return Stream.of(
                Arguments.of(6, 5, 6, 7, "value less then min"),
                Arguments.of(30, 30, 20, 50, "value more then min and less then max"),
                Arguments.of(106, 200, 59, 106, "value more then max"),
                Arguments.of(10, 10, 10, 500, "value equals min"),
                Arguments.of(30, -50, 30, 30, "value equals max"),
                Arguments.of(40, 40, 40, 40, "all values are equal"),
                Arguments.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, "all values equal min int"),
                Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, "all values equal max"),
                Arguments.of(-500, -500, Integer.MIN_VALUE, Integer.MAX_VALUE, "min and max values equal int min and max")

        );
    }

    @ParameterizedTest
    @MethodSource("clamParametersProvider")
    public void clamIsCalculated(int expected, int value, int min, int max, String message) {
     //   assertTrue()
    }
}
