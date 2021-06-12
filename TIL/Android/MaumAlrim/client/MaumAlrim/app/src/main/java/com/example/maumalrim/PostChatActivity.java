package com.example.maumalrim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.Item.ChatData;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.databinding.ActivityPostChatBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostChatActivity extends AppCompatActivity {

    private static final String TAG = "PostChatActivity";
    static RequestQueue requestQueue;
    ActivityPostChatBinding activityPostChatBinding;
    MainChat chatAdapter;
    Drawable lockdrawable;
    Drawable opendrawable;
    Drawable upPage;
    Drawable downPage;
    String thisUserId = "";
    String isPublic = "";
    String roomId = "";

    /*유저가 상담 내용 삭제하는 기능 추가 : 삭제할 때 isPublic은 N으로 함께 바꿔야함*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPostChatBinding = ActivityPostChatBinding.inflate(getLayoutInflater());
        View view = activityPostChatBinding.getRoot();
        setContentView(view);

        thisUserId = AutoLogin.getUserName(getApplication().getApplicationContext());

        lockdrawable = getResources().getDrawable(R.drawable.ic_lock_black_24dp);
        opendrawable = getResources().getDrawable(R.drawable.ic_lock_open_black_24dp);
        upPage = getResources().getDrawable(R.drawable.ic_expand_less_black_24dp);
        downPage = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);

   
        Intent intent = getIntent();
        roomId = intent.getExtras().getString("roomId");
        isPublic = intent.getExtras().getString("isPublic");
        String summary = intent.getExtras().getString("summary");
        String userNickname = intent.getExtras().getString("userNickname");
        String counselor = intent.getExtras().getString("counselor");

        activityPostChatBinding.tvChatRoomUser.setText(userNickname+"님의 상담");


        //유저아이디일때 자물쇠
        if (AutoLogin.getUserName(getApplication().getApplicationContext()).contains("@")){

            //넘어온 아이디가 이 계정 주인과 같으면 자물쇠가 보임
            String getUserId = intent.getExtras().getString("chatUserId");
            if (getUserId.equals(AutoLogin.getUserName(getApplication().getApplicationContext()))){
                activityPostChatBinding.imgIsPublic.setVisibility(View.VISIBLE);
                activityPostChatBinding.imgIsStatus.setVisibility(View.VISIBLE);


                activityPostChatBinding.tvChatRoomUser.setText(counselor+"상담사와 상담");

                if (isPublic.equals("N")){
                    Log.d(TAG, "onCreate: 비공개");
                    activityPostChatBinding.imgIsPublic.setImageDrawable(lockdrawable);
                }
                else {
                    Log.d(TAG, "onCreate: 공개");
                    activityPostChatBinding.imgIsPublic.setImageDrawable(opendrawable);
                }
            }else {
                // 이 계정 주인과 다른 계정
                thisUserId = getUserId;
            }

        }


        if (requestQueue == null){
            //RequestQueue 객체 생성하기
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "/chatDB.php";
        makeRequest(url, roomId);

        activityPostChatBinding.tvSummary.setText(summary);

        activityPostChatBinding.rvChatPart.setHasFixedSize(true);
        activityPostChatBinding.rvChatPart.setAdapter(chatAdapter);
//        chatAdapter.notifyDataSetChanged();
//        activityPostChatBinding.rvChatPart.scrollToPosition();
    }

    public void makeRequest(final String url, final String user_id){


        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: 응답 : "+response);
                        ArrayList<ChatData> getResponse = new ArrayList<>();

                        //채팅 어레이리스트
                        ArrayList<Chat> chats = new ArrayList<>();

                        if (response.contains("데이터가 없습니다.")){
                            Log.i(TAG, "onResponse: 데이터 없음!");

                            return;
                        }

                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(StaticValue.getJsonName);


                            for (int i = 0; i < jsonArray.length(); i++){
                                ChatData chatData = gson.fromJson(jsonArray.get(i).toString(), ChatData.class);
                                getResponse.add(chatData);
                                Log.i(TAG, "getResponse 추가 : "+getResponse.get(i).toString());//값 들어감!!

                                String userChattext = getResponse.get(i).getUser_text();
                                String userChatId = getResponse.get(i).getUser_id();

                                Log.d(TAG, "onResponse: userChattext = "+userChattext+" | userChatId = "+userChatId);

                                chats.add(new Chat(userChattext, userChatId));

                            }

                            //채팅 리싸이클러뷰 적용
                            if (getResponse.size()>0){
                                Log.d(TAG, "onResponse: 채팅 리싸이클러뷰 적용");
                                chatAdapter = new MainChat(chats, thisUserId);
                                activityPostChatBinding.rvChatPart.setAdapter(chatAdapter);

                                chatAdapter.notifyDataSetChanged();
//                                activityPostChatBinding.rvChatPart.scrollToPosition(chats.size()-1);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override //에러시 처리할 내용
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: 에러-> "+error.getMessage());
                    }
                }
        ){
            @Override //서버가 요청하는 파라미터를 담음
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id",user_id);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: 요청 보냄.");
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                Log.d(TAG, "onClick: 이 엑티비티 열기 전 화면으로 돌리기 위해서");

                finish();
                return;
            case R.id.img_is_public:
                //자물쇠 버튼을 눌렀을 때
                Log.d(TAG, "onClick: 자물쇠를 누르면 다이얼로그가 뜬다.");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                //공개가 N이면, 서버에서 공개를 Y로 바꿔주고, 자물쇠를 오픈으로 바꿔준다.
                if (isPublic.equals("N")){
                    Log.d(TAG, "onCreate: 비공개");
                    builder.setTitle("비공개 상태").setMessage("상담 내용을 공개로 변경하시겠습니까?");
                }
                else {
                    Log.d(TAG, "onCreate: 공개");
                    builder.setTitle("공개 상태").setMessage("공개 중인 상담 내용을 비공개로 변경하시겠습니까?");
                }

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"변경되지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isPublic.equals("N")){
                            Log.d(TAG, "onCreate: 비공개");
                            changeRequest("/changePublic.php",roomId,"Y");
                            activityPostChatBinding.imgIsPublic.setImageDrawable(opendrawable);
                        }
                        else {
                            Log.d(TAG, "onCreate: 공개");
                            changeRequest("/changePublic.php",roomId,"N");
                            activityPostChatBinding.imgIsPublic.setImageDrawable(lockdrawable);
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return;

            case R.id.img_is_status:

                Log.d(TAG, "onClick: 엑스를 누르면 다이얼로그가 뜬다.");

                AlertDialog.Builder builderRemover = new AlertDialog.Builder(this);

                builderRemover.setTitle("게시물 삭제").setMessage("해당 상담 내용을 삭제하시겠습니까?");

                builderRemover.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"삭제하지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
                });

                builderRemover.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //게시물 삭제 후 창 닫고, public도 비공개로 바꿈
                        changeRequest("/changeStatus.php",roomId,"N");
                        finish();
                    }
                });

                AlertDialog alertDialogRemover = builderRemover.create();
                alertDialogRemover.show();

                return;
        }
    }

    public void changeRequest(String url, final String room_id, final String is_public){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: 응답 : "+response);

                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override //에러시 처리할 내용
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: 에러-> "+error.getMessage());
                    }
                }
        ){
            @Override //서버가 요청하는 파라미터를 담음
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id",room_id);
                params.put("is_public",is_public);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "changeRequest: 요청 보냄");
    }

    public void onShowPage(View view) {
        if (activityPostChatBinding.ivShowPage.getDrawable().equals(upPage)){
            activityPostChatBinding.ivShowPage.setImageDrawable(downPage);
            activityPostChatBinding.llSummary.setVisibility(View.VISIBLE);
        }
        else {
            activityPostChatBinding.ivShowPage.setImageDrawable(upPage);
            activityPostChatBinding.llSummary.setVisibility(View.GONE);
        }
    }
}
