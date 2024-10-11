package ru.aleksandrchistov.restaurantvoting.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.aleksandrchistov.restaurantvoting.AbstractControllerTest;
import ru.aleksandrchistov.restaurantvoting.common.util.JsonUtil;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;
import ru.aleksandrchistov.restaurantvoting.menu.repository.MenuRepository;
import ru.aleksandrchistov.restaurantvoting.restaurant.RestaurantTestData;
import ru.aleksandrchistov.restaurantvoting.restaurant.model.Restaurant;
import ru.aleksandrchistov.restaurantvoting.restaurant.repository.RestaurantRepository;
import ru.aleksandrchistov.restaurantvoting.user.UserTestData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aleksandrchistov.restaurantvoting.restaurant.RestaurantTestData.*;
import static ru.aleksandrchistov.restaurantvoting.restaurant.web.RestaurantController.ADMIN_REST_URL;
import static ru.aleksandrchistov.restaurantvoting.restaurant.web.RestaurantController.USER_REST_URL;

class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private MenuRepository menuRepository;

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

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInconsistentId() throws Exception {
        Restaurant updated = new Restaurant(2, "KFC", Collections.emptySet());
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + "/" + RestaurantTestData.KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_REST_URL + "/" + RestaurantTestData.KFC_ID))
                .andExpect(status().isNoContent());
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(repository.findAll(), getMcDonalds());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllWithMenuBetweenDate() throws Exception {
        MenuItem newMenu1 = new MenuItem(null, "Coffee", 2500L, KFC_ID);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        newMenu1.setAddedToRestaurant(yesterday);
        MenuItem newMenu2 = new MenuItem(null, "Coffee2", 3500L, KFC_ID);

        Restaurant expected = getKFC();
        expected.setMenu(Collections.singletonList(newMenu1));

        menuRepository.save(newMenu1);
        menuRepository.save(newMenu2);

        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "/with-menu?startDate=" + yesterday + "&endDate=" + yesterday))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(List.of(expected)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllWithTodayMenu() throws Exception {
        MenuItem newMenu1 = new MenuItem(null, "Coffee", 2500L, KFC_ID);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        newMenu1.setAddedToRestaurant(yesterday);
        MenuItem newMenu2 = new MenuItem(null, "Coffee2", 3500L, KFC_ID);

        Restaurant expected = getKFC();
        expected.setMenu(Collections.singletonList(newMenu2));

        menuRepository.save(newMenu1);
        menuRepository.save(newMenu2);

        perform(MockMvcRequestBuilders.get(USER_REST_URL + "/with-menu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(List.of(expected)));
    }
}
