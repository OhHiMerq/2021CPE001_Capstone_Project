package com.CPE001_2021.capstoneproject;

public class InventoryItem {
    String userId,itemId,itemWeightExp;

    public InventoryItem() {
    }

    public InventoryItem(String userId, String itemId, String itemWeightExp) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemWeightExp = itemWeightExp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemWeightExp() {
        return itemWeightExp;
    }

    public void setItemWeightExp(String itemWeightExp) {
        this.itemWeightExp = itemWeightExp;
    }
}
