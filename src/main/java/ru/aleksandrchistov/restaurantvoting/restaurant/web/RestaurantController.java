package ru.aleksandrchistov.restaurantvoting.restaurant.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.aleksandrchistov.restaurantvoting.restaurant.model.Restaurant;
import ru.aleksandrchistov.restaurantvoting.restaurant.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation.assureIdConsistent;
import static ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation.checkNew;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    private final Logger log = getLogger(RestaurantController.class);

    static final String ADMIN_REST_URL = "/api/admin/restaurants";
    static final String USER_REST_URL = "/api/restaurants";

    @Autowired
    private RestaurantRepository repository;

    @PostMapping(value = ADMIN_REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_REST_URL + "/{restaurantId}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = ADMIN_REST_URL + "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int restaurantId) {
        log.info("update {} with restaurantId={}", restaurant, restaurantId);
        assureIdConsistent(restaurant, restaurantId);
        repository.save(restaurant);
    }

    @GetMapping(ADMIN_REST_URL + "/{restaurantId}")
    public Restaurant get(@PathVariable int restaurantId) {
        log.info("get");
        return repository.getExisted(restaurantId);
    }

    @DeleteMapping(ADMIN_REST_URL + "/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public void delete(@PathVariable int restaurantId) {
        log.info("delete");
        repository.deleteById(restaurantId);
    }

    @GetMapping(ADMIN_REST_URL)
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }

    @GetMapping(USER_REST_URL + "/with-menu")
    @Cacheable("restaurants_with_menu")
    public List<Restaurant> getAllWithTodayMenu() {
        log.info("getAllWithMenu");
        return repository.findAllWithTodayMenu();
    }

}
