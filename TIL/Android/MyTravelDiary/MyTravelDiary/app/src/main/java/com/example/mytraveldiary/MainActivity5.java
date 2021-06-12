package com.example.mytraveldiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.list.Profile;
import com.example.mytraveldiary.sharedPreferences.PreferenceManager;
import com.example.mytraveldiary.sharedPreferences.UserProfile;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {

    private static final int REQUEST_CODE_PROFEDIT = 2000;//프로필 수정 request 코드
    private static final int REQUEST_CODE_ALBOMUP = 2001;//앨범 업로드 request 코드
    private static final int REQUEST_CODE_ALBOMED = 2002;//앨범 업로드 request 코드

    private Context mContext;

    RecyclerView recyclerView;
    MyAlbumAdapter adapter;

    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;
    Button button_5;
    Button button_editProfile;//프로필수정
    Button button_albumUpload;//앨범업로드
    ImageView imageView_home;
    ImageView imageView_menu;
    ImageView imageView_profileImg; //프로필 사진
    TextView textView_profileName; //프로필 이름
    TextView textView_profileInfo;//프로필정보

    Uri uri = null;
    int arr_index;//앨범 수정할 때 사용하는 위치 값
    ArrayList<MyAlbum> items = new ArrayList<>();//ArrayList로 저장하기 위해 선언
    ArrayList<Profile> userProfile = new ArrayList<>();//Profile 쉐어드에 저장하기 위해 선언

    //android.permission.READ_EXTERNAL_STORAGE
    /*앱이 실행되면 권한창을 띄워주고, 권한을 허가하지않으면 앱이 종료되는 코드*/
    String[] permission_list = {//필요한 권한을 스트링배열에 넣으면 됨
            Manifest.permission.READ_EXTERNAL_STORAGE//저장공간 읽기 권한
    };


    @Override//공개 선택 데이터 배열명 : PRIVATE
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        mContext = this;//test

        checkPermission();
        InitializeView();

        //저장된 데이터 복원
        items = PreferenceManager.loadArr(mContext, LoginActivity.userID);

        /*입력 데이터 위치*/
        recyclerView =  (RecyclerView)  findViewById(R.id.recyclerView);
        textView_profileName = (TextView) findViewById(R.id.textView_profileName);
        textView_profileInfo = (TextView) findViewById(R.id.textView_profileInfo);
        imageView_profileImg = (ImageView) findViewById(R.id.imageView_profileImg);

        button_editProfile = (Button) findViewById(R.id.button_editProfile);
        button_albumUpload = (Button) findViewById(R.id.button_albumUpload);

        //리싸이클러뷰에 그리드 레이아웃 매니저 설정하기
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        //여기에 어댑터를 달아줌
        /*Activity는 이 최초값을 몽땅 다 받아와서 여기 어댑터에 주면서
        "니가 알아서 지지고 볶아라(니네 자식에다가 나눠주든 지지고 볶든 마음대로 해라)" 하고 던저주는 역할만 하고 끝남*/
        adapter = new MyAlbumAdapter(items);//어댑터 안에 arraylist의 매개변수를 넣어서 arraylist로 어댑터와 메인5를 연결 시켜줌
        recyclerView.setAdapter(adapter);//리싸이클러뷰에 어댑터 설정하기; adapter를 연결해줌


        //앨범업로드버튼
        Button button = findViewById(R.id.button_albumUpload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity5.this, MyAlbumActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ALBOMUP);//앨범 업로드에 필요한 데이터를 MyAlbumActivity에 요청
            }
        });

        /*해당 리싸이클러뷰를 선택했을 때 앨범 내용으로 페이지 이동*/
//        adapter.setOnItemClickListener(new OnAlbumItemClickListener() {
//            @Override
//            public void onItemClick(MyAlbumAdapter.ViewHolder holder, View view, int position) {
//                MyAlbum item = adapter.getItem(position);//해당 앨범의 위치를 가져옴
///*//확인용
//                Println.println("불러온 이름 : "+item.getTitle());
//                Println.println("불러온 사진 : "+item.getTitleImgUri());
//                Println.println("불러온 공개여부 : "+item.getPrivateAlbum());
//                Println.println("불러온 위치 : "+item.getLocation());
//*/
//                //수정하기 누르면 창 이동 > MyAlbumActivity
//                Intent intent = new Intent(MainActivity5.this, MyAlbumActivity.class);
//                intent.putExtra("getAlbumUri", item.getTitleImgUri());//Uri 넘기기
//                intent.putExtra("getTitle", item.getTitle());//타이틀 넘기기
//                intent.putExtra("this_position",position );//포지션값 넘기기
//                intent.putExtra("getPrivate", item.getPrivateAlbum());//앨범 공개/비공개 여부
//                intent.putExtra("getLocation", item.getLocation());//장소
//                arr_index = position;//포지션값 arr_index 저장해서 아래 onActivityResult 메소드에 공유
//
//                startActivityForResult(intent, REQUEST_CODE_ALBOMED);//앨범 업로드에 필요한 데이터를 MyAlbumActivity에 요청
//
//                Toast.makeText(getApplicationContext(), "앨범 수정창", Toast.LENGTH_SHORT).show();
//            }
//        });

        /*해당 리사이클러뷰 페이지를 클릭했을 때 다이얼로그가 뜸*/
        adapter.setOnItemClickListener(new OnAlbumItemClickListener() {
            @Override
            public void onItemClick(MyAlbumAdapter.ViewHolder holder, View view, final int position) {
/*              MyAlbum item = adapter.getItem(position);
                adapter.remove(position);
                adapter.notifyDataSetChanged();//데이터가 바뀐 것을 리싸이클러뷰가 알게해서 데이터가 바뀜*/
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity5.this);//다이얼로그 빌더를 선언
                builder.setTitle("앨범 수정").setMessage("해당 앨범에서 변경하고 싶은 사항을 클릭해주세요");

                //수정하기 버튼을 눌렀을 때
                builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        MyAlbum item = adapter.getItem(position); //수정하기 누르면 수정할 수 있는 창이 뜸

                        //수정하기 누르면 창 이동 > MyAlbumActivity
                        Intent intent = new Intent(MainActivity5.this, MyAlbumActivity.class);
                        intent.putExtra("getAlbumUri", item.getTitleImgUri());//Uri 넘기기
                        intent.putExtra("getTitle", item.getTitle());//타이틀 넘기기
                        intent.putExtra("this_position",position );//포지션값 넘기기
                        intent.putExtra("getPrivate", item.getPrivateAlbum());//앨범 공개/비공개 여부
                        intent.putExtra("getLocation", item.getLocation());//장소
                        arr_index = position;//포지션값 arr_index 저장해서 아래 onActivityResult 메소드에 공유

                        startActivityForResult(intent, REQUEST_CODE_ALBOMED);//앨범 업로드에 필요한 데이터를 MyAlbumActivity에 요청

                    }
                });

                //선택한 앨범 삭제하기
                builder.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        MyAlbum item = adapter.getItem(position); //item에 해당 위치의 앨범을 가져와서
                        adapter.remove(position);//지우기
                        adapter.notifyDataSetChanged();//데이터가 바뀐 것을 리싸이클러뷰가 알게해서 데이터가 바뀜

                        //데이터 저장
                        PreferenceManager.saveArr(mContext, LoginActivity.userID, MyAlbumAdapter.items);
                        Toast.makeText(getApplicationContext(), "해당 앨범이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                //뒤로 돌아가기
                builder.setNeutralButton("돌아가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



        //프로필수정 버튼 클릭, requestCode = REQUEST_CODE_PROFEDIT
        button_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity5.this, ProfileEditActivity.class);

                if(userProfile.size() == 0){}
                else {
                    //프로필에 저장한 값 불러오기
                    userProfile = UserProfile.loadArr(mContext, LoginActivity.userID);

                    //변경사항 적용
                    String getuserprofile = userProfile.get(userProfile.size()-1).getUserPicture();
                    String getusername = userProfile.get(userProfile.size()-1).getUserName();
                    String getusermsg = userProfile.get(userProfile.size()-1).getUserMessage();

                    Println.println("getuserprofile = "+getuserprofile);

                    intent.putExtra("img", getuserprofile);//프로필사진이 있으면 해당 사진을 ProfileEditActivity에 보내기
                    intent.putExtra("na", getusername);//이름
                    intent.putExtra("msg",getusermsg );
                }

                startActivityForResult(intent, REQUEST_CODE_PROFEDIT);
            }
        });
    }

    //프로필수정 버튼 클릭, requestCode = 3000
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Main 화면0");//확인용

        if (requestCode == REQUEST_CODE_PROFEDIT ){//프로필 수정 코드
            if (resultCode == RESULT_OK) {
                System.out.println("Main 화면");//확인용

                /*변경 데이터 불러오기*/
                String name = data.getStringExtra("inputName");
                String intro = data.getStringExtra("inputIntro");
                String profile = data.getStringExtra("inputProfile");

                System.out.println("name = "+name);//확인용
                System.out.println("intro = "+intro);//확인용
                System.out.println("profile = "+profile);//확인용

//                userProfile = new ArrayList<>();
                userProfile.add( new Profile(profile, name, intro));

                //변경내용 저장하기
                UserProfile.saveArr(mContext, LoginActivity.userID, userProfile);

                //변경사항 적용
                textView_profileName.setText(name);
                textView_profileInfo.setText(intro);

                uri = Uri.parse(profile);//uri로 변환
//                uri = data.getData();//uri 가져오기
                imageView_profileImg.setImageURI(uri);//uri 이미지 보여주기
            }
        }

        if(requestCode == REQUEST_CODE_ALBOMUP){//앨범 업로드 정보 받아오기
            System.out.println("Main 화면2");//확인용

            if(resultCode == RESULT_OK) {
                String title = data.getStringExtra("inputTitle");
                String imgUri = data.getStringExtra("inputImg");
                String albumPrivate = data.getStringExtra("inputPri");
                String location = data.getStringExtra("inputLoc");

                System.out.println("inputTitle"+ title);//확인용
                System.out.println("inputImg"+ imgUri);//확인용
                System.out.println("albumPrivate"+ albumPrivate);//확인용
                System.out.println("location"+ location);//확인용

//                Uri profileUri = Uri.parse(imgUri);

                adapter.addItem(new MyAlbum(title, imgUri, albumPrivate, location));
                adapter.notifyDataSetChanged();//데이터가 바뀐 것을 리싸이클러뷰가 알게해서 데이터가 바뀜

                //현재 입력상자에 입력된 데이터를 저장
                PreferenceManager.saveArr(mContext,LoginActivity.userID, MyAlbumAdapter.items );
            }
        }

        //앨범 수정하기
        if (requestCode == REQUEST_CODE_ALBOMED){
            if (resultCode == RESULT_OK){
                System.out.println("Main 화면3");//확인용
                Toast.makeText(getApplicationContext(), "앨범이 수정되었습니다.", Toast.LENGTH_SHORT).show();

                if(resultCode == RESULT_OK) {
                    String title = data.getStringExtra("inputTitle");
                    String imgUri = data.getStringExtra("inputImg");
                    String albumPrivate = data.getStringExtra("inputPri");
                    String location = data.getStringExtra("inputLoc");

                    System.out.println("화면3 : inputTitle"+ title);//확인용
                    System.out.println("화면3 : inputImg"+ imgUri);//확인용
                    System.out.println("화면3 : albumPrivate"+ albumPrivate);//확인용
                    System.out.println("화면3 : location"+ location);//확인용

                    Uri profileUri = Uri.parse(imgUri);
                    System.out.println("화면3 : profileUri"+ profileUri);//확인용

//                    adapter.addItem(new MyAlbum(title, profileUri));
                    adapter.setItem(arr_index, new MyAlbum(title, imgUri, albumPrivate, location));
                    adapter.notifyDataSetChanged();//데이터가 바뀐 것을 리싸이클러뷰가 알게해서 데이터가 바뀜
//                    onSaveData(); //현재 입력상자에 입력된 데이터를 저장

                    //데이터저장
                    PreferenceManager.saveArr(mContext,LoginActivity.userID, MyAlbumAdapter.items );

                }
            }
        }
    }


    //권한확인 메서드
    public void checkPermission(){
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

    //권한 요청 메서드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됐다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){}
                else {
                    //권한을 하나라도 허용하지 않으면 앱 종료
                    Toast.makeText(getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    public void InitializeView(){

        imageView_home = (ImageView) findViewById(R.id.imageView_home);
        imageView_menu = (ImageView) findViewById(R.id.imageView_menu);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);//화면 finish될 때 애니메이션 끄기
//        Toast.makeText(this, "onPause 호출됨",Toast.LENGTH_LONG).show();
        System.out.println("onPause");

    }

    //페이지전환
    public void OnClick(View view) {
        /*페이지 전환 intent*/
        Intent intent;
        switch (view.getId()){
            case R.id.imageView_home:
            case R.id.button_1:
                intent = new Intent(MainActivity5.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

            case R.id.imageView_menu:
                //수정하기
                intent = new Intent(MainActivity5.this, LoginChoiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

            case R.id.button_2:
                intent = new Intent(MainActivity5.this, MainActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_3:
                intent = new Intent(MainActivity5.this, MainActivity3.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_4:
                intent = new Intent(MainActivity5.this, MainActivity4.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_5:
                Toast.makeText(getApplicationContext(), "현재 페이지", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");

    }

    //기본 프로필 데이터 저장 :content://media/external/images/media/93818
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
        System.out.println("onStart 데이터 크기 = " + userProfile.size());
        System.out.println("onStart 데이터 이름 = " + UserProfile.getString(mContext, LoginActivity.userID));

        //기본 프로필 데이터 저장
        textView_profileName = (TextView) findViewById(R.id.textView_profileName);
        textView_profileInfo = (TextView) findViewById(R.id.textView_profileInfo);
        imageView_profileImg = (ImageView) findViewById(R.id.imageView_profileImg);

        if (UserProfile.getString(mContext, LoginActivity.userID).equals("") ){//값이 기본값으로 설정되어 있을 때 or 어레이리스트 사이즈가 1 이상일때; LoginActivity.userID.equals("") || userProfile == null ||
        userProfile = new ArrayList<>();

        }
        else {//userProfile 크기가 2 이상일때

            //저장된 데이터 복원
            userProfile = UserProfile.loadArr(mContext, LoginActivity.userID);

            System.out.println("데이터 크기 = " + userProfile.size());

            //변경사항 적용
            String getuserprofile = userProfile.get(userProfile.size()-1).getUserPicture();
            String getusername = userProfile.get(userProfile.size()-1).getUserName();
            String getusermsg = userProfile.get(userProfile.size()-1).getUserMessage();

            System.out.println("getuserprofile = "+getuserprofile);
            System.out.println("getusername = "+getusername);
            System.out.println("getusermsg = "+getusermsg);

            Uri profileUri = Uri.parse(getuserprofile);

            imageView_profileImg.setImageURI(profileUri);
            textView_profileName.setText(getusername);
            textView_profileInfo.setText(getusermsg);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }
}
