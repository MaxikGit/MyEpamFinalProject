package com.max.restaurant.model.entity;

import java.util.Objects;

public class Dish implements SimpleEntity {
    private int id;
    private String name;
    private double price;
    private String details;
    private int categoryId;
    private String imagePath;

    public Dish() {
    }

    public Dish(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dish #" + id +
                ", " + name + '\'' +
                ", price='" + price + ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && categoryId == dish.categoryId && name.equals(dish.name) && Objects.equals(price, dish.price) && Objects.equals(details, dish.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, details, categoryId);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (imagePath.matches("\\w*[./\\\\]\\w*"))
            this.imagePath = "/views/images/" + imagePath;
        else this.imagePath = imagePath;
    }
}
