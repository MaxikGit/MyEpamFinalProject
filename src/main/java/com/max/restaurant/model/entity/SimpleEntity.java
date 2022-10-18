package com.max.restaurant.model.entity;

import java.io.Serializable;

/**
 *This interface presents the most common model of all entities from database of this app
 */
public interface SimpleEntity extends Serializable {
    int getId();
    void setId(int id);
}
