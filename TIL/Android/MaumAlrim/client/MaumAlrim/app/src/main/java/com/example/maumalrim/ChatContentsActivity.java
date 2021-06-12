package com.example.maumalrim;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maumalrim.Adapter.MainChat;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.ChatRoom;
import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivityChatContentsBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatContentsActivity extends AppCompatActivity {
    private static final String TAG = "ChatContentsActivity";
    static RequestQueue requestQueue;

    MainChat chatAdapter;
    String userChatId;
    String userNickname;
    ActivityChatContentsBinding activityChatContentsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 시작");
        //쉐어드 저장내용 불러오기(확인용)
        ArrayList<EmployeeInfo> employeeInfos = Information.getArr(getApplicationContext());
        userNickname = employeeInfos.get(0).getUser_nickname();
        userChatId = employeeInfos.get(0).getUser_phone();

        Log.d(TAG, "Information 닉네임 : "+userNickname+" | ID : "+userChatId);

        activityChatContentsBinding = ActivityChatContentsBinding.inflate(getLayoutInflater());
        View view = activityChatContentsBinding.getRoot();
        setContentView(view);

        activityChatContentsBinding.rvChatPart.setHasFixedSize(true);
        chatAdapter = new MainChat(StaticValue.chats,userChatId);

        //채팅리사이클러뷰에 채팅어뎁터 인스턴스를 추가한다.
        activityChatContentsBinding.rvChatPart.setAdapter(chatAdapter);

        chatAdapter.notifyDataSetChanged();
        activityChatContentsBinding.rvChatPart.scrollToPosition(StaticValue.chats.size()-1);

        if (requestQueue == null){
            //RequestQueue 객체 생성하기
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//입력창 가리지 않기
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 시작");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 시작");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 시작");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 시작");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 시작");
    }

    public void onSubmit(View view) {
        Log.d(TAG, "onSubmit: 시작");
        //제출을 누르면 내담자에게 서머리가 전송됨
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("상담 요약 제출").setMessage("내담자에게 해당 상담 요약내용을 전달하겠습니까?");

        builder.setPositiveButton("제출", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (AutoLogin.getUserName(getApplicationContext()).contains("@")){

                }
                else {//상담사일 때 -> HTTP로 채팅 데이터 저장해야함.
                    Gson gson = new Gson();

                    ArrayList<ChatRoom> chatRooms = new ArrayList<>();
                        //채팅방 서버에 저장 : user_id / user_name / other_id /other_name /chat_category
                        for (int i = StaticValue.userLists.size()-1; i >= 0; i--){
                            if (StaticValue.userLists.get(i).getUserId().equals(StaticValue.recipientId)){
                                Log.d(TAG, "onClick: 현재 유저 채팅방 정보");

                                Log.d(TAG, "onClick: 타입 - "+StaticValue.userLists.get(i).getType()+
                                        "\nID - "+StaticValue.userLists.get(i).getUserId() + //other_id
                                        "\n닉네임 - "+StaticValue.userLists.get(i).getUserNickName()+
                                        "\n카테고리 - "+StaticValue.userLists.get(i).getUserCategory()+
                                        "\n시작시간 - "+StaticValue.userLists.get(i).getStartTime()+
                                        "\n종료시간 - "+StaticValue.userLists.get(i).getFinishTime());
                                chatRooms.add(new ChatRoom("",userChatId,userNickname,"",StaticValue.userLists.get(i).getUserId(),StaticValue.userLists.get(i).getUserNickName(),
                                        StaticValue.userLists.get(i).getUserCategory(),StaticValue.userLists.get(i).getUserMessage(),activityChatContentsBinding.etSummary.getText().toString(),"","",""));
                                break;
                            }
                        }

                        String chatRoomsJson = gson.toJson(chatRooms);
                        Log.d(TAG, "onClick: chatRoomsjson 결과 : "+chatRoomsJson);

                        //채팅내용 서버에 저장
                        Log.d(TAG, "onClick: 상담사가 채팅 종료할 때 json으로 채팅 어레이리스트를 바꿔줌");
                        gson = new Gson();
                        String chatJson = gson.toJson(StaticValue.chats);
                        Log.d(TAG, "onClick: json 결과 : "+chatJson);

                        //채팅 내용 데이터 보내기
                        makeRequest("/saveChats.php", chatRoomsJson, chatJson);

                        StaticValue.recipientId = "null";
                        //chats 초기화
                        StaticValue.chats = new ArrayList<>();
                        Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "상담이 종료되었습니다.", Toast.LENGTH_LONG).show();
                        StaticValue.isUserGone = false;//초기값으로
                        finish();

                }


            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //데이터 저장하고 응답받기
    public void makeRequest(String url, final String roomData, final String chatData){

//        String url = StaticValue.MyServer_IP;

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: 응답 : "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: 에러 -> "+error.getMessage());
                    }
                }
                ){
            @Override //서버가 요청하는 파라미터를 담음
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requestRoomJson", roomData);
                params.put("requestChatJson", chatData);
                return params;
        }

    };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: 요청 보냄");
    }
}
