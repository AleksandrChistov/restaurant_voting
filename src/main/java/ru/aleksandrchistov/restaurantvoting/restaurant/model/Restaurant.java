package ru.aleksandrchistov.restaurantvoting.restaurant.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import ru.aleksandrchistov.restaurantvoting.common.model.NamedEntity;
import ru.aleksandrchistov.restaurantvoting.common.views.IgnoreView;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonView(IgnoreView.class)
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
