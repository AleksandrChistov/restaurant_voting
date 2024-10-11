package ru.aleksandrchistov.restaurantvoting.vote.web;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.aleksandrchistov.restaurantvoting.AbstractControllerTest;
import ru.aleksandrchistov.restaurantvoting.common.util.JsonUtil;
import ru.aleksandrchistov.restaurantvoting.user.UserTestData;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;
import ru.aleksandrchistov.restaurantvoting.vote.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aleksandrchistov.restaurantvoting.vote.VoteTestData.*;
import static ru.aleksandrchistov.restaurantvoting.vote.web.VoteController.*;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void create() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);

        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createInvalid() throws Exception {
        Vote newVote = new Vote(null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void update() throws Exception {
        Vote updated = getUpdated();
        LocalTime zeroLocalTime = LocalTime.of(0, 0);
        try (MockedStatic<LocalTime> mock = mockStatic(LocalTime.class, CALLS_REAL_METHODS)) {
            mock.when(LocalTime::now).thenReturn(zeroLocalTime);
            perform(MockMvcRequestBuilders.put(REST_URL + "/" + USER_VOTE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
        VOTE_MATCHER.assertMatch(repository.getExisted(USER_VOTE_ID), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateInvalid() throws Exception {
        Vote updated = new Vote(null, 1, null);
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + USER_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        Vote updated = getUpdated();
        LocalTime afterStopVoteHours = LocalTime.of(STOP_VOTE_HOURS, 1);
        try (MockedStatic<LocalTime> mock = mockStatic(LocalTime.class, CALLS_REAL_METHODS)) {
            mock.when(LocalTime::now).thenReturn(afterStopVoteHours);
            perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void findAllByAuthUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(USER_VOTE)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void findByAuthUserIdAndDateNow() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(USER_VOTE)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void findAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "?userId=" + USER_VOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(USER_VOTE)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void findAllByCreatedAt() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "?createdAt=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(USER_VOTE, ADMIN_VOTE)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void findAllByUserIdAndCreatedAt() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "?userId=" + USER_VOTE_ID + "&createdAt=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(USER_VOTE)));
    }

}
