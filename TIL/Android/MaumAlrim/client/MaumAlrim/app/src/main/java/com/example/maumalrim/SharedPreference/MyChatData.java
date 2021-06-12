package com.example.maumalrim.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.maumalrim.Item.ChatData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyChatData {
    //ChatRoom 번호를 key(저장소 이름)값으로
    private static final String PREFERENCES_NAME = "MyChatData";

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    //키값 삭제
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.apply();
    }

    //모든 저장 데이터 삭제
    public static void clear(Context context){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();
    }

    public static void setArr(Context context, String key, ArrayList arrayList){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);//items에 있는 자바 언어를 gson을 이용해 json으로 변환시켜줌

        edit.putString(key, json);
        edit.apply();

    }

    public static ArrayList getArr(Context context, String key){

//        ArrayList arrayList = new ArrayList();
        SharedPreferences prefs = getPreferences(context);

        try{
            Gson gson = new Gson();
            String json = prefs.getString(key, "");
            Type type = new TypeToken<ArrayList<ChatData>>(){}.getType();
            ArrayList arrayList = gson.fromJson(json, type);
            if (arrayList == null){arrayList = new ArrayList<>();}

            return arrayList;

        } catch (NullPointerException e){
            ArrayList arrayList = new ArrayList<>();
            return arrayList;
        }
    }

    /*    //키 값 없이 모든 저장값 가져오기
    public static void allValue(Context context){
        SharedPreferences prefs = getPreferences(context);
        Collection<?> col = prefs.getAll().values();
        Iterator<?> it = col.iterator();
        while (it.hasNext()){
            String msg = (String) it.next();
        }
    }*/
}
