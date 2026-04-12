package com.example.SpringAIProject.Services;


import com.example.SpringAIProject.model.MockOrder;
import com.example.SpringAIProject.model.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    // Mock the orders
    // here we not calling the database . but we are storing the values in the map
    private final Map<String, MockOrder> mockOrderMap = new HashMap<>();


    public OrderService(){
        mockOrderMap.put("12345",new MockOrder("Not Completed","90","2000","Abhijit Joshi"));
        mockOrderMap.put("1234",new MockOrder("Not Completed","91","4000"," Nishant Lihare"));
        mockOrderMap.put("123",new MockOrder("Not Completed","92","5000","John deo"));
        mockOrderMap.put("12",new MockOrder("Not Completed","93","6000","Shashikala joshi"));
        mockOrderMap.put("1",new MockOrder("Not Completed","94","7000","Dattatraya  Joshi"));

    }

    public OrderResponse getOrderStatus(String orderId){
       // OrderResponse mockOrderMap1 = (OrderResponse) mockOrderMap.get(orderId);

// here we call the database also .
        // For sample purpose i am storing into the database

        return new OrderResponse();



    }





}
