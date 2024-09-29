package ru.aleksandrchistov.restaurantvoting.vote.web;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.aleksandrchistov.restaurantvoting.AbstractControllerTest;
import ru.aleksandrchistov.restaurantvoting.common.util.JsonUtil;
import ru.aleksandrchistov.restaurantvoting.user.UserTestData;
import ru.aleksandrchistov.restaurantvoting.vote.VoteTestData;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;
import ru.aleksandrchistov.restaurantvoting.vote.repository.VoteRepository;

import java.time.LocalTime;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aleksandrchistov.restaurantvoting.vote.web.VoteController.REST_URL;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void update() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        LocalTime zeroLocalTime = LocalTime.of(0, 0);
        try (MockedStatic<LocalTime> mock = mockStatic(LocalTime.class, CALLS_REAL_METHODS)) {
            mock.when(LocalTime::now).thenReturn(zeroLocalTime);
            perform(MockMvcRequestBuilders.post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            VoteTestData.VOTE_MATCHER.assertMatch(repository.getExisted(VoteTestData.VOTE_ID), updated);
        }
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateInvalid() throws Exception {
        Vote updated = new Vote(null, 1, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        LocalTime zeroLocalTime = LocalTime.of(11, 1);
        try (MockedStatic<LocalTime> mock = mockStatic(LocalTime.class, CALLS_REAL_METHODS)) {
            mock.when(LocalTime::now).thenReturn(zeroLocalTime);
            perform(MockMvcRequestBuilders.post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }
    }
}
