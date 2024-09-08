package ru.aleksandrchistov.restaurantvoting.error;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(msg);
    }
}