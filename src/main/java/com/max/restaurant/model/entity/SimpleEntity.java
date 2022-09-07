package com.max.restaurant.model.entity;

public interface SimpleEntity {
    int getId();
    void setId(int id);
    default SimpleEntity getEntity(){return this;}
}
