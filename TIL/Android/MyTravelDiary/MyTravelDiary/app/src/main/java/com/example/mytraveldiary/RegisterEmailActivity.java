package com.example.mytraveldiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytraveldiary.list.Account;
import com.example.mytraveldiary.sharedPreferences.AccountData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/*회원가입창
* 아이디 > ArrayList add
* 비밀번호> ArrayList add & 6자 이상 비밀번호
* 비밀번호 확인 > 비밀번호 동일한지 체크*/

public class RegisterEmailActivity extends AppCompatActivity {
    private static final String KEY_SERVER_ACCOUNT = "ACCOUNT";

    TextView tv_pw_exception;
    Button Button_login_check;
    com.google.android.material.textfield.TextInputEditText TextInputEditText_email;//이메일 입력
    com.google.android.material.textfield.TextInputEditText TextInputEditText_password;// 비밀번호 입력
    com.google.android.material.textfield.TextInputEditText TextInputEditText_passwordcheck;// 비밀번호 확인
    CheckBox CheckBox_terms;//체크박스 checked="false"

    String userEmail;//유저이메일
    String userPW;//유저비밀번호
    String userPWcheck;//유저비밀번호 확인

    private Context mContext;

//    ArrayList<Account> accounts = new ArrayList<>();//계정 arraylist 생성

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        mContext = this;

        /*id값 찾기*/
        Button_login_check  = (Button) findViewById(R.id.Button_login_check);
        CheckBox_terms = (CheckBox) findViewById(R.id.CheckBox_terms);
        TextInputEditText_email = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.TextInputEditText_email);
        TextInputEditText_password = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.TextInputEditText_password);
        TextInputEditText_passwordcheck = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.TextInputEditText_passwordcheck);
        tv_pw_exception = (TextView) findViewById(R.id.tv_pw_exception);

        Button_login_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextInputEditText_email.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
                }

                else {
//                    loadData();//데이터불러오기
//                    System.out.println( "accounts.get(0) = "+ accounts.get(0).toString());

                    //유저 이메일 중복확인 필요
                    /*String 변수에 입력값 받기*/
                    userEmail = TextInputEditText_email.getText().toString();

                    String userEmailCheck = AccountData.getString(mContext, userEmail); //userEmail에 들어있는 value(비밀번호) 값 반환
                    Log.e("확인",userEmailCheck);
                    if(userEmailCheck.equals("")){

                        //이메일 중복이 아님
                        if (TextInputEditText_password.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                        }
                        else {
                            userPW = TextInputEditText_password.getText().toString();

                            /*비밀번호 6자 이상 체크*/
                            if (userPW.length() < 6){

                                /*6자이상 경고 메세지 색 변경*/
                                tv_pw_exception.setTextColor(Color.RED);
                            }
                            else {
                                /*경고 색 다시 원상복귀*/
                                tv_pw_exception.setTextColor(Color.GRAY);

                                if(TextInputEditText_passwordcheck.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 해주세요", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    //동일한 비밀번호인지 확인
                                    userPWcheck = TextInputEditText_passwordcheck.getText().toString();

                                    if(!userPW.equals(userPWcheck)){
                                        //비밀번호가 다르면
                                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        /*개인정보 이용약관 체크박스 체크 여부*/
                                        if(!CheckBox_terms.isChecked()){
                                            Toast.makeText(getApplicationContext(), "개인정보처리방침 및 이용약관에 동의해주세요", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Intent intent = new Intent(RegisterEmailActivity.this, CreateIDLoadingActivity.class);//다시 로그인창으로 이동
                                            startActivity(intent);
//                                            Toast.makeText(getApplicationContext(), "가입완료! 로그인을 해주세요", Toast.LENGTH_LONG).show();
                                            AccountData.setString(mContext, userEmail, userPW);//userEmail 에 userPW 넣기

//                                            accounts.add(new Account(userEmail, userPW));
//                                        onSaveData();
                                            finish();//버튼 누르면 액티비티 종료
                                        }
                                    }
                                }
                            }

                        }
                    }
                    else{//이메일 중복

                        Toast.makeText(getApplicationContext(), "중복된 이메일 계정입니다. 다른 이메일을 입력해주세요.", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
