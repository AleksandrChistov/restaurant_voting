package ru.aleksandrchistov.restaurantvoting.restaurant;

import ru.aleksandrchistov.restaurantvoting.MatcherFactory;
import ru.aleksandrchistov.restaurantvoting.restaurant.model.Restaurant;

import java.util.Collections;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int KFC_ID = 1;

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant", Collections.emptySet());
    }

    public static Restaurant getUpdated() {
        return new Restaurant(KFC_ID, "Updated KFC", Collections.emptySet());
    }
}
