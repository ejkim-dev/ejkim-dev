package com.example.maumalrim.Item;

public class ChatRoom {
    //채팅방 번호는 index 번호로 함
    String master_id;
    String user_id;
    String user_name;
    String room_id;
    String other_id;
    String other_name;
    String chat_category;
    String contents;
    String summary;
    String end_time;
    String is_public;
    String is_status;

    public ChatRoom(String master_id, String user_id, String user_name, String room_id, String other_id, String other_name, String chat_category, String contents, String summary, String end_time, String is_public, String is_status) {
        this.master_id = master_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.room_id = room_id;
        this.other_id = other_id;
        this.other_name = other_name;
        this.chat_category = chat_category;
        this.contents = contents;
        this.summary = summary;
        this.end_time = end_time;
        this.is_public = is_public;
        this.is_status = is_status;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getChat_category() {
        return chat_category;
    }

    public void setChat_category(String chat_category) {
        this.chat_category = chat_category;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "master_id='" + master_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", room_id='" + room_id + '\'' +
                ", other_id='" + other_id + '\'' +
                ", other_name='" + other_name + '\'' +
                ", chat_category='" + chat_category + '\'' +
                ", contents='" + contents + '\'' +
                ", summary='" + summary + '\'' +
                ", end_time='" + end_time + '\'' +
                ", is_public='" + is_public + '\'' +
                ", is_status='" + is_status + '\'' +
                '}';
    }
}
