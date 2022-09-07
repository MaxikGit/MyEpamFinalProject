package com.max.restaurant.model.entity;

import java.util.Objects;

public class Status implements SimpleEntity{
    private int id;
    private String name;
    private String details;

    public Status() {
    }
    public Status(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Status# " + id +
                ", name='" + name + ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return id == status.id && Objects.equals(name, status.name) && Objects.equals(details, status.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, details);
    }
}
