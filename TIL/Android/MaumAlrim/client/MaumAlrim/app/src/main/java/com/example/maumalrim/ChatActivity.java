package com.example.maumalrim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maumalrim.Adapter.ChatAdapter;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivityChatBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
/*채팅창을 열면 처음에 챗봇라미가 먼저 말을 시키고,
* 상담 주제와 내용을 받고, 현재 접속해있는 상담사를 찾아서 연결해준다.*/
    private static final String TAG = "ChatActivity";
    ActivityChatBinding activityChatBinding;
    ChatAdapter chatAdapter;
    String stText = "";
    String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = activityChatBinding.getRoot();
        setContentView(view);
        Log.d(TAG, "1. onCreate: 시작");

        //뷰를 나눌때 유저 이메일에서 보낸 메세지가 맞는지 확인하기 위한 용도
        userEmail = AutoLogin.getUserName(getApplicationContext());
        Log.d(TAG, "2. 쉐어드에서 잘 받아왔나? userChatId : "+userEmail);   // 잘 받아왔는지 확인

        StaticValue.chatArrayList = new ArrayList<>();

        /*리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 아니면 일정한 크기를 사용할지를 지정한다.
         이를 false로 준다면 매번 아이템들의 크기를 계산해야 하므로 성능 저하에 영향을 미칠 수 있으니 true를 주는 게 좋다.*/
        activityChatBinding.myRecyclerView.setHasFixedSize(true);
        Log.d(TAG, "3. onCreate: 리사이클러뷰 안 아이템의 크기를 일정한 크기로 사용한다.");

        if (StaticValue.chatArrayList.size()==0) {
            StaticValue.chatArrayList.add(0, new Chat("안녕하세요, 챗봇 라미입니다.\n전 상담사를 연결해줄 수 있어요! \n상담 내용을 간략하게 알려주시겠어요?", "라미"));
            Log.d(TAG, "4. onCreate: 채팅 처음 켰을 경우 : 어레이리스트에 라미의 첫 마디를 입력한다. (채팅 크기 : " + StaticValue.chatArrayList.size() + ")");
        }
        chatAdapter = new ChatAdapter(StaticValue.chatArrayList, userEmail);
        Log.d(TAG, "4. onCreate: 현재 유저 이메일을 파라미터에 넣고 ChatAdapter 클래스의 인스턴스를 생성한다.");
        activityChatBinding.myRecyclerView.setAdapter(chatAdapter);
        Log.d(TAG, "5. onCreate: 채팅리사이클러뷰에 채팅어뎁터 인스턴스를 추가한다.");

        TextChanged(activityChatBinding.etText);
        Log.d(TAG, "6. onCreate: 텍스트가 바꼈습니다.");


    }

    public void onIconClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                Log.d(TAG, "onClick: 이 엑티비티 열기 전 화면으로 돌리기 위해서");
                if (AutoLogin.getUserName(getApplicationContext()).contains("@")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                 finish();
                return;
            case R.id.img_back_home:
                Log.d(TAG, "onClick: 메인 첫 페이지로 돌아간다");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return;
        }
    }

    public void onSendMessage(View view) {
        Log.d(TAG, "    1) onClick: 시작");
        stText = activityChatBinding.etText.getText().toString();
        Log.d(TAG, "    2) onClick: 유저 입력 = "+stText);

        //유저가 텍스트 입력하면 스래드로 출력
        BackgroundThread backgroundThread = new BackgroundThread();

        StaticValue.chatArrayList.add(new Chat(stText, userEmail));
        Log.d(TAG, "    3) onClick: 유저의 메세지를 추가한다. (채팅 크기 : "+StaticValue.chatArrayList.size()+")");


        /*상담내용이 있고, '나가기'라고 입력하면 창 닫음*/
        if (stText.contains("나가기") && StaticValue.counselingReceipt.size()>0){
            finish();
            //채팅 초기화
            StaticValue.chatArrayList = new ArrayList<>();
            activityChatBinding.myRecyclerView.setAdapter(chatAdapter);
            Log.d(TAG, "onClick: chatAdapter를 리사이클러뷰에 적용한다.");

            chatAdapter.notifyDataSetChanged();
            Log.d(TAG, "onClick: chatAdapter에 바뀐 내용을 알려준다.");

            /*상담기록지에 내용이 추가되어있고, 유저가 채팅창을 나갔으면 상담사에게 메세지가 간다.*/

            return;
        }

        Log.d(TAG, "    4) onClick: BackgroundThread 시작");
        backgroundThread.start();


        activityChatBinding.etText.getText().clear();
        Log.d(TAG, "    7) onClick: 채팅내용은 스래드로 보내고 에딧텍스트 채팅창을 비운다");
        activityChatBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);
        Log.d(TAG, "    8) onClick: 채팅창 스크롤은 chatArrayList 마지막(값 : "+(StaticValue.chatArrayList.size()-1)+"번째) 위치로 내린다.");

        activityChatBinding.myRecyclerView.setAdapter(chatAdapter);
        Log.d(TAG, "    9) onClick: chatAdapter를 리사이클러뷰에 적용한다.");

        chatAdapter.notifyDataSetChanged();
        Log.d(TAG, "    10) onClick: chatAdapter에 바뀐 내용을 알려준다.");

        Log.d(TAG, "    11) onClick: 종료");
    }



    class BackgroundThread extends Thread{
        @Override
        public void run() {
            Log.d(TAG, "    5) BackgroundThread : 스래드 들어옴");
            String lamiText = "전 심리테스트를 해주거나\n상담사를 연결해줄 수 있어요! \n [보기]\n1. 심리테스트\n2. 상담사 연결";
            try {
                Log.d(TAG, "    6) BackgroundThread : try");

                Thread.sleep(1000);
                Chatbot chatbot = new Chatbot(stText);
                lamiText = chatbot.ChatbotText();

                if (lamiText.contains("상담이 접수되었습니다")){

                    //상담접수 완료 메세지
                    if (StaticValue.counselingReceipt.size()>0){
                        Log.d(TAG, "onDestroy:  상담사에게 채팅을 보냄");

                        //상담 분류와 내용
                        Log.d(TAG, "onDestroy: "+StaticValue.counselingReceipt.get(0));
                        Log.d(TAG, "onDestroy: "+StaticValue.counselingReceipt.get(1));

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
                                        +";;"+StaticValue.counselingReceipt.get(1);//유저id+상담내용+현재시간

                        //서비스 실행
                        Log.d(TAG, "1. onDestroy: 서비스 실행");
                        Log.d(TAG, "2. 상담하기 : onStart: 포그라운드서비스 실행");

                        Intent startServiceIntent = new Intent(getApplicationContext(), MyService.class);
                        Log.d(TAG, "3. 상담하기 : startServiceIntent - "+startServiceIntent.toString());

                        startForegroundService(startServiceIntent);
                        Log.d(TAG, "4. 상담하기 : 서비스로 보내기 실행");

                    }
                }

                StaticValue.chatArrayList.add(new Chat(lamiText, "라미"));
                for (int i = 0; i <  StaticValue.chatArrayList.size(); i++){
                    Log.d(TAG, i+"번째 이메일(스래드) : "+StaticValue.chatArrayList.get(i).getUserEmail());
                    Log.d(TAG, i+"번째 택스트(스래드) : "+StaticValue.chatArrayList.get(i).getUserChattext());
                }
//                recyclerView.scrollToPosition(chatArrayList.size()-1);//스크롤 마지막으로 내리기
//                System.out.println("thread : 스크롤 마지막으로 내리기");
//                recyclerView.setAdapter(chatAdapter);
//                System.out.println("thread :  recyclerView.setAdapter(chatAdapter);");

                //핸들러에 메세지 추가
                handler.sendEmptyMessage(0);
            }
            catch (Exception e){
                Log.d(TAG, "catch : "+ e );
                Log.e(TAG, "run: 예외사항 : "+ e );
            }

        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage: 핸들러 시작");
            switch (msg.what){
                case 0:
                    activityChatBinding.myRecyclerView.scrollToPosition(StaticValue.chatArrayList.size()-1);//스크롤 마지막으로 내리기
                    Log.d(TAG, "handleMessage: thread : 스크롤 마지막으로 내리기");
                    chatAdapter.notifyDataSetChanged();
                    Log.d(TAG, "handleMessage: thread : chatAdapter.notifyDataSetChanged();");
//                    BackgroundThread thread = new BackgroundThread();
//                    thread.start();
//                    thread.run();
                    for (int i = 0; i < StaticValue.chatArrayList.size(); i++){
                        Log.d(TAG, i+"번째 이메일 : "+StaticValue.chatArrayList.get(i).getUserEmail());
                        Log.d(TAG, i+"번째 택스트 : "+StaticValue.chatArrayList.get(i).getUserChattext());
                    }
                    break;
                case 1:
                    Log.d(TAG, "makeRequest(); 요청 보냅니다.");

                    return;
            }
        }
    };

    void TextChanged(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (editText.getText().length() != 0){
                    activityChatBinding.btnSend.setEnabled(true);
                }
                else {
                    activityChatBinding.btnSend.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().length() != 0){
                    activityChatBinding.btnSend.setEnabled(true);
                }
                else {
                    activityChatBinding.btnSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().length() != 0){
                    activityChatBinding.btnSend.setEnabled(true);
                }
                else {
                    activityChatBinding.btnSend.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 실행");
        Log.d(TAG, "onStop: 상담지 크기 - "+StaticValue.counselingReceipt.size());

        //서비스가 실행중이면 챗봇 끝내기
        if (MyService.serviceIntent != null){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: 채팅창 나감");
        Log.i(TAG, "onDestroy: 상담지 크기: "+StaticValue.counselingReceipt.size());

        //챗봇 상담접수 시 서비스로 감

        if (StaticValue.counselingReceipt.size()>0){
            //보내고 나서 해당 채팅기록지는 삭제
            StaticValue.counselingReceipt = new ArrayList<>();
        }
    }
}
