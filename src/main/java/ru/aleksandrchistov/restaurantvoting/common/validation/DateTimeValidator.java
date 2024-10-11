package ru.aleksandrchistov.restaurantvoting.common.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@AllArgsConstructor
public class DateTimeValidator {

    public static boolean checkItIsAfter(LocalTime time) {
        return LocalTime.now().isAfter(time);
    }

}
