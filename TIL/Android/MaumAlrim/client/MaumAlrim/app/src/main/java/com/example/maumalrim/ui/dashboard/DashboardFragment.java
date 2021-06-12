package com.example.maumalrim.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.ChatRoom;
import com.example.maumalrim.MyService;
import com.example.maumalrim.PostChatActivity;
import com.example.maumalrim.R;
import com.example.maumalrim.databinding.FragmentDashboardBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*심리상담할 수 있는 곳*/
public class DashboardFragment extends Fragment {

    private static String TAG = "DashboardFragment";

    FragmentDashboardBinding fragmentDashboardBinding;
    ChatRoomAdapter adapter;

    RequestQueue requestQueue;


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


    }

    @Override// Android에서는 Activity간에 데이터를 주고 받을 때 Bundle 클래스를 사용하여 데이터를 전송
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*ViewModelProviders : ViewModels 범위를 제공하는 유틸리티 클래스*/
        Log.d(TAG, "호출_3_onCreateView: 프래그먼트가 생성되면 자신의 사용자 인터페이스를 생성하기위해 호출");
        Log.d(TAG, "호출_3_onCreateView: LayoutInflater - "+(inflater.toString())
                +"\n | ViewGroup - "+(container.toString()));

        fragmentDashboardBinding = FragmentDashboardBinding.inflate(getLayoutInflater());

        fragmentDashboardBinding.rcChatHistory.setHasFixedSize(true);

        if (requestQueue == null){
            // requestQueue 객체 생성
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        //채팅방클릭
        adapter = new ChatRoomAdapter(new ChatRoomAdapter.OnChatRoomClickListener() {
            @Override
            public void onChatRoomClicked(ChatRoom model) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roomId",model.getRoom_id());
                intent.putExtra("isPublic",model.getIs_public());
                intent.putExtra("summary", model.getSummary());
                intent.putExtra("userNickname", model.getOther_name());
                intent.putExtra("counselor", model.getUser_name());
                Log.d(TAG, "onChatRoomClicked: 상담사이름 : "+model.getUser_name());

                //내 채팅방인지 비교해줘야함
                intent.putExtra("chatUserId", model.getOther_id());
                startActivity(intent);
            }
        });

        fragmentDashboardBinding.rcChatHistory.setAdapter(adapter);

        //스와이프시 새로고침
        fragmentDashboardBinding.llSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: 새로고침 실행");
                String url = "/publicChatRoom.php";
                makeRequest(url);

                //새로고침 아이콘 사라지게함
                fragmentDashboardBinding.llSwipeRefresh.setRefreshing(false);
            }
        });




        /*프래그먼트 UI를 생성 및 인플레이트한 후 반환한다
        * 이 프래그먼트에 UI가 없으면 null을 반환한다.*/
        return fragmentDashboardBinding.getRoot();
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


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "호출_6_onResume: 활성수명이 시작될때 호출");
        Log.d(TAG, "호출_6_onResume:  화면을 잠깐 내렸다가 키면 나타남");

        String url = "/publicChatRoom.php";
        makeRequest(url);
        
        if (MyService.serviceIntent != null){
            Log.d(TAG, "onResume: 서비스실행중");
        }
        
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
    private static class ChatRoomAdapter extends RecyclerView.Adapter<DashboardFragment.ChatRoomAdapter.ChatRoomViewHolder> {
        interface OnChatRoomClickListener {
            void onChatRoomClicked(ChatRoom model);
        }

        private DashboardFragment.ChatRoomAdapter.OnChatRoomClickListener mListener;

        private List<ChatRoom> mItems = new ArrayList<>();

        public ChatRoomAdapter() {}

        public ChatRoomAdapter(DashboardFragment.ChatRoomAdapter.OnChatRoomClickListener listener) { mListener = listener; }

        public void setItems(List<ChatRoom> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DashboardFragment.ChatRoomAdapter.ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            final DashboardFragment.ChatRoomAdapter.ChatRoomViewHolder viewHolder = new DashboardFragment.ChatRoomAdapter.ChatRoomViewHolder(view);
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
        public void onBindViewHolder(@NonNull DashboardFragment.ChatRoomAdapter.ChatRoomViewHolder holder, int position) {
            ChatRoom chatRoom = mItems.get(position);

            holder.tvCounselor.setText(chatRoom.getUser_name()+" 상담사");
            holder.tvType.setText("상담유형 : "+chatRoom.getChat_category());
            holder.tvTime.setText(chatRoom.getEnd_time());
            holder.tvUserMsg.setText(chatRoom.getContents());
            holder.tvUserName.setText("by "+chatRoom.getOther_name());

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

            public ChatRoomViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCounselor = itemView.findViewById(R.id.tv_counselor);
                tvUserName = itemView.findViewById(R.id.tv_nickname);
                tvType = itemView.findViewById(R.id.tv_type);
                tvUserMsg = itemView.findViewById(R.id.tv_contents);
                tvTime = itemView.findViewById(R.id.tv_date);
            }
        }
    }

    public void makeRequest(String url){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {//불러온 값을 쉐어드에 저장
                        Log.i(TAG, "onResponse: 응답-> "+response);
                        ArrayList<ChatRoom> getResponse = new ArrayList<>();

                        if (response.contains("데이터가 없습니다.")){
                            Log.i(TAG, "onResponse: 데이터 없음!");
                            //데이터 없을 때 나타나는 페이지

                            return;
                        }


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


                            //리사이클러뷰 데이터 추가
                            adapter.setItems(getResponse);

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
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: 요청 보냄");
    }

}
