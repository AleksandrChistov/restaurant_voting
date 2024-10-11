package ru.aleksandrchistov.restaurantvoting.menu.web;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;
import ru.aleksandrchistov.restaurantvoting.menu.repository.MenuRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation.assureIdConsistent;
import static ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation.checkNew;

@Validated
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    private final Logger log = getLogger(MenuController.class);

    static final String ADMIN_REST_URL = "/api/admin/restaurants/menu";

    @Autowired
    private MenuRepository repository;

    @PostMapping(value = ADMIN_REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItem menu) {
        log.info("createWithLocation {}", menu);
        checkNew(menu);
        MenuItem created = repository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = ADMIN_REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public void update(@Valid @RequestBody MenuItem menu, @PathVariable int id) {
        log.info("update {} with id={}", menu, id);
        assureIdConsistent(menu, id);
        repository.save(menu);
    }

    @DeleteMapping(value = ADMIN_REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete with id={}", id);
        repository.deleteExisted(id);
    }

    @GetMapping(value = ADMIN_REST_URL + "/{id}")
    public MenuItem get(@PathVariable int id) {
        log.info("get with id={}", id);
        return repository.getExisted(id);
    }

    @GetMapping(value = ADMIN_REST_URL)
    public List<MenuItem> getAllByRestaurantIdAndAddedToRestaurant(@Nullable @RequestParam Integer restaurantId, @Nullable @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate addedDate) {
        log.info("getAllByRestaurantIdAndAddedToRestaurant");
        if (restaurantId != null && addedDate != null) {
            return repository.getAllByRestaurantIdAndAddedToRestaurant(restaurantId, addedDate);
        } else if (addedDate != null) {
            return repository.getAllByAddedToRestaurant(addedDate);
        } else if (restaurantId != null) {
            return repository.getAllByRestaurantId(restaurantId);
        }
        return repository.findAll();
    }
}
