package com.CamperGlobe.CampZiara.models;

import java.io.Serializable;

public class UserObject implements Serializable {

    private String uId,  name, PhoneNum, notificationKey;

    private Boolean selected = false;

    public UserObject(String uId) {
        this.uId = uId;

    }

    public UserObject(String uId, String name, String phoneNum) {
        this.uId = uId;
        this.name = name;
        this.PhoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "uId='" + uId + '\'' +
                ", name='" + name + '\'' +
                ", PhoneNum='" + PhoneNum + '\'' +
                '}';
    }

    public String getuId() { return uId; }

    public String getName() { return name; }

    public String getPhoneNum() { return PhoneNum; }

    public String getNotificationKey() { return notificationKey; }

    public Boolean getSelected() { return selected; }

    public void setSelected(Boolean selected) { this.selected = selected; }

    public void setName(String name) { this.name = name; }

    public void setNotificationKey(String notificationKey) { this.notificationKey = notificationKey; }


}
