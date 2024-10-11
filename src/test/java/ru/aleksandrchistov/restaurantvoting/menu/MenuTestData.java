package ru.aleksandrchistov.restaurantvoting.menu;

import ru.aleksandrchistov.restaurantvoting.MatcherFactory;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;

public class MenuTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "addedToRestaurant", "restaurantId");

    public static final MenuItem MENU1 = new MenuItem(1, "Hamburger", 10050L, null);
    public static final MenuItem MENU2 = new MenuItem(2, "Poached eggs", 25000L, null);

    public static MenuItem getNew() {
        return new MenuItem(null, "Coffee", 2500L, null);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU1.getId(), "New name", MENU1.getPriceInCents(), MENU1.getRestaurantId());
    }
}
