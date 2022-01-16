package com.CPE001_2021.capstoneproject;

public class Item_ListElement {
    String name, quantity,expiration;
    int imageId;
    public Item_ListElement(){}
    public Item_ListElement(String name, String quantity, String expiration, int imageId) {
        this.name = name;
        this.quantity = quantity;
        this.expiration = expiration;
        this.imageId = imageId;
    }

    public Item_ListElement(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
