package ru.aleksandrchistov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aleksandrchistov.restaurantvoting.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}