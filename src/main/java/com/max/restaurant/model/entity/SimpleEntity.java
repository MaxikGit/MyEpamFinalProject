package com.max.restaurant.model.entity;

import java.io.Serializable;

/**
 *This interface presents all entities from database of this app
 */
public interface SimpleEntity extends Serializable {
    int getId();
    void setId(int id);
    default SimpleEntity getEntity(){return this;}
}
