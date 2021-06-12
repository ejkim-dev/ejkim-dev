package com.example.maumalrim.Item;

public class EmployeeInfo {
    /*로그인한 유저를 담는 저장소.
      1. 상담사 :
      2. 추후 사진 추가*/
    String master_id;
    String user_phone;//상담사는 phone, 사용자는 e-mail
    String user_nickname;
    String reg_time;//회원가입일시
    String is_cert;//인증받았는지 여부, 상담사만 필요함
    String is_status;//회원 여부(탈퇴시 false)
    String is_super;//어드민확인


    public EmployeeInfo(String masterId, String userId, String userNickname, String isCert, String isStatus, String isSuper, String reg_time) {
        this.master_id = masterId;
        this.user_phone = userId;
        this.user_nickname = userNickname;
        this.is_cert = isCert;
        this.is_status = isStatus;
        this.is_super = isSuper;
        this.reg_time = reg_time;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getIs_cert() {
        return is_cert;
    }

    public void setIs_cert(String is_cert) {
        this.is_cert = is_cert;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    public String getIs_super() {
        return is_super;
    }

    public void setIs_super(String is_super) {
        this.is_super = is_super;
    }


    @Override
    public String toString() {
        return "EmployeeInfo{" +
                "master_id='" + master_id + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", is_cert='" + is_cert + '\'' +
                ", is_status='" + is_status + '\'' +
                ", is_super='" + is_super +
                '}';
    }
}
