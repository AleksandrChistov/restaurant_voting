package ru.aleksandrchistov.restaurantvoting.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksandrchistov.restaurantvoting.common.model.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "vote",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "created"}, name = "uk_user_id_created")})
@Getter
@Setter
@NoArgsConstructor
public class Vote extends BaseEntity {
    @Column(name = "created", nullable = false, insertable = false, updatable = false, columnDefinition = "date default CURRENT_DATE")
    @NotNull
    @JsonIgnore
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "user_id", nullable = false, updatable = false)
    @JsonIgnore
    private Integer userId;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Integer restaurantId;

    public Vote(Vote v) {
        this(v.id, v.createdAt, v.userId, v.restaurantId);
    }

    public Vote(Integer id, LocalDate createdAt, Integer userId, Integer restaurantId) {
        super(id);
        this.createdAt = createdAt;
        this.userId = userId;
        this.restaurantId = restaurantId;

    }
}
