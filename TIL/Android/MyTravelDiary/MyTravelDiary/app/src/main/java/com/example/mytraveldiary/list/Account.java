package com.example.mytraveldiary.list;

//리스트 데이터들이 들어갈 것을 선언해야함 : 로그인 이메일, 비밀번호
public class Account {
    String email;
    String password;

    //생성자 : 구조를 만들어야 함
    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
