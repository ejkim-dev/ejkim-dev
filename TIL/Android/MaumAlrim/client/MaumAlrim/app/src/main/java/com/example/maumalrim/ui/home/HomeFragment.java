package com.example.maumalrim.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maumalrim.ChatbotActivity;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.ChatRoom;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.MyService;
import com.example.maumalrim.PostChatActivity;
import com.example.maumalrim.R;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.SharedPreference.MyChatRoom;
import com.example.maumalrim.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    public static SessionResponse watsonAssistantSession;
    static RequestQueue requestQueue;
    FragmentHomeBinding fragmentHomeBinding;
    Intent foregroundServiceIntent;
    ChatRoomAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "호출_1_onAttach: 프래그먼트가 그 부모 액티비티에 연결될 때 호출");
        Log.d(TAG, "호출_1_onAttach: 부모 컴포넌트를 나타내는 Context 참조 : "+context.toString());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "호출_2_onCreate: 프래그먼트 초기화 : 프래그먼트가 처음 생성될 때 호출");
//        Log.d(TAG, "호출_2_onCreate: Bundle : "+(savedInstanceState.toString() == null ? "" : savedInstanceState.toString())); //NullPointerException
    /*아래와 같이 Activity를 생성할 때 아래와 같이 Bundle savedInstanceState 객체를 가지고 와서,
    액티비티를 중단할 때 savedInstanceState 메서드를 호출하여 임시적으로 데이터를 저장한다.
    즉 전에 저장된 데이터가 있으면, 그 데이터를 가지고 Activity를 다시 생성한다.*/

        //상담초기화 : 데이터 보내고, 받아서 세팅하기
/*        StaticValue.chatArrayList = new ArrayList<>();*/

        //데이터 보내기
    /*    GetData getData = new GetData();
        getData.execute();*/

    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*ViewModelProviders : ViewModels 범위를 제공하는 유틸리티 클래스*/
        Log.d(TAG, "호출_3_onCreateView: 프래그먼트가 생성되면 자신의 사용자 인터페이스를 생성하기위해 호출");
        Log.d(TAG, "호출_3_onCreateView: LayoutInflater - "+(inflater.toString())
                +"\n | ViewGroup - "+(container.toString()));

        fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = fragmentHomeBinding.getRoot();


        /*//        구분선 추가
        fragmentDashboardBinding.rcChatHistory.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));*/

        fragmentHomeBinding.rcChatHistory.setHasFixedSize(true);


        //상담버튼 클릭
        fragmentHomeBinding.btnCounselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클라이언트 소켓 연결 : 서비스 이동
                Log.d(TAG, "1. 상담하기 : 버튼 클릭 및 실행");
                if (fragmentHomeBinding.btnCounselling.getText().toString().equals("상담하기")){

                    // 챗봇에게 상담접수 | 바로 상담접수 다이얼로그 띄우기

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("상담접수").setMessage("챗봇으로 상담접수를하면 상담사에게 미리 상담내용을 전달할 수 있어요. 챗봇 상담 접수를 하시겠습니까?");

                    builder.setPositiveButton("챗봇접수", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext().getApplicationContext(), ChatbotActivity.class);
                            Log.d(TAG, "2. 상담하기 : intent 챗봇방으로 이동 - "+intent.toString());
                            startActivity(intent);
                            Log.d(TAG, "3. 상담하기 : intent 챗봇방으로 이동 실행");
                        }
                    });

                    builder.setNegativeButton("바로접수", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<UserInfo> userInfos = Information.getArr(getContext().getApplicationContext());
                            String nickname = userInfos.get(0).getUser_nickname();
                            //현재날짜
                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                            String regDate = simpleDateFormat.format(calendar.getTime());

                            StaticValue.request =
                                    AutoLogin.getUserName(getContext().getApplicationContext())
                                            +";;"+nickname
                                            +";;"+regDate
                                            +";;"+"기타"//상담유형
                                            +";;"+"내용없음";//상담내용

                            Intent startServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                            getActivity().startForegroundService(startServiceIntent);
                            fragmentHomeBinding.btnCounselling.setText("상담대기중");
                            fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorPrimary));

                        }
                    });

                    builder.setNeutralButton("뒤로가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                } else if(fragmentHomeBinding.btnCounselling.getText().toString().equals("상담대기중")) {//버튼이 '상담대기중'일때
                    Log.d(TAG, "onClick: 상담 신청 완료");
                    Log.d(TAG, "onClick: 상담대기중 버튼일 때 다이얼로그 띄우기");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("상담대기 상태").setMessage("상담대기 상태를 변경하시겠습니까?");

                    builder.setPositiveButton("재신청", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getContext().getApplicationContext(),"재신청",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick: 재신청 클릭 시 버튼을 상담하기로 변경");
                            fragmentHomeBinding.btnCounselling.setText("상담하기");
                            fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorLightPink));

                            //실행중인 서비스 종료
                            Intent stopServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                            getActivity().stopService(stopServiceIntent);

                            //챗봇으로 이동
                            Intent intent = new Intent(getContext().getApplicationContext(), ChatbotActivity.class);
                            startActivity(intent);

                        }
                    });

                    builder.setNegativeButton("대기취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getContext().getApplicationContext(),"상담취소",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick: 상담취소 클릭시 버튼 상담하기로 변경");
                            fragmentHomeBinding.btnCounselling.setText("상담하기");
                            fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorLightPink));

                            //실행중인 서비스 종료
                            Intent stopServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                            getActivity().stopService(stopServiceIntent);

                        }
                    });

                    builder.setNeutralButton("뒤로가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            Toast.makeText(getContext().getApplicationContext(),"뒤로가기",Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else {//버튼이 '상담중'일때
                    Log.d(TAG, "onClick: 상담사와 상담중");
//                    Intent intent = new Intent(getContext().getApplicationContext(), MainChatActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    Toast.makeText(getContext().getApplicationContext(),"현재 상담사와 상담 중입니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });



        //채팅방을 클릭하면
        adapter = new ChatRoomAdapter(new ChatRoomAdapter.OnChatRoomClickListener(){
            @Override
            public void onChatRoomClicked(ChatRoom model) {

                Intent intent = new Intent(getActivity().getApplicationContext(), PostChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roomId",model.getRoom_id());
                intent.putExtra("isPublic",model.getIs_public());
                intent.putExtra("summary", model.getSummary());
                intent.putExtra("userNickname", model.getOther_name());
                intent.putExtra("counselor", model.getUser_name());
                //내 채팅방인지 비교해줘야함
                intent.putExtra("chatUserId", model.getOther_id());
                startActivity(intent);

//                Toast.makeText(getContext().getApplicationContext(), "채팅방 : "+model.getRoom_id(), Toast.LENGTH_SHORT).show();
            }
        });

        fragmentHomeBinding.rcChatHistory.setAdapter(adapter);

        //채팅방 추가 : ChatRoom{master_id='31', user_id='01011112222', user_name='aaa', room_id='010111122221593136992', other_id='w@w.com', other_name='why', chat_category='기타 상담',
        // summary='jjjjj', end_time='1593136992', is_public='N', is_status='Y'}
        ArrayList<ChatRoom> chatRooms = MyChatRoom.getArr(getActivity().getApplicationContext());

        if (adapter.getItemCount() != 0) {
            Log.d(TAG, "onCreateView: chatRooms 크기 : "+chatRooms.size());
            setDataPage();
            adapter.setItems(chatRooms);
        }
        else {
            Log.d(TAG, "onCreateView: chatRooms에 데이터 없음");
            noDataPage();
        }
        //스와이프시 새로 고침
        fragmentHomeBinding.llSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //여기서 새로고침
                Log.d(TAG, "onRefresh: 새로고침 실행");

                String url = "/chatRoomDB.php";

                //데이터 불러오기
                makeRequest(url, AutoLogin.getUserName(getActivity().getApplicationContext()));


                //새로고침이 완료되면 호출해야함
                fragmentHomeBinding.llSwipeRefresh.setRefreshing(false);
                Log.d(TAG, "onRefresh: 새로고침 완료 - 아이콘 사라짐");
            }
        });

        if (requestQueue == null){
            // requestQueue 객체 생성
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }


        /*프래그먼트 UI를 생성 및 인플레이트한 후 반환한다
         * 이 프래그먼트에 UI가 없으면 null을 반환한다.*/
        return view;
    }

    //부모 액티비티와 프래그먼트의 UI가 생성되면 호출된다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //특히 부모 액티비티의 초기화 또는 프래그먼트 뷰가 인플레이트되는데 필요한 작업을 수행한다.
        Log.d(TAG, "호출_4_onActivityCreated: 프래그넌트 초기화(부모 액티비티의 초기화 또는 프래그먼트 뷰가 인플레이트되는데 필요한 작업을 수행)");
//        Log.d(TAG, "onActivityCreated: Bundle - "+savedInstanceState.toString());//null
    }

    //가시 수명이 시작될 때 호출된다.

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "호출_5_onStart: 프래그먼트가 보이게되는 시점에 필요한 UI 변경을 처리");
        Log.d(TAG, "호출_5_onStart: 화면을 잠깐 내렸다가 키면 나타남");
        Log.d(TAG, "onStart: 현재 연결중인 상담사 : "+StaticValue.recipientId);

//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.detach(this).attach(this).commit();

        //서비스가 실행중이면 버튼 텍스트(상담대기중), 버튼 색상 변경하기
        if (MyService.serviceIntent == null){
            Log.d(TAG, "onStart: 포그라운드 실행안함 = "+MyService.serviceIntent);
//            Toast.makeText(getContext().getApplicationContext(), "여기에서 심리상담이 가능합니다.", Toast.LENGTH_LONG).show();


        }else {//포그라운드 실행중일 때

            Log.d(TAG, "onStart: 포그라운드 실행중 = "+MyService.serviceIntent);
            foregroundServiceIntent = MyService.serviceIntent;

//            //소켓열려있는지 확인
            MyService myService = new MyService();
            Log.d(TAG, "onStart: 소켓 상태 - "+myService.isSocketOpen());

            if (myService.isSocketOpen()) {

                //상담사가 잡혔으면 "상담중"으로 바꾸기
                if (StaticValue.recipientId.equals("null")){
                    Log.d(TAG, "onStart: 연결된 상담사 id 없음; 버튼 '상담대기중'으로 변경 : 현재상태 - "+fragmentHomeBinding.btnCounselling.getText());
//                    Toast.makeText(getContext().getApplicationContext(), "상담 대기중 입니다.", Toast.LENGTH_LONG).show();
                    fragmentHomeBinding.btnCounselling.setText("상담대기중");
                    fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorPrimary));
                }
                else {
                    Log.d(TAG, "onStart: 연결된 상담사 id 있음; 버튼 '상담중'으로 변경 : 현재상태 - "+fragmentHomeBinding.btnCounselling.getText());
                    Toast.makeText(getContext().getApplicationContext(), "상담중 입니다.", Toast.LENGTH_LONG).show();
                    fragmentHomeBinding.btnCounselling.setText("상담중");
                    fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorLight));
                }


            }else {
                //실행중인 서비스 종료
                Log.d(TAG, "onStart: 소캣이 없는데 서비스가 돌고 있을 때; 버튼 '상담하기'로 변경 : 현재상태 - "+fragmentHomeBinding.btnCounselling.getText());
                Intent stopServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                getActivity().stopService(stopServiceIntent);

                fragmentHomeBinding.btnCounselling.setText("상담하기");
                fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorLightPink));
            }
        }
        //서버에서 데이터 받아서 쉐어드 저장하기
        if (requestQueue == null){
            // requestQueue 객체 생성
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        //데이터 불러오기
        String url = "/chatRoomDB.php";
        makeRequest(url, AutoLogin.getUserName(getActivity().getApplicationContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "호출_6_onResume: 활성수명이 시작될때 호출");
        Log.d(TAG, "호출_6_onResume:  화면을 잠깐 내렸다가 키면 나타남");


        if (MyService.serviceIntent != null){
            Log.d(TAG, "onResume: 서비스실행중");
        } else {
            Log.d(TAG, "onResume: 서비스 멈춤");
        }

        //리사이클러뷰 새로 고침
//        fragmentHomeBinding.llSwipeRefresh.setOnRefreshListener();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 실행");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 실행");

        //서비스가 실행중이면 버튼 텍스트(상담대기중), 버튼 색상 변경하기
        if (MyService.serviceIntent == null){
            Log.d(TAG, "onPause: 포그라운드 실행안함 = "+MyService.serviceIntent);


        }else {//포그라운드 실행중일 때
            Log.d(TAG, "onPause: 포그라운드 실행중 = "+MyService.serviceIntent);
            foregroundServiceIntent = MyService.serviceIntent;

            //소켓열려있는지 확인
            MyService myService = new MyService();
            Log.d(TAG, "onPause: 소켓 상태 - "+myService.isSocketOpen());

            if (myService.isSocketOpen()) {

                //상담사가 잡혔으면 "상담중"으로 바꾸기
                if (StaticValue.recipientId.equals("null")){
                    Log.d(TAG, "onPause: 연결된 상담사 id 없음 버튼 '상담대기중'로 변경 : 현재상태 - "+fragmentHomeBinding.btnCounselling.getText());
//                    Toast.makeText(getContext().getApplicationContext(), "상담 대기중 입니다.", Toast.LENGTH_LONG).show();
                    fragmentHomeBinding.btnCounselling.setText("상담대기중");
                    fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorPrimary));
                }
                else {
                    Log.d(TAG, "onPause: 연결된 상담사 id 있음 버튼 '상담중'으로 변경 : 현재상태 - "+fragmentHomeBinding.btnCounselling.getText());
                    Toast.makeText(getContext().getApplicationContext(), "상담중 입니다.", Toast.LENGTH_LONG).show();
                    fragmentHomeBinding.btnCounselling.setText("상담중");
                    fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorLight));
                }


            }else {
                //실행중인 서비스 종료
                Log.d(TAG, "onPause: 소캣이 없는데 서비스가 돌고 있을 때");
                Log.d(TAG, "onPause: 버튼 '상담하기'로 변경 : 현재상태 - "+fragmentHomeBinding.btnCounselling.getText());
                Intent stopServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                getActivity().stopService(stopServiceIntent);

                fragmentHomeBinding.btnCounselling.setText("상담하기");
                fragmentHomeBinding.btnCounselling.setBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorLightPink));
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 실행");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: 실행");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: 실행");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: 실행");
    }

    //채팅 목록 어뎁터
    private static class ChatRoomAdapter extends RecyclerView.Adapter<HomeFragment.ChatRoomAdapter.ChatRoomViewHolder> {
        interface OnChatRoomClickListener {
            void onChatRoomClicked(ChatRoom model);
        }

        private HomeFragment.ChatRoomAdapter.OnChatRoomClickListener mListener;

        private List<ChatRoom> mItems = new ArrayList<>();

        public ChatRoomAdapter() {}

        public ChatRoomAdapter(HomeFragment.ChatRoomAdapter.OnChatRoomClickListener listener) { mListener = listener; }

        public void setItems(List<ChatRoom> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HomeFragment.ChatRoomAdapter.ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            final HomeFragment.ChatRoomAdapter.ChatRoomViewHolder viewHolder = new HomeFragment.ChatRoomAdapter.ChatRoomViewHolder(view);

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
        public void onBindViewHolder(@NonNull HomeFragment.ChatRoomAdapter.ChatRoomViewHolder holder, int position) {
            ChatRoom chatRoom = mItems.get(position);

            holder.tvCounselor.setText(chatRoom.getUser_name()+" 상담사");
            holder.tvType.setText("상담유형 : "+chatRoom.getChat_category());
            holder.tvTime.setText(chatRoom.getEnd_time());
            holder.tvUserMsg.setText(chatRoom.getContents());
            holder.tvUserName.setText("by "+chatRoom.getOther_name());

            if (chatRoom.getIs_public().equals("Y")){
                holder.tvPublic.setText("공개글");
                holder.tvPublic.setTextColor(ContextCompat.getColor(holder.tvPublic.getContext() ,R.color.colorPrimary));
            }else {
                holder.tvPublic.setText("비공개글");
                holder.tvPublic.setTextColor(ContextCompat.getColor(holder.tvPublic.getContext() ,R.color.colorGray));
            }

            //상담중일 때 글자 설정
//            if (ChatRoom.getState().equals("상담중")){
//                holder.state.setText(ChatRoom.getState());
////                holder.state.setTextColor(ContextCompat.getColor(get, R.color.colorPrimary));
//
//            }else {//상담 끝났을 때
//
//            }
        }

        @Override
        public int getItemCount() {
            return (null != mItems ? mItems.size() : 0);
        }

        public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
            TextView tvCounselor;
            TextView tvUserName;
            TextView tvType;
            TextView tvUserMsg;
            TextView tvTime;
            TextView tvPublic;

            public ChatRoomViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCounselor = itemView.findViewById(R.id.tv_counselor);
                tvUserName = itemView.findViewById(R.id.tv_nickname);
                tvType = itemView.findViewById(R.id.tv_type);
                tvUserMsg = itemView.findViewById(R.id.tv_contents);
                tvTime = itemView.findViewById(R.id.tv_date);
                tvPublic = itemView.findViewById(R.id.tv_public);
            }
        }
    }

    //데이터 불러오기
    public void makeRequest(String url, final String other_id){
//        String url = "/testMemberDB.php";

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {//불러온 값을 쉐어드에 저장
                        Log.i(TAG, "onResponse: 응답-> "+response);
                        ArrayList<ChatRoom> getResponse = new ArrayList<>();
                        //쉐어드 비우기
                        Log.d(TAG, "onResponse: 쉐어드 초기화");
                        MyChatRoom.clearChatRoom(getActivity().getApplicationContext());

                        if (response.contains("데이터가 없습니다.")){
                            Log.i(TAG, "onResponse: 데이터 없음!");
                            //데이터 없을 때 나타나는 페이지


                            return;
                        }

                        setDataPage();

                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(StaticValue.getJsonName);

//                            int index = 0;
//                            while (index < jsonArray.length()){
//                                ChatRoom chatRoom = gson.fromJson(jsonArray.get(index).toString(), ChatRoom.class);
//                                getResponse.add(chatRoom);
//
//                                Log.i(TAG, "getResponse : "+getResponse.get(getResponse.size()-1).toString());//값 들어감!!
//                                index++;
//                            }
                            //최신데이터를 0번으로 저장
                            int index = 0;
                            for (int i = jsonArray.length()-1; i >= 0; i--){
                                ChatRoom chatRoom = gson.fromJson(jsonArray.get(i).toString(), ChatRoom.class);
                                getResponse.add(chatRoom);

                                Log.i(TAG, "getResponse 추가 : "+getResponse.get(index).toString());//값 들어감!!
                                index++;
                            }

                            //쉐어드에 저장
                            MyChatRoom.setArr(getActivity().getApplicationContext(), getResponse);

                            //쉐어드에 저장한 내용 확인하기
                            Log.d(TAG, "onResponse: 쉐어드에 저장한 내용 확인");
                            for (int i = 0; i < MyChatRoom.getArr(getActivity().getApplicationContext()).size(); i++){
                                Log.d(TAG, "onResponse: "+i+"번째 데이터 - "+MyChatRoom.getArr(getActivity().getApplicationContext()).get(i).toString());
                            }

                            //리사이클러뷰 데이터 추가
                            ArrayList<ChatRoom> chatRooms = MyChatRoom.getArr(getActivity().getApplicationContext());
                            adapter.setItems(chatRooms);

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
                params.put("other_id", other_id);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: 요청 보냄");
    }

    public void noDataPage(){
        fragmentHomeBinding.llSwipeRefresh.setVisibility(View.GONE);
        fragmentHomeBinding.llNoData.setVisibility(View.VISIBLE);

    }

    public void setDataPage(){
        fragmentHomeBinding.llSwipeRefresh.setVisibility(View.VISIBLE);
        fragmentHomeBinding.llNoData.setVisibility(View.GONE);
    }

}
