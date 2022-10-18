package com.max.restaurant.model.services;

import com.max.restaurant.model.dao.daoimpl.CategoryDAO;
import com.max.restaurant.model.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class CategoryServiceTest {
    private static List<Category> categories;
    private static CategoryDAO mockDAO;
    private static CategoryService service;

    @BeforeAll
    static void init() throws IllegalAccessException, NoSuchFieldException {
        mockDAO = Mockito.mock(CategoryDAO.class);
        service = new CategoryService();
        Field daoField = CategoryService.class.getDeclaredField("categoryDAO");
        daoField.setAccessible(true);
        daoField.set(service, mockDAO);

        categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("cat" + i);
            categories.add(category);
        }
    }

    @Test
    void findAllCategoriesTest() {
        Assertions.assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findAll()).thenReturn(categories);
            Assertions.assertEquals(service.findAllCategories(), categories);
        });
    }
}