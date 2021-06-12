package com.example.mytraveldiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.list.Profile;
import com.example.mytraveldiary.sharedPreferences.UserProfile;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ProfileEditActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMG = 1000;

    /*이미지뷰 matrix 사이즈 조절 알아보기*/
    ImageView imageView_profile;//프로필이미지
    TextInputEditText textInputEditText_name;//이름
    TextInputEditText textInputEditText_intro;//소개글
    Button btn_input;//입력하기
    Uri uriEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //이미지를 클릭하고, 내용을 추가하고,입력하기 누르면 정보 MainActivity5 로 넘기기
        imageView_profile = (ImageView) findViewById(R.id.imageView_profile);
        textInputEditText_name = (TextInputEditText) findViewById(R.id.textInputEditText_name);
        textInputEditText_intro = (TextInputEditText) findViewById(R.id.textInputEditText_intro);
        btn_input = (Button) findViewById(R.id.btn_input);

        try {
            Intent intent = getIntent();
            String profile = intent.getExtras().getString("img");
            String na = intent.getExtras().getString("na");
            String msg =  intent.getExtras().getString("msg");
            uriEdit = Uri.parse(profile);//String profile을 uri로 바꾸기
            imageView_profile.setImageURI(uriEdit);//uri 이미지 보여주기
            textInputEditText_name.setHint(na);
            textInputEditText_intro.setHint(msg);

        }catch (NullPointerException e){}
        //권한승인 오류 발생 : SecurityException(READ_EXTERNAL_STORAGE)

        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");

                startActivityForResult(intent, REQUEST_CODE_IMG);//갤러리에서 가져옴
            }
        });

        //입력하기 버튼을 클릭했을 때
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //데이터 없으면 null 값
                if(textInputEditText_name.getText().toString().isEmpty() && textInputEditText_intro.getText().toString().isEmpty() && uriEdit == null){
                    Toast.makeText(ProfileEditActivity.this, "프로필에 변경사항이 없습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(textInputEditText_name.getText().toString().isEmpty()){ Toast.makeText(ProfileEditActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show(); }
                else if(textInputEditText_intro.getText().toString().isEmpty()){ Toast.makeText(ProfileEditActivity.this, "소개글 입력해주세요", Toast.LENGTH_SHORT).show(); }
                else if(uriEdit == null){ Toast.makeText(ProfileEditActivity.this, "프로필 사진을 추가해주세요", Toast.LENGTH_SHORT).show(); }
                else {
                    String inputName = textInputEditText_name.getText().toString();
                    String inputIntro = textInputEditText_intro.getText().toString();
                    String inputProfile = uriEdit.toString();

                    //결과를 MainActivity로 전달하고 현재 Activity 는 종료.
                    Intent intent = new Intent();
                    intent.putExtra("inputName", inputName);
                    intent.putExtra("inputIntro", inputIntro);
                    intent.putExtra("inputProfile", inputProfile);

                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMG){
            if (resultCode == RESULT_OK){
                try {
                    uriEdit = data.getData();//uri 가져오기
                    imageView_profile.setImageURI(uriEdit);//uri 이미지 보여주기

                } catch (Exception e) { }
            }
        }
        else if(resultCode == RESULT_CANCELED) { Toast.makeText(this, "프로필 사진 변경 취소됨", Toast.LENGTH_LONG).show(); }
    }

}
