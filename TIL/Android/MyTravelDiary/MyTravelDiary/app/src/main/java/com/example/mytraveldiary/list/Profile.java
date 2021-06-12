package com.example.mytraveldiary.list;

import com.example.mytraveldiary.LoginActivity;

public class Profile {
//    String userID;
    String userPicture;
    String userName;
    String userMessage;

    public Profile(String userPicture, String userName, String userMessage) {
//        this.userID = LoginActivity.userID;
        this.userPicture = userPicture;
        this.userName = userName;
        this.userMessage = userMessage;
    }

/*    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }*/

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
