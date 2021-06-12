package com.example.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CreateIDLoadingActivity extends AppCompatActivity {

    private Thread loadingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_idloading);

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
                intent.setClass(CreateIDLoadingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        loadingThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "가입완료! 로그인을 해주세요", Toast.LENGTH_LONG).show();
    }
}
