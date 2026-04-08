package com.example.SpringAIProject.Controller;

import com.example.SpringAIProject.Services.TicketAnalysisService;
import com.example.SpringAIProject.model.SuggestedResponse;
import com.example.SpringAIProject.model.TicketAnalyserAI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public class TicketAnalyserAiController {


    @Autowired
    TicketAnalysisService ticketAnalysisService;

    @PostMapping
    public Map<String, Object> reviewCode(
            @RequestBody Map<String, String> request,
            @RequestParam(defaultValue = "openai") String provider,
            @RequestParam(defaultValue = "gpt-5") String model
    )
    {
        // here we are getting tickect Text from the request
        String ticketText = request.get("ticketText");

       TicketAnalyserAI  ticketAnalyserAI= ticketAnalysisService.analyseTicket(ticketText,provider,model);

        System.out.println(ticketAnalyserAI.getTicketId());

        if (ticketAnalyserAI.getTickerLevel().equals("HIGH")){
             List<SuggestedResponse> suggestedResponse = ticketAnalysisService.analyseTicketOnUrgentBasis(ticketAnalyserAI,provider,model);

            return Map.of("analysis" , ticketAnalyserAI,
                    "prioirty" , ticketAnalyserAI.getTicketId(),
                    "SuggestedResponse" , suggestedResponse,
                    "level", ticketAnalyserAI.getTickerLevel());
        }

        return Map.of("analysis" , ticketAnalyserAI,
                "prioirty" , ticketAnalyserAI.getTicketId(),
                "level", ticketAnalyserAI.getTickerLevel());

    }


}
