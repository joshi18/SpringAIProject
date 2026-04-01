package com.example.SpringAIProject.Services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MultiModelService {


    private static final Logger log = LoggerFactory.getLogger(MultiModelService.class);
    @Autowired
    @Qualifier("openaiBean")
    private ChatClient openaiChatclient;

    @Autowired
    @Qualifier("googleGeminiBean")
    private ChatClient geminiChatclient;


    public ChatClient serviceProvider(String provider){

        log.info("Inside the service Class");

        if (provider.isBlank() || provider.isEmpty()){
            return openaiChatclient;
        }
        return
                switch (provider.toLowerCase()) {

                    case "openai" -> {
                        yield openaiChatclient;

                    }
                    case "gemini" -> {
                        yield geminiChatclient;


                    }
                    default -> throw new IllegalArgumentException("Unkown Exception" + provider){


                    };


                };


    }



}
