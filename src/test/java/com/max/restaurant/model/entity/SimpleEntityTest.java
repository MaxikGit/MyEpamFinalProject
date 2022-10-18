package com.max.restaurant.model.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleEntityTest {
    private static List<SimpleEntity> entities = new ArrayList<>();
    private static int idToSet = 3;


    static {
        entities.add(new Category());
        entities.add(new Custom());
        entities.add(new CustomHasDish());
        entities.add(new Dish());
        entities.add(new Role());
        entities.add(new Status());
        entities.add(new User());
        for (SimpleEntity entity: entities){
            entity.setId(idToSet);
        }
    }


    @Test
    void getId() {
        for (SimpleEntity entity: entities){
            assertEquals(entity.getId(), 3);
        }
    }

    @Test
    void setId() {
        int idNew = 5;
        for (SimpleEntity entity: entities){
            entity.setId(idNew);
            assertEquals(entity.getId(), 5);
        }
    }
}