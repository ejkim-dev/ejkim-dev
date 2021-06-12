package com.example.mytraveldiary.Tag;

import android.util.Log;

public class Println {

    private static String myTag = "My Tag";

    public Println(String myTag) {
        this.myTag = myTag;
    }

    public static void println(String data){
        Log.d(myTag, data);//태그이름(MyTag)로 검색하기

        /* 다이어그램 액티비티 생성부터 실행까지 자동으로 매서드가 호출되고 이러한 생명주기 메서드를 '콜백 메서드(Callback Method)'라고 함
         * 화면이 보일 때는 onCreate → onStart → onDestroy 순으로 호출되고,
         * 화면을 없앨 때는 onPause → onStop → onDestroy 순으로 호출 됨
         * 화면이 전환 될 때는 onDestroy가 호출되지 않음
         * 화면이 보일 때와 보이지 않을 때 항상 호출되는 메서드가 onResume, onPause 이고,
         * 갑자기 앱이 중지되거나 다시 화면에 나타날 때 앱 데이터의 저장과 복원을 이 메소드로 활용함
         * 예를 들어 게임을 할 때 사용자의 점수가 갑자기 사라지지 않도록 하려면
         * onPause메서드 안에 데이터를 저장하고 onResume 메서드 안에서 복원하여야 함
         * 앱 안에서 간단한 데이터를 저장하거나 복원할 때는 SharedPreFerences를 사용함
         * 이것은 앱 내부에 파일을 하나 만드는데 이 파일 안에서 데이터를 저장하거나 읽어올 수 있게 함*/
    }
}
