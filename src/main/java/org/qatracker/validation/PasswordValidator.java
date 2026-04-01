package org.qatracker.validation;

public class PasswordValidator {

    public static boolean isValid(String password){
        if(
           password == null || password.isBlank()) return false;
        if(
           password.length() < 8)  return false;
        if(!hasUpper(password)) return false;
        if(!hasLower(password)) return false;
        if(!hasDigit(password)) return false;
        return true;
    }

    public static boolean hasUpper(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }
    public static boolean hasLower(String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }

    public static boolean hasDigit(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

}
