package ru.aleksandrchistov.restaurantvoting.menu.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;
import ru.aleksandrchistov.restaurantvoting.menu.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    private final Logger log = getLogger(MenuController.class);

    static final String ADMIN_REST_URL = "/api/admin/restaurants/{restaurantId}/menu";

    @Autowired
    private MenuRepository repository;

    @PostMapping(value = ADMIN_REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public void create(@Valid @RequestBody List<MenuItem> menu, @PathVariable int restaurantId) {
        log.info("create {} with restaurantId={}", menu, restaurantId);
        menu.forEach(RestValidation::checkNew);
        repository.deleteAllByDateAndRestaurantId(LocalDate.now(), restaurantId);
        repository.prepareAndSaveAll(menu, restaurantId);
    }
}
