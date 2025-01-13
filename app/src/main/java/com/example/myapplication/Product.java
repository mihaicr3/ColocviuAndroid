package com.example.myapplication;



public class Product {
    private int id;
    private String title;
    private String thumbnail;
    private String description;

    public Product(int id, String title, String thumbnail, String description) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }
}
