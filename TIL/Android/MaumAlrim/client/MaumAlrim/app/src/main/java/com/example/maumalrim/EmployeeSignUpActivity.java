package com.example.maumalrim;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.databinding.ActivityEmployeeSignUpBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EmployeeSignUpActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeSignUpActivity";
    ActivityEmployeeSignUpBinding activityEmployeeSignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "1. onCreate: 실행");
        activityEmployeeSignUpBinding = ActivityEmployeeSignUpBinding.inflate(getLayoutInflater());
        View view = activityEmployeeSignUpBinding.getRoot();
        setContentView(view);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Log.d(TAG, "2. onCreate: 키보드가 텍스트 입력창 가리지 않기");

        //디버그 상태일 때 디버그 결과값 출력해서 보여주기 위해서
        activityEmployeeSignUpBinding.textViewMainResult.setMovementMethod(new ScrollingMovementMethod());

        if (StaticValue.isDebug){
            activityEmployeeSignUpBinding.llDebug.setVisibility(View.VISIBLE);
        }

        //핸드폰 인증 버튼 누르면 원래 인증문자칸이 나타나야하지만, 나중에 추가하기
        activityEmployeeSignUpBinding.btCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인증 확인하고 나면 수정못하게하기
                activityEmployeeSignUpBinding.etPhoneId.setFocusableInTouchMode(false);
                activityEmployeeSignUpBinding.etPhoneId.clearFocus();
                activityEmployeeSignUpBinding.btCert.setEnabled(false);
//                activityEmployeeSignUpBinding.etPhoneId.setNextFocusDownId(R.id.et_user_pw);

            }
        });

        //회원가입 누르면 가입됨
        activityEmployeeSignUpBinding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "닉네임경고메세지 : "+activityEmployeeSignUpBinding.tvNickAlert.getText().toString());
                Log.d(TAG, "아이디경고메세지 : "+activityEmployeeSignUpBinding.tvIdAlert.getText().toString());

                if (activityEmployeeSignUpBinding.tvNickAlert.getText().toString().equals("")&&activityEmployeeSignUpBinding.tvIdAlert.getText().toString().equals("")
                        &&activityEmployeeSignUpBinding.tvPwAlert.getText().toString().equals("")){

                    String user_nickname = activityEmployeeSignUpBinding.etNickname.getText().toString();
                    String user_phone = activityEmployeeSignUpBinding.etPhoneId.getText().toString();
                    String user_pw = activityEmployeeSignUpBinding.etUserPw.getText().toString();

                    InsertData task = new InsertData();
                    task.execute(StaticValue.MyServer_IP + "/employee_insert.php", user_nickname, user_phone, user_pw);

                    // 비밀번호 초기화
                    activityEmployeeSignUpBinding.etUserPw.getText().clear();
//                    etPwCheck.setText("");

                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "입력을 확인해주세요.", Toast.LENGTH_LONG).show();
            }
        });

        TextChanged(activityEmployeeSignUpBinding.etPhoneId, activityEmployeeSignUpBinding.tvIdAlert, "phone");
        TextChanged(activityEmployeeSignUpBinding.etUserPw, activityEmployeeSignUpBinding.tvPwAlert,"pw");
        TextChanged(activityEmployeeSignUpBinding.etPwCheck, activityEmployeeSignUpBinding.tvPwCheckAlert, "pwcheck");
        TextChanged(activityEmployeeSignUpBinding.etNickname, activityEmployeeSignUpBinding.tvNickAlert, "nickname");

    }

    //데이터 서버에 저장하기
    class InsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            activityEmployeeSignUpBinding.textViewMainResult.setText(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "params.length = "+params.length);
            for (int i = 0; i < params.length; i++){
                Log.d(TAG, "params["+i+"] = "+params[i]);
            }

            String user_nickname = (String)params[1];
            String user_phone =  (String)params[2];
            String user_pw =  (String)params[3];

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String serverURL = (String)params[0];
            String postParameters = "user_nickname=" + user_nickname + "&user_phone="+user_phone + "&user_pw="+user_pw;

            try {

                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL);// 주소가 저장된 변수를 이곳에 입력합니다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);//5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST");//요청 방식을 POST로 합니다.
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.

                outputStream.flush();
                outputStream.close();

                // 3. 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    // 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();

                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
                return sb.toString();

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "에러 : "+ e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Error - " +  e.getMessage());
                return new String("Error : " +  e.getMessage());
            }

        }
    }

//    //데이터 서버에서 가져오기
    private class GetData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override//에러가 있는 경우 에러메시지를 보여주고 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출합니다.
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();
            activityEmployeeSignUpBinding.textViewMainResult.setText(result +"gg");
            Log.d(TAG, "response - " + result);
            activityEmployeeSignUpBinding.tvIdAlert.setText(result);
            activityEmployeeSignUpBinding.tvIdAlert.setVisibility(View.VISIBLE);
            activityEmployeeSignUpBinding.btCert.setEnabled(false);

            if (result.equals("")){//중복되지 않은 데이터
//                btRegister.setEnabled(true);
                activityEmployeeSignUpBinding.tvIdAlert.setVisibility(View.GONE);
                activityEmployeeSignUpBinding.btCert.setEnabled(true);
            }
            if (result == null){

                activityEmployeeSignUpBinding.textViewMainResult.setText(errorString);
            }
        }

        @Override
        protected String doInBackground(String... params) {


            String serverURL = params[0];
            String user_phone = (String)params[1];

            String postParameters = "user_phone="+user_phone;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.e(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    // 문자 바뀐것 인식
    public void TextChanged(final EditText editText, final TextView textView, final String what){
        editText.addTextChangedListener(new TextWatcher() {

            @Override //글자 변화되기 전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//               Log.d(TAG, "beforeTextChanged - 텍스트 : "+s+" start : "+start+" count : "+count+" after : "+after);
            }

            @Override //글자 변화되는 중
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d(TAG, "onTextChanged - 텍스트 : "+s+" start : "+start+" count : "+count+" before : "+before);
            }

            @Override //글자 변화된 후 : 한 글자만 변화해도 바뀜
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged - 텍스트 : "+s+" 길이 : "+s.toString().length());

                try {
                    if (what.equals("pw")){
                        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(s);
                        if (m.matches()){
                            textView.setVisibility(View.GONE);
                            textView.setText("");
                        }
                        else {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("※8~20자리의 영문, 숫자, 특수문자 포함 ");
                        }
                    }
                    if (what.equals("pwcheck")){
                        Log.i(TAG, "afterTextChanged: "+activityEmployeeSignUpBinding.etUserPw.getText().toString());
                        if (activityEmployeeSignUpBinding.etUserPw.getText().toString().equals(s.toString())){

                            int grayColor = ContextCompat.getColor(getApplicationContext(),R.color.colorGray);
                            textView.setTextColor(grayColor);
                            textView.setText("※비밀번호가 일치합니다.");
                        }
                        else {
                            int pinkColor = ContextCompat.getColor(getApplicationContext(),R.color.colorPink);
                            textView.setTextColor(pinkColor);
                            textView.setText("※비밀번호가 일치하지 않습니다.");
                        }
                    }

                    //핸드폰번호 확인 : 서버에 보내기 : 인증버튼 활성화. 핸드폰 인증문자 보내고, 확인하기
                    if (what.equals("phone")){
                        String regex = "^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(s);
                        if (m.matches()){

                            textView.setVisibility(View.GONE);

                            //핸드폰 인증 버튼 활성화
                            activityEmployeeSignUpBinding.btCert.setEnabled(true);
                            // 서버에 중복 체크하기
                            GetData gtask = new GetData();//버튼을 클릭하면 검색창에 있는 내용을 아규먼트로하여 GetData AsyncTask를 실행합니다.
                            gtask.execute( StaticValue.MyServer_IP + "/employee_query.php", s.toString());

                        }else {
                            activityEmployeeSignUpBinding.btCert.setEnabled(false);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("※올바른 핸드폰 번호인지 확인해주세요.");
                        }
                    }

                    if(what.equals("nickname")) {
                        if(s.toString().matches(".*[ㄱ-ㅎㅏ-ㅣ]+.*")||s.toString().length()<2) {
                            // 자음이나 모음만 포함
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("※두 글자 이상의 완성된 한글을 써주세요.");

                        } else {
                            textView.setVisibility(View.GONE);
                            textView.setText("");
                        }
                    }
                } catch (PatternSyntaxException e) {

                    // 정규식에 오류가 있는 경우에 대한 처리
                    Log.e(TAG, "afterTextChanged: 정규식 오류");
                    e.printStackTrace();
                }


            }
        });
    }
}
