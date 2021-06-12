package com.example.maumalrim.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.example.maumalrim.ChatbotDailyActivity;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.Diary;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.MyDiaryActivity;
import com.example.maumalrim.MyService;
import com.example.maumalrim.R;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.SharedPreference.MyChatRoom;
import com.example.maumalrim.SignInActivity;
import com.example.maumalrim.databinding.FragmentNotificationsBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private static String TAG = "NotificationsFragment";

    FragmentNotificationsBinding fragmentNotificationsBinding;
    RequestQueue requestQueue;
    DiaryAdapter diaryAdapter;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ArrayList<UserInfo> userInfos = Information.getArr(getContext());
        String userNickname = userInfos.get(0).getUser_nickname();
        Log.d(TAG, "onCreateView: userNickname - "+userNickname);

        if (requestQueue == null){
            // requestQueue 객체 생성
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        fragmentNotificationsBinding = FragmentNotificationsBinding.inflate(getLayoutInflater());

        fragmentNotificationsBinding.tvMyTitle.setText(userNickname+"님의 일기");

        fragmentNotificationsBinding.rcDiary.setHasFixedSize(true);

        //클릭시
        diaryAdapter = new DiaryAdapter(new DiaryAdapter.OnDiaryClickListener() {
            @Override
            public void onDiaryClicked(Diary model) {

                Intent intent = new Intent(getContext(), MyDiaryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("diaryCode", model.getMaster_id());
                intent.putExtra("title", model.getUser_title());
                intent.putExtra("condition", model.getUser_condition());
                intent.putExtra("contents", model.getUser_text());
                startActivity(intent);

            }
        });

        fragmentNotificationsBinding.rcDiary.setAdapter(diaryAdapter);


//        ArrayList<Diary> diaries = new ArrayList<>();
//        diaries.add(new Diary("dsf","df","오늘날씨","넘 구리네","2020-06-27"));
//
//        diaryAdapter.setItems(diaries);






//        fragmentNotificationsBinding.textNotifications.setText(AutoLogin.getUserName(getContext().getApplicationContext())+"님 환영합니다.");

        //로그아웃할 수 있는 버튼 : 로그아웃하면 자동로그인 쉐어드 삭제됨
        fragmentNotificationsBinding.btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //로그아웃할 수 있는 버튼 : 로그아웃하면 자동로그인 쉐어드 삭제됨

                AutoLogin.clearUserName(getContext().getApplicationContext());//자동로그인 삭제 및 로그아웃
                Log.d(TAG, "onClick: AutoLogin 쉐어드 정보 삭제 : "+AutoLogin.getUserName(getContext().getApplicationContext()));
                Information.clearUserName(getContext().getApplicationContext());//회원 정보 삭제
                Log.d(TAG, "onClick: Information 쉐어드 정보 삭제 : "+Information.getArr(getContext().getApplicationContext()));
                MyChatRoom.clearAll(getContext().getApplicationContext());
                Log.d(TAG, "onClick: MyChatRoom 쉐어드 삭제"+MyChatRoom.getArr(getContext().getApplicationContext()));

                Intent stopServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                getActivity().stopService(stopServiceIntent);
                Intent intent = new Intent(getContext().getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });


        fragmentNotificationsBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //추가 아이콘 눌렀을 때 MyDiaryActivity -> 일기 편집하는 곳
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatbotDailyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("diaryCode", "");
                intent.putExtra("title", "");
                intent.putExtra("condition", "");
                intent.putExtra("contents", "");
                startActivity(intent);
            }
        });


        //새로고침
        fragmentNotificationsBinding.llSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //데이터 불러오기
                makeRequest("/getDiary.php",AutoLogin.getUserName(getContext().getApplicationContext()));

                fragmentNotificationsBinding.llSwipeRefresh.setRefreshing(false);
            }
        });



        return fragmentNotificationsBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 실행");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 실행");
        makeRequest("/getDiary.php",AutoLogin.getUserName(getContext().getApplicationContext()));
    }

    public void makeRequest(String url, final String user_id){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {//불러온 값을 쉐어드에 저장
                        Log.i(TAG, "onResponse: 응답-> "+response);
                        ArrayList<Diary> getResponse = new ArrayList<>();
//                        ArrayList<Diary> diaries = new ArrayList<>();

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
                                Diary diary = gson.fromJson(jsonArray.get(i).toString(), Diary.class);
                                getResponse.add(diary);

                                Log.i(TAG, "getResponse 추가 : "+getResponse.get(index).toString());//값 들어감!!
                                index++;
                            }

//                            diaries.add(new Diary("dsf","df","오늘날씨","넘 구리네","2020-06-27"));
                            diaryAdapter.setItems(getResponse);


                            //리사이클러뷰 데이터 추가
//                            diaryAdapter.setItems(getResponse);

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
                Log.d(TAG, "getParams: 넣은 데이터 = "+user_id);
                params.put("user_id", user_id);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: 요청 보냄");
    }

    private static class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
        interface OnDiaryClickListener {
            void onDiaryClicked(Diary model);
        }

        private OnDiaryClickListener mListener;

        private List<Diary> mItems = new ArrayList<>();

        public DiaryAdapter() {}

        public DiaryAdapter(OnDiaryClickListener listener) {
            mListener = listener;
        }

        public void setItems(List<Diary> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_diary, parent, false);
            final DiaryViewHolder viewHolder = new DiaryViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final Diary item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.onDiaryClicked(item);
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
            Diary item = mItems.get(position);

            holder.tvTitle.setText(item.getUser_title());
            holder.tvCondition.setText("기분 상태 : "+item.getUser_condition());
            holder.tvContents.setText(item.getUser_text());
            holder.tvData.setText(item.getReg_time());
        }

        @Override
        public int getItemCount() {
            return (null != mItems ? mItems.size() : 0);
        }

        public static class DiaryViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvCondition;
            TextView tvContents;
            TextView tvData;

            public DiaryViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvCondition = itemView.findViewById(R.id.tv_condition);
                tvContents = itemView.findViewById(R.id.tv_contents);
                tvData = itemView.findViewById(R.id.tv_date);

            }
        }
    }
}
