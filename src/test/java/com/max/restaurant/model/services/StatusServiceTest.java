package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.StatusDAO;
import com.max.restaurant.model.entity.Category;
import com.max.restaurant.model.entity.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StatusServiceTest {
    private static List<Status> entities;
    private static StatusDAO mockDAO;
    private static StatusService service;

    @BeforeAll
    static void init() throws IllegalAccessException, NoSuchFieldException, DAOException {
        mockDAO = Mockito.mock(StatusDAO.class);
        service = new StatusService();
        Field daoField = StatusService.class.getDeclaredField("statusDAO");
        daoField.setAccessible(true);
        daoField.set(service, mockDAO);

        entities = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Status entity = new Status();
            entity.setId(i);
            entity.setName("Status #" + i);
            entities.add(entity);
        }
    }

    @Test
    void findAllTest() {
        Assertions.assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findAll()).thenReturn(entities);
            Assertions.assertEquals(service.findAll(), entities);
        });
    }

    @Test
    void findStatusById() {
        Status entity = entities.get(0);
        int id = entity.getId();
        Assertions.assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findObjById(id)).thenReturn(entity);
            Assertions.assertEquals(service.findStatusById(id), entity);
        });
    }

    @Test
    void findStatusByIdFailed() {
        assertThrows(DAOServiceException.class, () -> service.findStatusById(0));
        assertThrows(DAOServiceException.class, () -> service.findStatusById(-1));
    }

}