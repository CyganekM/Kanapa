package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    Role getByAuthority(String authority);
}
