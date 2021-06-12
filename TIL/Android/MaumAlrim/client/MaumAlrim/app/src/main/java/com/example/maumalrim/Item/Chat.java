package com.example.maumalrim.Item;

public class Chat {
    String userChattext;
    String userEmail; //나중에 이미지url 추가

    public Chat(String userChattext, String userEmail) {
        this.userChattext = userChattext;
        this.userEmail = userEmail;
    }

    public String getUserChattext() {
        return userChattext;
    }

    public void setUserChattext(String userChattext) {
        this.userChattext = userChattext;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
