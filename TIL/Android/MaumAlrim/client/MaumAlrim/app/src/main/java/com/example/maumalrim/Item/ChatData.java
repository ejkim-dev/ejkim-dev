package com.example.maumalrim.Item;

public class ChatData {

    String master_id;
    String room_id;
    String user_text;
    String user_id;

    public ChatData(String master_id, String room_id, String user_text, String user_id) {
        this.master_id = master_id;
        this.room_id = room_id;
        this.user_text = user_text;
        this.user_id = user_id;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getUser_text() {
        return user_text;
    }

    public void setUser_text(String user_text) {
        this.user_text = user_text;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "master_id='" + master_id + '\'' +
                ", room_id='" + room_id + '\'' +
                ", user_text='" + user_text + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
