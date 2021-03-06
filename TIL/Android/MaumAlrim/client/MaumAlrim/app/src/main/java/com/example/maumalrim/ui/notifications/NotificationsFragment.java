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
            // requestQueue ?????? ??????
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        fragmentNotificationsBinding = FragmentNotificationsBinding.inflate(getLayoutInflater());

        fragmentNotificationsBinding.tvMyTitle.setText(userNickname+"?????? ??????");

        fragmentNotificationsBinding.rcDiary.setHasFixedSize(true);

        //?????????
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
//        diaries.add(new Diary("dsf","df","????????????","??? ?????????","2020-06-27"));
//
//        diaryAdapter.setItems(diaries);






//        fragmentNotificationsBinding.textNotifications.setText(AutoLogin.getUserName(getContext().getApplicationContext())+"??? ???????????????.");

        //??????????????? ??? ?????? ?????? : ?????????????????? ??????????????? ????????? ?????????
        fragmentNotificationsBinding.btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //??????????????? ??? ?????? ?????? : ?????????????????? ??????????????? ????????? ?????????

                AutoLogin.clearUserName(getContext().getApplicationContext());//??????????????? ?????? ??? ????????????
                Log.d(TAG, "onClick: AutoLogin ????????? ?????? ?????? : "+AutoLogin.getUserName(getContext().getApplicationContext()));
                Information.clearUserName(getContext().getApplicationContext());//?????? ?????? ??????
                Log.d(TAG, "onClick: Information ????????? ?????? ?????? : "+Information.getArr(getContext().getApplicationContext()));
                MyChatRoom.clearAll(getContext().getApplicationContext());
                Log.d(TAG, "onClick: MyChatRoom ????????? ??????"+MyChatRoom.getArr(getContext().getApplicationContext()));

                Intent stopServiceIntent = new Intent(getContext().getApplicationContext(), MyService.class);
                getActivity().stopService(stopServiceIntent);
                Intent intent = new Intent(getContext().getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });


        fragmentNotificationsBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????? ????????? ????????? ??? MyDiaryActivity -> ?????? ???????????? ???
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatbotDailyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("diaryCode", "");
                intent.putExtra("title", "");
                intent.putExtra("condition", "");
                intent.putExtra("contents", "");
                startActivity(intent);
            }
        });


        //????????????
        fragmentNotificationsBinding.llSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //????????? ????????????
                makeRequest("/getDiary.php",AutoLogin.getUserName(getContext().getApplicationContext()));

                fragmentNotificationsBinding.llSwipeRefresh.setRefreshing(false);
            }
        });



        return fragmentNotificationsBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ??????");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ??????");
        makeRequest("/getDiary.php",AutoLogin.getUserName(getContext().getApplicationContext()));
    }

    public void makeRequest(String url, final String user_id){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP+url,
                new Response.Listener<String>() {
                    @Override //?????? ????????? ????????? ??????
                    public void onResponse(String response) {//????????? ?????? ???????????? ??????
                        Log.i(TAG, "onResponse: ??????-> "+response);
                        ArrayList<Diary> getResponse = new ArrayList<>();
//                        ArrayList<Diary> diaries = new ArrayList<>();

                        if (response.contains("???????????? ????????????.")){
                            Log.i(TAG, "onResponse: ????????? ??????!");
                            //????????? ?????? ??? ???????????? ?????????

                            return;
                        }

                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(StaticValue.getJsonName);

                            //?????????????????? 0????????? ??????

                            int index = 0;
                            for (int i = jsonArray.length()-1; i >= 0; i--){
                                Diary diary = gson.fromJson(jsonArray.get(i).toString(), Diary.class);
                                getResponse.add(diary);

                                Log.i(TAG, "getResponse ?????? : "+getResponse.get(index).toString());//??? ?????????!!
                                index++;
                            }

//                            diaries.add(new Diary("dsf","df","????????????","??? ?????????","2020-06-27"));
                            diaryAdapter.setItems(getResponse);


                            //?????????????????? ????????? ??????
//                            diaryAdapter.setItems(getResponse);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override //????????? ????????? ??????
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: ??????-> "+error.getMessage());
                    }
                }
        ){
            @Override //????????? ???????????? ??????????????? ??????
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d(TAG, "getParams: ?????? ????????? = "+user_id);
                params.put("user_id", user_id);
                return params;
            }
        };
        request.setShouldCache(false); //?????? ????????? ?????? ??????
        requestQueue.add(request);
        Log.i(TAG, "makeRequest: ?????? ??????");
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
            holder.tvCondition.setText("?????? ?????? : "+item.getUser_condition());
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
