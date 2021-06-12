package com.example.mytraveldiary.list;

public class AlbumContents {
    String albumImg;//앨범에 들어가는 사진
    String albumText;
    String textVisibility;//visible / gone

    //생성자
    public AlbumContents(String albumImg, String albumText, String textVisibility) {
        this.albumImg = albumImg;
        this.albumText = albumText;
        this.textVisibility = textVisibility;
    }

    public String getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(String albumImg) {
        this.albumImg = albumImg;
    }

    public String getAlbumText() {
        return albumText;
    }

    public void setAlbumText(String albumText) {
        this.albumText = albumText;
    }

    public String getTextVisibility() {
        return textVisibility;
    }

    public void setTextVisibility(String textVisibility) {
        this.textVisibility = textVisibility;
    }
}
