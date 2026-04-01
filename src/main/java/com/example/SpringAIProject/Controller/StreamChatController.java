package com.example.SpringAIProject.Controller;

import com.example.SpringAIProject.Services.MultiModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/streamApi")
public class StreamChatController {

    private static final Logger log = LoggerFactory.getLogger(StreamChatController.class);
    @Autowired
    private ChatModel chatModel;

    @Autowired
    private MultiModelService multiModelService;

//    public ChatController(ChatModel chatModel) {
//        this.chatClient = ChatClient.builder(chatModel).build();
//    }



    @GetMapping("/getModels")
    public Map<String, Object> getAllAvaibleModels(){

        return Map.of("Models", List.of(Map.of("name","chatgpt","descrption","It all about the chagpt"),Map.of("name","GoogleGemini","description","Its all about the google gemini")));

    }


    @PostMapping("/chat/streamApi")
    public Flux<String> getResponse(

            @RequestHeader(value = "AI-PROVIDER",defaultValue = "openai") String provider,
            @RequestHeader(value = "AI-MODEL", defaultValue = "gpt-nano") String model,
            @RequestBody String message){


        log.info("<==Request Received ==>");
        log.info("Provider: {}", provider);
        log.info("Model: {}", model);



        ChatClient chatClient = multiModelService.serviceProvider(provider);


            if (model.isEmpty() || model.isBlank()){
                return chatClient.prompt().user(message)
                .options( OpenAiChatOptions.builder().model(model)
                        .build())
                        //.call().
                        .stream()
                        .content();
               // call()=>  This will give the entire response in one go MEANS it will wait to get the response from the opanai/gemini
               // sync() communication
            }
            else {
                return chatClient.prompt().user(message)
                        .stream().content();
            }



//        ChatClient chatClient = ChatClient.builder(chatModel).build();
//
//        return chatClient.prompt().user(message)
//                .options( OpenAiChatOptions.builder().model(model)
//                        .build())
//                .call().content();


    }






}
