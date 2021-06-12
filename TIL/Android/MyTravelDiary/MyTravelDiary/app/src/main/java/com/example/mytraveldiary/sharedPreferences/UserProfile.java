package com.example.mytraveldiary.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mytraveldiary.list.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*SharedPreferences는 데이터를 타입(String, int, .. 등)에 따라 관리하기 때문에 따로 클래스로 만들어서 사용하면 편리함.
한번 만들어 두면 다른 프로젝트에 그대로 복사해서 사용이 가능.*/

/*데이터 저장 및 로드 클래스 : 회원가입*/
public class UserProfile {

    private static final String PREFERENCES_NAME = "UserProfile";
    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;
    private static final long DEFAULT_VALUE_LONG = -1L;
    private static final float DEFAULT_VALUE_FLOAT = -1F;


    //데이터를 저장할 때는 SharedPreferences를 사용하며 sharedPreferences 문자열을 저장소의 이름으로 사용
    //SharedPreferences 객체를 사용하려면 getSharedPreferences 메서드로 참조함
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * String 값 저장
     * @param context
     * @param key
     * @param value*/
    public static void setString(Context context, String key, String value) {
       /*getPreferences(): 하나의 shared preference 파일을 사용한다면 Activity에서 호출하여 해당하는 파일을 가져올 수 있다. 이 때에는 Activity에 기본적으로 할당된 shared preference를 가져오므로 이름을 별도로 제공하지 않아도 된다.*/
        SharedPreferences prefs = getPreferences(context);//Editor 객체는 데이터를 저장할 수 있도록 edit() 메서드를 제공하는데 edit() 메서드를 호출한 후 putString() 메서드로 저장하려는 데이터를 설정할 수 있음

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * boolean 값 저장
     * @param context
     * @param key
     * @param value*/
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * int 값 저장
     * @param context
     * @param key
     * @param value*/
    public static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * long 값 저장
     * @param context
     * @param key
     * @param value*/
    public static void setLong(Context context, String key, long value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * float 값 저장
     * @param context
     * @param key
     * @param value*/
    public static void setFloat(Context context, String key, float value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * String 값 로드
     * @param context
     * @param key
     * @return*/
    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    /**
     * boolean 값 로드
     * @param context
     * @param key
     * @return*/
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
        return value;
    }

    /**
     * int 값 로드
     * @param context
     * @param key
     * @return*/
    public static int getInt(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, DEFAULT_VALUE_INT);
        return value;
    }

    /**
     * long 값 로드
     * @param context
     * @param key
     * @return*/
    public static long getLong(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        long value = prefs.getLong(key, DEFAULT_VALUE_LONG);
        return value;
    }

    /**
     * float 값 로드
     * @param context
     * @param key
     * @return*/
    public static float getFloat(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        float value = prefs.getFloat(key, DEFAULT_VALUE_FLOAT);
        return value;
    }

    /**
     * 키 값 삭제
     * @param context
     * @param key*/
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.apply();
    }

    /**
     * 모든 저장 데이터 삭제
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();
    }

    /**Array 저장
     * @param context
     * @param key
     * @param arrayList
     */
    public static void saveArr(Context context, String key, ArrayList arrayList){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);//items에 있는 자바 언어를 gson을 이용해 json으로 변환시켜줌

        edit.putString(key, json);
        edit.apply();

    }


    public static ArrayList loadArr(Context context, String key){

/*   Gson gson = new Gson();
     String json = UserProfile.getString(mContext, LoginActivity.userID);//test : json에서 가져올 key와 기본값 설정
      Type type = new TypeToken<ArrayList<Profile>>(){}.getType();//test
      userProfile = gson.fromJson(json, type);//test*/

        ArrayList arrayList = new ArrayList();
        SharedPreferences prefs = getPreferences(context);

        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        Type type = new TypeToken<ArrayList<Profile>>(){}.getType();
        arrayList = gson.fromJson(json, type);
        if (arrayList == null){arrayList = new ArrayList<>();}

        return arrayList;
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










