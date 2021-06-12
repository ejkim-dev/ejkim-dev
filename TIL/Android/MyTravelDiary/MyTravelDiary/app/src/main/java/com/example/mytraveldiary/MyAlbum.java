package com.example.mytraveldiary;

//리스트 데이터들이 들어갈 것을 선언해야함 : 메인사진과 제목
public class MyAlbum {
    String title;
    String titleImgUri;//json에 넣기위해 String으로 변환 시켜줌
    //공개 비공개 설정
    String privateAlbum;
    String location;


    //생성자 : 구조를 만들어야 함
    public MyAlbum(String title,  String titleImgUri, String privateAlbum, String location) {

            this.title = title;
            this.titleImgUri = titleImgUri;
            this.privateAlbum = privateAlbum;
            this.location = location;

    }

    public String getTitleImgUri() {
        return titleImgUri;
    }

    public void setTitleImgUri(String titleImgUri) {
        this.titleImgUri = titleImgUri;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getPrivateAlbum() {
        return privateAlbum;
    }

    public void setPrivateAlbum(String privateAlbum) {
        this.privateAlbum = privateAlbum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
