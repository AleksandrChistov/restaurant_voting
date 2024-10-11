package ru.aleksandrchistov.restaurantvoting.menu.web;

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
import ru.aleksandrchistov.restaurantvoting.user.UserTestData;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aleksandrchistov.restaurantvoting.menu.MenuTestData.*;
import static ru.aleksandrchistov.restaurantvoting.menu.web.MenuController.ADMIN_REST_URL;

class MenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void create() throws Exception {
        MenuItem newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        MenuItem created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);

        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(repository.getExisted(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuItem newMenu = new MenuItem(null, "", 0L, null);
        perform(MockMvcRequestBuilders.post(ADMIN_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(repository.getExisted(MENU1.getId()), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuItem updated = getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(ADMIN_REST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_REST_URL + "/1"))
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(repository.findAll(), MENU2);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(MENU1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByRestaurantId() throws Exception {
        MenuItem newMenu = new MenuItem(null, "Coffee", 2500L, 2);
        repository.save(newMenu);
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "?restaurantId=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(newMenu)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByAddedToRestaurant() throws Exception {
        MenuItem newMenu = new MenuItem(null, "Coffee", 2500L, 2);
        repository.save(newMenu);
        String now = LocalDate.now().toString();
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "?addedDate=" + now))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(newMenu)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByRestaurantIdAndAddedToRestaurant() throws Exception {
        MenuItem newMenu1 = new MenuItem(null, "Coffee", 2500L, 2);
        newMenu1.setAddedToRestaurant(LocalDate.of(2024, 10, 9));
        MenuItem newMenu2 = new MenuItem(null, "Coffee2", 3500L, 2);
        repository.save(newMenu1);
        repository.save(newMenu2);
        perform(MockMvcRequestBuilders.get(ADMIN_REST_URL + "?restaurantId=2&addedDate=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(newMenu2)));
    }
}