package com.CamperGlobe.CampZiara.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatObject implements Serializable {

    private String chatId;
    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId)  {
        this.chatId = chatId;
    }

    public void addUserToArrayList(UserObject mUser) {
        userObjectArrayList.add(mUser);
    }




    public String getChatId() {
        return chatId;
    }
    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }




    @Override
    public String toString() {
        return "ChatObject{" +
                "chatId='" + chatId + '\'' +
                '}';
    }
}
