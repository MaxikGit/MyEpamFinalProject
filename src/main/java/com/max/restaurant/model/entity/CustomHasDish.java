package com.max.restaurant.model.entity;

public class CustomHasDish implements SimpleEntity{
    private int customId;
    private int dishId;
    private int count;
    private double price;

    public CustomHasDish(){}

    public CustomHasDish(int customId, int dishId) {
        this.customId = customId;
        this.dishId = dishId;
    }

    @Override
    public String toString() {
        return "Custom id# " + customId +
                ", has dish id# " + dishId +
                ", with price= " + price +
                ", & quantity= " + count +
                ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomHasDish that = (CustomHasDish) o;

        if (customId != that.customId) return false;
        if (dishId != that.dishId) return false;
        return Double.compare(that.price, price) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = customId;
        result = 31 * result + dishId;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    @Override
    public int getId() {
        return customId;
    }

    @Override
    public void setId(int id) {
        customId = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
