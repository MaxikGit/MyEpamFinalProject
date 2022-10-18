package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomDAO;
import com.max.restaurant.model.entity.Custom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomServiceTest {
    private static CustomDAO mockDAO;
    private static CustomService service;

    @BeforeAll
    static void init() {
        mockDAO = Mockito.mock(CustomDAO.class);
        try {
            service = new CustomService();
            Field daoField = CustomService.class.getDeclaredField("customDAO");
            daoField.setAccessible(true);
            daoField.set(service, mockDAO);
        } catch (DAOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findCustomByIdWrongInputTest() {
        assertThrows(DAOServiceException.class, () -> service.findCustomById(0));
        assertThrows(DAOServiceException.class, () -> service.findCustomById(-55));
    }

    @Test
    void updateTest() {
        Custom custom = initlist(1).get(0);
        Connection mockConn = Mockito.mock(Connection.class);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.getConnection())
                    .thenReturn(mockConn);
            Mockito.when(mockDAO.updateObj(Mockito.eq(custom), Mockito.eq(mockConn)))
                    .thenReturn(true);
            service.update(custom);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0})
    void updateWrongCustomTest(int num) {
        List<Custom> customs = initlist(3);
        customs.get(0).setUserId(num);
        customs.get(1).setId(num);
        customs.get(2).setCreateTime(null);
        for (Custom custom : customs)
            assertThrows(DAOServiceException.class, () -> service.update(custom));
    }

    private static List<Custom> initlist(int size) {
        List<Custom> customs = new ArrayList<>();
        int rndMulti = 1000;
        for (int i = 1; i <= size; i++) {
            Custom custom = new Custom();
            custom.setId(i);
            custom.setCost(Math.random() * rndMulti + 1);
            custom.setCreateTime(new Timestamp(new Date().getTime()));
            custom.setUserId((int) (Math.random() * size + 1));
            custom.setStatusId(1 + (int) (Math.random() * 3));
            customs.add(custom);
        }
        return customs;
    }
}