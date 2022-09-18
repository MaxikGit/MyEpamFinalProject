package com.max.restaurant.model.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Custom implements SimpleEntity {
        private int id;
        private double cost;
        private Timestamp createTime;
        private int userId;
        private int statusId;

    public Custom() {
    }

    public Custom(Timestamp createTime, int userId, int statusId) {
        this.createTime = createTime;
        this.userId = userId;
        this.statusId = statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Custom custom = (Custom) o;
        return id == custom.id && Double.compare(custom.cost, cost) == 0 && userId == custom.userId
                && statusId == custom.statusId && createTime.equals(custom.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, createTime, userId, statusId);
    }

    @Override
    public String toString() {
        return "Custom{" +
                "id=" + id +
                ", cost=" + cost +
                ", createTime=" + createTime.toLocalDateTime() +
                ", userId=" + userId +
                ", statusId=" + statusId +
                '}';
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
