package com.example.maumalrim.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.Item.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Information {

        private static final String PREF_USER_NAME = "userInformation";//저장소 이름("username")

        // 모든 엑티비티에서 인스턴스를 얻기위함 : 마이페이지에서 수정할 경우 바로 변경됨(쉐어드와 http통신연결해서 sql 둘다 변경)
        static SharedPreferences getSharedPreferences(Context ctx) {
            return  PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        // 계정 정보 저장 - mysql에서 가져온 ArrayList 유저정보(상담사 or 사용자 회원가입 정보)를 저장하기 위함
        public static void setArr(Context ctx, ArrayList arrayList) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

            //저장하는 곳
            Gson gson = new Gson();
            String json = gson.toJson(arrayList);
            editor.putString(PREF_USER_NAME, json);

            editor.commit();
        }


    // 계정 정보 저장 - ArrayList 유저정보를 가져옴
        public static ArrayList getArr(Context ctx){
            ArrayList arrayList;
            SharedPreferences preferences = getSharedPreferences(ctx);

            try{
                Gson gson = new Gson();
                String json = preferences.getString(PREF_USER_NAME, "");

                //유저일경우
                if (AutoLogin.getUserName(ctx).contains("@")){
                    Type type = new TypeToken<ArrayList<UserInfo>>(){}.getType();
                    arrayList = gson.fromJson(json, type);
                    return arrayList;
                }
                else {//상담사일 경우
                    Type type = new TypeToken<ArrayList<EmployeeInfo>>(){}.getType();
                    arrayList = gson.fromJson(json, type);
                    return arrayList;
                }

            }catch (NullPointerException e){
                arrayList = new ArrayList<>();
                return arrayList;
            }
        }


        // 로그아웃 : 자동 로그인 해제 및 로그아웃 시 호출 될 메소드이다.
        public static void clearUserName(Context ctx) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.clear();
            editor.commit();
        }


    }


