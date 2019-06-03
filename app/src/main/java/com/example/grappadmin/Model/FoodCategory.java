package com.example.grappadmin.Model;

public class FoodCategory {
    public String categoryName;
    public String categoryImage;
    public String description;
    public String uploadFoodCategory;


    public FoodCategory() {
    }

    public FoodCategory(String categoryName, String categoryImage, String description, String uploadFoodCategory) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.description = description;
        this.uploadFoodCategory = uploadFoodCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadFoodCategory() {
        return uploadFoodCategory;
    }

    public void setUploadFoodCategory(String uploadFoodCategory) {
        this.uploadFoodCategory = uploadFoodCategory;
    }
}