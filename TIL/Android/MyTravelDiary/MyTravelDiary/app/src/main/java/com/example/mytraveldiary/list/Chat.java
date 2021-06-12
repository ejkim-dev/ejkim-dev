package com.example.mytraveldiary.list;

public class Chat {
    private String content;
    private int viewType;

    public Chat(String content,int viewType) {
        this.content = content;
        this.viewType = viewType;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
