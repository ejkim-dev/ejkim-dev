package com.example.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mytraveldiary.Tag.Println;

public class GameLoadingActivity extends AppCompatActivity {
    private Thread loadingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loading);
        Println.println("loading창 들어옴");

        loadingThread = new Thread(){

            @Override
            public void run() {
                try {
                    synchronized (this){
                        wait(150);
                        Println.println("잠깐 움직임");
                    }
                } catch (InterruptedException e) { }
                finish();
                Println.println("종료");

                Intent getIntent = getIntent();
                boolean start = getIntent.getBooleanExtra("start", false);

                Println.println("start = "+start);
                Intent intent = new Intent();

                if(start){

                    Println.println("화면넘김인텐트 선언");
                    intent.putExtra("start", true);
                    Println.println("넘길 데이터");
                }

                else if(!start) {
                    Println.println("여기는 false일때 들어옴");
                    intent.putExtra("start", false);
                    Println.println("RESULT_CANCELED 결과 넘기기");
                }
                setResult(RESULT_OK, intent);
                Println.println("RESULT_OK 결과 넘기기");


            }
        };
        loadingThread.start();

    }

}
