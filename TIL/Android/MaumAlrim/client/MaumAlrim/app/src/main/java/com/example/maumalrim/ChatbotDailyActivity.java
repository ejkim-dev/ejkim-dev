package com.example.maumalrim;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maumalrim.Adapter.ChatDailyAdapter;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.databinding.ActivityChatbotDailyBinding;
import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatbotDailyActivity extends AppCompatActivity {

    public static ArrayList<Chat> chatArrayList;
    private static final String TAG = "ChatbotDailyActivity";
    private static final String CHATBOT_ID = "챗봇데일리";
    private static final String OPTION_ID = "유형옵션";
    private SessionResponse watsonAssistantSession;
    private Assistant watsonAssistant;
    static RequestQueue requestQueue;

    ActivityChatbotDailyBinding activityChatbotDailyBinding;
    Drawable noSend;
    Drawable send;

    String apikey;
    String version;
    String url;
    String assistant_id;
    GetData getData;
    String userEmail;

    ChatDailyAdapter chatDailyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatbotDailyBinding = ActivityChatbotDailyBinding.inflate(getLayoutInflater());
        View view = activityChatbotDailyBinding.getRoot();
        setContentView(view);

        noSend = getResources().getDrawable(R.drawable.ic_send_gray_24dp);
        send = getResources().getDrawable(R.drawable.ic_send_24dp);
        userEmail = AutoLogin.getUserName(getApplicationContext());

        if (requestQueue == null){
            //RequestQueue 객체 생성하기
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        //onCreate 들어왔을 때  "안녕하세요, 일기 챗봇 데일리에요! 오늘 당신에게 무슨일이 있었는지 궁금하답니다~" -> 챗봇 인삿말보내기

        url = StaticValue.url;
        apikey = StaticValue.apikey;
        assistant_id = StaticValue.assistant_id_daily;
        version = StaticValue.version;

        GetData getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, "안녕");

        //리싸이클러뷰
        //챗봇창 초기화
        chatArrayList = new ArrayList<>();
        chatArrayList.add(new Chat("안녕하세요, 일기 챗봇 데일리에요!\n전 당신의 하루를 기록하는 것을 도와줄 수 있어요!\n오늘 하루를 저와 대화를하면서 기록해봐요!",CHATBOT_ID));

        activityChatbotDailyBinding.myRecyclerView.setHasFixedSize(true);
        chatDailyAdapter = new ChatDailyAdapter(chatArrayList, userEmail);
        chatDailyAdapter.setOnItemClickListener(new ChatDailyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (chatArrayList.get(position).getUserEmail().equals(OPTION_ID)){

                    chatArrayList.add(new Chat(chatArrayList.get(position).getUserChattext(), userEmail));
                    activityChatbotDailyBinding.myRecyclerView.scrollToPosition(chatArrayList.size()-1);
                    chatDailyAdapter.notifyDataSetChanged();

                    //유저가 클릭한 것 보내기
                    GetData getData = new GetData();
                    getData.execute(url, apikey, assistant_id, version, chatArrayList.get(position).getUserChattext());


                    Log.d(TAG, "onItemClick: 클릭이벤트 끝!!!");
                }
            }
        });

        activityChatbotDailyBinding.myRecyclerView.setAdapter(chatDailyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextChanged(activityChatbotDailyBinding.etText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatArrayList = new ArrayList<>();
        finish();
    }

    public void onIconClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;

            case R.id.img_back_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void onSendMessage(View view) {
        Log.d(TAG, "onSendMessage: 시작");

        String userMsg = activityChatbotDailyBinding.etText.getText().toString();
        chatArrayList.add(new Chat(userMsg, userEmail));

        activityChatbotDailyBinding.myRecyclerView.scrollToPosition(chatArrayList.size()-1);
        chatDailyAdapter.notifyDataSetChanged();

        getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, userMsg);

        activityChatbotDailyBinding.etText.getText().clear();
    }


    private class GetData extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetData";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "1. onPreExecute: 백그라운드 작업을 수행하기 전에 호출되며 메인 스레드에서 실행되고 초기화작업에 사용");

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "26. onPostExecute: 백그라운드 작업이 끝나면 호출이 되고 메모리 리소스를 해체하는 작업을 주로하고, " +
                    "백그라운드 작업의 결과를 매개변수로 전달받을 수도 있다");

            Log.d(TAG, "onPostExecute: 결과 : "+result);

            try {
                JSONObject jsonObject = new JSONObject(result);
//                Log.d(TAG, "onPostExecute: jsonObject = "+jsonObject.toString()+"\n\n");

                //첫번째 JSONObject를 가져와서 key-value로 읽기
                JSONObject output = jsonObject.getJSONObject("output");
                Log.d(TAG, "onPostExecute: output : "+ output.toString()+"\n\n");

                JSONArray generic = output.getJSONArray("generic");
                Log.d(TAG, "onPostExecute: generic : "+generic.toString()+"\n\n");


                Log.d(TAG, "onPostExecute: generic.length() = " + generic.length());



                String text = "";
                String title = "";
                /*if (chatArrayList.size()>1) {
                    chatArrayList.remove(chatArrayList.size() - 1);
                }*/

                for (int i = 0; i < generic.length(); i++){

                    JSONObject object = generic.getJSONObject(i);
                    Log.d(TAG, i+"번 onPostExecute: genericObject : "+ object.toString());

                    if (object.toString().contains("options")){
                        title = object.getString("title");
                        Log.d(TAG, "onPostExecute: title = "+title);

                        //옵션
                        JSONArray options = object.getJSONArray("options");
                        Log.d(TAG, "onPostExecute: options : " + options.toString());
                        chatArrayList.add(new Chat(title, "챗봇데일리"));

                        for (int j = 0; j < options.length(); j++) {
                            JSONObject jsonObject1 = options.getJSONObject(j);
                            Log.d(TAG, "    유형 " + (i + 1) + ". " + jsonObject1.getString("label"));
                            chatArrayList.add(new Chat(jsonObject1.getString("label"), "유형옵션"));
                        }

                    }else {

                        if (i == 0) {
                            text += object.getString("text");
                            Log.i(TAG, i + "번째 text - " + text);
                        } else {
                            text += "\n" + object.getString("text");
                            Log.i(TAG, i + "번째 text - " + text);
                        }

                        chatArrayList.add(new Chat(text, CHATBOT_ID));
//                        handler.sendEmptyMessage(0);
                    }
                }

                if (text.contains("[내페이지]")){

                    for (int i = chatArrayList.size()-1; i >= 0; i--){
                        if (chatArrayList.get(i).getUserEmail().equals(CHATBOT_ID)
                                &&chatArrayList.get(i).getUserChattext().contains("[내페이지]")){

                            String myTitle = chatArrayList.get(i-1).getUserChattext();
                            String myText = chatArrayList.get(i-3).getUserChattext();
                            String myCondition = chatArrayList.get(i-5).getUserChattext();

                            Log.d(TAG, "onPostExecute: 제목 : "+myTitle+" 기분 : "+myCondition+" 내용 : "+myText);
                            makeRequest("/saveDiary.php", userEmail, myTitle, myCondition, myText);
                            break;
                        }
                    }

                }


                activityChatbotDailyBinding.myRecyclerView.scrollToPosition(chatArrayList.size()-1);
                chatDailyAdapter.notifyDataSetChanged();

//                }

            }catch (JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: 백그라운드 작업 시작");

            String url = params[0];
            String apikey = params[1];
            String assistant_id = params[2];
            String version = params[3];
            String text = params[4];

            try {

                IamAuthenticator authenticator = new IamAuthenticator(apikey);
                watsonAssistant = new Assistant(version, authenticator);

                watsonAssistant.setServiceUrl(url);
//                Log.d(TAG, "makeRequst: assistant name = "+watsonAssistant.getName()+ " | serviceUrl"+watsonAssistant.getServiceUrl()+" | toString = "+watsonAssistant.toString());

                if (watsonAssistantSession == null){
                    ServiceCall<SessionResponse> call = watsonAssistant.createSession(new CreateSessionOptions.Builder().assistantId(assistant_id).build());
                    watsonAssistantSession = call.execute().getResult();
                }

                MessageInput input = new MessageInput.Builder()
                        .text(text) //text
                        .build();
//                Log.d(TAG, "run: input = "+input.toString());

                MessageOptions options = new MessageOptions.Builder()
                        .assistantId(assistant_id)
                        .input(input)
                        .sessionId(watsonAssistantSession.getSessionId())
                        .build();
//                Log.d(TAG, "run: options = "+options.toString());

                //응답 받음
                MessageResponse response = watsonAssistant.message(options).execute().getResult();
//                Log.d(TAG, "run: "+response);
                Log.d(TAG, "doInBackground: response 스트링 : "+response.toString());

                return response.toString();

            }catch (Exception e){
                Log.d(TAG, "InsertData: Error -> "+e);
                return null;
            }
        }
    }

    public void makeRequest(String url, final String user_id, final String user_title, final String user_condition,  final String user_text){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP + url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {
                        Log.d(TAG,"onResponse 응답-> "+response);
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override //에러시 처리할 내용
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"에러-> "+error.getMessage());
                    }
                }
        ){
            @Override //서버가 요청하는 파라미터를 담음
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("user_title",user_title);
                params.put("user_condition",user_condition);
                params.put("user_text",user_text);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.d(TAG,"요청 보냄.");
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage: 핸들러 시작");
            switch (msg.what){
                case 0:
                    activityChatbotDailyBinding.myRecyclerView.scrollToPosition(chatArrayList.size()-1);//스크롤 마지막으로 내리기
                    Log.d(TAG, "handleMessage: thread : 스크롤 마지막으로 내리기");
                    chatDailyAdapter.notifyDataSetChanged();
                    Log.d(TAG, "handleMessage: thread : chatAdapter.notifyDataSetChanged();");
//                    BackgroundThread thread = new BackgroundThread();
//                    thread.start();
//                    thread.run();
                    break;
                case 1:
                    Log.d(TAG, "");

                    return;
            }
        }
    };

    void TextChanged(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (editText.getText().length() != 0){
                    activityChatbotDailyBinding.btnSend.setEnabled(true);
                    activityChatbotDailyBinding.btnSend.setImageDrawable(send);
                }
                else {
                    activityChatbotDailyBinding.btnSend.setEnabled(false);
                    activityChatbotDailyBinding.btnSend.setImageDrawable(noSend);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().length() != 0){
                    activityChatbotDailyBinding.btnSend.setEnabled(true);
                    activityChatbotDailyBinding.btnSend.setImageDrawable(send);

                }
                else {
                    activityChatbotDailyBinding.btnSend.setEnabled(false);
                    activityChatbotDailyBinding.btnSend.setImageDrawable(noSend);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().length() != 0){
                    activityChatbotDailyBinding.btnSend.setEnabled(true);
                    activityChatbotDailyBinding.btnSend.setImageDrawable(send);

                }
                else {
                    activityChatbotDailyBinding.btnSend.setEnabled(false);
                    activityChatbotDailyBinding.btnSend.setImageDrawable(noSend);

                }
            }
        });
    }
}
