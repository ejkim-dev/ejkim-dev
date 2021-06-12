package com.example.mytraveldiary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytraveldiary.sharedPreferences.AccountData;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

//    private static final String KEY_SERVER_ACCOUNT = "ACCOUNT";
    private Context mContext;
    private Thread thread;
    private AdView adView;

    Button Button_login;//로그인 버튼
    TextView tv_createID;//회원가입하기 텍스트
    com.google.android.material.textfield.TextInputEditText tiet_email;
    com.google.android.material.textfield.TextInputEditText tiet_password;
//    ImageView iv_adimg;

    public static String userID;//쉐어드에 저장되는 키값; 로그인시 값이 생김
    String userPW;
    int times = 0;
    private int turn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        Button_login  = findViewById(R.id.Button_login);
        tv_createID = findViewById(R.id.tv_createID);
        tiet_email = findViewById(R.id.tiet_email);
        tiet_password = findViewById(R.id.tiet_password);
//        iv_adimg = findViewById(R.id.iv_adimg);

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
                Log.d("@@@", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d("@@@", "onAdFailedToLoad " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }


            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
               // Code to be executed when the user is about to return
               // to the app after tapping on an ad.
            }

        });


        //광고 스레드
/*        thread = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //핸들러
                    handler.sendEmptyMessage(0);
*//*
                    if(turn >= 2){turn = 0; }
                    else { turn++; }*//*
                }
            }
        };
        thread.start();*/

        /*로그인 버튼 누르면 로딩창*/
        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ID랑 PW가 같아야 로그인 됨
//AccountData.getString(mContext, userEmail); //userEmail에 들어있는 value(비밀번호) 값 반환

                userID =  tiet_email.getText().toString();//string 으로 변환
                userPW = tiet_password.getText().toString();

                //데이터에 저장되어있는 유저의 PW를 가져와라
                String getUserPW = AccountData.getString(mContext, userID);
                System.out.println("입력한 값 : "+userPW);//확인용
                System.out.println("불러온 값 : "+getUserPW);

                if(userPW.equals(getUserPW) && !userPW.isEmpty()){//비밀버호 창이 비어있지 않고 비밀번호와 데이터값이 같으면 로그인 됨
                    Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                    startActivity(intent);
                    finish();//버튼 누르면 액티비티 종료
                }
                else if(getUserPW.equals("")){

                    if(userID.isEmpty()  || userPW.isEmpty()){
                        Toast.makeText(getApplicationContext(), "로그인 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                    }
                   else {
                    Toast.makeText(getApplicationContext(), "없는 계정입니다. 회원가입을 해주세요.", Toast.LENGTH_LONG).show();}
                }
                else {
                    times++;//일정 횟수 이상 비밀번호가 틀리면 비밀번호 찾기 이메일 보내기; 임시 비밀번호 발급됨
                    Toast.makeText(getApplicationContext(), "비밀번호가 "+times+"회 틀렸습니다. 다시 확인해 주세요.", Toast.LENGTH_LONG).show();

                    //5회이상 틀리면 비밀번호 재발급
                    if(times >= 5){
                        //인텐트로 메일 주소 넘기기
                        Intent intent = new Intent(LoginActivity.this, FindPWActivity.class);
                        intent.putExtra("passEmail", userID);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });

        tv_createID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterEmailActivity.class);
                startActivity(intent);

                finish();//버튼 누르면 액티비티 종료
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            adView.setAdUnitId("setAdUnitId");

        }
    };


}
