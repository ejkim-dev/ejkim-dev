package com.example.maumalrim.Item;

public class UserList {

    String type;//대기중,상담중,상담종료,퇴장
    String userId;
    String userNickName;
    String userCategory;
    String userMessage;
    String startTime;//시작시간
    String finishTime;//종료시간


    public UserList(String type, String userId, String userNickName, String userCategory, String userMessage, String startTime, String finishTime) {
        this.type = type;
        this.userId = userId;
        this.userNickName = userNickName;
        this.userCategory = userCategory;
        this.userMessage = userMessage;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
