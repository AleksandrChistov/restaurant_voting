package ru.aleksandrchistov.restaurantvoting.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksandrchistov.restaurantvoting.common.model.NamedEntity;

import java.time.LocalDate;

@Entity
@Table(name = "menu_item",
        indexes = @Index(name = "idx_created_restaurant_id", columnList = "created, restaurant_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "price", "created"}, name = "uk_name_price_created")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends NamedEntity {
    @Column(name = "price", nullable = false)
    @NotNull
    private Long priceInCents;

    @Column(name = "created", nullable = false, insertable = false, updatable = false, columnDefinition = "date default CURRENT_DATE")
    @NotNull
    @JsonIgnore
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "restaurant_id", nullable = false, updatable = false)
    @JsonIgnore
    private Integer restaurantId;

    public MenuItem(MenuItem m) {
        this(m.id, m.name, m.priceInCents, m.restaurantId);
    }

    public MenuItem(int id, String name, Long priceInCents, Integer restaurantId) {
        super(id, name);
        this.priceInCents = priceInCents;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priceInCents=" + priceInCents +
                ", createdAt=" + createdAt +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
