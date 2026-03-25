package org.qatracker.exception;
// Checked исключение: компилятор потребует обработки.
// Используется при парсинге CSV в занятии 12.
public class InvalidTestDataException extends Exception {

    private final String rawData;

    public InvalidTestDataException(String rawData, String reason) {
        super("Invalid test data '" + rawData + "': " + reason);
        this.rawData = rawData;
    }

    public InvalidTestDataException(String rawData, String reason, Throwable cause) {
        super("Invalid test data '" + rawData + "': " + reason, cause);
        this.rawData = rawData;
    }

    public String getRawData() { return rawData; }
}