package ru.aleksandrchistov.restaurantvoting.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.aleksandrchistov.restaurantvoting.AbstractControllerTest;
import ru.aleksandrchistov.restaurantvoting.common.util.JsonUtil;
import ru.aleksandrchistov.restaurantvoting.restaurant.RestaurantTestData;
import ru.aleksandrchistov.restaurantvoting.restaurant.model.Restaurant;
import ru.aleksandrchistov.restaurantvoting.restaurant.repository.RestaurantRepository;
import ru.aleksandrchistov.restaurantvoting.user.UserTestData;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aleksandrchistov.restaurantvoting.restaurant.web.RestaurantController.ADMIN_REST_URL;

class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RestaurantTestData.RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocationInvalid() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, null, Collections.emptySet());
        perform(MockMvcRequestBuilders.post(ADMIN_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + "/" + RestaurantTestData.KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(repository.getExisted(RestaurantTestData.KFC_ID), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant updated = new Restaurant(null, null, Collections.emptySet());
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + "/" + RestaurantTestData.KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
