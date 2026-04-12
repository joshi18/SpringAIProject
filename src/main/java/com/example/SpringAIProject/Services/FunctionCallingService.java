package com.example.SpringAIProject.Services;


import com.example.SpringAIProject.Config.FunctionConfig;
import com.example.SpringAIProject.model.SuggestedResponse;
import com.example.SpringAIProject.model.TicketAnalyserAI;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class FunctionCallingService {

    @Autowired
    ModelService modelService;

    @Autowired
    FunctionConfig functionConfig;




    public String chatWithOderTracking(String usermessage, String provider, String model) {


        ChatClient chatClient = modelService.getChatClient(provider);
        return chatClient.prompt()
                .options(OpenAiChatOptions.builder()
                        .model(model)
                        .build()
                )
                .tools(functionConfig)
                .call()
                .content();

    }

    // tool related information
//        {
//            "type": "function",
//                "function": {
//            "name": "getOrderStatus",
//                    "description": "Get order status by ID",
//                    "parameters": {
//                "type": "object",
//                        "properties": {
//                    "orderId": {
//                        "type": "string",
//                                "description": "Order ID"
//                    }
//                },
//                "required": ["orderId"]
//            }


}
