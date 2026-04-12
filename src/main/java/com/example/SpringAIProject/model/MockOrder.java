package com.example.SpringAIProject.model;

public class MockOrder {


    public String orderStatus;
    public String orderId;
    public String orderValue;
    public String customerName;

    public MockOrder(String orderStatus, String orderId, String orderValue, String customerName) {
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.customerName = customerName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
