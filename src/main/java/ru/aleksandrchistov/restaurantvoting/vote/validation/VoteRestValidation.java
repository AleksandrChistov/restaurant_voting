package ru.aleksandrchistov.restaurantvoting.vote.validation;

import lombok.experimental.UtilityClass;
import ru.aleksandrchistov.restaurantvoting.common.error.IllegalRequestDataException;

import java.time.LocalTime;

@UtilityClass
public class VoteRestValidation {
    public static void assureItIsNotAfterEleven() {
        LocalTime now = LocalTime.now();
        LocalTime eleven = LocalTime.of(11, 0);
        if (now.isAfter(eleven)) {
            throw new IllegalRequestDataException("It's too late, vote can't be changed");
        }
    }
}
