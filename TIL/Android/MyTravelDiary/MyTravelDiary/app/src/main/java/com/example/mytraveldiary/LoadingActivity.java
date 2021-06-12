package com.example.mytraveldiary;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {


    private Thread loadingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadingThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        wait(5000);
                    }
                } catch (InterruptedException ex) {
                }
                finish();//한번 실행 후 액티비티 종료
                // Run next activity
                Intent intent = new Intent();
                intent.setClass(LoadingActivity.this, MainActivity5.class);
                startActivity(intent);
            }
        };
        loadingThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        finish();//다른 화면으로 전환되면 액티비티 종료
    }
}
