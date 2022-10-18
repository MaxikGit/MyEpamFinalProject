package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CategoryDAO;
import com.max.restaurant.model.dao.daoimpl.CustomDAO;
import com.max.restaurant.model.dao.daoimpl.DishDAO;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Dish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.max.restaurant.utils.UtilsEntityFields.DISH_CATEGORY_ID;
import static org.junit.jupiter.api.Assertions.*;

class DishServiceTest {
    private static List<Dish> dishes;
    private static DishDAO mockDAO;
    private static DishService service;
    private static Logger mockLogger;

    @BeforeAll
    static void init() throws NoSuchFieldException, DAOException, IllegalAccessException {
        mockDAO = Mockito.mock(DishDAO.class);
            service = new DishService();
            Field daoField = DishService.class.getDeclaredField("dishDAO");
            daoField.setAccessible(true);
            daoField.set(service, mockDAO);
    }

    @Test
    void findDishByCategoryId() {
        List<Dish> dishes = initlist(5);
        String categoryId = String.valueOf(dishes.get(0).getCategoryId());
        Connection mockConn = Mockito.mock(Connection.class);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.getConnection())
                    .thenReturn(mockConn);
            Mockito.when(mockDAO.findObjByParam(
                            Mockito.eq(DISH_CATEGORY_ID), Mockito.eq(categoryId), Mockito.eq(mockConn)))
                    .thenReturn(dishes);
            assertEquals(service.findDishByCategoryId(categoryId), dishes);
        });
    }

    @Test
    void findDishesByIdTest() {
        dishes = initlist(5);
        Map<Integer, Integer> ids = initIdQuantityMap(dishes);
        assertDoesNotThrow(() -> {
            for (int i = 0; i < dishes.size(); i++) {
                int dishId = dishes.get(i).getId();
                Dish dish = dishes.stream().filter(x -> (x.getId() == dishId)).findFirst().get();
                Mockito.when(mockDAO.findObjById(Mockito.eq(dishId)))
                        .thenReturn(dish);
            }
            Map<Dish, Integer> dishMap = service.findDishesById(ids);
            assertEquals(dishMap.size(), dishes.size());
            System.out.println(dishMap);
            System.out.println(ids);
            for (Map.Entry<Dish, Integer> dishesQuantities : dishMap.entrySet()) {
                assertEquals(ids.get(dishesQuantities.getKey().getId()), dishesQuantities.getValue());
            }
        });
    }

    @Test
    void findDishesByIdWrongParam() {
        assertThrows(DAOServiceException.class, () -> service.findDishesById(null));
        assertThrows(DAOServiceException.class, () -> service.findDishesById(new HashMap<>()));
    }

    @Test
    void findAllDishesTest() {
        dishes = initlist(5);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findAll()).thenReturn(dishes);
            Assertions.assertEquals(service.findAllDishes(), dishes);
        });
    }

    @Test
    void getDishByIdFromMap() {
        List<Dish> dishes = initlist(10);
        Map<Dish, Integer> dishMap = initDishMap(dishes);
        assertEquals(service.getDishByIdFromMap(dishMap, dishes.get(0).getId()), dishes.get(0));
    }

    private static List<Dish> initlist(int size) {
        dishes = new ArrayList<>();
        int rndMulti = 100;
        for (int i = 1; i <= size; i++) {
            Dish dish = new Dish();
            dish.setId(i);
            dish.setName("dish #" + i);
            dish.setCategoryId(((int) (Math.random() * size + 1)) % 4);
            dish.setPrice(Math.random() * rndMulti + 1);
            dish.setImagePath("/some/path/to/image.gif");
            dishes.add(dish);
        }
        return dishes;
    }

    private static Map<Integer, Integer> initIdQuantityMap(List<Dish> dishes) {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < dishes.size(); i++) {
            result.put(dishes.get(i).getId(), i % 4 + 1);
        }
        return result;
    }

    private static Map<Dish, Integer> initDishMap(List<Dish> dishes) {
        Map<Dish, Integer> result = new HashMap<>();
        for (int i = 0; i < dishes.size(); i++) {
            result.put(dishes.get(i), i % 4 + 1);
        }
        return result;
    }
}