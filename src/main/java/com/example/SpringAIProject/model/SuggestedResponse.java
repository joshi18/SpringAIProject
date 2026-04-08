package com.example.SpringAIProject.model;

public class SuggestedResponse {


    private String tone;
    private String estimitaedTime;
    private String responseText;

    public SuggestedResponse(String tone, String estimitaedTime, String responseText) {
        this.tone = tone;
        this.estimitaedTime = estimitaedTime;
        this.responseText = responseText;
    }

    public SuggestedResponse() {
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getEstimitaedTime() {
        return estimitaedTime;
    }

    public void setEstimitaedTime(String estimitaedTime) {
        this.estimitaedTime = estimitaedTime;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
