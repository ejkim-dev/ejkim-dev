package com.example.maumalrim.Common;

import android.util.Log;

import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.Item.UserList;

import java.util.ArrayList;

public class StaticValue {
    public static boolean isDebug = false;
    public static String MyServer_IP = "https://서버주소/heartbellServer_php";
    public static String request = "null";//요청
    public static String recipientId = "null";//받는사람 id, 채팅방 클릭하면 연결되고, 나갈때 리셋되어야함

    //접속 내담자 확인용 채팅방 : 상담사가 소켓이 연결되면, 내담자의 접속 리스트를 받을 수 있다.
    public static ArrayList<UserList> userLists = new ArrayList<>();

    //현재 채팅창 : 현재 작성중인 채팅창이며, 채팅이 완료되면 쉐어드에 저장할 예정
    public static ArrayList<Chat> chats = new ArrayList<>();

    //챗봇 임시 : 챗봇 클래스와 소통하기위해
    public static ArrayList<Chat> chatArrayList = new ArrayList<>();

    //유저가 채팅창을 먼저 꺼야 상담사가 종료할 수 있음
    public static boolean isUserGone = false;

    //상담 접수지
    public static ArrayList<String> counselingReceipt = new ArrayList<>();

    public static void Println(String TAG, String Message){
        if (isDebug){
            Log.d(TAG, Message);
        }
    }

    //JsonArryname
    public static String getJsonName = "webnautes";

    // 챗봇 api
    public static String apikey = apikey ";
    public static String version ="2020-04-01";
    public static String url = "https://api.kr-seo.assistant.watson.cloud.ibm.com/instances/제공받은주소";
    public static String assistant_id = assistant_id";

    //일기챗봇
    public static String assistant_id_daily = "assistant_id_daily ";
}
