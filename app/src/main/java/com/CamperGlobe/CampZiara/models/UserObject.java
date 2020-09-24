package com.CamperGlobe.CampZiara.models;

public class UserObject {

    private String uId,  name, PhoneNum;

    public UserObject(String UId, String name, String phoneNum) {
        this.name = name;
        PhoneNum = phoneNum;
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

    public void setName(String name) { this.name = name; }
}
