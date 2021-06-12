package com.example.maumalrim.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.maumalrim.Item.ChatRoom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MyChatRoom {

    private static final String PREF_USER_NAME = "MyChatRoom";//저장소 이름("username")

    // 모든 엑티비티에서 인스턴스를 얻기위함 : 마이페이지에서 수정할 경우 바로 변경됨(쉐어드와 http통신연결해서 sql 둘다 변경)
    static SharedPreferences getSharedPreferences(Context ctx) {
        return  ctx.getSharedPreferences(PREF_USER_NAME, Context.MODE_PRIVATE);
    }

    // 계정 정보 저장 - mysql에서 가져온 ArrayList 유저정보(상담사 or 사용자 회원가입 정보)를 저장하기 위함
    public static void setArr(Context ctx, ArrayList arrayList) {
        SharedPreferences preferences = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();

        //저장하는 곳
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString(PREF_USER_NAME, json);

        editor.commit();
    }


    // 계정 정보 저장 - ArrayList 유저정보를 가져옴
    public static ArrayList getArr(Context ctx){

        SharedPreferences preferences = getSharedPreferences(ctx);

        try{
            Gson gson = new Gson();
            String json = preferences.getString(PREF_USER_NAME, "");

            Type type = new TypeToken<ArrayList<ChatRoom>>(){}.getType();
            ArrayList arrayList = gson.fromJson(json, type);
            return arrayList;

        }catch (NullPointerException e){
            ArrayList arrayList = new ArrayList<>();
            return arrayList;
        }
    }

    //key만 삭제한다.
    public static void clearChatRoom(Context ctx) {

        SharedPreferences.Editor edit = getSharedPreferences(ctx).edit();
        edit.remove(PREF_USER_NAME);
        edit.commit();
    }

    // 전체 쉐어드를 삭제한다
    public static void clearAll(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

    //Key의 갯수 - 채팅창 저장할 때 방 번호(key값)를 저장하기 위해서
    public static int KeysNum(Context ctx){
        int value = 0;
        SharedPreferences preferences = getSharedPreferences(ctx);

        //key의 갯수
        Collection<?> col = preferences.getAll().keySet();
        Iterator<?> iterator = col.iterator();//이터럴 객체 iterator은 모든 객체를 가져온다

        while (iterator.hasNext()){
            value++;
        }
        return  value;
    }
}
