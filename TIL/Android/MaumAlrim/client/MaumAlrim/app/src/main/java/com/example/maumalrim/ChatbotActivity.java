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
        Log.d(TAG, "onCreate: ??????");
        activityChatbotBinding = ActivityChatbotBinding.inflate(getLayoutInflater());
        View view = activityChatbotBinding.getRoot();
        setContentView(view);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        isConsultationReception = true;
        Log.d(TAG, " isConsultationReception = "+isConsultationReception);

        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.my_chat_bubble);

        //?????? Api??? ???????????? ??????
        apikey = StaticValue.apikey;
        version = StaticValue.version;
        url = StaticValue.url;
        assistant_id = StaticValue.assistant_id;

        //?????? ????????? ?????? ??????????????? ?????? ???????????? ????????? ???????????? ?????? ??????
        userEmail = AutoLogin.getUserName(getApplicationContext());

        //????????? ?????????
        StaticValue.chatArrayList = new ArrayList<>();
        StaticValue.chatArrayList.add(new Chat("???????????????! ?????? ????????????~!\n??? ??????????????? ???????????? ??? ?????????.\n????????? ????????????????","????????????"));

        Log.d(TAG, "onCreate: ?????? ????????? ?????? : "+StaticValue.chatArrayList.size());

        activityChatbotBinding.myRecyclerView.setHasFixedSize(true);
        Log.d(TAG, "3. onCreate: ?????????????????? ??? ???????????? ????????? ????????? ????????? ????????????.");

        //?????? ???????????? ??? ????????? ??? ????????? ???????????? -> ????????? ?????? ?????? api??? ????????? '??????'????????? ?????????
 /*       getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, "??????");*/

        chatAdapter = new ChatAdapter(StaticValue.chatArrayList, userEmail);

        if (isConsultationReception){

            chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    // TODO : ????????? ?????? ???????????? MainActivity?????? ??????.
                    if (StaticValue.chatArrayList.get(position).getUserEmail().equals("????????????")) {
//                    Toast.makeText(getApplicationContext(), position+"??? "+StaticValue.chatArrayList.get(position).getUserChattext(), Toast.LENGTH_SHORT).show();

                        //?????? ?????? ??????
//                    drawable.setColor(Color.GRAY);
//                    v.setClickable(false);

                        StaticValue.chatArrayList.add(new Chat(StaticValue.chatArrayList.get(position).getUserChattext(), userEmail));
                        activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
                        chatAdapter.notifyDataSetChanged();

                        //????????? ????????? ??? ?????????
                        getData = new GetData();
                        getData.execute(url, apikey, assistant_id, version, StaticValue.chatArrayList.get(position).getUserChattext());


                        Log.d(TAG, "onItemClick: ??????????????? ???!!!");
                    }
                }
            });
        }


        activityChatbotBinding.myRecyclerView.setAdapter(chatAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ??????");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ??????");

        if (StaticValue.chatArrayList.size()>1){

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            Log.d(TAG, "6. onCreate: ???????????? ????????? ????????? ????????? ??????");
        }


        TextChanged(activityChatbotBinding.etText);
        Log.d(TAG, "6. onCreate: ???????????? ???????????????.");
    }

    //????????? ????????? ???
    public void onSendMessage(View view) {
        Log.d(TAG, "onSendMessage: ??????");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        String userMsg = activityChatbotBinding.etText.getText().toString();
        if (userMsg.equals("?????????")){
            finish();

            Log.d(TAG, "onClick: chatAdapter??? ?????? ????????? ????????????.");
            return;
        }

        StaticValue.chatArrayList.add(new Chat(userMsg, userEmail));
        activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
        chatAdapter.notifyDataSetChanged();

        //????????? ????????? ??? ?????????
        getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, userMsg);

        activityChatbotBinding.etText.getText().clear();
        Log.d(TAG, "    7) onClick: ??????????????? ???????????? ????????? ??????????????? ???????????? ?????????");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ??????");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ??????");

        //????????? ????????? ?????? stop ?????? finish
        for (int i = StaticValue.chatArrayList.size()-1; i>=0 ; i--){
            if (StaticValue.chatArrayList.get(i).getUserChattext().contains("????????? ?????????????????????.")&&StaticValue.chatArrayList.get(i).getUserEmail().contains("????????????")){
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ??????");
        getData = new GetData();
        getData.execute(url, apikey, assistant_id, version, "??????:???????????????");

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
            Log.d(TAG, "1. onPreExecute: ??????????????? ????????? ???????????? ?????? ???????????? ?????? ??????????????? ???????????? ?????????????????? ??????");
            StaticValue.chatArrayList.add(new Chat("????????? ?????????","????????????"));
            activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
            chatAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "26. onPostExecute: ??????????????? ????????? ????????? ????????? ?????? ????????? ???????????? ???????????? ????????? ????????????, " +
                    "??????????????? ????????? ????????? ??????????????? ???????????? ?????? ??????");

            Log.d(TAG, "onPostExecute: ?????? : "+result);

            try {
                JSONObject jsonObject = new JSONObject(result);
//                Log.d(TAG, "onPostExecute: jsonObject = "+jsonObject.toString()+"\n\n");

                //????????? JSONObject??? ???????????? key-value??? ??????
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

//                if (strIntent.equals("????????????")) {
                    Log.d(TAG, "onPostExecute: generic.length() = " + generic.length());


//                    JSONObject object1 = generic.getJSONObject(0);
//                    String text = object1.getString("text");

                String text = "";
                String title = "";
                if (StaticValue.chatArrayList.size()>1) {
                    StaticValue.chatArrayList.remove(StaticValue.chatArrayList.size() - 1);
                }
//                Log.i(TAG, "?????? text - " + text);
//                StaticValue.chatArrayList.add(new Chat(text, "????????????"));
                    for (int i = 0; i < generic.length(); i++){

                        JSONObject object = generic.getJSONObject(i);
                        Log.d(TAG, i+"??? onPostExecute: genericObject : "+ object.toString());

                        if (object.toString().contains("options")){
                            title = object.getString("title");
//                            Log.d(TAG, "onPostExecute: title = "+title);

                            //??????
                            JSONArray options = object.getJSONArray("options");
                            Log.d(TAG, "onPostExecute: options : " + options.toString());
                            StaticValue.chatArrayList.add(new Chat(title, "????????????"));

                            for (int j = 0; j < options.length(); j++) {
                                JSONObject jsonObject1 = options.getJSONObject(j);
//                                Log.d(TAG, "    ?????? " + (i + 1) + ". " + jsonObject1.getString("label"));
                                StaticValue.chatArrayList.add(new Chat(jsonObject1.getString("label"), "????????????"));
                            }

                        }else {

                                if (i == 0) {
                                    text += object.getString("text");
                                    Log.i(TAG, i + "?????? text - " + text);
                                } else {
                                    text += "\n" + object.getString("text");
                                    Log.i(TAG, i + "?????? text - " + text);
                                }

                            }
                    }

                    if (text.equals("")){

                    }else if (text.equals("?????? ????????? ????????? ?????????.")){
                        StaticValue.chatArrayList = new ArrayList<>();
                        return;
                    }
                    else {
                        if (text.contains("????????? ?????????????????????.")){
                            isConsultationReception = false;
                            Log.d(TAG, " isConsultationReception = "+isConsultationReception);

                            //???????????? ??????
                            for (int i = StaticValue.chatArrayList.size()-1; i>=0 ; i--){
                                if (StaticValue.chatArrayList.get(i).getUserChattext().contains("???????????? ?????? ????????? ???????????? ?????????????????????????")&&StaticValue.chatArrayList.get(i).getUserEmail().contains("????????????")){
                                    Log.d(TAG, "onPostExecute: ?????? ?????? : "+StaticValue.chatArrayList.get(i-1).getUserChattext());
                                    Log.d(TAG, "onPostExecute: ?????? ?????? : "+StaticValue.chatArrayList.get(i+1).getUserChattext());

                                    ArrayList<UserInfo> userInfos = Information.getArr(getApplicationContext());
                                    String nickname = userInfos.get(0).getUser_nickname();
                                    String birthYear = userInfos.get(0).getBirth_year();
                                    String userYear = (Calendar.getInstance().get(Calendar.YEAR)-(Integer.parseInt(birthYear)))+"";
                                    Log.d(TAG, "onDestroy: ?????? ??? ?????? - "+userYear);

                                    //????????????
                                    Calendar calendar = Calendar.getInstance();

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                                    String regDate = simpleDateFormat.format(calendar.getTime());

                                    // ????????????//????????????
                                    //????????? ???????????? ?????????????????? : birth_year, gender, job,
                                    StaticValue.request =
                                            AutoLogin.getUserName(getApplicationContext())
                                                    +";;"+nickname
                                                    +";;"+regDate
                                                    +";;"+StaticValue.chatArrayList.get(i-1).getUserChattext()//????????????
                                                    +";;"+StaticValue.chatArrayList.get(i+1).getUserChattext();//????????????

                                    //????????? ??????
                                    Log.d(TAG, "1. onDestroy: ????????? ??????");
                                    Log.d(TAG, "2. ???????????? : onStart: ???????????????????????? ??????");

                                    Intent startServiceIntent = new Intent(getApplicationContext(), MyService.class);
                                    Log.d(TAG, "3. ???????????? : startServiceIntent - "+startServiceIntent.toString());

                                    startForegroundService(startServiceIntent);
                                    Log.d(TAG, "4. ???????????? : ???????????? ????????? ??????");
                                    break;
                                }
                            }
                        }


                        StaticValue.chatArrayList.add(new Chat(text, "????????????"));

                    }


                    activityChatbotBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
                    chatAdapter.notifyDataSetChanged();

//                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.i(TAG, "onPostExecute: ????????????");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: ??????????????? ?????? ??????");

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

                //?????? ??????
                MessageResponse response = watsonAssistant.message(options).execute().getResult();
//                Log.d(TAG, "run: "+response);
//                Log.d(TAG, "doInBackground: response ????????? : "+response.toString());

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
