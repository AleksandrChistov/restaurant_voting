package ru.aleksandrchistov.restaurantvoting.common.error;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}