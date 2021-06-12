package com.example.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytraveldiary.Tag.Code;
import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.list.Chat;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {

    public static ArrayList<Chat> dataList = new ArrayList<>();

    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;
    Button button_5;
    Button bt_send;
    ImageView imageView_home;
    ImageView imageView_menu;
    EditText editText1;
    String simTalk;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        InitializeView();
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        myAdapter = new MyAdapter(dataList);

        bt_send = findViewById(R.id.bt_send);
        editText1 = findViewById(R.id.editText1);

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Println.println("onCreate search 버튼 클릭");
                String utext = editText1.getText().toString();
                Println.println(utext);

                simsim(utext);
                if(simTalk.isEmpty()||simTalk.equals("")){simTalk = ".....";}
                Println.println("onCreate 심심이 말 : "+simTalk);

//                dataList = new ArrayList<>();

                dataList.add(new Chat(utext,  Code.ViewType.LEFT_CONTENT));
                dataList.add(new Chat(simTalk,  Code.ViewType.RIGHT_CONTENT));

                for (int i = 0; i < dataList.size(); i++) {
                    Println.println("온클릭 안 : " + dataList.get(i).getContent());
                }

                Println.println("리사이클러뷰 시작");
                recyclerView.setAdapter(new MyAdapter(dataList));
            }
        });
//        String user = editText1.getText().toString();
//        this.initializeData(simTalk, user);//test





        /*recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(dataList));  // Adapter 등록*/


    }
    public void initializeData(String simTalk, String user)
    {
        Println.println("initializeData");
        dataList = new ArrayList<>();

        dataList.add(new Chat(simTalk,  Code.ViewType.LEFT_CONTENT));
        dataList.add(new Chat(user,  Code.ViewType.RIGHT_CONTENT));

        if(dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Println.println("?" + dataList.get(i).getContent());
            }
        }
    }

    protected void simsim(final String utext){
        final String sim_id = "sim_id";//API 키

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    String text = utext;// URLEncoder.encode(utext, "UTF-8");

                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\n\t\"utext\": \""+text+"\", \n\t\"lang\": \"ko\", \n\t\"atext_bad_prob_max\": \""+0.7+"\" \n}");//"country": "KR",
                            Println.println(body.toString());

                    //url과 해당 url에 보낼(post 형식으로) body를 요청사항에 넣고 request를 만들어줬다. 이제 client에게 요청하도록 시켜보자.
                    Request request = new Request.Builder()
                            .url("https://wsapi.simsimi.com/190410/talk/")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("x-api-key", sim_id)
                            .build();

                    Response response = client.newCall(request).execute();//json 형식
                    Println.println(response.toString());

                    client.newCall(request).execute();     //동기 처리시 execute함수 사용

                    //string 으로 출력
                    String message = response.body().string();
                    Println.println("message = " +message);

                    String[] array;
                    array = message.split("\"");


                    int k = 0;
                    for(int i = 0; i < array.length; i++){
                        Println.println(i+"번째 : "+array[i]);

                        if(array[i].equals("atext")){
                            k = (i+2);
                        }
                    }
                  /*  Println.println("심심이 말 : "+array[k]);*/

                    simTalk = array[k];

                    //어레이리스트 저장 안됨
//                    dataList.add(new Chat(simTalk,  Code.ViewType.LEFT_CONTENT));
//                    dataList.add(new Chat(text,  Code.ViewType.RIGHT_CONTENT));
/*
                    for (int i = 0; i < dataList.size(); i++) {
                        Println.println("어레이"+i+" : " + dataList.get(i).getContent());
                    }*/

                }catch (Exception e){
                    Println.println("error : " + e);
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);//화면 finish될 때 애니메이션 끄기
    }

    public void InitializeView(){

        imageView_home = (ImageView) findViewById(R.id.imageView_home);
        imageView_menu = (ImageView) findViewById(R.id.imageView_menu);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
    }

    public void OnClick(View view) {
        /*페이지 전환 intent*/
        Intent intent;
        switch (view.getId()){
            case R.id.imageView_home:
            case R.id.button_1:
                intent = new Intent(MainActivity3.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

            case R.id.imageView_menu:
                //수정하기
                intent = new Intent(MainActivity3.this, LoginChoiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

            case R.id.button_2:
                intent = new Intent(MainActivity3.this, MainActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_3:
                Toast.makeText(getApplicationContext(), "현재 페이지", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_4:
                intent = new Intent(MainActivity3.this, MainActivity4.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_5:
                intent = new Intent(MainActivity3.this, MainActivity5.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
        }
    }
   }
