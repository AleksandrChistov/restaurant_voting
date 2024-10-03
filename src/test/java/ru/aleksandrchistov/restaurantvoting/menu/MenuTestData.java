package ru.aleksandrchistov.restaurantvoting.menu;

import ru.aleksandrchistov.restaurantvoting.MatcherFactory;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "id");

    public static List<MenuItem> getNew() {
        MenuItem menu1 = new MenuItem(null, "Curd", 5500L, 1);
        MenuItem menu2 = new MenuItem(null, "Coffee", 2500L, 1);
        return new ArrayList<>(Arrays.asList(menu1, menu2));
    }
}
