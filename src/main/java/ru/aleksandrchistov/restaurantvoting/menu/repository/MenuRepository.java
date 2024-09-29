package ru.aleksandrchistov.restaurantvoting.menu.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrchistov.restaurantvoting.common.BaseRepository;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<MenuItem> {

    @Modifying
    @Transactional
    @Query(value="DELETE FROM MenuItem m WHERE m.createdAt = :somedate AND m.restaurantId = :restaurantId")
    void deleteAllByDateAndRestaurantId(LocalDate somedate, Integer restaurantId);

    @Transactional
    default List<MenuItem> prepareAndSaveAll(List<MenuItem> menu, int restaurantId) {
        menu.forEach(m -> m.setRestaurantId(restaurantId));
        return saveAll(menu);
    }
}
