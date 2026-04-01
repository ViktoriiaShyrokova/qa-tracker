package org.qatracker.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.qatracker.validation.PasswordValidator;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidationTest {

    /*
    Требования к PasswordValidator.isValid(String password):
    Минимум 8 символов
    Хотя бы одна заглавная буква
    Хотя бы одна цифра
    Не null и не пустой
     */

//
    @Test
    public void nullPasswordShouldBeInvalid(){
        assertFalse(PasswordValidator.isValid(null));
    }
    @Test
    public void emptyPasswordShouldBeInvalid(){
        assertFalse(PasswordValidator.isValid(""));
    }

    @Test
    public void PasswordWithSpacesShouldBeInvalid(){
        assertFalse(PasswordValidator.isValid("  "));
    }


    @Test
    public void passwordShouldContainMinEightSymbols(){
       // assertEquals(true, "123".length()>=8);

        assertEquals(false,PasswordValidator.isValid("123"), "Expected true, got false");
    }

    @Test
    public void passwordShouldContainAtLeastOneUpper(){
        assertEquals(false, PasswordValidator.isValid("password"));
    }
    @Test
    public void passwordShouldContainAtLeastOneLower(){
        assertEquals(false, PasswordValidator.isValid("PASSWORD"));
    }

    @Test
    public void passwordShouldContainAtLeastOneDigit(){
        assertEquals(false,PasswordValidator.isValid("Password"));
    }


    static Stream<Arguments> invalidPasswords() {
        return Stream.of(
                Arguments.of(null,          "null"),
                Arguments.of("",            "empty"),
                Arguments.of("Short1",      "too short (6 chars)"),
                Arguments.of("alllowercase1","no uppercase"),
                Arguments.of("ALLUPPERCASE1","no lowercase — should be VALID?"),
                Arguments.of("SecurePass",  "no digit"),
                Arguments.of("12345678",    "no letter")
        );
    }

    @ParameterizedTest(name = "{1} -> {0}")
    @MethodSource("invalidPasswords")
    public void invalidPasswordShouldReturnFalse(String password,String message){
        assertFalse(PasswordValidator.isValid(password),message);
    }

//    @Test
//    public void passwordShouldThrow(){
//        assertThrows();
//    }
}
