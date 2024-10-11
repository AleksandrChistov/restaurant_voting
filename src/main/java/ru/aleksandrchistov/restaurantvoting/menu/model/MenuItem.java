package ru.aleksandrchistov.restaurantvoting.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksandrchistov.restaurantvoting.common.model.NamedEntity;
import ru.aleksandrchistov.restaurantvoting.common.views.DefaultView;

import java.time.LocalDate;

@Entity
@Table(name = "menu_item",
        indexes = @Index(name = "idx_restaurant_id_added_to_restaurant", columnList = "restaurant_id, added_to_restaurant"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"restaurant_id", "added_to_restaurant", "name"}, name = "uk_restaurant_id_added_to_restaurant_name")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends NamedEntity {
    @Column(name = "price", nullable = false)
    @NotNull
    @JsonView(DefaultView.class)
    private Long priceInCents;

    @Column(name = "added_to_restaurant")
    @JsonIgnore
    private LocalDate addedToRestaurant;

    @Setter(AccessLevel.NONE)
    @Column(name = "restaurant_id")
    @Min(1)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonView(DefaultView.class)
    private Integer restaurantId;

    public MenuItem(MenuItem m) {
        this(m.id, m.name, m.priceInCents, m.restaurantId);
    }

    public MenuItem(Integer id, String name, Long priceInCents, Integer restaurantId) {
        super(id, name);
        setPriceInCents(priceInCents);
        setRestaurantId(restaurantId);
    }

    public void setRestaurantId(@Min(1) Integer restaurantId) {
        this.restaurantId = restaurantId;
        if (restaurantId != null) {
            this.setAddedToRestaurant(LocalDate.now());
        }
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priceInCents=" + priceInCents +
                ", addedToRestaurant=" + addedToRestaurant +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
