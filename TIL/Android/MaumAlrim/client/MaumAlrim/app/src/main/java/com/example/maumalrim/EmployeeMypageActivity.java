package com.example.maumalrim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.ChatRoom;
import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.SharedPreference.MyChatRoom;
import com.example.maumalrim.databinding.ActivityEmployeeMypageBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeMypageActivity extends AppCompatActivity {

    ActivityEmployeeMypageBinding activityEmployeeMypageBinding;
    private static final String TAG = "EmployeeMypageActivity";
    static RequestQueue requestQueue;
    private ChatRoomAdapter chatRoomAdapter;
    ArrayList<EmployeeInfo> employeeInfos;

    //쉐어드에 저장한 채팅 히스토리를 불러오기 위해 선언
//    ArrayList<ChatRoom> chatRooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEmployeeMypageBinding = ActivityEmployeeMypageBinding.inflate(getLayoutInflater());
        View view = activityEmployeeMypageBinding.getRoot();
        setContentView(view);

        if (requestQueue == null){
            // requestQueue 객체 생성
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        //데이터 불러오기
        String url = "/employeeChatRoom.php";
        makeRequest(url, AutoLogin.getUserName(getApplicationContext()));

        //리사이클러뷰
        activityEmployeeMypageBinding.rvHistory.setHasFixedSize(true);
        activityEmployeeMypageBinding.rvHistory.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));//구분선

        //채팅방 클릭
        chatRoomAdapter = new ChatRoomAdapter(new ChatRoomAdapter.OnChatRoomClickListener() {
            @Override
            public void onChatRoomClicked(ChatRoom model) {
                Toast.makeText(getApplicationContext(), "채팅방 : "+model.getRoom_id(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PostChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roomId",model.getRoom_id());
                intent.putExtra("isPublic",model.getIs_public());
                intent.putExtra("summary", model.getSummary());
                intent.putExtra("userNickname", model.getOther_name());
                intent.putExtra("counselor", model.getUser_name());
                startActivity(intent);
            }
        });

        //어댑터 세팅 완료
        activityEmployeeMypageBinding.rvHistory.setAdapter(chatRoomAdapter);

        //채팅 데이터 불러오기
//        ArrayList<ChatRoom> chatRooms = MyChatRoom.getArr(getApplicationContext());
//        if (chatRooms.size()>0 || chatRooms != null){//데이터가 있으면 세팅
//            Log.d(TAG, "onCreate: chatRooms 데이터가 있으면 여기 들어옴");
//            chatRoomAdapter.setItems(chatRooms);
//        }else {
//            Log.d(TAG, "onCreate: chatRooms이 빈값!");
//        }


        //상담내역 텍스트 세팅
        employeeInfos = Information.getArr(getApplicationContext());
        activityEmployeeMypageBinding.tvHello.setText(employeeInfos.get(0).getUser_nickname()+"님의 상담 내역 입니다!");

        activityEmployeeMypageBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityEmployeeMypageBinding.imgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        activityEmployeeMypageBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 로그아웃 클릭");

                AutoLogin.clearUserName(getApplicationContext());
                Log.d(TAG, "onClick: AutoLogin 삭제 "+AutoLogin.getUserName(getApplicationContext()));
                Information.clearUserName(getApplicationContext());
                Log.d(TAG, "onClick: Information 삭제 "+Information.getArr(getApplicationContext()));
                MyChatRoom.clearAll(getApplicationContext());
                Log.d(TAG, "onClick: MyChatRoom 쉐어드 삭제"+MyChatRoom.getArr(getApplicationContext()));

                //서비스 종료
                Intent stopServiceIntent = new Intent(getApplicationContext(), MyService.class);
                stopService(stopServiceIntent);

                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //어드민 계정 로그인 했을 때 어드민 버튼 생성

        //데이터 불러오기
//        String url = "/employeeChatRoom.php";
//        makeRequest(url, AutoLogin.getUserName(getApplicationContext()));

        //쉐어드에 저장한 채팅방 :
        //counsellingType;기타 상담; createDate;Jun 19, 2020 3:33:00 AM;
        // guestId;ma@ma.com; guestName;마마마; isExist:true,state;join


//        chatRooms = MyChatRoom.getArr(getApplicationContext());
//        Log.d(TAG, "onStart: 채팅방 쉐어드에 저장한 개수 - "+chatRooms.size());

//        ArrayList<ChatRoom> chatRooms = MyChatRoom.getArr(getApplicationContext());
//            chatRoomAdapter.setItems(chatRooms);

//        }

/*        //어드민 페이지 다음에 추가
        if (employeeInfos.get(0).getIs_super().equals("Y")) {
//            activityEmployeeMypageBinding.btAdmin.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 실행");

//        if (MyChatRoom.KeysNum(getApplicationContext())>0){
//            Log.d(TAG, "onResume: 쉐어드에 저장한 채팅방 개수 : "+MyChatRoom.KeysNum(getApplicationContext()));
//        }else {
//            Log.d(TAG, "onResume: 쉐어드에 저장한 채팅방이 없음");
//        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 실행");
        overridePendingTransition(0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 실행");
//        finish();
    }

//    public void onSendAdminpage(View view) {
//        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//    }

    //상담리스트(히스토리) 리사이클러뷰 아뎁터
    private static class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {
        interface OnChatRoomClickListener {
            void onChatRoomClicked(ChatRoom model);
        }

        private OnChatRoomClickListener mListener;

        private List<ChatRoom> mItems = new ArrayList<>();

        public ChatRoomAdapter() {}

        public ChatRoomAdapter(OnChatRoomClickListener listener) {
            mListener = listener;
        }

        public void setItems(List<ChatRoom> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_list, parent, false);
            final ChatRoomViewHolder viewHolder = new ChatRoomViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final ChatRoom item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.onChatRoomClicked(item);
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
            ChatRoom item = mItems.get(position);
            holder.tvUserName.setText(item.getOther_name());
            holder.tvUserMsg.setText(item.getChat_category());
            holder.tvTime.setText(item.getEnd_time());
        }

        @Override
        public int getItemCount() {
            return (null != mItems ? mItems.size() : 0);
        }

        public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
            TextView tvUserName;
            TextView tvUserMsg;
            TextView tvTime;

            public ChatRoomViewHolder(@NonNull View itemView) {
                super(itemView);
                tvUserName = itemView.findViewById(R.id.tv_user_name);
                tvUserMsg = itemView.findViewById(R.id.tv_user_msg);
                tvTime = itemView.findViewById(R.id.tv_time);
            }
        }
    }

    //데이터 불러오기
    public void makeRequest(String url, final String user_id){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {//불러온 값을 쉐어드에 저장
                        Log.i(TAG, "onResponse: 응답-> "+response);
                        ArrayList<ChatRoom> getResponse = new ArrayList<>();
                        //쉐어드 비우기
                        Log.d(TAG, "onResponse: 쉐어드 초기화");
                        MyChatRoom.clearChatRoom(getApplicationContext());
                        
                        if (response.contains("데이터가 없습니다.")){
                            Log.i(TAG, "onResponse: 데이터 없음!");
                            //데이터 없을 때 나타나는 페이지
                            noDataPage();

                            return;
                        }

                        setDataPage();

                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(StaticValue.getJsonName);

                            //최신데이터를 0번으로 저장
                            int index = 0;
                            for (int i = jsonArray.length()-1; i >= 0; i--){
                                ChatRoom chatRoom = gson.fromJson(jsonArray.get(i).toString(), ChatRoom.class);
                                getResponse.add(chatRoom);

                                Log.i(TAG, "getResponse 추가 : "+getResponse.get(index).toString());//값 들어감!!
                                index++;
                            }

                            //쉐어드에 저장
                            if (getResponse.size() > 0) {
                                MyChatRoom.setArr(getApplicationContext(), getResponse);
                            
                                //쉐어드에 저장한 내용 확인하기
                                Log.d(TAG, "onResponse: 쉐어드에 저장한 내용 확인");
                                for (int i = 0; i < MyChatRoom.getArr(getApplicationContext()).size(); i++){
                                    Log.d(TAG, "onResponse: "+i+"번째 데이터 - "+MyChatRoom.getArr(getApplicationContext()).get(i).toString());
                                }

                                //리사이클러뷰 데이터 추가
                                ArrayList<ChatRoom> chatRooms = MyChatRoom.getArr(getApplicationContext());
                                chatRoomAdapter.setItems(chatRooms);
                            }
                            else {
                                Log.d(TAG, "onResponse: "+user_id+"의 데이터가 없음!!");
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
                params.put("user_id", user_id);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: 요청 보냄");
    }

    public void noDataPage(){
        activityEmployeeMypageBinding.rvHistory.setVisibility(View.GONE);
        activityEmployeeMypageBinding.llNoData.setVisibility(View.VISIBLE);


    }

//    public void setUpEmptyAnimation(LottieAnimationView animationView){
//        //재생할 애니메이션 삽입
//        animationView.setAnimation("empty.json");
//        // 반복횟수를 무한히 주고 싶을 땐 LottieDrawable.INFINITE or 원하는 횟수
//        animationView.setRepeatCount(LottieDrawable.INFINITE);
//        //시작
//        animationView.playAnimation();
//    }


    public void setDataPage(){
        activityEmployeeMypageBinding.rvHistory.setVisibility(View.VISIBLE);
        activityEmployeeMypageBinding.llNoData.setVisibility(View.GONE);
    }
}
