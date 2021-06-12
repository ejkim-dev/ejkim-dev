package com.example.maumalrim;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        Log.d(TAG, "onCreate: 실행");


        //메뉴에 있는 각 ID는 최상위 대상으로 간주되므로 각 메뉴 ID를 ID 세트로 전달합니다.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        //프래그먼트 ID 넣어줌
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.navView, navController);

        Log.d(TAG, "Information 데이터 : "+ Information.getArr(this));
        //쉐어드 저장내용 불러오기(확인용)
        ArrayList<UserInfo> userInfos = Information.getArr(getApplicationContext());
        Log.d(TAG, "Information 닉네임 : "+userInfos.get(0).getUser_nickname());


//글라이드 사용법
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(activityMainBinding.image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 실행");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 실행");
//finish하면 들어옴
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 실행");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 실행");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "onPostCreate: 실행");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: 실행");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 실행");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 실행");
//        finish();

    }
    
    
}
