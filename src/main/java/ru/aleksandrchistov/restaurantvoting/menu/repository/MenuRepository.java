package ru.aleksandrchistov.restaurantvoting.menu.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrchistov.restaurantvoting.common.BaseRepository;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<MenuItem> {

    List<MenuItem> getAllByRestaurantId(int restaurantId);

    List<MenuItem> getAllByAddedToRestaurant(LocalDate addedDate);

    List<MenuItem> getAllByRestaurantIdAndAddedToRestaurant(int restaurantId, LocalDate addedDate);

}
