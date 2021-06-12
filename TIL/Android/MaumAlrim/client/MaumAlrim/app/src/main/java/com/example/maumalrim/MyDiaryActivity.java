package com.example.maumalrim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.databinding.ActivityMyDiaryBinding;

import java.util.HashMap;
import java.util.Map;

public class MyDiaryActivity extends AppCompatActivity {
    private static final String TAG = "MyDiaryActivity";
    static RequestQueue requestQueue;

    ActivityMyDiaryBinding activityMyDiaryBinding;

    String diaryCode;
    String title;
    String condition;
    String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyDiaryBinding = ActivityMyDiaryBinding.inflate(getLayoutInflater());
        View view = activityMyDiaryBinding.getRoot();
        setContentView(view);

        diaryCode = getIntent().getStringExtra("diaryCode");
        title = getIntent().getStringExtra("title");
        condition = getIntent().getStringExtra("condition");
        contents = getIntent().getStringExtra("contents");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (requestQueue == null){
            //RequestQueue 객체 생성하기
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (!title.equals("") && !condition.equals("") && !contents.equals("")){
            activityMyDiaryBinding.etTitle.setText(title);
            activityMyDiaryBinding.etCondition.setText(condition);
            activityMyDiaryBinding.etText.setText(contents);
            activityMyDiaryBinding.btnSubmit.setText("수정하기");
        }


    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                Log.d(TAG, "onClick: 이 엑티비티 열기 전 화면으로 돌리기 위해서");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("뒤로가기").setMessage("아직 저장되지 않았습니다. 계속하시겠습니까?");

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        Toast.makeText(getApplicationContext(),"저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return;
            case R.id.btn_submit:

                if (activityMyDiaryBinding.btnSubmit.getText().equals("저장하기")) {
                    Log.d(TAG, "onClick: 저장되었습니다.");
                    makeRequest("/saveDiary.php", AutoLogin.getUserName(getApplication().getApplicationContext()),
                            activityMyDiaryBinding.etTitle.getText().toString(),
                            activityMyDiaryBinding.etCondition.getText().toString(),
                            activityMyDiaryBinding.etText.getText().toString());
//                Toast.makeText(getApplicationContext(),"저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    AlertDialog.Builder builderEdit = new AlertDialog.Builder(this);
                    builderEdit.setTitle("수정하기").setMessage("수정된 내용을 적용하시겠습니까?");

                    builderEdit.setPositiveButton("내용적용", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //다이어리 번호 불러와서 저장하기
                            makeRequest("/editDiary.php", diaryCode,//다이어리 코드 불러서 저장
                                    activityMyDiaryBinding.etTitle.getText().toString(),
                                    activityMyDiaryBinding.etCondition.getText().toString(),
                                    activityMyDiaryBinding.etText.getText().toString());

                            finish();

                        }
                    });

                    builderEdit.setNegativeButton("내용삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //다이어리 번호 불러와서 저장하기
                            makeRequest("/deleteDiary.php", diaryCode, title, condition, contents);
                            finish();

                        }
                    });

                    builderEdit.setNeutralButton("뒤로가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialogEdit = builderEdit.create();
                    alertDialogEdit.show();
//                  Toast.makeText(getApplicationContext(),"수정", Toast.LENGTH_SHORT).show();

                }

                return;
        }
    }

    public void makeRequest(String url, final String user_id, final String user_title, final String user_condition,  final String user_text){

        StringRequest request = new StringRequest(Request.Method.POST, StaticValue.MyServer_IP + url,
                new Response.Listener<String>() {
                    @Override //서버 응답시 처리할 내용
                    public void onResponse(String response) {
                        Log.d(TAG,"onResponse 응답-> "+response);
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override //에러시 처리할 내용
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"에러-> "+error.getMessage());
                    }
                }
        ){
            @Override //서버가 요청하는 파라미터를 담음
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("user_title",user_title);
                params.put("user_condition",user_condition);
                params.put("user_text",user_text);
                return params;
            }
        };
        request.setShouldCache(false); //이미 사용한 것은 제거
        requestQueue.add(request);
        Log.d(TAG,"요청 보냄.");
    }


}
