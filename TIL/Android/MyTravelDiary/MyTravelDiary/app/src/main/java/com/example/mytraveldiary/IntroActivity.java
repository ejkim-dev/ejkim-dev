package com.example.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.list.Account;
import com.example.mytraveldiary.sharedPreferences.AccountData;
import com.example.mytraveldiary.sharedPreferences.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;

public class IntroActivity extends AppCompatActivity {

    private Thread introThread, thread;
    private Context mContext = this;
/*    boolean isThread = false;
    private String[] msg_list = {"10%", "34%", "67%", "88%", "100%"};
    private int msg_num;*/
    TextView tv_intro_msg;
    private ProgressBar progressBar;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        tv_intro_msg = (TextView) findViewById(R.id.tv_intro_msg);

/*        ProgressThread progressThread = new ProgressThread(progressBar);
        progressThread.start();*/

        //인트로 장면 스레드
        introThread = new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 5; i++){
                    SystemClock.sleep(100);//1초동안
                    progressBar.incrementProgressBy(25);//25씩 증가
/*                    Println.println(" progressBar.getScrollBarSize() = "+ progressBar.getScrollBarSize()+"");
                    Println.println(" progressBar.getScrollBarDefaultDelayBeforeFade() = "+ progressBar.getScrollBarDefaultDelayBeforeFade()+"");
                    Println.println(" progressBar.getVerticalScrollbarPosition() = "+ progressBar.getVerticalScrollbarPosition()+"");*/

                    //while이 끝나기 직전에 handler 만들어야함
                    str = (i+1)*25+"%";
                    handler.sendEmptyMessage(0);

                    if(i == 4) {
                        finish();
                        // Run next activity
                        Intent intent = new Intent();
                        intent.setClass(IntroActivity.this,
                                LoginChoiceActivity.class);//LoginChoiceActivity
                        startActivity(intent);
                    }
                  }
            }
        };
        introThread.start();

//        msg_num = 0;

//        tv_intro_msg = (TextView) findViewById(R.id.tv_intro_msg);
 //----------------------------------------------모든키값 연습
/*        //Test
*//*      String testValue = AccountData.allValue(this);//모든 value
        Println.println(testValue);*//*
        String allKey = AccountData.allKey(this);//모든 키값
        Println.println(allKey);

        String[] arrKey = allKey.split("/");
        //모든 키를 분리함
        for (int i = 0; i < arrKey.length ; i++){
            Println.println(arrKey[i]);
        }*/
//---------------------------------------------------------------------------

        //진행율 스레드 시작
      /*  isThread = true;
        thread = new Thread(){
            public void run(){
                while (isThread){
                    try {
                        sleep(100);//1000
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //while이 끝나기 직전에 handler 만들어야함
                    handler.sendEmptyMessage(0);

                    if (msg_num == msg_list.length-1){
                        isThread = false;
                        finish();
                        // Run next activity
                        Intent intent = new Intent();
                        intent.setClass(IntroActivity.this,
                                MainActivity2.class);//    LoginChoiceActivity
                        startActivity(intent);
                    }
                }
            }
        };
        thread.start();*/

        //인트로 장면 스레드
/*        introThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        wait(3000);
                    }
                } catch (InterruptedException ex) {
                }
                finish();
                // Run next activity
                Intent intent = new Intent();
                intent.setClass(IntroActivity.this,
                        LoginChoiceActivity.class);
                startActivity(intent);
            }
        };
        introThread.start();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
     /*       tv_intro_msg.setText(msg_list[msg_num]);
            msg_num++;*/
            tv_intro_msg.setText(str);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        msg_num = 0;
    }
}

/* class ProgressThread extends Thread {

    public ProgressBar progressBar;

     public ProgressThread(ProgressBar progressBar) {
         this.progressBar = progressBar;
     }

     @Override
     public void run() {
         for(int i = 0; i < 10; i++){
             SystemClock.sleep(1000);//1초동안
             progressBar.incrementProgressBy(25);//25씩 증가
         }
     }
 }*/
