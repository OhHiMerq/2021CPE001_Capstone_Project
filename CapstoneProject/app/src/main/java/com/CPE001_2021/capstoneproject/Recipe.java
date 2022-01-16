package com.CPE001_2021.capstoneproject;

public class Recipe {
    public String scrName, scrTimeReq, imageURL,scrDefServing,scrIngredient,scrDescription,scrInstruction;

    public Recipe(){}
    public Recipe(String scrName, String scrTimeReq, String imageURL, String scrDefServing, String scrIngredient, String scrDescription, String scrInstruction) {
        this.scrName = scrName;
        this.scrTimeReq = scrTimeReq;
        this.imageURL = imageURL;
        this.scrDefServing = scrDefServing;
        this.scrIngredient = scrIngredient;
        this.scrDescription = scrDescription;
        this.scrInstruction = scrInstruction;
    }

    public String getScrName() {
        return scrName;
    }

    public void setScrName(String scrName) {
        this.scrName = scrName;
    }

    public String getScrTimeReq() {
        return scrTimeReq;
    }

    public void setScrTimeReq(String scrTimeReq) {
        this.scrTimeReq = scrTimeReq;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getScrDefServing() {
        return scrDefServing;
    }

    public void setScrDefServing(String scrDefServing) {
        this.scrDefServing = scrDefServing;
    }

    public String getScrIngredient() {
        return scrIngredient;
    }

    public void setScrIngredient(String scrIngredient) {
        this.scrIngredient = scrIngredient;
    }

    public String getScrDescription() {
        return scrDescription;
    }

    public void setScrDescription(String scrDescription) {
        this.scrDescription = scrDescription;
    }

    public String getScrInstruction() {
        return scrInstruction;
    }

    public void setScrInstruction(String scrInstruction) {
        this.scrInstruction = scrInstruction;
    }
}
