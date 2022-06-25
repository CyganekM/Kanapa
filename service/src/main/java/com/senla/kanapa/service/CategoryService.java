package com.senla.kanapa.service;

import com.senla.kanapa.entity.Category;
import com.senla.kanapa.service.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    void editCategory(CategoryDto categoryDto, Long categoryId);

    void editCategory(CategoryDto categoryDto);

    Category getCategoryById(Long categoryId);

    List<Category> getAllCategory();

    List<Category> getCategoryAndSubCategoryById(Long categoryId);
}
