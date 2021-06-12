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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.databinding.ActivitySignUpBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SignUpActivity extends AppCompatActivity {

    /* PHP 코드에서 MySQL 서버에 접속하여 전달받은 데이터를 저장합니다.*/
    private static String TAG = "SignUpActivity";

    private String birth_year = "";
    private String gender = "";

    //xml이 뷰바인딩에 의해서 ActivitySignUpBinding이라는 클래스가 만들어짐
    ActivitySignUpBinding activitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //activitySignUpBinding에 activity_sign_up.xml이 만들어진 뷰 객체를 받아옴
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        //view를 아래 setContentView에 등록을 해줘야함 : view가 최상위 레이아웃(ConstraintLayout)을 받아옴
        View view = activitySignUpBinding.getRoot();
        setContentView(view);//activity_sign_up.xml이 setContentView로 해서 등록이 됨

        //디버그 상태일 때 디버그 결과값 출력해서 보여주기 위해서
        activitySignUpBinding.textViewMainResult.setMovementMethod(new ScrollingMovementMethod());

        //디버그 상태일때 보이고, 아닐때 안보이게 : 설정은 StaticValue 클래스에서 수기로 수정
        if (StaticValue.isDebug){
            activitySignUpBinding.llDebug.setVisibility(View.VISIBLE);
        }

        //문자가 바뀐 것을 인식하는 클래스
        TextChanged(activitySignUpBinding.etNickname,   activitySignUpBinding.tvNickAlert, false,"");
        TextChanged(activitySignUpBinding.etJob,        activitySignUpBinding.tvJobAlert, false,"");
        TextChanged(activitySignUpBinding.etUserId,     activitySignUpBinding.tvIdAlert, true,"");
        TextChanged(activitySignUpBinding.etUserPw,     activitySignUpBinding.tvPwAlert, false,"pw");
        TextChanged(activitySignUpBinding.etPwCheck,    activitySignUpBinding.tvPwCheckAlert, false,"pwcheck");

        // 생년 스피너
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySignUpBinding.spnBirthYear.setAdapter(adapter);
        // 사용자가 드롭다운에서 항목을 선택하면 Spinner 객체가 항목 선택(on-item-selected) 이벤트를 수신
        activitySignUpBinding.spnBirthYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("태어난해")){ birth_year = ""; return; }
                birth_year = parent.getItemAtPosition(position).toString();
//                Toast.makeText(SignUpActivity.this, "태어난 해 : "+birth_year, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(SignUpActivity.this, "태어난 해를 선택해주세요", Toast.LENGTH_LONG).show();
            }
        });

        // 성별 스피너
        adapter = ArrayAdapter.createFromResource(this, R.array.planets_gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySignUpBinding.spnGender.setAdapter(adapter);
        // 사용자가 드롭다운에서 항목을 선택하면 Spinner 객체가 항목 선택(on-item-selected) 이벤트를 수신
        activitySignUpBinding.spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//parent : 글자
                if (parent.getItemAtPosition(position).equals("성별")){ gender = ""; return;}
                if(parent.getItemAtPosition(position).equals("남자")){
                    gender = "M";
                }
                if(parent.getItemAtPosition(position).equals("여자")){
                    gender = "F";
                }
//                Toast.makeText(SignUpActivity.this, "성별 : "+gender, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(SignUpActivity.this, "성별을 선택해주세요", Toast.LENGTH_LONG).show();
            }
        });

//        btRegister.setEnabled(true);

        activitySignUpBinding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "닉네임경고메세지 : "+activitySignUpBinding.tvNickAlert.getText().toString());
                Log.d(TAG, "아이디경고메세지 : "+activitySignUpBinding.tvIdAlert.getText().toString());

                if (activitySignUpBinding.tvNickAlert.getText().toString().equals("")&&activitySignUpBinding.tvIdAlert.getText().toString().equals("")
                        &&activitySignUpBinding.tvPwAlert.getText().toString().equals("")&&!gender.equals("")&&!birth_year.equals("")){

                    String user_nickname = activitySignUpBinding.etNickname.getText().toString();
                    String user_email = activitySignUpBinding.etUserId.getText().toString();
                    String user_pw = activitySignUpBinding.etUserPw.getText().toString();
                    String job = activitySignUpBinding.etJob.getText().toString();

                    InsertData task = new InsertData();
                    task.execute(StaticValue.MyServer_IP + "/member_insert.php", user_nickname, user_email, user_pw, birth_year, job, gender);

                    // 비밀번호 초기화
                    activitySignUpBinding.etUserPw.getText().clear();
//                    etPwCheck.setText("");

                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(SignUpActivity.this, "입력을 확인해주세요.", Toast.LENGTH_LONG).show();

            }
        });

    }


    //데이터 서버에 저장하기
    class InsertData extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(SignUpActivity.this,
//                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();
            activitySignUpBinding.textViewMainResult.setText(result);
            Toast.makeText(SignUpActivity.this, result, Toast.LENGTH_LONG).show();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "params.length = "+params.length);
            for (int i = 0; i < params.length; i++){
                Log.d(TAG, "params["+i+"] = "+params[i]);
            }

            String user_nickname = (String)params[1];
            String user_email =  (String)params[2];
            String user_pw =  (String)params[3];
            String birth_year =  (String)params[4];
            String job =  (String)params[5];
            String gender =  (String)params[6];

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String serverURL = (String)params[0];
            String postParameters = "user_nickname=" + user_nickname + "&user_email="+user_email
                    + "&user_pw="+user_pw + "&birth_year="+birth_year + "&job="+job + "&gender="+gender;

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
                Toast.makeText(SignUpActivity.this, "에러 : "+ e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Error - " +  e.getMessage());
                return new String("Error : " +  e.getMessage());
            }

        }
    }

    //데이터 서버에서 가져오기
    private class GetData extends AsyncTask<String, Void, String>{

//        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(SignUpActivity.this,
//                    "Please Wait", null, true, true);
        }


        @Override//에러가 있는 경우 에러메시지를 보여주고 아니면 JSON을 파싱하여 화면에 보여주는 showResult 메소드를 호출합니다.
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();
            activitySignUpBinding.textViewMainResult.setText(result +"gg");
            Log.d(TAG, "response - " + result);
            activitySignUpBinding.tvIdAlert.setText(result);
            activitySignUpBinding.tvIdAlert.setVisibility(View.VISIBLE);
            if (result.equals("")){//중복되지 않은 데이터
//                btRegister.setEnabled(true);
            }
            if (result == null){

                activitySignUpBinding.textViewMainResult.setText(errorString);
            }
        }

        @Override
        protected String doInBackground(String... params) {


            String serverURL = params[0];
            String user_email = (String)params[1];

            String postParameters = "user_email="+user_email;

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

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    // 문자 바뀐것 인식 : 에딧텍스트에서 문자를 받아올때 바뀌는 것을 알리기위한 클래스
    public void TextChanged(final EditText editText, final TextView textView, final boolean isServer, final String what){
        editText.addTextChangedListener(new TextWatcher() {
            /*
             * @param CharSequence s 현재 에디트 텍스트에 입력된 문자열을 담고 있다.
             * @param int start s에 저장된 문자열 내에 새로 추가될 문자열의 위치값
             * @param count s에 담긴 문자열 가운데 새로 사용자가 입력할 문아열에 의해 변경될 문자열의 수
             * @param int after 새로 추가될 문자열의 수 */
            @Override //글자 변화되기 전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                textView.setVisibility(View.VISIBLE);
//                Toast.makeText(SignUpActivity.this,"beforeTextChanged = "+ s, Toast.LENGTH_LONG).show();
                Log.d(TAG, "beforeTextChanged - 텍스트 : "+s+" start : "+start+" count : "+count+" after : "+after);
            }

            /* @param CharSequence s 사용자가 새로 입력한 문자열을 포함한 에디트 텍스트의 문자열
             * @param int start 새로 추가된 문자열의 시작 위치값
             * @param count 새로 추가된 문자열의 수
             * @param int before 새 문자열 대신 삭제된 기존 문자열의 수 */
            @Override //글자 변화되는 중
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                textView.setVisibility(View.VISIBLE);
//                Toast.makeText(SignUpActivity.this,"onTextChanged = "+ s, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onTextChanged - 텍스트 : "+s+" start : "+start+" count : "+count+" before : "+before);


            }

            @Override //글자 변화된 후 : 한 글자만 변화해도 바뀜
            public void afterTextChanged(Editable s) {

//                Toast.makeText(SignUpActivity.this,"afterTextChanged = "+ s, Toast.LENGTH_LONG).show();
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
                    /*if (what.equals("pwcheck")){
                        if (etUserPw.getText().toString().equals(s)){
                            textView.setText("※일치함 ");
                        }
                    }*/

                    if (isServer && what.equals("")){
                        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(s);
                        if (m.matches()){

                            textView.setVisibility(View.GONE);
                            // 서버에 중복 체크하기
                            GetData gtask = new GetData();//버튼을 클릭하면 검색창에 있는 내용을 아규먼트로하여 GetData AsyncTask를 실행합니다.
                            gtask.execute( StaticValue.MyServer_IP + "/member_query.php", s.toString());

                        }else {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("※이메일을 확인해주세요.");
                        }
                    } else if( !isServer && what.equals("")) {
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
