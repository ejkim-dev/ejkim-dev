package com.example.maumalrim.Item;

//채팅상담 히스토리 관련 아이템 : 상담사 프로필사진, 상담사이름, 상담유형, 상담상태 : 상담중 or 상담종료(yyyy-mm-dd)
public class ChatHistory {
     String name;
//     String profileUrl;
     String category;
     String state;

    public ChatHistory(String name,  String category, String state) { //String profileUrl,
        this.name = name;
//        this.profileUrl = profileUrl;
        this.category = category;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
