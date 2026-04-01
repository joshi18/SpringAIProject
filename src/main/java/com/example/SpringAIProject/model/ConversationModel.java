package com.example.SpringAIProject.model;


import org.springframework.ai.chat.messages.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConversationModel {


    private String conversionID;
    private List<Message> messageList;
    private LocalDateTime createdAt;
    private  LocalDateTime uodatedAt;
    private int totaltoken;

    public ConversationModel(String conversionID) {
        this.conversionID = conversionID;
        this.messageList = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.uodatedAt = LocalDateTime.now();
        this.totaltoken = 0;
    }


    public String getConversionID() {
        return conversionID;
    }

    public void setConversionID(String conversionID) {
        this.conversionID = conversionID;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUodatedAt() {
        return uodatedAt;
    }

    public void setUodatedAt(LocalDateTime uodatedAt) {
        this.uodatedAt = uodatedAt;
    }

    public int getTotaltoken() {
        return totaltoken;
    }

    public void setTotaltoken(int totaltoken) {
        this.totaltoken = totaltoken;
    }

    public void addMessage(Message message){
        this.messageList.add(message);
        this.uodatedAt = LocalDateTime.now();
        this.totaltoken += message.getText().length() /4;

    }



}
