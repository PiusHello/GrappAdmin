package com.example.grappadmin.Model;

public class AdminOrders {

    private String Date,ItemsBought,OrderStatus,Price,Quantity, Time;

    public AdminOrders()
    {
    }

    public AdminOrders(String date, String itemsBought, String orderStatus, String price, String quantity, String time) {
        Date = date;
        ItemsBought = itemsBought;
        OrderStatus = orderStatus;
        Price = price;
        Quantity = quantity;
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getItemsBought() {
        return ItemsBought;
    }

    public void setItemsBought(String itemsBought) {
        ItemsBought = itemsBought;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
