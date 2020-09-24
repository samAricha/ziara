package com.CamperGlobe.CampZiara.models;

public class ChatObject {

    private String chatId;

    public ChatObject(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "ChatObject{" +
                "chatId='" + chatId + '\'' +
                '}';
    }

    public String getChatId() {
        return chatId;
    }
}
