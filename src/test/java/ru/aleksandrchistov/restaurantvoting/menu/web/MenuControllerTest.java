package ru.aleksandrchistov.restaurantvoting.menu.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.aleksandrchistov.restaurantvoting.AbstractControllerTest;
import ru.aleksandrchistov.restaurantvoting.common.util.JsonUtil;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;
import ru.aleksandrchistov.restaurantvoting.menu.repository.MenuRepository;
import ru.aleksandrchistov.restaurantvoting.user.UserTestData;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aleksandrchistov.restaurantvoting.menu.MenuTestData.MENU_MATCHER;
import static ru.aleksandrchistov.restaurantvoting.menu.MenuTestData.getNew;
import static ru.aleksandrchistov.restaurantvoting.menu.web.MenuController.ADMIN_RESTAURANT_REST_URL;
import static ru.aleksandrchistov.restaurantvoting.menu.web.MenuController.MENU_REST_URL;

class MenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void create() throws Exception {
        List<MenuItem> newMenu = getNew();
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANT_REST_URL + "/1" + MENU_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        MENU_MATCHER.assertMatch(repository.getAllByRestaurantId(1), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        List<MenuItem> newMenu = getNew().stream().peek(m -> m.setName(null)).toList();
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANT_REST_URL + "/1" + MENU_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}