package com.example.maumalrim.Item;

public class Diary {
    String master_id;
    String user_id;
    String user_title;
    String user_condition;
    String user_text;
    String reg_time;
    String is_status;


    public Diary(String master_id, String user_id, String user_title, String user_condition, String user_text, String reg_time, String is_status) {
        this.master_id = master_id;
        this.user_id = user_id;
        this.user_title = user_title;
        this.user_condition = user_condition;
        this.user_text = user_text;
        this.reg_time = reg_time;
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

    public String getUser_title() {
        return user_title;
    }

    public void setUser_title(String user_title) {
        this.user_title = user_title;
    }

    public String getUser_condition() {
        return user_condition;
    }

    public void setUser_condition(String user_condition) {
        this.user_condition = user_condition;
    }

    public String getUser_text() {
        return user_text;
    }

    public void setUser_text(String user_text) {
        this.user_text = user_text;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "master_id='" + master_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_title='" + user_title + '\'' +
                ", user_condition='" + user_condition + '\'' +
                ", user_text='" + user_text + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", is_status='" + is_status + '\'' +
                '}';
    }
}
