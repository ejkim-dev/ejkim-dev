package com.example.maumalrim.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//자동로그인을 위한 쉐어드 : 로그인하고 나면 ID를 저장함
public class AutoLogin {
    private static final String PREF_USER_NAME = "username";//저장소 이름("username")

    // 모든 엑티비티에서 인스턴스를 얻기위함
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장 : 로그인 시 자동 로그인 여부에 따라 호출 될 메소드이다. userName이 저장된다.
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    // 저장된 정보 가져오기 : 현재 저장된 정보를 가져오기 위한 메소드이다.
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    // 로그아웃 : 자동 로그인 해제 및 로그아웃 시 호출 될 메소드이다.
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

    //로그인하면 저장됨. ID에 @가 있는지 여부에 따라 arrylist에 다른 클래스를 담을 예정

}