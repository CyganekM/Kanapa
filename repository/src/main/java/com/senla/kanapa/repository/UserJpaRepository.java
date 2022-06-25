package com.senla.kanapa.repository;

import com.senla.kanapa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String login);
}
