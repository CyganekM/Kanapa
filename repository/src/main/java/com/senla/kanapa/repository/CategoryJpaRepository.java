package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    @Query(value = "select * from categories c where c.id = :categoryId or c.parent = :categoryId", nativeQuery = true)
    List<Category> getByIdOrParentCategory(Long categoryId);
}
