package com.example.mytraveldiary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;

import static androidx.core.content.PermissionChecker.checkCallingOrSelfPermission;

public class CheckPermission {

    //권한확인 클래스 나중에 만들기

    //android.permission.READ_EXTERNAL_STORAGE
    /*앱이 실행되면 권한창을 띄워주고, 권한을 허가하지않으면 앱이 종료되는 코드*/
    String[] permission_list;


    //권한확인 메서드
  /*  public void checkPermission(View view){
        *//* = {//필요한 권한을 스트링배열에 넣으면 됨
            Manifest.permission.READ_EXTERNAL_STORAGE//저장공간 읽기 권한
    };*//*
        permission_list[0] =  Manifest.permission.READ_EXTERNAL_STORAGE;//저장공간 읽기 권한

        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    private int checkCallingOrSelfPermission(String permission) {
    }*/
}
