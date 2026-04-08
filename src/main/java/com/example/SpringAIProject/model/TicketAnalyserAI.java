package com.example.SpringAIProject.model;


public class TicketAnalyserAI {


    private String ticketId;
    private String tickerLevel;
    private String keyIssues;
    private String suggestedTteam;
    private String priority;
    private String Summary;

    public TicketAnalyserAI() {
    }

    public TicketAnalyserAI(String ticketId, String tickerLevel, String keyIssues, String suggestedTteam, String priority, String summary) {
        this.ticketId = ticketId;
        this.tickerLevel = tickerLevel;
        this.keyIssues = keyIssues;
        this.suggestedTteam = suggestedTteam;
        this.priority = priority;
        Summary = summary;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTickerLevel() {
        return tickerLevel;
    }

    public void setTickerLevel(String tickerLevel) {
        this.tickerLevel = tickerLevel;
    }

    public String getKeyIssues() {
        return keyIssues;
    }

    public void setKeyIssues(String keyIssues) {
        this.keyIssues = keyIssues;
    }

    public String getSuggestedTteam() {
        return suggestedTteam;
    }

    public void setSuggestedTteam(String suggestedTteam) {
        this.suggestedTteam = suggestedTteam;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }
}
