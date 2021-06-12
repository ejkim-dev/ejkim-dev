package com.example.mytraveldiary.list;

public class Game {
    private String ranNum;
    private int backColor;

    public Game(String ranNum, int backColor) {
        this.ranNum = ranNum;
        this.backColor = backColor;
    }

    public String getRanNum() {
        return ranNum;
    }

    public void setRanNum(String ranNum) {
        this.ranNum = ranNum;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }
}
