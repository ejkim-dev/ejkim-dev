package com.example.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytraveldiary.sharedPreferences.AccountData;

import java.util.Random;

public class FindPWActivity extends AppCompatActivity {

    TextView tv_userEmail;//회원정보에 있는 이메일만 전송 가능
    Button bt_sendEmail;//이메일 보내기 버튼
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        mContext = this;
        tv_userEmail = (TextView) findViewById(R.id.tv_userEmail);
        bt_sendEmail = (Button) findViewById(R.id.bt_sendEmail);

        //앞에서 전달받은 이메일
        try {
            final Intent intent = getIntent();

            String getUserEmail = intent.getExtras().getString("passEmail");
            tv_userEmail.setText(getUserEmail);

            //이메일 보내기

            bt_sendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Random rd = new Random();
                    int impw = rd.nextInt(999999)+100000;//6자리 비밀번호 랜덤 발급

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("plain/Text");
                    email.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {tv_userEmail.getText().toString()});//Intent.EXTRA_EMAIL: 받을 사람 이메일 (ex) utjjal0119@naver.com)tv_userEmail.getText().toString()/"silverjk7@gmail.com"
//                    email.setType("message/html");//rfc822
                    email.putExtra(Intent.EXTRA_SUBJECT, "<여행기록 임시 비밀번호 발급>");//Intent.EXTRA_SUBJECT: 미리 설정할 제목이 있다면 입력
                    email.putExtra(Intent.EXTRA_TEXT, "임시 비밀번호는 "+impw+" 입니다.");//Intent.EXTRA_TEXT: 미리 설정할 내용이 있다면 입력

                    AccountData.setString(mContext, tv_userEmail.getText().toString(), impw+"");//userEmail 에 userPW 넣기

                    email.setType("message/html");//rfc822
                    email.setPackage("com.google.android.gm");
//                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@gmail.com"});
                    startActivity(email);

//                    startActivity(Intent.createChooser(intent,"Send Email"));
                }
            });

        }catch (NullPointerException e){}
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = new Intent(FindPWActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "다시 로그인 해주세요", Toast.LENGTH_LONG).show();
        finish();//버튼 누르면 액티비티 종료
    }
}
