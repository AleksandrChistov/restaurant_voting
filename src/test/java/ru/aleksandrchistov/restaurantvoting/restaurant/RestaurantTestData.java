package ru.aleksandrchistov.restaurantvoting.restaurant;

import ru.aleksandrchistov.restaurantvoting.MatcherFactory;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;
import ru.aleksandrchistov.restaurantvoting.restaurant.model.Restaurant;

import java.util.Collections;
import java.util.Set;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu.restaurantId");

    public static final int KFC_ID = 1;
    public static final Restaurant KFC_RESTAURANT = new Restaurant(KFC_ID, "KFC", Set.of(
            new MenuItem(1, "Gamburger", 10050L, 1),
            new MenuItem(2, "Poached eggs", 25000L, 1)
    ));
    public static final Restaurant MC_DONALDS_RESTAURANT = new Restaurant(2, "McDonald’s", Set.of(
            new MenuItem(3, "Marbled Beef Steak", 83500L, 2)
    ));

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant", Collections.emptySet());
    }

    public static Restaurant getUpdated() {
        return new Restaurant(KFC_ID, "Updated KFC", Collections.emptySet());
    }
}
