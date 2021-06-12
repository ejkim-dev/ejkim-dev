package com.example.mytraveldiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.sharedPreferences.PreferenceManager;

import java.util.ArrayList;

public class MyAlbumActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GETIMAG = 0;//이미지 가져오기 request 코드
    private Context mContext;
    private int arr_index;

    ImageView imageView_profile;
    EditText editText_name;
    EditText editText_location;//장소
    Button btn_input;//입력하기 버튼
    Button btn_private;//공개/비공개 버튼
    Uri uri = null;
    String albumImg;
    String albumTitle;
    String albumPrivate;
    String albumlocation;
    boolean getSth = false;

    ArrayList<MyAlbum> items = new ArrayList<>();//ArrayList로 저장하기 위해 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_album);
        mContext = this;

        //저장된 데이터 복원
        items = PreferenceManager.loadArr(mContext, LoginActivity.userID);

        for(int i = 0; i < items.size() ; i++){
            Println.println("복원 값 "+i+" = "+items.get(i));
        }

        //데이터를 입력하는 뷰 설정
        imageView_profile = (ImageView) findViewById(R.id.imageView_profile);
        editText_name = (EditText) findViewById(R.id.editText_name);
        btn_input = (Button) findViewById(R.id.btn_input);
        btn_private = (Button) findViewById(R.id.btn_private);
        editText_location = (EditText) findViewById(R.id.editText_location);

        //앞에서 전달받은 이미지와 텍스트
        try {
            Intent intent = getIntent();

            albumImg = intent.getStringExtra("getAlbumUri");
            Uri uriEdit = Uri.parse(albumImg);
            imageView_profile.setImageURI(uriEdit);//uri 이미지 보여주기

            albumTitle = intent.getStringExtra("getTitle");
            editText_name.setHint(albumTitle);

            //position 가져오기
            arr_index = intent.getExtras().getInt("this_position");

            albumPrivate = intent.getStringExtra("getPrivate");

            //앨범 공개/비공개 버튼 세팅
            if (albumPrivate.equals("비공개")){ //버튼은 "공개"
                btn_private.setText("공개");
                int colorGray = ContextCompat.getColor(MyAlbumActivity.this, R.color.colorGray);
                btn_private.setBackgroundColor(colorGray);//배경의 색을 바꾼다
            }

            albumlocation = intent.getStringExtra("getLocation");
            editText_location.setHint(albumlocation);
            getSth = true;

        }catch (NullPointerException e){}//전달받을 데이터가 없다면 NullPoint

        //버튼 누르면 공개/비공개 바뀜
        btn_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_private.getText().toString().equals("비공개")){//해당 포지션에 있는 privateAlbum 이름을 가져와야함
                    btn_private.setText("공개");

                    // "colorLightGray" 라는 이름의 <color> 리소스를 가져온다. int 값.
                    int colorGray = ContextCompat.getColor(MyAlbumActivity.this, R.color.colorGray);
                    btn_private.setBackgroundColor(colorGray);//배경의 색을 바꾼다

                    Toast.makeText(MyAlbumActivity.this, "앨범 비공개 설정 완료", Toast.LENGTH_SHORT).show();

                }
                else if(btn_private.getText().toString().equals("공개")){
                    btn_private.setText("비공개");

                    // "color_red" 라는 이름의 <color> 리소스를 가져온다. int 값.
                    int colorDarkGray = ContextCompat.getColor(MyAlbumActivity.this, R.color.colorDarkGray);
                    btn_private.setBackgroundColor(colorDarkGray);//배경의 색을 바꾼다

                    Toast.makeText(MyAlbumActivity.this, "앨범 공개 설정 완료", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //사진 불러와서(REQUEST_CODE_GETIMAG) 표시하기
        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//사진표시하기, 갤러리에서 앨범 가져오기
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GETIMAG);//갤러리에서 가져옴
            }
        });

        //버튼 입력하면 데이터 전달하기
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("setOnClickListener_btn_input-------------------------------------------------------------------");//확인용
                System.out.println("uri = "+uri);//확인용 : uri = null
                System.out.println("uri (원래입력값) = "+albumImg);//확인용 : uri = null /imageView_profile
                System.out.println("editText_name = "+editText_name.getText().toString());//확인용 : editText_name =
                System.out.println("editText_name(hint) = "+editText_name.getHint().toString());//확인용 :  editText_name(hint) = 움
                System.out.println("비공개 or 공개 = "+btn_private.getText()); //확인용 : 비공개 or 공개 = 비공개
                System.out.println("장소 = " +editText_location.getText().toString() );//확인용 : 장소 =
                System.out.println("장소(hint) = " +editText_location.getHint().toString() );//확인용 : 장소(hint) = ㅜㅜ


                String albumpublic = "공개";

                if( btn_private.getText().toString().equals("공개")){ albumpublic = "비공개"; }

                //넘어온게 있을 때
                if(getSth){
                    /*이름이 빈칸일 때*/
                    if(editText_name.getText().toString().equals("")){ editText_name.setText(editText_name.getHint().toString()); }
                    /*사진이 빈칸일 때*/
                    if(uri == null){ uri = Uri.parse(albumImg);}
                    /*장소가 빈칸일 때*/
                    if(editText_location.getText().toString().equals("")){ editText_location.setText(editText_location.getHint().toString()); }
                }

                try {
                    if(uri.toString().isEmpty()){
                        Toast.makeText(MyAlbumActivity.this, "메인 사진을 추가해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText_name.getText().toString().isEmpty()){
                        Toast.makeText(MyAlbumActivity.this, "제목을 입력 해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText_location.getText().toString().isEmpty()){
                        Toast.makeText(MyAlbumActivity.this, "장소를 입력 해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        albumTitle = editText_name.getText().toString();
                        albumImg = uri.toString();
                        albumPrivate = albumpublic;//공개 or 비공개
                        albumlocation = editText_location.getText().toString();//입력된 장소

                        //결과를 MainActivity로 전달하고 현재 Activity 는 종료.
                        Intent intent = new Intent();

                        intent.putExtra("inputTitle", albumTitle);
                        intent.putExtra("inputImg", albumImg);
                        intent.putExtra("inputPri", albumPrivate);
                        intent.putExtra("inputLoc", albumlocation);
//                    intent.putExtra("this_position", this_position);

                        //확인용
                        System.out.println("inputTitle"+ albumTitle);//확인용
                        System.out.println("inputImg"+ albumImg);//확인용
                        System.out.println("albumPrivate"+ albumPrivate);//확인용
                        System.out.println("location"+ albumlocation);//확인용

                        //this_position

                        setResult(RESULT_OK, intent);
                        System.out.println("RESULT_OK");//확인용
                        finish();
                    }
                } catch (NullPointerException e){
                    //기본값- 추후 수정 > 한개만 수정되어도 수정 반영 될 수 있도록 고쳐보기
 /*                   Intent intent = new Intent();

                    intent.putExtra("inputTitle", albumTitle);
                    intent.putExtra("inputImg", albumImg);
                    intent.putExtra("inputPri", albumPrivate);
                    intent.putExtra("inputLoc", albumlocation);
//                    intent.putExtra("this_position", this_position);

                    //this_position
                    setResult(RESULT_OK, intent);
                    System.out.println("RESULT_OK");//확인용
                    finish();*/
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GETIMAG){
            if(resultCode == RESULT_OK){
                try {
                    uri = data.getData();//uri 가져오기
                    imageView_profile.setImageURI(uri);//uri 이미지 보여주기

                } catch (Exception e) { }

            }

            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "앨범 업로드 취소됨", Toast.LENGTH_LONG).show();
            }

        }
    }
}
