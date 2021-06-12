package com.example.maumalrim;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.maumalrim.Adapter.ChatAdapter;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivityChatbotBinding;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatbotActivity extends AppCompatActivity {

    private static final String TAG = "ChatbotActivity";
    private SessionResponse watsonAssistantSession;
    private Assistant watsonAssistant;
    public static boolean isConsultationReception = false;

    String apikey;
    String version;
    String url;
    String assistant_id;
    GetData getData;
    String userEmail;
    ChatAdapter chatAdapter;


    ActivityChatbotBinding activityChatbotBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 시작");
        activityChatbotBinding = ActivityChatbotBinding.inflate(getLayoutInflater());
        View view = activityChatbotBinding.getRoot();
        setContentView(view);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        isConsultationReception = true;
        Log.d(TAG, " isConsultationReception = "+isConsultationReception);

        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.my_chat_bubble);

        //챗봇 Api를 쓰기위해 세팅
        apikey = StaticValue.apikey;
        version = StaticValue.version;
        url = StaticValue.url;
        assistant_id = StaticValue.assistant_id;

        //뷰를 나눌때 유저 이메일에서 보낸 메세지가 맞는지 확인하기 위한 용도
        userEmail = AutoLogin.getUserName(getApplicationContext());

        //챗봇창 초기화
        StaticValue.chatArrayList = new ArrayList<>();
        StaticValue.chatArrayList.add(new Chat("안녕하세요! 챗봇 라미에요~!\n전 상담접수를 도와드릴 수 있어요.\n상담을 원하시나요?","챗봇라미"));

        Log.d(TAG, "onCreate: 챗봇 데이터 크기 : "+StaticValue.chatArrayList.size());

        activityChatbotBinding.myRecyclerView.setHasFixedSize(true);
        Log.d(TAG, "3. onCreate: 리사이클러뷰 안 아이템의 크기를 일정한 크기로 사용한다.");

        //처음 액티비티 창 열었을 때 메세지 보내야함 -> 여기서 한번 챗봇 api에 메세지 '안녕'이라고 보내기
 /*       getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, "안녕");*/

        chatAdapter = new ChatAdapter(StaticValue.chatArrayList, userEmail);

        if (isConsultationReception){

            chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    // TODO : 아이템 클릭 이벤트를 MainActivity에서 처리.
                    if (StaticValue.chatArrayList.get(position).getUserEmail().equals("유형옵션")) {
//                    Toast.makeText(getApplicationContext(), position+"번 "+StaticValue.chatArrayList.get(position).getUserChattext(), Toast.LENGTH_SHORT).show();

                        //버튼 배경 변경
//                    drawable.setColor(Color.GRAY);
//                    v.setClickable(false);

                        StaticValue.chatArrayList.add(new Chat(StaticValue.chatArrayList.get(position).getUserChattext(), userEmail));
                        activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
                        chatAdapter.notifyDataSetChanged();

                        //유저가 클릭한 것 보내기
                        getData = new GetData();
                        getData.execute(url, apikey, assistant_id, version, StaticValue.chatArrayList.get(position).getUserChattext());


                        Log.d(TAG, "onItemClick: 클릭이벤트 끝!!!");
                    }
                }
            });
        }


        activityChatbotBinding.myRecyclerView.setAdapter(chatAdapter);


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

        if (StaticValue.chatArrayList.size()>1){

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            Log.d(TAG, "6. onCreate: 키보드가 텍스트 입력창 가리지 않기");
        }


        TextChanged(activityChatbotBinding.etText);
        Log.d(TAG, "6. onCreate: 텍스트가 바꼈습니다.");
    }

    //메세지 보내는 곳
    public void onSendMessage(View view) {
        Log.d(TAG, "onSendMessage: 시작");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        String userMsg = activityChatbotBinding.etText.getText().toString();
        if (userMsg.equals("나가기")){
            finish();

            Log.d(TAG, "onClick: chatAdapter에 바뀐 내용을 알려준다.");
            return;
        }

        StaticValue.chatArrayList.add(new Chat(userMsg, userEmail));
        activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
        chatAdapter.notifyDataSetChanged();

        //유저가 클릭한 것 보내기
        getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, userMsg);

        activityChatbotBinding.etText.getText().clear();
        Log.d(TAG, "    7) onClick: 채팅내용은 스래드로 보내고 에딧텍스트 채팅창을 비운다");

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

        //상담이 접수된 후에 stop 되면 finish
        for (int i = StaticValue.chatArrayList.size()-1; i>=0 ; i--){
            if (StaticValue.chatArrayList.get(i).getUserChattext().contains("상담이 접수되었습니다.")&&StaticValue.chatArrayList.get(i).getUserEmail().contains("챗봇라미")){
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: 시작");
        getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, "명령:상담초기화");

//        StaticValue.chatArrayList = new ArrayList<>();
    }

    public void onIconClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;

            case  R.id.img_back_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetData";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "1. onPreExecute: 백그라운드 작업을 수행하기 전에 호출되며 메인 스레드에서 실행되고 초기화작업에 사용");
            StaticValue.chatArrayList.add(new Chat("메세지 입력중","챗봇라미"));
            activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
            chatAdapter.notifyDataSetChanged();
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



//                JSONArray intents = output.getJSONArray("intents");
//                Log.d(TAG, "onPostExecute: intents : "+intents.toString());

//                String strIntent = "";

                /*if (intents.length()>0) {
                    JSONObject intent = intents.getJSONObject(0);
                    strIntent = intent.getString("intent");
                    Log.d(TAG, "strIntent: " + strIntent);
                }*/

//                if (strIntent.equals("환영인사")) {
                    Log.d(TAG, "onPostExecute: generic.length() = " + generic.length());


//                    JSONObject object1 = generic.getJSONObject(0);
//                    String text = object1.getString("text");

                String text = "";
                String title = "";
                if (StaticValue.chatArrayList.size()>1) {
                    StaticValue.chatArrayList.remove(StaticValue.chatArrayList.size() - 1);
                }
//                Log.i(TAG, "라미 text - " + text);
//                StaticValue.chatArrayList.add(new Chat(text, "챗봇라미"));
                    for (int i = 0; i < generic.length(); i++){

                        JSONObject object = generic.getJSONObject(i);
                        Log.d(TAG, i+"번 onPostExecute: genericObject : "+ object.toString());

                        if (object.toString().contains("options")){
                            title = object.getString("title");
//                            Log.d(TAG, "onPostExecute: title = "+title);

                            //옵션
                            JSONArray options = object.getJSONArray("options");
                            Log.d(TAG, "onPostExecute: options : " + options.toString());
                            StaticValue.chatArrayList.add(new Chat(title, "챗봇라미"));

                            for (int j = 0; j < options.length(); j++) {
                                JSONObject jsonObject1 = options.getJSONObject(j);
//                                Log.d(TAG, "    유형 " + (i + 1) + ". " + jsonObject1.getString("label"));
                                StaticValue.chatArrayList.add(new Chat(jsonObject1.getString("label"), "유형옵션"));
                            }

                        }else {

                                if (i == 0) {
                                    text += object.getString("text");
                                    Log.i(TAG, i + "번째 text - " + text);
                                } else {
                                    text += "\n" + object.getString("text");
                                    Log.i(TAG, i + "번째 text - " + text);
                                }

                            }
                    }

                    if (text.equals("")){

                    }else if (text.equals("상담 접수를 초기화 합니다.")){
                        StaticValue.chatArrayList = new ArrayList<>();
                        return;
                    }
                    else {
                        if (text.contains("상담이 접수되었습니다.")){
                            isConsultationReception = false;
                            Log.d(TAG, " isConsultationReception = "+isConsultationReception);

                            //상담유형 찾기
                            for (int i = StaticValue.chatArrayList.size()-1; i>=0 ; i--){
                                if (StaticValue.chatArrayList.get(i).getUserChattext().contains("고민하고 계신 내용을 간략하게 작성해주시겠어요?")&&StaticValue.chatArrayList.get(i).getUserEmail().contains("챗봇라미")){
                                    Log.d(TAG, "onPostExecute: 상담 유형 : "+StaticValue.chatArrayList.get(i-1).getUserChattext());
                                    Log.d(TAG, "onPostExecute: 상담 내용 : "+StaticValue.chatArrayList.get(i+1).getUserChattext());

                                    ArrayList<UserInfo> userInfos = Information.getArr(getApplicationContext());
                                    String nickname = userInfos.get(0).getUser_nickname();
                                    String birthYear = userInfos.get(0).getBirth_year();
                                    String userYear = (Calendar.getInstance().get(Calendar.YEAR)-(Integer.parseInt(birthYear)))+"";
                                    Log.d(TAG, "onDestroy: 유저 만 나이 - "+userYear);

                                    //현재날짜
                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                                    String regDate = simpleDateFormat.format(calendar.getTime());

                                    // 상담유형//상담내용
                                    //유저는 채팅방이 생성되어야함 : birth_year, gender, job,
                                    StaticValue.request =
                                            AutoLogin.getUserName(getApplicationContext())
                                                    +";;"+nickname
                                                    +";;"+regDate
                                                    +";;"+StaticValue.chatArrayList.get(i-1).getUserChattext()//상담유형
                                                    +";;"+StaticValue.chatArrayList.get(i+1).getUserChattext();//상담내용

                                    //서비스 실행
                                    Log.d(TAG, "1. onDestroy: 서비스 실행");
                                    Log.d(TAG, "2. 상담하기 : onStart: 포그라운드서비스 실행");

                                    Intent startServiceIntent = new Intent(getApplicationContext(), MyService.class);
                                    Log.d(TAG, "3. 상담하기 : startServiceIntent - "+startServiceIntent.toString());

                                    startForegroundService(startServiceIntent);
                                    Log.d(TAG, "4. 상담하기 : 서비스로 보내기 실행");
                                    break;
                                }
                            }
                        }


                        StaticValue.chatArrayList.add(new Chat(text, "챗봇라미"));

                    }


                    activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
                    chatAdapter.notifyDataSetChanged();

//                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.i(TAG, "onPostExecute: 예외발생");
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
//                Log.d(TAG, "doInBackground: response 스트링 : "+response.toString());

                return response.toString();

            }catch (Exception e){
                Log.d(TAG, "InsertData: Error -> "+e);
                return null;
            }
        }
    }

    void TextChanged(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (editText.getText().length() != 0){
                    activityChatbotBinding.btnSend.setEnabled(true);
                }
                else {
                    activityChatbotBinding.btnSend.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().length() != 0){
                    activityChatbotBinding.btnSend.setEnabled(true);
                }
                else {
                    activityChatbotBinding.btnSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().length() != 0){
                    activityChatbotBinding.btnSend.setEnabled(true);
                }
                else {
                    activityChatbotBinding.btnSend.setEnabled(false);
                }
            }
        });
    }

}
