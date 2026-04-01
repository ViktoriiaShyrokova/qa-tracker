package org.qatracker.tdd;

import org.junit.jupiter.api.Test;
import org.qatracker.model.Calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    public void shouldReturnSumOfTwoNumbers(){
        // AAA - Arange, Act, Assert

        //Arrange
        Calculator calculator = new Calculator();

        //Act
        int result = calculator.add(2,3);

        //Assert
        assertEquals(5, result, "Sum should be 5");
    }
}
