package com.senla.kanapa.controller;

import com.senla.kanapa.entity.Category;
import com.senla.kanapa.service.CategoryService;
import com.senla.kanapa.service.dto.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Добавить категорию")
    @ResponseStatus(HttpStatus.OK)
    public void addCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.editCategory(categoryDto);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Редактировать категорию")
    @ResponseStatus(HttpStatus.OK)
    public void editCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long categoryId) {
        categoryService.editCategory(categoryDto, categoryId);
    }

    @GetMapping
    @Operation(summary = "Вернуть все категории")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Показать категорию с подкатегорями")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategoryAndSubCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryAndSubCategoryById(categoryId);
    }
}
