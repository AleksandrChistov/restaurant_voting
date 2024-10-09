package ru.aleksandrchistov.restaurantvoting.restaurant.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrchistov.restaurantvoting.common.BaseRepository;
import ru.aleksandrchistov.restaurantvoting.restaurant.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menu m WHERE m.createdAt BETWEEN :startDate AND :endDate")
    List<Restaurant> findAllWithMenu(LocalDate startDate, LocalDate endDate);
}
