package ru.aleksandrchistov.restaurantvoting.common.error;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg);
    }
}