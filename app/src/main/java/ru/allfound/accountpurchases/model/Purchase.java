package ru.allfound.accountpurchases.model;

/*
 * Purchase.java    v.1.1 10.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class Purchase{
    private long id;
    private String description;
    private String category;
    private String date;
    private String time;
    private int price;

    public Purchase() {}

    public Purchase(long id, String description, String category, String date, String time, int price) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public static Purchase newInstance(Purchase purchase) {
        return new Purchase(
                purchase.getId(),
                purchase.getDescription(),
                purchase.getCategory(),
                purchase.getDate(),
                purchase.getTime(),
                purchase.getPrice());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
