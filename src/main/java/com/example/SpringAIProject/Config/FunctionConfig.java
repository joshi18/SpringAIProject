package com.example.SpringAIProject.Config;


import com.example.SpringAIProject.Services.OrderService;
import com.example.SpringAIProject.model.OrderResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FunctionConfig {


    @Autowired
    private OrderService orderService;


    @Tool(description = "Getting the order response from the database")
    public OrderResponse getResponse(
            @ToolParam(description = "To check the staus of specific order Id")
            String orderId){

       OrderResponse orderResponse =  orderService.getOrderStatus(orderId);

       return  orderResponse;

    }



}
