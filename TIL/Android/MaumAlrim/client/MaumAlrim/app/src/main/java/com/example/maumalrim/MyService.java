package com.example.maumalrim;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.Item.ChatRoom;
import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.Item.UserList;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    public static Intent serviceIntent = null;
    public static String sendMsg = null;
    private static final String TAG = "MyService";
    private static final String SERVER_IP = "SERVER_IP";
    private static final int SERVER_PORT = 5001;
    public static Socket socket;
    public static String name = "";
    private static  SocketConnect socketConnect;
    private static ArrayList<ChatRoom> chatRooms = new ArrayList<>();//쉐어드에 저장할 채팅방
    private static Intent notificationIntent;

    //서버에 보내야할 것 : user_email, user_nickname, isEmployee
    //포그라운드 서비스 : 강제종료되지 않는 서비스 : 상단의 알림을 함께 제공해야함.

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //서비스 시작 시 한번 호출
        Log.d(TAG, "7. onCreate 상담하기 : 서비스 onCreate: 소켓 연결 요청");
//      tcp 채팅 서버로 소켓 연결 요청
        socketConnect = new SocketConnect();
        Log.d(TAG, "8.onCreate 상담하기 : 서비스 socketConnect 시작");
        socketConnect.start();
        Log.d(TAG, "onCreate: 노티피케이션 시작");
        initializeNotification();//포그라운드 서비스 노티피케이션 시작
    }

    //startService가 실행되면 여기서 tcp 채팅 서버로 메세지 보내는 작업
    @Override//계속 업로드 됨
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 실행");
        serviceIntent = intent;
        Log.d(TAG, "onStartCommand: serviceIntent - "+serviceIntent.toString());
//        Log.d(TAG, "onStartCommand: 포그라운드 서비스 노티피케이션 시작");
//        initializeNotification();//포그라운드 서비스 노티피케이션 시작

        String key = "";//chatbotMsg
//        switch ()


        if (intent == null){
            // Thread, Timer 등으로 처리
        /*START_STICKY
        : Service가 강제 종료되었을 경우 시스템이 다시 Service를 재시작 시켜 주지만 intent 값을 null로 초기화 시켜서 재시작 합니다.
        Service 실행시 startService(Intent service) 메서드를 호출 하는데 onStartCommand(Intent intent, int flags, int startId)
        메서드에 intent로 value를 넘겨 줄 수 있습니다. 기존에 intent에 value값이 설정이 되있다고 하더라도 Service 재시작시 intent 값이 null로 초기화 되서 재시작 됩니다.

        START_NOT_STICKY
        : 이 Flag를 리턴해 주시면, 강제로 종료 된 Service가 재시작 하지 않습니다. 시스템에 의해 강제 종료되어도 괸찮은 작업을 진행 할 때 사용해 주시면 됩니다.

        START_REDELIVER_INTENT
        : START_STICKY와 마찬가지로 Service가 종료 되었을 경우 시스템이 다시 Service를 재시작 시켜 주지만 intent 값을 그대로 유지 시켜 줍니다.
        startService() 메서드 호출시 Intent value값을 사용한 경우라면 해당 Flag를 사용해서 리턴값을 설정해 주면 됩니다.*/
            Log.d(TAG, "onStartCommand: intent값이 null -> START_STICKY로 리턴");
            return Service.START_STICKY;
        }else {
//            try{

                if (intent.getStringExtra("myMsg") !=null){
//            StaticValue.recipientId = intent.getStringExtra("recipientId");//서비스로 보낸 보낼 사람 아이디
                    Log.d(TAG, "1. onStartCommand: 서비스에서 받은 메세지 : "+intent.getStringExtra("myMsg"));

                    if (intent.getStringExtra("myMsg").equals("")){
                        Toast.makeText(getApplicationContext(), "응답 메세지 오류! 로그 확인", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "2. onStartCommand: 서비스에서 받은 메세지 : "+intent.getStringExtra("myMsg"));

                    }else {
                        // 인텐트에서 부가데이터 가져오기
                        sendMsg  = "message::"+intent.getStringExtra("myMsg")+"::"+ StaticValue.recipientId;//보낼 사람에게 전송
                        Log.d(TAG, "2. processCommand: myMsg - "+sendMsg+" | 보낼사람 - "+StaticValue.recipientId);

                        //메세지 보내기
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PrintWriter pw;
                                try {
                                    pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                                    pw.println(sendMsg);
                                }catch (IOException e){
                                    Log.d(TAG, "run: 메세지 보냄 오류");
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
//            }catch (RuntimeException re){
//                Log.d(TAG, "onStartCommand: 런타임 오류 "+re.getMessage()+"\n");
//                re.printStackTrace();
//                stopClient();
//                //[메시지 수신] : join;;null
//            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    //소켓 연결
    private class SocketConnect extends Thread{
        private static final String TAG = "SocketConnect";
        @Override
        public void run() {
            try {
                Log.d(TAG, "9. 상담하기 : socketConnect run");
                socket = new Socket();
                socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT));
                Log.d(TAG, "10. 상담하기 : run: socket - "+socket.toString());


                //채팅방에 입장하면 입장한 사람 닉네임을 서버로 보냄
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                Log.d(TAG, "11. 상담하기 : run: PrintWriter 닉네임 보낼 예정 - "+pw.toString());

                String request = "join::";

                //유저
                if (AutoLogin.getUserName(getApplication().getApplicationContext()).contains("@")){
                    Log.d(TAG, "12. 상담하기 : run: 유저면 들어와라");
                    ArrayList<UserInfo> userInfos = Information.getArr(getApplicationContext());
                    Log.d(TAG, "13. 상담하기 : run: 유저 닉네임 - "+userInfos.get(0).getUser_nickname());

                    request += userInfos.get(0).getUser_email();

                    //유저 메세지도 보내기
                    Log.d(TAG, "14. 상담하기 : run: 요청2 - "+request);
                }else {//상담사
                    Log.d(TAG, "12 상담하기 : run: 상담사면 들어와라");

                    ArrayList<EmployeeInfo> employeeInfos =  Information.getArr(getApplicationContext());
                    Log.d(TAG, "13. 상담하기 : run: 상담사 닉네임 = "+employeeInfos.get(0).getUser_nickname());

                    request += employeeInfos.get(0).getUser_phone();

                    Log.d(TAG, "14. 상담하기 : run: 요청2 - "+request);
                }
                request += "::"+StaticValue.recipientId+"::"+ StaticValue.request;
                Log.d(TAG, "15. 상담하기 : run: 서버에 요청 내용 : "+request);

                pw.println(request);
                Log.d(TAG, "16. 상담하기 : 서버에 요청 보냄 요청 보냄!!");

            }catch (IOException e){//입출력에러로 바꿔보기
                Log.d(TAG, "run: 소켓연결 중 입출력 에러");
                e.printStackTrace();
                if(!socket.isClosed()) {
                    Log.d(TAG, "run: 열려있는 소켓이 있다면 닫음");
                    stopClient();
                }
                return;
            }
            receive();
        }
    }


    //	받은 메세지
    void receive() {
        while(true) {
            Log.d(TAG, "17. receive: 시작");

            try {

                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                Log.d(TAG, "18. receive: bufferedReader - "+bufferedReader.toString());

                String message = bufferedReader.readLine();
                Log.d(TAG, "19. receive: [메시지 수신] : " + message);//오류 : join;;null

                //유저가 채팅방에 들어갈 때 메세지를 보내는 오류
//                if (message.equals("join;;null")){
//                    Log.d(TAG, "receive: 오류 message = "+message);
//                    throw new IOException();
//                }

                if (message == null){
                    Log.d(TAG, "receive: message가 null 일 때 : 클라이언트로부터 연결 끊김");
                    throw new IOException();
                }

                Intent showIntent;

         /*
                데이터 : String state;//join|quit, 추후 상담중인지 상담 끝인지도 저장
                       String guestId; String guestName; String counsellingType; Date createDate; Date finishDate; boolean isExist;//채팅방 존재 여부, false일때는 삭제된 것*/
                if (message.contains("join;;")){//join;;ma@ma.com;;마마마;;2020-06-15 11:10;;안녕하세요 반갑습니다

                        showIntent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                        Log.d(TAG, "21. receive: 액티비티를 띄우기 위한 인텐트(EmployeeMainActivity 보냄) 객체 만들기 showIntent - "+showIntent.toString());
                        // EmployeeMainActivity로 보내겠다.

                        //여기에서 저장하기
                        String[] receive = message.split(";;");

                        if (receive.length == 6){
                            Log.d(TAG, "receive: 메세지 종류 - "+receive[0]);
                            Log.d(TAG, "receive: 발신자 - "+receive[1]);
                            Log.d(TAG, "receive: 발신자닉네임 - "+receive[2]);
                            Log.d(TAG, "receive: 발신날짜 - "+receive[3]);
                            Log.d(TAG, "receive: 발신자상담유형- "+receive[4]);
                            Log.d(TAG, "receive: 발신자메세지- "+receive[5]);


                            String type = "";//입장일 때 빈값
                            String getId = receive[1];
                            String getName = receive[2];

                            Log.d(TAG, "onNewIntent: 유저 들어옴 : "+receive[1]);

                            //String으로 된 시간을 받아와서 (2020-06-18 20:37 형식) 같은 형식으로 받아오고, 시간형식으로 바꿔주려고.
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

                            ParsePosition pos = new ParsePosition(0);
                            Date date = simpleDateFormat.parse(receive[3], pos);

                            Log.d(TAG, "onNewIntent: date = "+date);

                            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);

                            String getTime = simpleDateFormat.format(date);
                            Log.d(TAG, "onNewIntent: getTiem = "+getTime);

                            String getCategory = receive[4];
                            String getMessage = receive[5];

/*
                            Chatbot chatbot = new Chatbot("");
                            ArrayList<String> getCategories = chatbot.Category(getMessage);

                            for (int i = 0; i < getCategories.size(); i++){
                                Log.d(TAG, "onNewIntent: 카테고리_"+(i+1)+"- "+getCategories.get(i));
                                getCategory += getCategories.get(i);

                                if (i == getCategories.size()-1){}
                                else {  getCategory += " | ";    }
                            }
*/

                            //MyService에서 String[유저ID;;닉네임;;상담내용;;현재시간] 형태로 받아온다.
                            // 구분자(;;)를 기준으로 상담내용과 현재시간을 나누고,
                            //현재시간(String)을 date로 파싱한다.
                            StaticValue.userLists.add(new UserList(type, getId, getName, getCategory, getMessage, getTime,""));

                            //쉐어드저장
                            // String state;//join|quit, 추후 상담중인지 상담 끝인지도 저장
                            //                       String guestId; String guestName; String counsellingType; Date createDate; Date finishDate; boolean isExist;//채팅방 존재 여부, false일때는 삭제된 것
                           /* chatRooms.add(new ChatRoom(receive[0], getId, getName, getCategory, date, null,true));
                            Log.d(TAG, "receive: chatRooms 크기 : "+chatRooms.size());
                            Log.d(TAG, "receive: chatRooms = "+chatRooms.toString());*/

//                        MyChatRoom.setArr(getApplicationContext(), chatRooms);

/*                                for (int i = 0; i < chatRooms.size(); i++) {
//                            MyChatRoom.setArr(getApplicationContext(), chatRooms);
                                Log.d(TAG, "receive: chatRoom 저장 완료 : "+chatRooms.get(i).toString());
                            }*/

                            Log.d(TAG, "receive: EmployeeMainActivity가 실행중인가? = "+EmployeeMainActivity.isActivityStart);
                            if (EmployeeMainActivity.isActivityStart){
                                /*Intent.FLAG_ACTIVITY_SINGLE_TOP |  Intent.FLAG_ACTIVITY_CLEAR_TOP
                                 * ㄴ MainChatActivity 객체가 이미 메모리에 만들어져 있을 때 재사용하도록 함*/
                                showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP);// intent 객체 안에다가  데이터를 보내고 싶을 경우
                                Log.d(TAG, "22. receive: showIntent의 Flag - "+showIntent.getFlags());

                                showIntent.putExtra("receive",message);//message = 발신자::발신내용
                                Log.d(TAG, "23. receive: receive라는 키에 수신된 메세지를 보냄 - "+message);

                                startActivity(showIntent);
                                Log.d(TAG, "24. receive: showIntent 시작");
                            }


                        }else {
                            //java.lang.ArrayIndexOutOfBoundsException: length=2; index=2 을 파악하기 위해서
                            Toast.makeText(getApplicationContext(), "응답 메세지 오류! 로그 확인", Toast.LENGTH_LONG).show();
                            for (int i = 0; i < receive.length; i++) {
                                Log.d(TAG, "receive_"+i+": "+receive[i]);
                            }
                        }

                }
                else if (message.contains("quit;;")){
                    showIntent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                /*내담자가 퇴장할 때 2가지 경우가 있다.
                1. 상담을 하지 않고 퇴장을 하면, 수신자가 정해지지 않아서 전체 상담사에게 메세지가 발송된다.
                   ㄴ 이 경우는 채팅방이 생성되는 EmployeeMainActivity로 넘겨야한다. 그럼 리사이클러뷰에서 퇴장했다고 알려줘야한다.
                2. 상담을 하고 퇴장하면 수신자가 정해져있어서 한 상담사에게만 메세지가 발송된다.
                   ㄴ이 경우는 메세지 보낼때 처럼 처리하면 되기 때문에 문제가 없음 -> 문제가 있다. 다른 상담사에게도 알려줘야한다.
                   ㄴ퇴장은 서버에서 전체 상담사에게 메세지를 보낸다. (quit;;발신자id;;수신자id)
                   ㄴ이때 수신자id = null 일 경우 상담을 하지 않은 유저이고, 반대일 경우는 상담을 마친 유저이다.
                   ㄴ상담을 하지 않은 유저는 (퇴장)을 붙여주고, 상담을 마친 유저는 (상담완료)를 붙여준다.*/
                    //여기에서 저장하기
                    String[] receive = message.split(";;");
                    if (receive.length == 3){
                        Log.d(TAG, "receive(퇴장): 메세지 종류 - "+receive[0]);
                        Log.d(TAG, "receive(퇴장): 발신자 ID - "+receive[1]);
                        Log.d(TAG, "receive(퇴장): 수신자 ID - "+receive[2]);

                        String type = receive[0];
                        String senderId = receive[1];
                        String receiverId = receive[2];

                        if (receiverId.equals("null")){
                            type = "퇴장";
                        }
                        else {
                            type = "상담종료";
                        }

                        Log.d(TAG, "receive(퇴장): 퇴장 타입 : "+type+" | 유저 : "+senderId+" | 받는 유저 : "+receiverId);
                        Log.d(TAG, "receive(퇴장): 유저리스트 크기 : "+StaticValue.userLists.size());

                        //받는 유저와 내 아이디가 같으면
                        if (AutoLogin.getUserName(getApplicationContext()).equals(receiverId)){
                            showIntent = new Intent(getApplicationContext(), MainChatActivity.class);
                            Log.d(TAG, "receive(퇴장): 퇴장한 유저가 보낸 받는사람 id와 같으면 메세지 추가됨");

                            StaticValue.isUserGone = true;

                            StaticValue.chats.add(new Chat("[알림] 내담자가 채팅방을 나갔습니다.", senderId));
                            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP);// intent 객체 안에다가  데이터를 보내고 싶을 경우
                            Log.d(TAG, "receive(퇴장): showIntent 시작");
                            startActivity(showIntent);
                        }

                        Log.d(TAG, "receive(퇴장): 퇴장한 id 찾기");

                        for (int i = StaticValue.userLists.size()-1; i >= 0; i--){
                            //퇴장한 id와 같은 id를 찾음
                            Log.d(TAG, "receive(퇴장): 퇴장한 id와 같은 id를 찾음 | 현재 id = "+StaticValue.userLists.get(i).getUserId()+" | 찾는 id = "+senderId);
                            if (StaticValue.userLists.get(i).getUserId().equals(senderId)){
                                Log.d(TAG, "receive(퇴장): 해당하는 id 찾아서 들어옴");
                                String userNickName = StaticValue.userLists.get(i).getUserNickName();
                                String userCategory = StaticValue.userLists.get(i).getUserCategory();
                                String userTime = StaticValue.userLists.get(i).getStartTime();
                                String userMessage = StaticValue.userLists.get(i).getUserMessage();

                                //퇴장날짜 추가
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                                String regDate = simpleDateFormat.format(calendar.getTime());

                                StaticValue.userLists.set(i, new UserList(type,senderId,userNickName,userCategory,userMessage,userTime,regDate));

                                Log.d(TAG, "receive(퇴장): 적용 완료 : 타입 - "+StaticValue.userLists.get(i).getType()+" | id - "+StaticValue.userLists.get(i).getUserId()
                                        +" | 닉네임 - "+StaticValue.userLists.get(i).getUserNickName());
                                Log.d(TAG, "receive(퇴장): 카테고리 - "+StaticValue.userLists.get(i).getUserCategory()+
                                        " | 시작시간 - "+StaticValue.userLists.get(i).getStartTime()+" | 종료시간 - "+StaticValue.userLists.get(i).getFinishTime());
                                initializeNotification();
                                break;
                            }

                            //현재 유저리스트 확인하기
                            for (int i1 = 0; i1 < StaticValue.userLists.size(); i1++){
                                Log.d(TAG, "receive(퇴장): 유저리스트 "+(i+1)+" : "+StaticValue.userLists.get(i).toString());
                            }
                        }
//                            else {//quit;;w@w.com;;null;;null;;w@w.com 퇴장
//                                Log.d(TAG, "onNewIntent: 유저 나감 : "+receive[1]);
//                                // getId가 나가면 이름앞에 (퇴장)을 붙여준다.
//                                //해당 리사이클러뷰의 뷰 index를 가져와야하는데 어떻게 가져올까?
//                                for (int i = StaticValue.userLists.size()-1; i >= 0; i--){
//
//                                    //퇴장한 id와 같은 id를 찾음
//                                    if (StaticValue.userLists.get(i).getUserId().equals(getId)){
//                                        String userNickName = StaticValue.userLists.get(i).getUserNickName();
//                                        String userCategory = StaticValue.userLists.get(i).getUserCategory();
//                                        String userTime = StaticValue.userLists.get(i).getUserDate();
//
//                                        if (!userNickName.contains("(퇴장)")){
//                                            userNickName = "(퇴장) "+StaticValue.userLists.get(i).getUserNickName();
//                                        }
//
//                                        //퇴장시간 추가
//                                        //현재날짜
//                                        Calendar calendar = Calendar.getInstance();
//
//                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
//                                        String regDate = simpleDateFormat.format(calendar.getTime());
//
//                                        StaticValue.userLists.set(i, new UserList(getId, userNickName, userCategory,"퇴장하였습니다.",userTime+" - "+regDate));
//                                        break;
//                                    }
//                                }
//                            }
                        Log.d(TAG, "receive: EmployeeMainActivity가 실행중인가? = "+EmployeeMainActivity.isActivityStart);
                        if (EmployeeMainActivity.isActivityStart){

                            /*Intent.FLAG_ACTIVITY_SINGLE_TOP |  Intent.FLAG_ACTIVITY_CLEAR_TOP
                             * ㄴ MainChatActivity 객체가 이미 메모리에 만들어져 있을 때 재사용하도록 함*/
                            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP);// intent 객체 안에다가  데이터를 보내고 싶을 경우
                            Log.d(TAG, "22. receive: showIntent의 Flag - "+showIntent.getFlags());

                            showIntent.putExtra("receive",message);//message = 발신자::발신내용
                            Log.d(TAG, "23. receive: receive라는 키에 수신된 메세지를 보냄 - "+message);

                            startActivity(showIntent);
                            Log.d(TAG, "24. receive: showIntent 시작");
                        }


                    }else {
                        Toast.makeText(getApplicationContext(), "응답 메세지 오류! 로그 확인", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "receive: quit 메세지 오류 내용 : "+ message);
                    }

                }

                else {
                    //상담중도 보내야함
                    showIntent = new Intent(getApplicationContext(), MainChatActivity.class);
                    Log.d(TAG, "21. receive: 액티비티를 띄우기 위한 인텐트(MainActivity로 보냄) 객체 만들기 showIntent - "+showIntent.toString());
                    // MainActivity로 보내겠다.

                    // 수신메세지 저장 : 01011112222::안녕하세요, 상담사 aaa 입니다.
                    String[] receive = message.split("::");
                    if (receive.length==2){
                        Log.d(TAG, "receive: 받은 메세지 크기가 2일 때");
                        Log.d(TAG, "receive: 보내는사람 ID - "+receive[0]);
                        Log.d(TAG, "receive: 내용 - "+receive[1]);

                        if (receive[0].contains("@")&receive[1].equals("상담중")){
                            Log.d(TAG, "receive(상담중): 유저가 상담중일 때 상담중이라고 세팅하기");
                            showIntent = new Intent(getApplicationContext(), EmployeeMainActivity.class);

                            for (int i = StaticValue.userLists.size()-1; i >= 0; i--){
                                //상담할 id와 같은 id를 찾음
                                Log.d(TAG, "receive(상담중): 상담할 id와 같은 id를 찾음 | 현재 id = "+StaticValue.userLists.get(i).getUserId()+" | 찾는 id = "+receive[0]);

                                if (StaticValue.userLists.get(i).getUserId().equals(receive[0])){
                                    Log.d(TAG, "receive(상담중): 해당하는 id 찾아서 들어옴");
                                    String userNickName = StaticValue.userLists.get(i).getUserNickName();
                                    String userCategory = StaticValue.userLists.get(i).getUserCategory();
                                    String userTime = StaticValue.userLists.get(i).getStartTime();
                                    String senderId = StaticValue.userLists.get(i).getUserId();
                                    String userMessage = StaticValue.userLists.get(i).getUserMessage();
                                    String finishTime = StaticValue.userLists.get(i).getFinishTime();

                                    Log.d(TAG, "receive(상담중): Type 빼고 다른 값은 기존과 똑같이 세팅");
                                    StaticValue.userLists.set(i, new UserList("상담중", senderId, userNickName, userCategory,userMessage,userTime,finishTime));

                                    Log.d(TAG, "receive(상담중): 적용 완료 : 타입 - "+StaticValue.userLists.get(i).getType()+" | id - "+StaticValue.userLists.get(i).getUserId()
                                            +" | 닉네임 - "+StaticValue.userLists.get(i).getUserNickName());
                                    Log.d(TAG, "receive(상담중): 카테고리 - "+StaticValue.userLists.get(i).getUserCategory()+
                                            " | 시작시간 - "+StaticValue.userLists.get(i).getStartTime()+" | 종료시간 - "+StaticValue.userLists.get(i).getFinishTime());
                                    break;
                                }

                                //현재 유저리스트 확인하기
                                for (int i1 = 0; i1 < StaticValue.userLists.size(); i1++){
                                    Log.d(TAG, "receive(상담중): 유저리스트 "+(i+1)+" : "+StaticValue.userLists.get(i).toString());
                                }
                            }

                            Log.d(TAG, "receive(상담중): EmployeeMainActivity가 실행중인가? = "+EmployeeMainActivity.isActivityStart);
                            if (EmployeeMainActivity.isActivityStart && !StaticValue.recipientId.equals(receive[0])){
                                /*Intent.FLAG_ACTIVITY_SINGLE_TOP |  Intent.FLAG_ACTIVITY_CLEAR_TOP
                                 * ㄴ MainChatActivity 객체가 이미 메모리에 만들어져 있을 때 재사용하도록 함*/
                                showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP);// intent 객체 안에다가  데이터를 보내고 싶을 경우
                                Log.d(TAG, "22. receive(상담중): showIntent의 Flag - "+showIntent.getFlags());

                                showIntent.putExtra("receive",message);//message = 발신자::발신내용
                                Log.d(TAG, "23. receive(상담중): receive라는 키에 수신된 메세지를 보냄 - "+message);

                                startActivity(showIntent);
                                Log.d(TAG, "24. receive(상담중): showIntent 시작");
                            }

                        }
                        else {
                            Log.d(TAG, "receive: 채팅 메세지 받아서 적용하기");
                            StaticValue.recipientId = receive[0];
                            StaticValue.chats.add(new Chat(receive[1], StaticValue.recipientId));
                            chatNotification(receive[0], receive[1]);
                            /*Intent.FLAG_ACTIVITY_SINGLE_TOP |  Intent.FLAG_ACTIVITY_CLEAR_TOP
                             * ㄴ MainChatActivity 객체가 이미 메모리에 만들어져 있을 때 재사용하도록 함*/
                            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP);// intent 객체 안에다가  데이터를 보내고 싶을 경우
                            Log.d(TAG, "22. receive: showIntent의 Flag - "+showIntent.getFlags());


//                        showIntent.putExtra("receive",message);//message = 발신자::발신내용
//                        Log.d(TAG, "23. receive: receive라는 키에 수신된 메세지를 보냄 - "+message);

                            //질문

                            startActivity(showIntent);
                            Log.d(TAG, "24. receive: showIntent 시작");
                        }

                    }else {
                        Log.d(TAG, "receive: 받은 메세지 크기가 2가 아닐 때 | 내용 - "+message);
                    }

                }



            } catch (IOException e) {
                Log.d(TAG, "receive: 서버와 통신 안됨 소켓 닫기" );
                e.printStackTrace();
                stopClient();
                break;
            }
//            catch (RuntimeException re){
//                Log.d(TAG, "receive: 런타임 오류 "+re.getMessage());
//                re.printStackTrace();
//                stopClient();
//                break;
//            }

        }
    }

    //소켓 닫기
    void stopClient() {
        Log.d(TAG, "stopClient: 실행");
        if(socket!=null && !socket.isClosed()) {
            try {
                socket.close();
                Log.d(TAG, "stopClient: 소켓 닫기 완료");
            } catch (IOException e) {
                Log.d(TAG, "stopClient: 소켓 닫는 중 입출력 에러");
                e.printStackTrace();
            }
        }
    }


    public void initializeNotification() {
        Log.d(TAG, "    1) initializeNotification: 노티피케이션 시작");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        //오래오버전부터 채널이 반드시 필요함 : 채널이 없으면 노티 동작 안함
        Log.d(TAG, "    2) initializeNotification: 빌더 - "+builder.toString());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Log.d(TAG, "    3) initializeNotification: 아이콘 세팅");
        builder.setContentTitle("마음알림");
        Log.d(TAG, "    4) initializeNotification: 타이틀 세팅");
        builder.setContentText("마음알림 서비스 동작중");
        Log.d(TAG, "    5) initializeNotification: 텍스트 세팅");
        builder.setAutoCancel(false);//사용자가 탭하면 알림 삭제가능
        //실제로 동작이 되어야하는 인탠트 : 노티피케이션 실행했을 때 우리가 하고싶은 것 : 액티비티 실행
        if (AutoLogin.getUserName(getApplicationContext()).contains("@")) {
            notificationIntent = new Intent(this, MainActivity.class);
        }else {
            notificationIntent = new Intent(this, EmployeeMainActivity.class);
        }
        Log.d(TAG, "    6) initializeNotification: 노티피케이션 인텐트 - "+notificationIntent.toString());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d(TAG, "    7) initializeNotification: notificationIntent 플래그 - "+notificationIntent.getFlags());

        //이것을 클릭했을 때 동작하는 인탠트 : 인탠트를 잠시 대기해 놓는다. 바로 일반 인탠트를 실행할 수 없기 때문에
        //팬딩인탠트 지정해서 노티피케이션에 지정을 해놓으면 수행이 된다.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "    8) initializeNotification: PendingIntent 대기 : 이것을 클릭했을 때 동작하는 인탠트 - 채팅방으로 이동하게 하기 위해");
        builder.setContentIntent(pendingIntent);
        Log.d(TAG, "    9) initializeNotification: 클릭했을 때 실행되는 팬딩인텐트 지정");

        //오래오에서는 채널을 추가해야함. getSystemService에서 NOTIFICATION_SERVICE를 가져옴
        //  NotificationManager.IMPORTANCE_DEFAULT : 이것에 따라 노티피케이션 보이는 형태가 달라짐
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d(TAG, "    10) initializeNotification: 노티피케이션 매니저");

        manager.createNotificationChannel(new NotificationChannel("default", "알림 설정", NotificationManager.IMPORTANCE_DEFAULT));
        Log.d(TAG, "    11) initializeNotification: 노티피케이션 채널 생성");

        //id는 0이 아닌값, builder.build() 해당 노티피케이션 실행
        startForeground(1, builder.build());
        Log.d(TAG, "    12) initializeNotification: 노티피케이션 포그라운드 실행");

    }

    public void chatNotification(String name, String text) {
        Log.d(TAG, "    1) initializeNotification: 노티피케이션 시작");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        //오래오버전부터 채널이 반드시 필요함 : 채널이 없으면 노티 동작 안함
        Log.d(TAG, "    2) initializeNotification: 빌더 - "+builder.toString());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Log.d(TAG, "    3) initializeNotification: 아이콘 세팅");
        builder.setContentTitle(name);
        Log.d(TAG, "    4) initializeNotification: 타이틀 세팅");
        builder.setContentText(text);
        Log.d(TAG, "    5) initializeNotification: 텍스트 세팅");
        builder.setAutoCancel(false);//사용자가 탭하면 알림 삭제가능
        //실제로 동작이 되어야하는 인탠트 : 노티피케이션 실행했을 때 우리가 하고싶은 것 : 액티비티 실행
        notificationIntent = new Intent(this, MainChatActivity.class);
        Log.d(TAG, "    6) initializeNotification: 노티피케이션 인텐트 - "+notificationIntent.toString());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d(TAG, "    7) initializeNotification: notificationIntent 플래그 - "+notificationIntent.getFlags());

        //이것을 클릭했을 때 동작하는 인탠트 : 인탠트를 잠시 대기해 놓는다. 바로 일반 인탠트를 실행할 수 없기 때문에
        //팬딩인탠트 지정해서 노티피케이션에 지정을 해놓으면 수행이 된다.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "    8) initializeNotification: PendingIntent 대기 : 이것을 클릭했을 때 동작하는 인탠트 - 채팅방으로 이동하게 하기 위해");
        builder.setContentIntent(pendingIntent);
        Log.d(TAG, "    9) initializeNotification: 클릭했을 때 실행되는 팬딩인텐트 지정");

        //오래오에서는 채널을 추가해야함. getSystemService에서 NOTIFICATION_SERVICE를 가져옴
        //  NotificationManager.IMPORTANCE_DEFAULT : 이것에 따라 노티피케이션 보이는 형태가 달라짐
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d(TAG, "    10) initializeNotification: 노티피케이션 매니저");

        manager.createNotificationChannel(new NotificationChannel("default", "채팅메세지", NotificationManager.IMPORTANCE_DEFAULT));
        Log.d(TAG, "    11) initializeNotification: 노티피케이션 채널 생성");

        //id는 0이 아닌값, builder.build() 해당 노티피케이션 실행
        startForeground(1, builder.build());
        Log.d(TAG, "    12) initializeNotification: 노티피케이션 포그라운드 실행");

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: 실행");

    }

    @Override//StopService 호출 : 서비스 종료
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 서비스 종료");
       if (!socketConnect.isInterrupted()){
           Log.d(TAG, "onDestroy: 열려있는 소켓이 있다면");
           stopClient();//열려있는 소켓을 따로 닫아줘야함
           socketConnect.interrupt();
           Log.d(TAG, "onDestroy: 소켓 , receive 스래드 중단");
       }

//        serviceIntent =  null;
        Log.d(TAG, "onDestroy: serviceIntent = "+serviceIntent.toString());
        //여기로 오면 서비스 종료되는데 여기서 실행시키고 있던 스래드가 있다면 별도로 중단시켜야함
        /* if (mThread != null){
            mThread.interrupt();
            mThread = null;
            mCount = 0;
        }*/
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind: 실행");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isSocketOpen (){
        Log.d(TAG, "isSocketOpen: 실행");
        boolean isSocketOpen = false;

        if (socket!=null && !socket.isClosed()){
            isSocketOpen = true;
        } else {
            isSocketOpen = false;
        }

        return isSocketOpen;
    }
}
