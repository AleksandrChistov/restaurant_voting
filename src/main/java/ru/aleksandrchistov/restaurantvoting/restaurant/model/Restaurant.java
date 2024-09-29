package ru.aleksandrchistov.restaurantvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import ru.aleksandrchistov.restaurantvoting.common.model.NamedEntity;
import ru.aleksandrchistov.restaurantvoting.menu.model.MenuItem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"name"}, name = "uk_name")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "restaurantId")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<MenuItem> menu = new HashSet<>();

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.menu);
    }

    public Restaurant(Integer id, String name, @NonNull Collection<MenuItem> menu) {
        super(id, name);
        setMenu(menu);
    }

    public void setMenu(Collection<MenuItem> menu) {
        this.menu = menu.isEmpty() ? Collections.emptySet() : Set.copyOf(menu);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }
}
