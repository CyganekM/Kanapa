package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Category;
import com.senla.kanapa.repository.CategoryJpaRepository;
import com.senla.kanapa.service.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CategoryServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CategoryServiceImplTest {
    @MockBean
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    private List<Category> getCategoryList() {
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Animal");

        Category category1 = new Category();
        category1.setDescription("Different dogs");
        category1.setId(2L);
        category1.setName("Dogs");
        category1.setParentCategory(category);

        Category category2 = new Category();
        category2.setDescription("Different cats");
        category2.setId(3L);
        category2.setName("Cats");
        category2.setParentCategory(category);

        List<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        return categories;
    }

    @Test
    void CategoryServiceImpl_saveCategory_categoryDto() {
        when(categoryJpaRepository.getReferenceById(1L)).thenReturn(getCategoryList().get(0));
        when(categoryJpaRepository.save(any())).thenReturn(getCategoryList().get(1));
        CategoryDto categoryDto = mock(CategoryDto.class);
        when(categoryDto.getId()).thenReturn(2L);
        when(categoryDto.getParent()).thenReturn(1L);
        when(categoryDto.getDescription()).thenReturn("Different dogs");
        when(categoryDto.getName()).thenReturn("Dogs");
        categoryServiceImpl.editCategory(categoryDto, 1L);
        verify(categoryJpaRepository).getReferenceById(1L);
        verify(categoryJpaRepository).save(any());
        verify(categoryDto, atLeast(1)).getParent();
        verify(categoryDto).getDescription();
        verify(categoryDto).getName();
    }

    @Test
    void CategoryServiceImpl_getCategoryById() {
        when(categoryJpaRepository.getReferenceById(2L)).thenReturn(getCategoryList().get(1));
        assertEquals(getCategoryList().get(1), categoryServiceImpl.getCategoryById(2L));
        verify(categoryJpaRepository).getReferenceById(any());
    }

    @Test
    void CategoryServiceImpl_getAllCategory_emptyList() {
        ArrayList<Category> categoryList = new ArrayList<>();
        when(categoryJpaRepository.findAll()).thenReturn(categoryList);
        List<Category> actualAllCategory = categoryServiceImpl.getAllCategory();
        assertSame(categoryList, actualAllCategory);
        assertTrue(actualAllCategory.isEmpty());
        verify(categoryJpaRepository).findAll();
    }

    @Test
    void testGetCategoryAndSubCategoryById() {
        ArrayList<Category> categoryList = new ArrayList<>();
        when(categoryJpaRepository.getByIdOrParentCategory(1L)).thenReturn(categoryList);
        List<Category> actualCategoryAndSubCategoryById = categoryServiceImpl.getCategoryAndSubCategoryById(1L);
        assertSame(categoryList, actualCategoryAndSubCategoryById);
        assertTrue(actualCategoryAndSubCategoryById.isEmpty());
        verify(categoryJpaRepository).getByIdOrParentCategory(anyLong());
    }
}

