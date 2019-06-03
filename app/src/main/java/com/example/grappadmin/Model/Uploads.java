package com.example.grappadmin.Model;

public class Uploads
{
    private String foodName,foodImage,foodCategory,foodPrice;
    public Uploads(String trim, String s)
    {
    }

    public Uploads(String foodName, String foodImage, String foodCategory, String foodPrice) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodCategory = foodCategory;
        this.foodPrice = foodPrice;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }
}
