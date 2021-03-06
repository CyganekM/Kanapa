package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Category;
import com.senla.kanapa.repository.CategoryJpaRepository;
import com.senla.kanapa.service.CategoryService;
import com.senla.kanapa.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    @Transactional
    @Override
    public void editCategory(CategoryDto categoryDto, Long categoryId) {

        Category category = Category.builder()
                .description(categoryDto.getDescription())
                .name(categoryDto.getName())
                .build();
        category.setId(categoryId);
        if (categoryDto.getParent() != null) {
            category.setParentCategory(categoryJpaRepository.getReferenceById(categoryDto.getParent()));
        }
        categoryJpaRepository.save(category);
    }

    @Transactional
    @Override
    public void editCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .id(categoryDto.getId())
                .description(categoryDto.getDescription())
                .name(categoryDto.getName())
                .build();
        if (categoryDto.getParent() != null) {
            category.setParentCategory(categoryJpaRepository.getReferenceById(categoryDto.getParent()));
        }
        categoryJpaRepository.save(category);
    }

    @Transactional
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryJpaRepository.getReferenceById(categoryId);
    }

    @Transactional
    @Override
    public List<Category> getAllCategory() {
        return categoryJpaRepository.findAll();
    }

    @Transactional
    @Override
    public List<Category> getCategoryAndSubCategoryById(Long categoryId) {
        return categoryJpaRepository.getByIdOrParentCategory(categoryId);
    }
}
