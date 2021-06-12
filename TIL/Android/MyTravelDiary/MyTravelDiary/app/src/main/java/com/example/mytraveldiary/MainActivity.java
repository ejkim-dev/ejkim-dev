package com.example.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.sharedPreferences.AccountData;
import com.example.mytraveldiary.sharedPreferences.PreferenceManager;

import java.util.ArrayList;


/*엑티비티의 대표적인 상태정보
1. 실행(Running) : 화면상에 액티비티가 보이면서 실행되어있는 상태, 액티비티 스택의 최상위에 있으며 포커스를 가지고 있음
2. 일시정지(Pause) : 사용자에게 보이지만 다른 액티비티가 위에 있어 포커스를 받지 못하는 상태, 대화상자가　위에　있어　일부가　가려진　경우　해당
3. 중지(Stopped) : 다른　액티비티에　의해　완전히　가려져　보이지　않는　상태
*/

/*게임과 같은 실제 앱을 구성할 때는 중간에 전화가 오거나 갑자기 전화기가 종료된 이후에도 게임 진행중이던 상태로 다시 돌아갈 수 있어야 함
 * 예를 들어, 사용자가 게임의 2단계를 진행하고 있는 상태였다면 그 정보를 저장해 두었다가 앱이 다시 실행되었을 때 그 상태부터 다시 시작할 수 있도록 만들어주어야 함
 * 이런 경우에 사용되는 액티비티의 수명주기 메서드는 onPause()와 onResume() 임
 * 이 두 가지 메서드는 앱이 멈추거나 없어질 때, 그리고 앱이 다시 보이거나 새로 실행될 때 호출되므로
 * 이 두 가지 메서드를 구현하여 앱의 상태를 저장하거나 복원해야 함
 * 이러한 방법 이외에도 액티비티를 중지시키기 전에 호출되는 onSaveInstanceState() 메서드를 이용해 데이터를 임시 저장할 수 있음
 * (번들, Bundle :  문자열로된 키와 여러가지 타입의 값을 저장하는 일종의 Map클래스 )
 * onSaveInstanceState() 메서드의 파라미터로 전달되는 번들 객체를 이용해 데이터를 저장하면 onCreate() 메서드나 onRestoreInstanceState()메서드로 저장했던 데이터가 전달 됨
 * 이 방식을 이용하면 앱이 강제 종료되거나 비정상 종료된 이후에 앱이 재실행 됐을 때도 그 상태 그대로 보일 수 있도록 만들어 줌*/

//메인 피드 페이지
public class MainActivity extends AppCompatActivity  { //implements View.OnClickListener

    private static final int REQUEST_CODE = 0;
    private Context mContext;

    RecyclerView recyclerView;
    MyAlbumAdapter adapter;

    LinearLayout linear_main;
    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;
    Button button_5;
    ImageView imageView_home;
    ImageView imageView_menu;

    ArrayList<MyAlbum> items = new ArrayList<>();//ArrayList로 저장하기 위해 선언

    /*onCreate()
    －액티비티가 처음에 만들어졌을 때 호출됨
    －화면에 보이는 뷰들의 일반적인 상태를 설정하는 부분
    －이전 상태가 저장되어있는 경우에는 번들 객체를 참조하여 이전 상태 복원 가능
    －이 메서드 다음에 항상 onStart() 메서드가 호출됨 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;//test

        Println.println("onCreate 호출");
        InitializeView();

        /*저장 데이터 복원*/
        items = PreferenceManager.loadArr(mContext, LoginActivity.userID);

        /*테스트용 : 모든 키 값 뽑기*/
        String allKey = AccountData.allKey(this);//모든 키값
        String[] arrKey = allKey.split("/");

        ArrayList<MyAlbum> Allitems = new ArrayList<>();//ArrayList로 저장하기 위해 선언
        //모든 키를 분리함
        for (int i = 0; i < arrKey.length ; i++){

            items = PreferenceManager.loadArr(mContext, arrKey[i]);

            for(int j = 0; j < items.size(); j++){
                String title = items.get(j).getTitle();
                String titleImgUri = items.get(j).getTitleImgUri();
                String privateAlbum = items.get(j).getPrivateAlbum();
                String location = items.get(j).getLocation();

                if(privateAlbum.equals("공개")){//공개인 것만 넣기
//                    Allitems.add(new MyAlbum(title, titleImgUri, privateAlbum,  location));
                    //저장하기
                    Println.println("title "+j+" = "+title);
                    Println.println("titleImgUri "+j+" = "+titleImgUri);
                    Println.println("privateAlbum "+j+" = "+privateAlbum);
                    Println.println("location "+j+" = "+location);
                    Allitems.add(new MyAlbum(title, titleImgUri, privateAlbum,  location));
                }


            }

        }

//        items = PreferenceManager.loadArr(mContext, LoginActivity.userID);


        /*입력 데이터 위치*/
        linear_main = (LinearLayout) findViewById(R.id.linear_main);
        recyclerView =  (RecyclerView)  findViewById(R.id.recyclerView);

        //리싸이클러뷰에 레이아웃 매니저 설정하기
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        //여기에 어댑터를 달아줌
        adapter = new MyAlbumAdapter(Allitems);//어댑터 안에 arraylist의 매개변수를 넣어서 arraylist로 어댑터와 메인5를 연결 시켜줌
        recyclerView.setAdapter(adapter);//리싸이클러뷰에 어댑터 설정하기; adapter를 연결해줌

        //선택한 앨범
        adapter.setOnItemClickListener(new OnAlbumItemClickListener() {
            @Override
            public void onItemClick(MyAlbumAdapter.ViewHolder holder, View view, int position) {
                MyAlbum item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "아이템 선택됨 : " + item.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
        /*다른 사람의 앨범을 선택하면 '구독 / 구독취소' 할 수 있음*/
    }

    /*onStart()
     －액티비티가 화면에 보이기 바로 전에 호출됨
     －액티비티가 화면 상에 보이면 이 메서드 다음에 onResume() 메서드가 호출됨
     －액티비티가 화면에서 가려지게되면 이 메서드 다음에 onStop() 메서드가 호출됨*/
    //어떤 상태들이 호출되는지 확인하기위한 함수들
    @Override
    protected void onStart() {
        super.onStart();
        Println.println("onStart 호출");
    }

    /*onStop()
    - 액티비티가 사용자에게 더 이상 보이지 않을 때 호출됨
    - 액티비티가 소멸되거나 또 다른 액티비티가 화면을 가릴 때 호출됨
    - 액티비티가 이 상태에 들어가면 시스템은 액티비티를 강제 종료할 수 있음*/
    @Override
    protected void onStop() {
        super.onStop();
        Println.println("onStop 호출");

    }

    /*onDestroy()
    - 액티비티가 소멸되어 없어지기 전에 호출됨
    - 이 메서드는 액티비티가 받는 마지막 호출이 됨
    - 액티비티가 앱에 의해 종료되거나(finish() 메서드 호출) 시스템이 강제로 종료시키는 경우에 호출될 수 있음
    - 위의 두 가지 경우를 구분할 때 isFinishing() 메서드를 이용함
-액티비티가 이 상태에 들어가면 시스템은 액티비티를 강제 종료할 수 있음*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Println.println("onDestroy 호출");
        finish();
    }

    /*onPause()
    - 또 다른 액티비티를 시작하려고 할 때 호출됨
    - 저장되지 않은 데이터를 저장소에 저장하거나 애니메이션 중인 작업을 중지하는 등의 기능을 수행하는 메서드임
    - 이 메서드가 리턴하기 전에는 다음 액티비티가 시작될 수 없으므로 이 작업은 매우 빨리 수행된 후 리턴되어야함
    - 액티비티가 이 상태에 들어가면 시스템은 액티비티를 강제 종료할 수 있음*/
    @Override
    protected void onPause() {
        super.onPause();
        Println.println("onPause 호출");

        //화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);

        //가지고 있던 데이터값을 저장해두기.
        SharedPreferences pref = getSharedPreferences("MainActivity_pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit(); //name은 저장할 때와 복구할 때 같은 이름으로 지정해주어야함
        editor.putString("MainActivity_name","SharedPreferences.Editor_onPause"); //put으로 넣고 get으로 가져옴

        editor.commit();        //commit을 꼭 해주어야 저장이 됨.


    }

    /*onResume()
    －액티비티가 사용자와 상호작용하기 바로 전에 호출됨*/
    @Override
    protected void onResume() {
        super.onResume();

        Println.println("onResume 호출");

        //onPause일 때 저장해두었던 데이터를 복구해봅시다.
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if(pref != null){
            String name = pref.getString("name","");
            //name값이 없는 경우에는 빈 값을 달라는 뜻

//            Toast.makeText(this,"onPause일 때 저장해두었던 데이터_복구 된 이름 : "+name ,Toast.LENGTH_SHORT).show();
        }
    }

    /*onRestart()
    －액티비티가 중지된 이후에 호출되는 메서드로 다시 시작하기 바로 전에 호출됨
    －이 메서드 다음에는 항상 onStart() 메서드가 호출됨*/
    @Override
    protected void onRestart() {
        super.onRestart();
        Println.println("onRestart 호출");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

    }


    /*페이지 전환을 위해 그룹*/
    public void InitializeView(){

        imageView_home = (ImageView) findViewById(R.id.imageView_home);
        imageView_menu = (ImageView) findViewById(R.id.imageView_menu);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
    }

    /*페이지 전환*/
    public void OnClick(View view) {

        /*페이지 전환 intent*/
        Intent intent;

        switch (view.getId()){
            case R.id.imageView_home:
            case R.id.button_1:
                Toast.makeText(getApplicationContext(), "현재 페이지", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageView_menu:
                //수정하기
                intent = new Intent(MainActivity.this, LoginChoiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

                case R.id.button_2:
                intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션 없애기
                startActivity(intent);
                finish();
                break;

            case R.id.button_3:
                intent = new Intent(MainActivity.this, MainActivity3.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_4:
                intent = new Intent(MainActivity.this, MainActivity4.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_5:
                intent = new Intent(MainActivity.this, MainActivity5.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
        }

    }
}
