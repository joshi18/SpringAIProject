package com.example.SpringAIProject.Config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MultiModelConfig {


    @Value("${gemini.api.apikey}")
    private String geminiapikey;

    @Value("${gemini.api.url}")
    private String geminiurl;

    @Value("${gemini.api.model}")
    private  String googleGeminiModel;


    @Bean(value = "openaiBean")
    @Primary
    public ChatClient getBeanOfOpenApi(OpenAiChatModel openAiChatModel){
           ChatClient chatClient = ChatClient.create(openAiChatModel);
           return  chatClient;
    }

    @Bean(value = "googleGeminiBean")
    public ChatClient getBeanOfGoogleGemini(OpenAiChatModel openAiChatModel){

        OpenAiApi goofleGeminiApi  =  OpenAiApi
                .builder()
                .apiKey(geminiapikey)
                .baseUrl(geminiurl)
                .build();

        OpenAiChatModel geminiModel = OpenAiChatModel.builder()
                .openAiApi(goofleGeminiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(googleGeminiModel)
                        .build())
                .build();
        ChatClient Client = ChatClient.create(geminiModel);

        return Client;


    }
}
