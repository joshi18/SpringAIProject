package com.example.SpringAIProject.Services;


import com.example.SpringAIProject.model.SuggestedResponse;
import com.example.SpringAIProject.model.TicketAnalyserAI;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class TicketAnalysisService {

    private  final ModelService modelService;
    private final ResourceLoader resourceLoader;

    public TicketAnalysisService(ModelService modelService, ResourceLoader resourceLoader) {
        this.modelService = modelService;
        this.resourceLoader = resourceLoader;
    }


    public TicketAnalyserAI analyseTicket(String ticketText, String provider,String model){

        // here with help of provider we are getting object of chat client.
        // OpenAi , Gemini, Anthropic Cloud

        ChatClient chatClient = modelService.getChatClient(provider);

        // Create a ticket analyser Propmt using different method
        // Input will be ticketText and out will be prompt in the format (PROMPT)

        Prompt prompt = createAnalysisPrompt(ticketText);
        return chatClient.prompt(prompt)
                .options(OpenAiChatOptions.builder()
                        .model(model)
                        .build()
                )
                .call()
                .entity(TicketAnalyserAI.class);

    }
    public List<SuggestedResponse> analyseTicketOnUrgentBasis(TicketAnalyserAI ticketAnalyserAI, String provider,String model){

        ChatClient chatClient = modelService.getChatClient(provider);

        // Create a ticket analyser Propmt using different method
        // Input will be ticketText and out will be prompt in the format (PROMPT)

        Prompt prompt = createAnalysisResponses(ticketAnalyserAI);
        return chatClient.prompt(prompt)
                .options(OpenAiChatOptions.builder()
                        .model(model)
                        .build()
                )
                .call()
                .entity(new ParameterizedTypeReference<List<SuggestedResponse>>() {
                });

    }

    private Prompt createAnalysisPrompt(String ticketText) {

        // load template from classpath
        String templateContent  = loadTemplate("classpath:templates/ticket-review.txt"); //  with help of ClassPath it loads the template

        // Create Spring AI prompt Template

        PromptTemplate promptTemplate = new PromptTemplate(templateContent);

        Map<String,Object>  variables = Map.of(
                "tickectText", ticketText
                //"placeHolderInTheTemplate", PlaceHolderValue
        );

        return promptTemplate.create(variables);



    }
    private Prompt createAnalysisResponses(TicketAnalyserAI ticketAnalyserAI) {

        // load template from classpath
        String templateContent  = loadTemplate("classpath:templates/ticket-review-respsonses.txt"); //  with help of ClassPath it loads the template

        // Create Spring AI prompt Template

        PromptTemplate promptTemplate = new PromptTemplate(templateContent);

        Map<String,Object>  variables = Map.of(
                "Analysis", ticketAnalyserAI.getSummary(),
                "Priority", ticketAnalyserAI.getPriority()
                //"placeHolderInTheTemplate", PlaceHolderValue
        );

        return promptTemplate.create(variables);



    }

    private String loadTemplate(String path) {
        try {
            // Load resource from classpath (works in JAR and IDE)
            Resource resource = resourceLoader.getResource(
                    path
            );
            // Read entire file content as UTF-8 string
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Fail fast with clear error message
            // In production, consider custom exception for better error handling
            throw new RuntimeException("Failed to load code review template", e);
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

}
