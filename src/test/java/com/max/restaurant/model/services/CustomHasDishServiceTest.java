package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomHasDishDAO;
import com.max.restaurant.model.entity.CustomHasDish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CustomHasDishServiceTest {
    private static List<CustomHasDish> customHasDishes;
    private static CustomHasDishDAO mockDAO;
    private static CustomHasDishService service;

    @BeforeAll
    static void init() {
        mockDAO = Mockito.mock(CustomHasDishDAO.class);
        try {
            service = new CustomHasDishService();
            Field daoField = CustomHasDishService.class.getDeclaredField("customHasDishDAO");
            daoField.setAccessible(true);
            daoField.set(service, mockDAO);
        } catch (DAOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findByCustomIdTest() {
        int id = 1;
        customHasDishes = initlist(5);
        List<CustomHasDish> filtered = customHasDishes.stream()
                .filter(x->x.getCustomId() == id)
                .collect(Collectors.toList());
        assertDoesNotThrow(() -> {
                    Mockito.when(mockDAO.findObjByParam(Mockito.any(), Mockito.any(), Mockito.any()))
                            .thenReturn(filtered);
                    Assertions.assertEquals(filtered, service.findByCustomId(id));
//            System.out.println(filtered + "\n = \n" + service.findByCustomId(id));
                });
    }

    @Test
    void findByCustomIdFailedTest() {
        assertThrows(DAOServiceException.class, () -> service.findByCustomId(0));
        assertThrows(DAOServiceException.class, () -> service.findByCustomId(-55));
    }
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    void findByCustomIdDishIdTest(int dishId) {
        int customId = dishId;
//        int dishId = 1;
        customHasDishes = initlist(10);
        List<CustomHasDish> filtered = customHasDishes.stream()
                .filter(x-> (x.getCustomId() == customId) && (x.getDishId() == dishId) )
                .collect(Collectors.toList());
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findObjByParam(Mockito.any(), Mockito.any(), Mockito.any()))
                    .thenReturn(filtered);
            Assertions.assertEquals(filtered.isEmpty() ? null : filtered.get(0), service.findByCustomIdDishId(customId, dishId));
//            System.out.println( (filtered.isEmpty() ? null : filtered.get(0)) + "\n = \n" + service.findByCustomIdDishId(customId, dishId));
        });
    }

    @Test
    void findByCustomIdDishIdWrongInputTest() {
        assertThrows(DAOServiceException.class, () -> service.findByCustomIdDishId(0, 1));
        assertThrows(DAOServiceException.class, () -> service.findByCustomIdDishId(1, 0));
    }

    @Test
    void findAllTest() {
        customHasDishes = initlist(5);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findAll())
                    .thenReturn(customHasDishes);
            Assertions.assertEquals(customHasDishes, service.findAll());
//            System.out.println(customHasDishes + "\n = \n" + service.findAll());
        });
    }

    @Test
    void updateOk() {
        CustomHasDish customHasDish = initlist(1).get(0);
        Connection mockConn = Mockito.mock(Connection.class);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.getConnection())
                    .thenReturn(mockConn);
            Mockito.when(mockDAO.updateObj(Mockito.eq(customHasDish), Mockito.eq(mockConn)) )
                    .thenReturn(true);
            service.update(customHasDish);
        });
    }
    @ParameterizedTest
    @CsvSource({"0,1", "1,0"})
    void customHasDishIsValidTest(int customId, int dishId) {
        CustomHasDish customHasDish = initlist(1).get(0);
        customHasDish.setCustomId(customId);
        customHasDish.setDishId(dishId);

        Connection mockConn = Mockito.mock(Connection.class);
        assertThrows(DAOException.class, () -> {
            Mockito.when(mockDAO.getConnection())
                    .thenReturn(mockConn);
            Mockito.when(mockDAO.updateObj(Mockito.eq(customHasDish), Mockito.eq(mockConn)) )
                    .thenReturn(true);
            service.update(customHasDish);
        });
    }

    @Test
    void createList() {
    }

    @Test
    void insertCustomNumTest() {
        int customId = 2;
        List<CustomHasDish> customHasDishList = initlist(5);
        service.insertCustomNum(customId, customHasDishList);
        for (CustomHasDish hasDish: customHasDishList) {
            assertEquals(hasDish.getCustomId(), customId);
        }
    }

    @Test
    void sortToInsertUpdate() {
    }


    private static List<CustomHasDish> initlist(int size) {
        List<CustomHasDish> customHasDishes = new ArrayList<>();
        int rndMulti = 100;
        for (int i = 1; i <= size; i++) {
            CustomHasDish customHasDish = new CustomHasDish();
            customHasDish.setCustomId(i);
            customHasDish.setCount((int) (Math.random() * size + 1));
            customHasDish.setPrice(Math.random() * rndMulti + 1);
            customHasDish.setDishId(i + (int) (Math.random() * size/2));
            customHasDishes.add(customHasDish);
        }
        return customHasDishes;
    }
}