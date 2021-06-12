package com.example.maumalrim;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.Item.UserInfo;
import com.example.maumalrim.SharedPreference.AutoLogin;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivitySignInBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/* PHP 코드에서 MySQL 서버에 접속하여 전달받은 데이터를 저장합니다.*/
public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";


    ActivitySignInBinding activitySignInBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "1. onCreate: 실행");

        activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        Log.d(TAG, "2. onCreate: activitySignInBinding - "+activitySignInBinding.toString());
        Log.d(TAG, "3. onCreate: getLayoutInflater - "+getLayoutInflater().toString());

        View view = activitySignInBinding.getRoot();
        Log.d(TAG, "4. onCreate: view - "+view.toString());
        setContentView(view);

        Log.d(TAG, "5. onCreate: setContentView(view)");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Log.d(TAG, "6. onCreate: 키보드가 텍스트 입력창 가리지 않기");


        //디버그 상태일때 보이고, 아닐때 안보이게 : 설정은 StaticValue 클래스에서 수기로 수정
        if (StaticValue.isDebug){
            Log.d(TAG, "    1) onCreate: 디버그 모드일 때 디버그용 화면 생성");
            activitySignInBinding.textViewMainResult.setMovementMethod(new ScrollingMovementMethod());
            Log.d(TAG, "    2) onCreate: 디버그 모드일 때 textViewMainResult.setMovementMethod(new ScrollingMovementMethod())");
            activitySignInBinding.llDebug.setVisibility(View.VISIBLE);
            Log.d(TAG, "    3) onCreate: 디버그 모드일 때 llDebug.setVisibility(View.VISIBLE)");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "7. onStart: 실행");
        Log.d(TAG, "8. onStart: 쉐어드에 ID 정보 있으면 자동로그인");
        Log.d(TAG, "9. onStart: AutoLogin.getUserName(SignInActivity.this).length() - "+ AutoLogin.getUserName(this).length());
        if (AutoLogin.getUserName(SignInActivity.this).length() != 0){
            // call Login Activity
            Log.d(TAG, "10. onStart: 쉐어드에 자동 로그인 정보 있으면 화면 전환");

            Intent in;
            Log.d(TAG, "11. onStart: 자동 로그인 시, 유저와 상담사가 다른 화면으로 전환됨. 아이디에 @ 유무로 구분함");
            if (AutoLogin.getUserName(SignInActivity.this).contains("@")){
                in = new Intent(SignInActivity.this, MainActivity.class);
                Log.d(TAG, "12. onStart: 아이디에 @가 있을 경우 MainActivity로 감");

            }
            else {
                in = new Intent(SignInActivity.this, EmployeeMainActivity.class);
                Log.d(TAG, "12. onStart: 아이디에 @가 없을 경우 MainChatActivity로 감");

            }

            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Log.d(TAG, "12. onStart: 인텐트 사용시 Flag 달아서 화면전환 애니메이션을 없앰 - "+in.getFlags());
            startActivity(in);
            Log.d(TAG, "13. onStart: 인탠트 실행");
            this.finish();
            Log.d(TAG, "14. onStart: 현재 창 finish");

        } else {

            Log.d(TAG, "10. onStart: 쉐어드에 자동 로그인 정보 없으면 로그인 화면 유지");
        }
    }

    public void ChangeSigninMode(View view) {

        Log.d(TAG, "    1) ChangeSigninMode: 로그인 모드 전환하기(내담자|상담사)");

        String ChangeLoginText = getString(R.string.login_employee);
        Log.d(TAG, "    2) ChangeSigninMode: String 리소스에서 login_employee 가져오기 - "+ChangeLoginText);

        //상담사로그인을 클릭할 경우와 아닐 경우 구분
        if (activitySignInBinding.tvChangeSignin.getText().toString().equals(ChangeLoginText)){
            Log.d(TAG, "    3) ChangeSigninMode: 클릭한 글자가 ["+ChangeLoginText+"] 일 때");
            Toast.makeText(SignInActivity.this, "상담사 로그인", Toast.LENGTH_SHORT).show();

            activitySignInBinding.tvTitle.setVisibility(View.INVISIBLE);
            Log.d(TAG, "    4) ChangeSigninMode: 타이틀 감추기 - "+activitySignInBinding.tvTitle.getVisibility());
            activitySignInBinding.tvTitle2.setVisibility(View.VISIBLE);
            Log.d(TAG, "5) ChangeSigninMode: 상담사용 타이틀 보이기 - "+activitySignInBinding.tvTitle2.getVisibility());
            activitySignInBinding.etId2.setVisibility(View.VISIBLE);
            Log.d(TAG, "    6) ChangeSigninMode: 상담사용 아이디 입력창 보이기 - "+activitySignInBinding.etId2.getVisibility());
            activitySignInBinding.etId.setVisibility(View.GONE);
            Log.d(TAG, "    7) ChangeSigninMode: 아이디 입력창 감추기 - "+activitySignInBinding.etId.getVisibility());
            activitySignInBinding.etPassword2.setVisibility(View.VISIBLE);
            Log.d(TAG, "    8) ChangeSigninMode: 상담사용 아이디 입력창 보이기 - "+activitySignInBinding.etPassword2.getVisibility());
            activitySignInBinding.etPassword.setVisibility(View.GONE);
            Log.d(TAG, "    9) ChangeSigninMode: 비밀번호 입력창 감추기 - "+activitySignInBinding.etPassword.getVisibility());
            activitySignInBinding.btLogin2.setVisibility(View.VISIBLE);
            Log.d(TAG, "    10) ChangeSigninMode: 상담사용 로그인 버튼 보이기 - "+activitySignInBinding.btLogin2.getVisibility());
            activitySignInBinding.btLogin.setVisibility(View.GONE);
            Log.d(TAG, "    11) ChangeSigninMode: 로그인 버튼 감추기 - "+activitySignInBinding.btLogin.getVisibility());
            activitySignInBinding.btRegister.setVisibility(View.GONE);
            Log.d(TAG, "    12) ChangeSigninMode: 회원가입 버튼 감추기 - "+activitySignInBinding.btRegister.getVisibility());
            activitySignInBinding.btRegister2.setVisibility(View.VISIBLE);
            Log.d(TAG, "    13) ChangeSigninMode: 상담사용 회원가입 버튼 보이기 - "+activitySignInBinding.btRegister2.getVisibility());
            activitySignInBinding.tvChangeSignin.setText("상담을 받고 싶으신가요?");
            Log.d(TAG, "    14) ChangeSigninMode: 클릭 메세지 바꾸기 - "+activitySignInBinding.tvChangeSignin.getText());
        } else {
            Log.d(TAG, "    3) ChangeSigninMode: 클릭한 글자가 ["+ChangeLoginText+"] 아닐 때");
            Toast.makeText(SignInActivity.this, "유저 로그인", Toast.LENGTH_SHORT).show();

            activitySignInBinding.tvTitle.setVisibility(View.VISIBLE);
            Log.d(TAG, "    4) ChangeSigninMode: 타이틀 보이기 - "+activitySignInBinding.tvTitle.getVisibility());
            activitySignInBinding.tvTitle2.setVisibility(View.GONE);
            Log.d(TAG, "5) ChangeSigninMode: 상담사용 타이틀 감추기 - "+activitySignInBinding.tvTitle2.getVisibility());
            activitySignInBinding.etId2.setVisibility(View.GONE);
            Log.d(TAG, "    6) ChangeSigninMode: 상담사용 아이디 입력창 감추기 - "+activitySignInBinding.etId2.getVisibility());
            activitySignInBinding.etId.setVisibility(View.VISIBLE);
            Log.d(TAG, "    7) ChangeSigninMode: 아이디 입력창 보이기 - "+activitySignInBinding.etId.getVisibility());
            activitySignInBinding.etPassword2.setVisibility(View.GONE);
            Log.d(TAG, "    8) ChangeSigninMode: 상담사용 아이디 입력창 감추기 - "+activitySignInBinding.etPassword2.getVisibility());
            activitySignInBinding.etPassword.setVisibility(View.VISIBLE);
            Log.d(TAG, "    9) ChangeSigninMode: 비밀번호 입력창 보이기 - "+activitySignInBinding.etPassword.getVisibility());
            activitySignInBinding.btLogin2.setVisibility(View.GONE);
            Log.d(TAG, "    10) ChangeSigninMode: 상담사용 로그인 버튼 감추기 - "+activitySignInBinding.btLogin2.getVisibility());
            activitySignInBinding.btLogin.setVisibility(View.VISIBLE);
            Log.d(TAG, "    11) ChangeSigninMode: 로그인 버튼 보이기 - "+activitySignInBinding.btLogin.getVisibility());
            activitySignInBinding.btRegister2.setVisibility(View.GONE);
            Log.d(TAG, "    12) ChangeSigninMode: 회원가입 버튼 감추기 - "+activitySignInBinding.btRegister2.getVisibility());
            activitySignInBinding.btRegister.setVisibility(View.VISIBLE);
            Log.d(TAG, "    13) ChangeSigninMode: 상담사용 회원가입 버튼 보이기 - "+activitySignInBinding.btRegister.getVisibility());
            activitySignInBinding.tvChangeSignin.setText(ChangeLoginText);
            Log.d(TAG, "    14) ChangeSigninMode: 클릭 메세지 바꾸기 - "+activitySignInBinding.tvChangeSignin.getText());
        }
    }

    public void onSignInButton(View view) {
        Log.d(TAG, "    1) onSignInButton: 로그인 버튼을 누르면 ChatActivity로 이동");
        final String stEmail = activitySignInBinding.etId.getText().toString();//상수
        Log.d(TAG, "    2) onSignInButton: 로그인 이메일 - "+stEmail);
        String stPassword = activitySignInBinding.etPassword.getText().toString(); //비밀번호 받아오기
        Log.d(TAG, "    3) onSignInButton: 비밀번호 - "+stPassword);

        if (stEmail.isEmpty()){
            Log.d(TAG, "    4) onSignInButton: 이메일이 비어있을때 나가기");
            Toast.makeText(SignInActivity.this, "가입하신 Email을 입력해주세요", Toast.LENGTH_LONG).show();
            return;// 아래 코드가 실행되지 않고 여기서 멈춤
        }
        if(stPassword.isEmpty()){
            Log.d(TAG, "    4) onSignInButton: 비밀번호가 비어있을때 나가기");
            Toast.makeText(SignInActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
            return;// 아래 코드가 실행되지 않고 여기서 멈춤
        }
        //email이나 password가 없으면 위에서 멈출거기 때문에 이 곳에 프로그래스바가 실행되도록 함
//                activitySignInBinding.progressBar.setVisibility(View.VISIBLE);


        //여기서부터 서버로그인 추가해야됨
        Log.d(TAG, "    4) onSignInButton: 버튼을 클릭하면 검색창에 있는 내용을 아규먼트로하여 GetData AsyncTask를 실행");
        GetData gtask = new GetData();
        gtask.execute( StaticValue.MyServer_IP  + "/login_query.php", stEmail, stPassword);//아규먼트를 보내고 백그라운드 실행
    }

    public void onEmployeeSignInButton(View view) {

        Log.d(TAG, "    1) onEmployeeSignInButton: 상담사 로그인을 누르면 상담사 로그인");
        final String stPhone = activitySignInBinding.etId2.getText().toString();//상수
        Log.d(TAG, "    2) onEmployeeSignInButton: 로그인 이메일 - "+stPhone);

        String stPassword = activitySignInBinding.etPassword2.getText().toString(); //비밀번호 받아오기
        Log.d(TAG, "    3) onEmployeeSignInButton: 비밀번호 - "+stPassword);

//                이메일이나 비밀번호가 비어있을때
        if (stPhone.isEmpty()){
            Log.d(TAG, "    4) onEmployeeSignInButton: 이메일이 비어있을때 나가기");
            Toast.makeText(SignInActivity.this, "가입하신 핸드폰번호를 입력해주세요", Toast.LENGTH_LONG).show();
            return;// 아래 코드가 실행되지 않고 여기서 멈춤
        }
        if(stPassword.isEmpty()){
            Log.d(TAG, "    4) onEmployeeSignInButton: 비밀번호가 비어있을때 나가기");

            Toast.makeText(SignInActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
            return;// 아래 코드가 실행되지 않고 여기서 멈춤
        }
        //email이나 password가 없으면 위에서 멈출거기 때문에 이 곳에 프로그래스바가 실행되도록 함
//                activitySignInBinding.progressBar.setVisibility(View.VISIBLE);


        Log.d(TAG, "    4) onEmployeeSignInButton: 버튼을 클릭하면 검색창에 있는 내용을 아규먼트로하여 GetData AsyncTask를 실행");
        GetData gtask = new GetData();//버튼을 클릭하면 검색창에 있는 내용을 아규먼트로하여 GetData AsyncTask를 실행합니다.
        gtask.execute( StaticValue.MyServer_IP  + "/employee_login_query.php", stPhone, stPassword);//아규먼트를 보내고 백그라운드 실행
    }

    public void onSignUpButton(View view) {
        Log.d(TAG, "    1) onSignUpButton: 회원가입 버튼 클릭");
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);//SignUpActivity / ServerTestActivity
        Log.d(TAG, "    2) onSignUpButton: intent - "+intent.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Log.d(TAG, "    3) onSignUpButton: intent flags - "+intent.getFlags());
        startActivity(intent);
        Log.d(TAG, "    4) onSignUpButton: 인텐트 실행");
    }

    public void onEmployeeSignUpButton(View view) {
        Log.d(TAG, "    1) onEmployeeSignUpButton: 회원가입 버튼 클릭 함");
        Intent intent = new Intent(getApplicationContext(), EmployeeSignUpActivity.class);
        Log.d(TAG, "    2) onEmployeeSignUpButton: intent - "+intent.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Log.d(TAG, "    3) onEmployeeSignUpButton: intent flags - "+intent.getFlags());
        startActivity(intent);
        Log.d(TAG, "    4) onSignUpButton: 인텐트 실행");
    }

//    데이터 서버에서 가져오기 : AsynTask란 클래스를 상속하여 클래스를 만들면 해당 클래스안에 스레드를 위한 동작코드와 UI 접근 코드를 한꺼번에 넣을 수 있습니다.
    private class GetData extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetData";

        String errorString = null;

        /* onPreExcute(), onProgressUpdate(), onPostExcute()는 메인 스레드에서 실행되므로 UI 객체에 자유롭게 접근이 가능합니다. (셋 모두)
            onProgressUpdate()는 백그라운드 작업의 진행하면서 중간 중간에 UI객체에 접근하는 경우 사용됩니다.
            즉 doInBackground()가 진행중에 사용된다고 보면 됩니다. 이 메소드가 호출될려면 doInBackground에서 publishProgress() 메소드를 호출해야합니다..!
            onCancelled()이라는 메소드도 있는데 이 메소드는 AsyncTask객체를 cancel()로 종료시키면 호출되는 메소드입니다.*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "1. onPreExecute: 백그라운드 작업을 수행하기 전에 호출되며 메인 스레드에서 실행되고 초기화작업에 사용");
        }
        /*마지막으로 onPostExcute()는 백그라운드 작업이 끝나면 호출이 되고 메모리 리소를 해체하는 작업을 주로합니다.
        백그라운드 작업의 결과를 매개변수로 전달받을 수도 있습니다.*/

        @Override//에러가 있는 경우 에러메시지를 보여주고 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출합니다.
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "26. onPostExecute: 백그라운드 작업이 끝나면 호출이 되고 메모리 리소를 해체하는 작업을 주로하고, " +
                    "백그라운드 작업의 결과를 매개변수로 전달받을 수도 있다");

            activitySignInBinding.textViewMainResult.setText(result +"gg");
            Log.d(TAG, "27. response - " + result);
//            Toast.makeText(SignInActivity.this, result,Toast.LENGTH_LONG).show();

            if (result == null || result.equals("로그인 정보를 다시 확인해주세요")){

                activitySignInBinding.textViewMainResult.setText(errorString);
                Toast.makeText(SignInActivity.this, "로그인 실패 : 로그인 정보를 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                return;
            }

            //중복되지 않은 데이터
            //상담사 로그인상황과 유저의 로그인 상황을 구분하여야함
            ArrayList<UserInfo> userInfos = new ArrayList<>();//유저
            ArrayList<EmployeeInfo> employeeInfos = new ArrayList<>();//상담사
            Gson gson = new Gson();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(StaticValue.getJsonName);

                    int index = 0;
                    while (index<jsonArray.length()){

                        if (activitySignInBinding.btLogin.getVisibility()==View.VISIBLE) {
                            Log.d(TAG, "onPostExecute: 유저 정보 받아옴");
                            UserInfo userInfo = gson.fromJson(jsonArray.get(index).toString(), UserInfo.class);
                            userInfos.add(userInfo);

                            Log.i(TAG, "userInfos : "+userInfos.get(userInfos.size()-1).toString());//값 들어감!!

                        }
                        else {
                            Log.d(TAG, "onPostExecute: 직원 정보 받아옴");

                            EmployeeInfo employeeInfo = gson.fromJson(jsonArray.get(index).toString(),EmployeeInfo.class);
                            employeeInfos.add(employeeInfo);

                            Log.i(TAG, "employeeInfos : "+employeeInfos.get(employeeInfos.size()-1).toString());//값 들어감!!

                        }

                        index++;
                    }

                    if (activitySignInBinding.btLogin.getVisibility()==View.VISIBLE){
                        //쉐어드 저장
                        Information.setArr(getApplicationContext(), userInfos);

                        //쉐어드 저장내용 불러오기(확인용)
                        ArrayList<UserInfo> userInfos1 = new ArrayList<>();
                        userInfos1 = Information.getArr(getApplicationContext());

                        Log.d(TAG, "/////////////////////////////////////////////////////////////////////////////");
                        Log.d(TAG, "onPostExecute: 사이즈 : "+(userInfos1.size()-1)+"개");//0
                        Log.d(TAG, "getMaster_id: "+ userInfos.get(userInfos1.size()-1).getMaster_id());
                        Log.d(TAG, "getUser_nickname: "+ userInfos.get(userInfos1.size()-1).getUser_nickname());
                        Log.d(TAG, "getUser_email: "+ userInfos.get(userInfos1.size()-1).getUser_email());

                    }
                    else {
                        //쉐어드 저장
                        Information.setArr(getApplicationContext(), employeeInfos);

                        //쉐어드 저장내용 불러오기(확인용)
                        ArrayList<EmployeeInfo> employeeInfos1 = new ArrayList<>();
                        employeeInfos1 = Information.getArr(getApplicationContext());

                        Log.d(TAG, "/////////////////////////////////////////////////////////////////////////////");
                        Log.d(TAG, "getMaster_id: "+ employeeInfos.get(employeeInfos1.size()-1).getMaster_id());
                        Log.d(TAG, "getUser_nickname: "+ employeeInfos.get(employeeInfos1.size()-1).getUser_nickname());
                        Log.d(TAG, "getUser_phone: "+ employeeInfos.get(employeeInfos1.size()-1).getUser_phone());

                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

            Intent in;

            if (activitySignInBinding.btLogin.getVisibility()==View.VISIBLE){
                    in = new Intent(SignInActivity.this, MainActivity.class);
//                in.putExtra("email", stEmail);//이메일도 같이 넘김 > 쉐어드에 로그인 정보 저장
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    AutoLogin.setUserName(getApplicationContext(), activitySignInBinding.etId.getText().toString());

                    //아무거나 입력해도 로그인가능함. 오류 확인하기
                    Log.d(TAG, "쉐어드 저장 아이디 확인1 = "+AutoLogin.getUserName(SignInActivity.this));

                    //http통신 필요 : mysql에서 activitySignInBinding.etId.getText().toString()에 해당하는 값 정보 가져오기

                }

                else {
                    in = new Intent(SignInActivity.this, EmployeeMainActivity.class);
//                in.putExtra("email", stEmail);//이메일도 같이 넘김 > 쉐어드에 로그인 정보 저장
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    AutoLogin.setUserName(SignInActivity.this, activitySignInBinding.etId2.getText().toString());
                    Log.d(TAG, "쉐어드 저장 아이디 확인2 = "+AutoLogin.getUserName(SignInActivity.this));
                }

                startActivity(in);
                finish();
            }



        /*새로 만들어진 스레드, 즉 백그라운드 작업을 할 수 있습니다.
        그리고 excute() 메소드를 호출할 때 사용도니 파라미터를 배열로 전달받을 수 있습니다.
        중간중간마다 UI객체에 접근할려면 메인스레드에서 해야하므로 publishProgress() 를 호출해 nProgressUpdate()를 불러와서 메인스레드에서 UI작업이 가능해집니다.*/
        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "2. doInBackground: excute() 받았니?");

            String serverURL = params[0];
            Log.d(TAG, "3. doInBackground: serverURL - "+serverURL);
            String user_email = (String)params[1];
            Log.d(TAG, "4. doInBackground: user_email - "+user_email);
            String user_pw = (String)params[2];
            Log.d(TAG, "5. doInBackground: user_pw - "+user_pw);

            String postParameters = "user_email="+user_email+"&user_pw="+user_pw;
            Log.d(TAG, "6. doInBackground: 서버로 보낼 파라미터 - "+postParameters);

            try {
                URL url = new URL(serverURL);
                Log.d(TAG, "7. doInBackground: String으로 넘어온 서버 url를 URL로 바꾸기- "+url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "8. doInBackground: httpURLConnection - "+httpURLConnection.toString());
                httpURLConnection.setReadTimeout(5000);
                Log.d(TAG, "9. doInBackground: httpURLConnection 읽는 타임아웃 5초 - "+httpURLConnection.toString());
                httpURLConnection.setConnectTimeout(5000);
                Log.d(TAG, "10. doInBackground: httpURLConnection 연결 타임아웃 5초 - "+httpURLConnection.toString());
                httpURLConnection.setRequestMethod("POST");
                Log.d(TAG, "11. doInBackground: httpURLConnection 요청 방법 POST - "+httpURLConnection.toString());
                httpURLConnection.setDoInput(true);
                Log.d(TAG, "12. doInBackground: httpURLConnection Sever 통신에서 입력 가능 모드 설정 - "+httpURLConnection.toString());
                httpURLConnection.connect();
                Log.d(TAG, "13. doInBackground: httpURLConnection 연결- "+httpURLConnection.toString());

                OutputStream outputStream = httpURLConnection.getOutputStream();
                Log.d(TAG, "14. doInBackground: outputStream - "+outputStream.toString());
                outputStream.write(postParameters.getBytes("UTF-8"));
                Log.d(TAG, "15. doInBackground: outputStream write - "+outputStream.toString());
                outputStream.flush();
                Log.d(TAG, "16. doInBackground: outputStream flush - "+outputStream.toString());
                outputStream.close();
                Log.d(TAG, "17. doInBackground: outputStream close - "+outputStream.toString());


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "18. response code http통신 응답 코드 받아오기 - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    Log.d(TAG, "19. doInBackground: InputStream 정상 - "+inputStream.toString());
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                    Log.d(TAG, "19. doInBackground: InputStream 오류 - "+inputStream.toString());
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                Log.d(TAG, "20. doInBackground: inputStream 읽기(InputStreamReader) - "+inputStreamReader.toString());

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                Log.d(TAG, "21. doInBackground: InputStreamReader 읽기(BufferedReader) - "+bufferedReader.toString());

                StringBuilder sb = new StringBuilder();
                Log.d(TAG, "22. doInBackground: StringBuilder 선언 ");
                String line;

                while((line = bufferedReader.readLine()) != null){
                    Log.d(TAG, "23. doInBackground:  bufferedReader.readLine() 서버에서 받은 텍스트 라인 읽기 - "+line);
                    sb.append(line);
                    Log.d(TAG, "24. doInBackground: StringBuilder - "+sb.toString());
                }

                bufferedReader.close();
                Log.d(TAG, "25. doInBackground: bufferedReader.close();");

                return sb.toString().trim();//trim : 문자열 공백 제거


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    //레트로핏으로 데이터 저장하기 : 나중에 써보기!
 /*   private class RetrofitCommunication{
        private static final String TAG = "RetrofitCommunication";

        private Gson gson;
        private Retrofit retrofit;
        private RetrofitService service;

        // ** 레트로핏 요청 (콜백함수, 요청구분, 파라미터) ** //
        public void request(Callback callback, String gubun, final HashMap<String, String> hashMap) {
            Log.d(TAG,  "=========> 요청 : " + gubun);

            if (gson == null){
                gson = new GsonBuilder().setLenient().create();
            }
            if (retrofit == null){
                retrofit = new Retrofit.Builder()
                        .baseUrl(StaticValue.MyServer_IP)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

            }
            if (service == null){
                service = retrofit.create(RetrofitService.class);
            }

            if ("userInfoCheck".equals(gubun)){
                Call<UserInfo> call = service.userInfoCheck(hashMap.get())
            }
        }

    }

    private interface RetrofitService {

        @FormUrlEncoded
        @POST("/searchquery.php")
        Call<UserInfo> userInfoCheck(@Field("userId") String userId,
                                     @Field("userNickname") String userNickname,
                                     @Field("birthYear") String birthYear,
                                     @Field("gender") String gender,
                                     @Field("job") String job,
                                     @Field("reg_time") String reg_time,
                                     @Field("isCert") String isCert,
                                     @Field("isStatus") String isStatus);

    }*/

}
