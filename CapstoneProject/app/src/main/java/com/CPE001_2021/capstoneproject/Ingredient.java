package com.CPE001_2021.capstoneproject;

public class Ingredient {
    private String defItemName,analogousNames,itemType,expiry,imageURL;
    public String itemId;
    public Ingredient(){}
    public Ingredient(String defItemName, String analogousNames, String itemType, String expiry, String imageURL) {
        this.defItemName = defItemName;
        this.analogousNames = analogousNames;
        this.itemType = itemType;
        this.expiry = expiry;
        this.imageURL = imageURL;
    }

    public String getDefItemName() {
        return defItemName;
    }

    public void setDefItemName(String defItemName) {
        this.defItemName = defItemName;
    }

    public String getAnalogousNames() {
        return analogousNames;
    }

    public void setAnalogousNames(String analogousNames) {
        this.analogousNames = analogousNames;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
