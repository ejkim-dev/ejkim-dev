package com.example.mytraveldiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginChoiceActivity extends AppCompatActivity {

    private Button Button_login_email; //Button_login_email
//    private Button Button_login_google;
//    private Button Button_login_facebook;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        //이메일 로그인 버튼 > 회원가입창
        Button_login_email  = findViewById(R.id.Button_login_email);
        Button_login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginChoiceActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        //구글 로그인 버튼 > 페이지 로딩창
/*        Button_login_google = findViewById(R.id.Button_login_google);
        Button_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginChoiceActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();

            }
        });*/

        //페이스북 로그인 버튼 > 페이지 로딩창
/*        Button_login_facebook = findViewById(R.id.Button_login_facebook);
        Button_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginChoiceActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();

            }
        });*/
    }

/*    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent();
        intent.setClass(LoginChoiceActivity.this,
                IntroActivity.class);
        startActivity(intent);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
//        finish();//다른 화면으로 전환되면 액티비티 종료
    }

}
