package com.example.maumalrim;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maumalrim.Adapter.MainChat;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivityMainChatBinding;

import java.util.ArrayList;

public class MainChatActivity extends AppCompatActivity {

//    여기서는 라미가 상담사에게 채팅메세지를 보내면 상담사가 메세지를 받고,
    //상담사가 채팅을 보내면 유저에게 채팅방이 생성되고, 채팅 알람이 뜬다.
    private static final String TAG = "MainChatActivity";
//    private static ArrayList<Chat> chatArrayList = new ArrayList<>();


    ActivityMainChatBinding activityMainChatBinding;
    ArrayList<UserInfo> userInfos;
    ArrayList<EmployeeInfo> employeeInfos;
    Intent foregroundServiceIntent;
    MainChat chatAdapter;
    String stText = "";
    String userChatId;
    String nickName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainChatBinding = ActivityMainChatBinding.inflate(getLayoutInflater());
        View view = activityMainChatBinding.getRoot();
        setContentView(view);

        //뷰를 나눌때 유저 이메일에서 보낸 메세지가 맞는지 확인하기 위한 용도
        userChatId = AutoLogin.getUserName(getApplicationContext());
        Log.d(TAG, "쉐어드에서 잘 받아왔나? userEmail : "+userChatId);   // 잘 받아왔는지 확인

        /*리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 아니면 일정한 크기를 사용할지를 지정한다.
         이를 false로 준다면 매번 아이템들의 크기를 계산해야 하므로 성능 저하에 영향을 미칠 수 있으니 true를 주는 게 좋다.*/
        activityMainChatBinding.rvChatRoom.setHasFixedSize(true);
//        Log.d(TAG, "onCreate: 리사이클러뷰 안 아이템의 크기를 일정한 크기로 사용한다.");



        //닉네임
        //유저일때
        if (AutoLogin.getUserName(this).contains("@")){
            Log.d(TAG, "onCreate: 유저일 때 접근");
            userInfos = Information.getArr(this);
            nickName = userInfos.get(0).getUser_nickname();

            if (StaticValue.chats.size()>0){
                Log.d(TAG, "onCreate: 첫번째 메세지 : "+StaticValue.chats.get(0).getUserChattext());
                String message = StaticValue.chats.get(0).getUserChattext();
                String[] receive = message.split(" ");
                activityMainChatBinding.tvMainTitle.setText( receive[1]+" 상담");
            }




        }else {// 상담사일 때
            Log.d(TAG, "onCreate: 상담사일 때 접근");
            employeeInfos = Information.getArr(this);
            nickName = employeeInfos.get(0).getUser_nickname();

            Intent passedIntent = getIntent();
//            processIntent(passedIntent);

            String strMsg = "- 닉네임 : "+passedIntent.getStringExtra("name")+"\n"
                    +"- 상담유형 : "+passedIntent.getStringExtra("category")+"\n"
                    +"- 내용 : "+passedIntent.getStringExtra("message");
            Log.d(TAG, "onCreate: 인텐트에서 받은 메세지 - "+strMsg);

            activityMainChatBinding.tvMainTitle.setText(passedIntent.getStringExtra("name")+"님의 상담");

            StaticValue.chats.add(new Chat(strMsg, StaticValue.recipientId));

        }
        Log.d(TAG, "onStart: 닉네임 : "+ nickName);


        //현재 유저 이메일을 파라미터에 넣고 ChatAdapter 클래스의 인스턴스를 생성한다.
        chatAdapter = new MainChat(StaticValue.chats,userChatId);

        //채팅리사이클러뷰에 채팅어뎁터 인스턴스를 추가한다.
        activityMainChatBinding.rvChatRoom.setAdapter(chatAdapter);

        chatAdapter.notifyDataSetChanged();
        activityMainChatBinding.rvChatRoom.scrollToPosition(StaticValue.chats.size()-1);

        TextChanged(activityMainChatBinding.etText);


    }

    @Override
    protected void onStart() {
        super.onStart();

/*        //처음 앱 실행
        if (MyService.serviceIntent == null){

            foregroundServiceIntent = new Intent(this, MyService.class);

            startForegroundService(foregroundServiceIntent);
            Toast.makeText(getApplicationContext(), "채팅 서비스 시작", Toast.LENGTH_LONG).show();

        }else {//포그라운드 실행중일 때

            foregroundServiceIntent = MyService.serviceIntent;
            Toast.makeText(getApplicationContext(), "채팅 서비스 중", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }


    //전송 버튼 클릭 : 메세지를 전송했을 때 리사이클러뷰 추가
    public void onSendMessage(View view) {

        stText = activityMainChatBinding.etText.getText().toString();
        Log.d(TAG, "onSendMessage: 유저 입력 = "+stText);

        StaticValue.chats.add(new Chat(stText, userChatId));
        activityMainChatBinding.rvChatRoom.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        activityMainChatBinding.rvChatRoom.scrollToPosition(StaticValue.chats.size()-1);

        activityMainChatBinding.etText.getText().clear();

        //상담사 메세지를 서버로 보내야함
//        String request = "message::" + stText;
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        intent.putExtra("myMsg", stText);//보낼때 아이디랑 닉네임도 보내야함
        intent.putExtra("recipientId","ddd");//채팅 보낼 사람

        startForegroundService(intent);

    }

    public void onBackPage(View view) {
        if (AutoLogin.getUserName(getApplicationContext()).contains("@")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), EmployeeMypageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    //X버튼 누르면 "상담을 종료하시겠습니까?"다이얼로그가 뜨고, ok 누르면 상담이 종료됨.
    //유저는 서비스 종료되고, 상담사는 서비스 종료 안됨
    //대화내용은 쉐어드에 저장되고,
    // 백업하기를 누르면 서버에 올라감-나중에
    public void onChangPage(View view) {
        //다이얼로그 띄우기 : 1. 유저 - 소켓 닫기 2. 공통 : 앱닫으면 receiptID 초기화, 채팅 초기화

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("채팅 종료").setMessage("채팅을 종료하시겠습니까?");

        builder.setPositiveButton("채팅종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (AutoLogin.getUserName(getApplicationContext()).contains("@")){
                    //실행중인 서비스 종료
                    Intent stopServiceIntent = new Intent(getApplicationContext(), MyService.class);
                    stopService(stopServiceIntent);

                    StaticValue.recipientId = "null";
                    //채팅창 초기화
                    StaticValue.chats = new ArrayList<>();
                    //현재창 닫음
                    Toast.makeText(getApplicationContext(), "상담이 종료되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {//상담사일 때 -> 요약작성페이지 이동
                    if (StaticValue.isUserGone){

                      Log.d(TAG, "onClick: 상담사 채팅방 나가서 작성 페이지로 이동");
                      Intent intent = new Intent(getApplicationContext(), ChatContentsActivity.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                      startActivity(intent);
                      finish();
                    }
                    else {
                        //내담자가 나가지 않으면 종료 못함
                        Toast.makeText(getApplicationContext(), "내담자가 나간 후에 종료해주세요.", Toast.LENGTH_LONG).show();

                    }
                }


            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //onNewIntent 메소드가 호출 되게 된다. -> mainActivity로 넘어 갈것.
    // 서비스쪽에서 던져준 데이터를 받기위한 메서드이다. processCommand는 출력하기위한 메소드
    // 처음이면 oncreate에서 확인 하고 그렇지 않으면(처음이 아니라면) oncreate에서 호출되지 않고
    // onNewIntent() 를 호출 하게 된다. 서비스 -> 액티비티에서 확인하는경우.

    @Override
    protected void onNewIntent(Intent intent) {
        //액티비티가 이미 만들어져 있을 때 전달된 인텐트 처리하기
        super.onNewIntent(intent);

        //받은 메세지 처리하기, 메시지 받을때 유저 ID도 같이 받아야함
        if (intent != null){
            //리사이클러뷰에 적용
            activityMainChatBinding.rvChatRoom.setAdapter(chatAdapter);
            //어뎁터에 알려준다
            chatAdapter.notifyDataSetChanged();
            activityMainChatBinding.rvChatRoom.scrollToPosition(StaticValue.chats.size()-1);
 /*           if (intent.getStringExtra("receive").contains("::")){
                String[] receive = intent.getStringExtra("receive").split("::");

//            String otherID = intent.getStringExtra("otherID");
                Log.d(TAG, "onNewIntent: 받은 메세지 - "+receive[1]);
                Log.d(TAG, "onNewIntent: 받은 아이디 - "+receive[0]);

                if (AutoLogin.getUserName(getApplication()).contains("@")){
                    StaticValue.recipientId = receive[0];//상담사 id 저장
                }

                if (receive[0].equals(nickName)){
                    //받은 아이디와 유저 id가 같을 때
                    Log.d(TAG, "onNewIntent: 받은 아이디와 유저 id가 같음");

                }else {
                    Log.d(TAG, "onNewIntent: 받은 아이디와 유저 id가 다름");

                    //아이템 추가하기
//                    StaticValue.chats.add(new Chat(receive[1],receive[0]));

                    //리사이클러뷰에 적용
                    activityMainChatBinding.rvChatRoom.setAdapter(chatAdapter);
                    //어뎁터에 알려준다
                    chatAdapter.notifyDataSetChanged();
                    activityMainChatBinding.rvChatRoom.scrollToPosition(StaticValue.chats.size()-1);
                }
            } else {
                Toast.makeText(getApplicationContext(), "응답 메세지 오류! 로그 확인", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onNewIntent: 응답 내용 : "+intent.getStringExtra("receive"));
                *//*응답내용
                *1. 응답 내용 : 01012345678 퇴장
                *//*


            }*/


        }
    }

    private void processIntent(Intent intent){
        if (intent != null){
            if (AutoLogin.getUserName(getApplication()).contains("@")){

            }else {
                //상담사 계정일 경우, name, message를 넘겨받음
//                intent.getStringExtra("name");
//                intent.getStringExtra("message");

//                String strMsg = "- 이름 : "+intent.getStringExtra("name")+"\n"
//                        +"-내용 : "+intent.getStringExtra("message");

//                chatArrayList.add(new Chat(stText, StaticValue.recipientId));
//                activityMainChatBinding.rvChatRoom.setAdapter(chatAdapter);
//                chatAdapter.notifyDataSetChanged();
//                activityMainChatBinding.rvChatRoom.scrollToPosition(chatArrayList.size()-1);
            }
        }
    }

    //텍스트 바뀐것 감지
    void TextChanged(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (editText.getText().length() != 0){
                    activityMainChatBinding.btnSend.setEnabled(true);
                }
                else {
                    activityMainChatBinding.btnSend.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().length() != 0){
                    activityMainChatBinding.btnSend.setEnabled(true);
                }
                else {
                    activityMainChatBinding.btnSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().length() != 0){
                    activityMainChatBinding.btnSend.setEnabled(true);
                }
                else {
                    activityMainChatBinding.btnSend.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
