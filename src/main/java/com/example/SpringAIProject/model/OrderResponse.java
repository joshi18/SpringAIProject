package com.example.SpringAIProject.model;



public class OrderResponse {

    public  String OrderId;
    public  String orderName;
    public  String orderstates;

    public String getOrderstates() {
        return orderstates;
    }

    public void setOrderstates(String orderstates) {
        this.orderstates = orderstates;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderId() {
        return OrderId;
    }

    public OrderResponse() {
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
