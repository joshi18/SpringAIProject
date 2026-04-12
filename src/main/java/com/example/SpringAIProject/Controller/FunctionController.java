package com.example.SpringAIProject.Controller;


import com.example.SpringAIProject.Services.FunctionCallingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FunctionController {



    @Autowired
    FunctionCallingService functionCallingService;



    @GetMapping("/getOderStaus")
    public Map<String,Object> getOderStatusWithHelpOfOderId(
            @RequestBody Map<String,String> request,
            @RequestHeader String provider,
            @RequestHeader String model


    ){

        String userMessage = request.get("message");

        String response = functionCallingService.chatWithOderTracking(userMessage, provider, model);

        return Map.of("success","Getting response from Spring AI + LLM",

                "response",response);

    }

}
