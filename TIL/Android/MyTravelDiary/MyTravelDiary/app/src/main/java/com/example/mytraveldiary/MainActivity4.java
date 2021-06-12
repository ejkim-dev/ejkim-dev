package com.example.mytraveldiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytraveldiary.Tag.Println;
import com.example.mytraveldiary.list.Game;

import java.util.ArrayList;
import java.util.Random;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class MainActivity4 extends AppCompatActivity {

//    public static ArrayList<String> pass = new ArrayList<>();

    public static String[] pass = {"1",	"2",	"3",	"4",	"5",	"6",	"7",	"8",	"9",	"10",	"11",	"12",	"13",	"14",	"15",	"16",	"17",	"18",	"19",	"20",	"21",	"22",	"23",	"24",	"25"};
    private Thread gameMainThread;
    public static boolean gamePlay = false;
    public static int  score = 0;

    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;
    Button button_5;
    ImageView imageView_home;
    ImageView imageView_menu;
    public static  TextView tv_timer;//타이머

    private Context mContext;
    private int stuck = 10;

    public static long startTime = 0;//시작할때 0부터
    public static Button btn_start;//게임 시작
    public static Button btn_stop;//게임 종료(멈추기)
    Button btn_kakao;//개발자 후원하기
    public static int gameLv = 1;
    public static int size = 5;//버튼의 개수

    private String[] mDataset = new String[size];

    public static String timer;

    Handler gameHandler = new Handler();
    public static Handler timerHandler = new Handler();
    public static Runnable timerRunable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tv_timer.setText(String.format("%02d : %02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);//0.5초뒤에 실행
            timer = tv_timer.getText().toString();
        }
    };//시간을 세는 스래드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        InitializeView();
        Println.println("게임의 OnCreate");

//        size*=gameLv;

        mContext = this;


        //버튼 상태 세팅
        btn_kakao = (Button) findViewById(R.id.btn_kakao);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        tv_timer = (TextView) findViewById(R.id.tv_timer);

        //버튼 상태 세팅
        btn_start.setEnabled(true);
        btn_stop.setEnabled(false);
        Println.println("동작했니?");
//        btn_stop.setVisibility(View.GONE);
//        Println.println("GONE?");
/*테이블레이아웃*/
   /*     for(int i = 0; i < 8; i++){
            final TableRow tableRow = new TableRow(MainActivity4.this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            tableRow.setBackgroundColor(Color.GRAY);

            for(int j = 0; j<5; j++){
//                final TextView text = new TextView(MainActivity4.this);
//                text.setText(i + j);
           *//*     text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
                text.setText(i+""+j+"");

                text.setGravity(Gravity.CENTER);
                text.setTextSize(20);
                text.setHeight(90);
                tableRow.addView(text);*//*

                String id = i+""+j+"";
                int intID = Integer.parseInt(id);
                final Button button = new Button(MainActivity4.this);
                button.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 90));
                button.setText(id);
                button.setGravity(Gravity.CENTER);
                button.setId(intID);
                tableRow.addView(button);
            }
            table_game.addView(tableRow);
        }

        final Button button = new Button(MainActivity4.this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                }
            }
        });
*/


        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, "application id");

        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 결제호출
                BootUser bootUser = new BootUser().setPhone("010-1234-5678");
                BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

                Bootpay.init(getFragmentManager())
                        .setApplicationId( "application id" ) // 해당 프로젝트(안드로이드)의 application id 값
//                .setPG(PG.KAKAO) // 결제할 PG 사
//                        .setMethod(Method.EASY) // 결제수단
                        .setContext(mContext)
                        .setBootUser(bootUser)
                        .setBootExtra(bootExtra)
                        .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                        .setName("개발자 후원") // 결제할 상품명
                        .setOrderId("1234") // 결제 고유번호expire_month
                        .setPrice(1000) // 결제할 금액
//                        .addItem("개발자후원금", 1, "ITEM", 1000) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                        .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                        .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                            @Override
                            public void onConfirm(@Nullable String message) {

                                if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                                else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                Log.d("confirm", message);
                            }
                        })
                        .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                            @Override
                            public void onDone(@Nullable String message) {
                                Log.d("done", message);
                            }
                        })
                        .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                            @Override
                            public void onReady(@Nullable String message) {
                                Log.d("ready", message);
                            }
                        })
                        .onCancel(new CancelListener() { // 결제 취소시 호출
                            @Override
                            public void onCancel(@Nullable String message) {

                                Log.d("cancel", message);
                            }
                        })
                        .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                            @Override
                            public void onError(@Nullable String message) {
                                Log.d("error", message);
                            }
                        })
                        .onClose(
                                new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                    @Override
                                    public void onClose(String message) {
                                        Log.d("close", "close");
                                    }
                                })
                        .request();
            }
        });


        //게임 시작
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //시작하기
                Println.println("시작버튼");
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunable, 0);//0초부터 시작
                btn_start.setEnabled(false);
                btn_stop.setEnabled(true);
                gamePlay = true;

                Toast.makeText(mContext, gameLv+"단계 시작!",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity4.this, GameLoadingActivity.class);
                intent.putExtra("start", true);
                startActivityForResult(intent, 0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
//                recyclerView.setAdapter(gameAdapter);
/*                mDataset = new String[25];
                for(int i = 0 ; i < mDataset.length ; i++){
                    mDataset[i] = i+1+"";
                }

                List<Game> games = new ArrayList<>();
                for(int i = 0 ; i < mDataset.length; i++){
                    games.add(new Game(mDataset[i]));
                }*/

//                gameAdapter.setItems(games);
//                GameAdapter.notifyDataSetChanged();//데이터가 바뀐 것을 리싸이클러뷰가 알게해서 데이터가 바뀜
                Println.println("게임상황 : "+gamePlay);

            }
        });

        Println.println("게임시작 확인");
        //게임 종료
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunable);
                gamePlay = false;
                btn_start.setEnabled(true);
                btn_stop.setEnabled(false);
                Println.println("게임상황 : "+gamePlay);
                Intent intent = new Intent(MainActivity4.this, GameLoadingActivity.class);
                startActivityForResult(intent, 0);
/*                Intent intent = new Intent(MainActivity4.this, GameLoadingActivity.class);
                intent.putExtra("start", false);
                startActivityForResult(intent, 1);*/
            }
        });
        Println.println("게임종료 확인");

      /*  final List<Game> finalGames = games;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                gameAdapter.setItems(finalGames);
            }
        };
        gameHandler.post(r);

        Println.println("--------------------------------------------------------------");
        Println.println("크기 : "+games.size());
        for(int i = 0; i < games.size(); i++){
            Println.println("games "+i+" = "+games.get(i));
        }*/

    }//onCreate

/*    Handler gamehandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0: break;
                case 1: break;
            }
        }
    };*/

 /*   //리사이클러뷰 클래스
    public static class GameUI*/

    //어댑터
    public static class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
        interface OnGameClickListner {
            void onGameClicked(Game model);
        }

        public OnGameClickListner mListener;
        public static ArrayList<Game> mItems = new ArrayList<>();

        //생성자1
        public GameAdapter() {}

        //생성자2 : 리스너 필요
        public GameAdapter(OnGameClickListner listener) {
            mListener = listener;
        }
    
        public void setItems(ArrayList<Game> items) { //item 갈아치고 notify 다시 그리는 거
            this.mItems = items;
            notifyDataSetChanged();
        }
        public void remove(int position){
            try{
                this.mItems.remove(position);
                notifyItemRemoved(position);

            }catch (IndexOutOfBoundsException e){}
        }
    
        @NonNull
        @Override
        public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_game, parent, false);
            final GameViewHolder viewHolder = new GameViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final Game item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.onGameClicked(item);
                    }

                }
            });
            return viewHolder;
        }
    
        @Override
        public void onBindViewHolder(@NonNull final GameViewHolder holder, int position) {
            Game item = mItems.get(position);
            Println.println("Game 클래스 메모리 할당");
            holder.num_text.setText(item.getRanNum());
            Println.println("item.getRanNum() = "+item.getRanNum());

            holder.num_text.setBackgroundColor(item.getBackColor());
            Println.println("item.getBackColor() = "+item.getBackColor());

            holder.itemView.setTag(position);
            Println.println("position() = "+position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Println.println("아이템 클릭");
                    try{
                        Println.println("클릭 조건문 실행");

                        String gamestr = (score+1)+"";

                        if(holder.num_text.getText().toString().equals(gamestr)){
                            Toast.makeText(v.getContext(), "OK", Toast.LENGTH_SHORT).show();

                            holder.num_text.setText("★");
                            holder.num_text.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                            score++;
                        }
                        if(score == size){
                            score = 0; gamePlay =false;      gameLv++;
                            Println.println("score == 25");
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("게임 결과").setMessage("소요시간 : " + timer);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btn_start.setEnabled(true);
                                    btn_stop.setEnabled(false);
                                    timerHandler.removeCallbacks(timerRunable);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        Println.println("클릭 조건문 완료 : score = "+score);

                    }catch (ArrayIndexOutOfBoundsException e){
                        Println.println("오류 catch");
                        score = 0; gamePlay =false;
                        Println.println("다시 세팅");
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("게임 결과").setMessage("소요시간 : " + timer);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        timerHandler.removeCallbacks(timerRunable);
                    }
                }
            });
        }
    
        @Override
        public int getItemCount() {
            return mItems.size();
        }
    
        public static class GameViewHolder extends RecyclerView.ViewHolder {
//            Button btn_num;
            TextView num_text;
            
            public GameViewHolder(@NonNull View itemView) {
                super(itemView);
                num_text = itemView.findViewById(R.id.num_text);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        size = 5;

        Println.println("게임의 onStart / 게임상황 : "+gamePlay+" / score = "+score+" / size = "+size+" / gameLv = "+gameLv);
        size*=gameLv;
        Println.println( "   size*=gameLv = "+  size);

        Random rd = new Random();

        int mygreen = ContextCompat.getColor(this, R.color.colorPrimary);
        int myDarkGreen = ContextCompat.getColor(this, R.color.colorPrimaryDark);

        //리싸이클러뷰
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_game);
        final GameAdapter gameAdapter = new GameAdapter(new GameAdapter.OnGameClickListner(){
            @Override
            public void onGameClicked(Game model) {
                Println.println("onStart에 있는 클릭리스너");
                Toast.makeText(MainActivity4.this, model.getRanNum(), Toast.LENGTH_SHORT).show();
               /* if(model.getRanNum().equals(){
                    Toast.makeText(MainActivity4.this, "1을 선택했어요!", Toast.LENGTH_SHORT).show();
                }*/

            }
        });


        //데이터 : thread 로 만들기
        recyclerView.setAdapter(gameAdapter);//리사이클러뷰에 어댑터 설정; 어댑터 연결함
        Println.println("리사이클러뷰 어뎁터에 연결");
        ArrayList<Game> games = new ArrayList<>();
        Println.println("games 어레이리스트 선언");

        if(gamePlay){
            Println.println("게임 시작시 들어오는 조건문");
            games = new ArrayList<>();
            Println.println("games 어레이리스트 다시 선언");
            score = 0;
            ArrayList<String> val = new ArrayList<>();//어레이리스트 size만큼 값 넣기
            for(int i = 0; i<size; i++){
                val.add((i+1)+"");
            }

            for(int i = 0 ; i < size; i++){
                Println.println("게임실행");

                int valRan = rd.nextInt(val.size());
                String value = val.get(valRan);

                if(val.size() > 1){val.remove(valRan);}

                games.add(new Game(value, myDarkGreen));

            }  gameAdapter.setItems(games);
        }
        else {
            Println.println("게임 종료시 들어오는 조건문");
            games = new ArrayList<>();
            Println.println("games 종료 어레이리스트 다시 선언");
            for(int i = 0 ; i < size; i++){
                String value = "★";
                games.add(new Game(value, mygreen));
            }  gameAdapter.setItems(games);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Println.println("onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Println.println("onDestroy");
        timerHandler.removeCallbacks(timerRunable);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Println.println("onStop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Println.println("onPause");

        overridePendingTransition(0, 0);//화면 finish될 때 애니메이션 끄기
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Println.println("결과 받는 곳");
        if(requestCode == 0){
            Println.println("requestCode == 0");
            if(resultCode == RESULT_OK){
                Println.println(" RESULT_OK");

                boolean startGame = data.getBooleanExtra("start",true);
                Println.println(" startGame = "+ startGame);
                gamePlay = startGame;
                Println.println(" gamePlay = "+ gamePlay);

            }
            if(resultCode == RESULT_CANCELED){
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

    public void OnClick(View view) {
        /*페이지 전환 intent*/
        Intent intent;
        switch (view.getId()){
            case R.id.imageView_home:
            case R.id.button_1:
                intent = new Intent(MainActivity4.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

            case R.id.imageView_menu:
                intent = new Intent(MainActivity4.this, LoginChoiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//애니메이션효과취소
                startActivity(intent);
                finish();
                break;

            case R.id.button_2:
                intent = new Intent(MainActivity4.this, MainActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_3:
                intent = new Intent(MainActivity4.this, MainActivity3.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

            case R.id.button_4:
                Toast.makeText(getApplicationContext(), "현재 페이지", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_5:
                intent = new Intent(MainActivity4.this, MainActivity5.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
        }
    }
}
