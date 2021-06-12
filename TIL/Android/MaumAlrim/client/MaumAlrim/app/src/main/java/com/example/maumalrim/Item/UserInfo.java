package com.example.maumalrim.Item;

public class UserInfo {
    /*로그인한 유저를 담는 저장소.
        1. 유저일 경우 : 마스터ID, 유저닉네임, 유저이메일, 생년, 성별, 직업, 인증여부, 회원여부, 가입시간 담음
        2. 추후 유저 사진 추가*/
    String master_id;
    String user_email;//상담사는 phone, 사용자는 e-mail
    String user_nickname;
    String birth_year;//유저용 : 생년
    String gender;
    String job;
    String reg_time;//회원가입일시
    String is_cert;//인증받았는지 여부, 상담사만 필요함
    String is_status;//회원 여부(탈퇴시 false)

    public UserInfo(String masterId, String userId, String userNickname, String birthYear, String gender, String job, String reg_time, String isCert, String isStatus) {
        this.master_id = masterId;
        this.user_email = userId;
        this.user_nickname = userNickname;
        this.birth_year = birthYear;
        this.gender = gender;
        this.job = job;
        this.reg_time = reg_time;
        this.is_cert = isCert;
        this.is_status = isStatus;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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


    @Override
    public String toString() {
        return "UserInfo{" +
                "master_id='" + master_id + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", birth_year='" + birth_year + '\'' +
                ", gender='" + gender + '\'' +
                ", job='" + job + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", is_cert='" + is_cert + '\'' +
                ", is_status='" + is_status +
                '}';
    }
}
