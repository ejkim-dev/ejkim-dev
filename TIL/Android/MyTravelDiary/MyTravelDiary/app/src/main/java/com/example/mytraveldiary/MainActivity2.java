package com.example.mytraveldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.Dataset;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.bumptech.glide.Glide;
import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.list.News;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.http.Url;

public class MainActivity2 extends AppCompatActivity {

    public final String weather_id = "weather_id";//API 키
    String CITY = "Seoul";
    int wth_num = 0;//날씨 배열 번호
    private Thread weatherThread;
    String[] cityarr = {"Seoul","Inch’ŏn-gwangyŏksi","Kwach’ŏn","Daegu","Daejeon","Busan","Ulsan","Jeju"};//8개도시
    ArrayList<News> newsAL;

    LinearLayout linear_main2;
    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;
    Button button_5;
    ImageView imageView_home;
    ImageView imageView_menu;
    ImageView iv_wthImg;//날씨 이미지
    ImageButton imagebutton_search;
    BufferedReader br;
    StringBuilder searchResult;
    EditText editText;
    TextView tv_loc;//지역
    TextView tv_tem;//온도
//    TextView tv_hum;//습도
    LinearLayout linear_txt;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Println.println("onCreate 시작");

        InitializeView();
        linear_main2 = (LinearLayout) findViewById(R.id.linear_main2);
        imagebutton_search = (ImageButton) findViewById(R.id.imagebutton_search);
        editText = (EditText) findViewById(R.id.editText);
        iv_wthImg = (ImageView) findViewById(R.id.iv_wthImg);
        tv_loc = (TextView) findViewById(R.id.tv_loc);
        tv_tem = (TextView) findViewById(R.id.tv_tem);
//        tv_hum = (TextView) findViewById(R.id.tv_hum);
        linear_txt=(LinearLayout) findViewById(R.id.linear_txt);

        //리싸이클러뷰
        recyclerView = (RecyclerView) findViewById(R.id.rv_news);
        final NewsAdapter newsAdapter = new NewsAdapter(newsAL);
        recyclerView.setAdapter(newsAdapter);//뉴스어댑터에 연결

        Glide.with(this).load(R.drawable.progress5).into(iv_wthImg);
        Println.println("onCreate 로딩");
/*        tv_loc.setVisibility(View.GONE);
        tv_tem.setVisibility(View.GONE);*/
        linear_txt.setVisibility(View.GONE);
        Println.println("onCreate 텍스트 레이아웃 끄기");

  /*      String imgUrl = "http://openweathermap.org/img/wn/10d@2x.png";
        Glide.with(this).load(imgUrl).into(iv_wthImg);*/
        wth_num = 0;//날씨 배열 번호

        weatherThread = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        sleep(2000);
                    }catch (Exception e){}

                    handler.sendEmptyMessage(0);
                    Println.println("onCreate 날씨 핸들러보내기");
                    wth_num++;
                    if(wth_num == cityarr.length){ wth_num = 0;}
                }
            }
        };
        weatherThread.start();
        Println.println("onCreate 날씨 스래드 끝");

        imagebutton_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Println.println("onCreate 찾기버튼 클릭");
/*
                handler.sendEmptyMessage(1);
                Println.println("onCreate 찾기해서 핸들러에 보내기");
*/
                String searchStr = editText.getText().toString();
//                searchStr = "날씨";//확인용

//                newsAL = new ArrayList<>();
                searchNaver(searchStr);
                Println.println(" searchNaver(searchStr); 완료 > "+newsAL.size()+"개");
                Println.println("리사이클러뷰 시작");
                newsAdapter.setItems(newsAL);
                newsAdapter.notifyDataSetChanged();

            }
        });



    }//onCreate

    class weatherTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + weather_id);
            if(CITY.equals("Jeju")){
                response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat=33.500179&lon=126.503448&units=metric&appid=" + weather_id);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity") + "%";

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String weatherMain = weather.getString("main");

//                String address = jsonObj.getString("name") + ", " + sys.getString("country");
                String address = jsonObj.getString("name");
                linear_txt.setVisibility(View.VISIBLE);

                /* Populating extracted data into our views */
               /*    String[] cityarr = {"Seoul","Incheon","Daegu","Daejeon","Busan","Ulsan","Jeju"};//8개도시*/
                switch (address){
                    case "Seoul" : address = "서울"; break;
                    case "Inch’ŏn-gwangyŏksi" : address = "인천"; break;
                    case "Kwach’ŏn" : address = "과천"; break;
                    case "Daegu" : address = "대구"; break;
                    case "Daejeon" : address = "대전"; break;
                    case "Busan" : address = "부산"; break;
                    case "Ulsan" : address = "울산"; break;
                    case "Jeju" : address = "제주"; break;
                }
         /*       tv_loc.setVisibility(View.VISIBLE);
                tv_tem.setVisibility(View.VISIBLE);*/

                tv_loc.setText(address);
                tv_tem.setText(temp);
//                tv_hum.setText(humidity);
//                iv_wthImg.setImageURI();
                Println.println(weatherDescription);
                Println.println(weatherMain);
                weatherImg(weatherMain);



            } catch (JSONException e) {

            }

        }
    }

    private void getWeatherData( String location ){

        String url = "http://api.openweathermap.org/data/2.5/weather?lat=q"+ location +"weather_id";

    }

    protected void weatherImg(String weatherMain){
   /*     tv_loc.setVisibility(View.VISIBLE);
        tv_tem.setVisibility(View.VISIBLE);*/
        switch (weatherMain){
            case "Clear" :  iv_wthImg.setImageResource(R.drawable.clear_sky);
                break;
            case "Clouds" :   iv_wthImg.setImageResource(R.drawable.few_clouds);
                break;
            case "scattered clouds" :  iv_wthImg.setImageResource(R.drawable.scattered_clouds);
                break;
            case "broken clouds" :  iv_wthImg.setImageResource(R.drawable.broken_clouds);
                break;
            case "Drizzle" :  iv_wthImg.setImageResource(R.drawable.shower_rain);
                break;
            case "Rain" :  iv_wthImg.setImageResource(R.drawable.rain);
                break;
            case "Thunderstorm" :  iv_wthImg.setImageResource(R.drawable.thunderstorm);
                break;
            case "Snow" :  iv_wthImg.setImageResource(R.drawable.snow);
                break;
            case "Mist" :
            case "Smoke" :
            case "Haze" :
            case "Dust" :
            case "Fog" :
            case "Sand" :
            case "Ash" :
            case "Squall" :
            case "Tornado" :
                iv_wthImg.setImageResource(R.drawable.mist);
                break;
        }
    }



    public void searchNaver(final String searchObject) { // 검색어 = searchObject로 ;
        final String clientId = "U_csybH4jfPXP22982ES";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "mYy8fWuscx";//애플리케이션 클라이언트 시크릿값";
        final int display = 20; // 보여지는 검색결과의 수

        // 네트워크 연결은 Thread 생성 필요
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String text = URLEncoder.encode(searchObject, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=" + display + "&"; // json 결과
                    // Json 형태로 결과값을 받아옴.
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();

                    int responseCode = con.getResponseCode();
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");

                    }
                    br.close();
                    con.disconnect();
                    Println.println("확인용 : "+searchResult.toString());//5개 데이터 배열로 묶여있음

                    String data = searchResult.toString();

                    String[] array;
                    array = data.split("\"");
                    String[] title = new String[display];
                    String[] link = new String[display];
                    String[] description = new String[display];
                    String[] bloggername = new String[display];
                    String[] postdate = new String[display];

                    int k = 0;//확인용
                    for (int i = 0; i < array.length; i++) {
//                        Println.println("i값 = "+i+" / k값 = "+k);
                        if (array[i].equals("title"))
                            title[k] = array[i + 2];
//                            Println.println("title "+i+"-"+k+". "+title[k]+"\n");
                        if (array[i].equals("link"))
                            link[k] = array[i + 2];
//                            Println.println("link "+i+"-"+k+". "+link[k]+"\n");
                        if (array[i].equals("description"))
                            description[k] = array[i + 2];
//                            Println.println("description "+i+"-"+k+". "+description[k]+"\n");
                        if (array[i].equals("bloggername"))
                            bloggername[k] = array[i + 2];
//                            Println.println("bloggername "+i+"-"+k+". "+bloggername[k]+"\n");
                        if (array[i].equals("pubDate")) {
                            postdate[k] = array[i + 2];
//                            Println.println("pubDate "+i+"-"+k+". "+postdate[k]+"\n");
                            k++;
                        }
//                        Println.println("\n");
                    }

                    Println.println("title[0]잘나오니: " + title[0]+"\n"+ description[0]+"\n"+  postdate[0]+ "\n"+ link[0]);
                    Println.println("title[1]잘나오니: " + title[1]);
                    Println.println("title[2]잘나오니: " + title[2]);
                    // title[0], link[0], bloggername[0] 등 인덱스 값에 맞게 검색결과를 변수화하였다.

                    newsAL = new ArrayList<>();//news 어레이리스트 만들기
                    for(int i = 0; i < title.length ; i++){
                        String nTitle = title[i].replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                        nTitle = nTitle.replaceAll("&quot;","");
                        nTitle = nTitle.replaceAll("<b>","");
                        nTitle = nTitle.replaceAll("</b>","");

                        String ndescript = description[i].replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                        ndescript = ndescript.replaceAll("&quot;","");//<b></b>
                        ndescript = ndescript.replaceAll("<b>","");
                        ndescript = ndescript.replaceAll("</b>","");

                        newsAL.add(i, new News(nTitle,  ndescript, postdate[i],  link[i]));
                    }

                    Println.println("어레이리스트 잘 들어갔니? 개수 : "+newsAL.size()+"개");

                    /*어레이리스트에 데이터 잘 들어감 확인*/
                    for(int i = 0; i <newsAL.size() ; i++){
                        Println.println("제목 "+i+" : "+newsAL.get(i).getTitle());
                        Println.println("내용 "+i+" : "+newsAL.get(i).getDescription());
                        Println.println("날짜 "+i+" : "+newsAL.get(i).getPubDate());
                        Println.println("링크 "+i+" : "+newsAL.get(i).getLink());
                        Println.println("_________________________________________");
                    }

                } catch (Exception e) {
                    Println.println("error : " + e);
                }

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Println.println("onStart 시작");
    }

    public void InitializeView(){

        imageView_home = (ImageView) findViewById(R.id.imageView_home);
        imageView_menu = (ImageView) findViewById(R.id.imageView_menu);
        imagebutton_search = (ImageButton) findViewById(R.id.imagebutton_search);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
    }

    public void OnClick(View view) {

        /*페이지 전환 intent*/
        Intent intent;
        switch (view.getId()){

            case R.id.imagebutton_search:
                //수정하기
                Toast.makeText(getApplicationContext(), "찾기입니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageView_home:
            case R.id.button_1:
                intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                break;

            case R.id.imageView_menu:
                //수정하기
                intent = new Intent(MainActivity2.this, LoginChoiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                break;

            case R.id.button_2:
                Toast.makeText(getApplicationContext(), "현재 페이지", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_3:

                intent = new Intent(MainActivity2.this, MainActivity3.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;

            case R.id.button_4:

                intent = new Intent(MainActivity2.this, MainActivity4.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;

            case R.id.button_5:
                intent = new Intent(MainActivity2.this, MainActivity5.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }

    }

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            Println.println("handleMessage 시작");
            switch (msg.what){
                case 0 :
                    Println.println("날씨핸들러 시작");
                    CITY = cityarr[wth_num];
                    new weatherTask().execute();
                    break;
                case 1 :
/*                    Println.println("검색핸들러 시작");
                    String searchStr = editText.getText().toString();
                    searchStr = "날씨";//확인용
                    searchNaver(searchStr);*/
                    break;
            }
        }
    };

    private static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        interface OnNewsClickListener {
            void OnNewsClicked(News model);
        }
        
        private OnNewsClickListener mListener;
        
        private ArrayList<News> mItems = new ArrayList<>();

        //기본생성자
        public NewsAdapter(ArrayList<News> newsAL) {
            this.mItems = newsAL;
        }

        //리스너 필요한 생성자

        public void setItems(ArrayList<News> items) {
            this.mItems = items;
            Println.println("크기 : "+mItems.size());
            notifyDataSetChanged();
        }
    
        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_news, parent, false);
            final NewsViewHolder viewHolder = new NewsViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final News item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.OnNewsClicked(item);

                    }
                }
            });
            return viewHolder;
        }
    
        @Override
        public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
            Println.println("뉴스뷰홀더 시작");
            final News news = mItems.get(position);
            holder.title.setText(news.getTitle());
            holder.descript.setText(news.getDescription());
            holder.date.setText(news.getPubDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    String newsUrl = news.getLink();
                    Println.println("newsUrl = "+newsUrl);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl));
//                    Activity context = new Activity();
                    context.startActivity(intent);
                }
            });

        }
    
        @Override
        public int getItemCount() {  return (null != mItems ? mItems.size() : 0) ; }// (null != items ? items.size() : 0)
    
        public static class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView descript;
            TextView date;
            
            public NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tv_title);
                descript = itemView.findViewById(R.id.tv_descript);
                date = itemView.findViewById(R.id.tv_date);
            }
        }
    }

}
