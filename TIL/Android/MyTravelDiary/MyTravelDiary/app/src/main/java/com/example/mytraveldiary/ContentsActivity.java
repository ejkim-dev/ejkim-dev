package com.example.mytraveldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytraveldiary.adepters.AlbumContentsAdapter;
import com.example.mytraveldiary.list.AlbumContents;

import java.util.ArrayList;

public class ContentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AlbumContentsAdapter adapter;

    ImageView iv_back;//뒤로가기 버튼(MainActivity5)
    TextView tv_private;//공개/비공개 설정
    TextView tv_albumEdit;//앨범 편집

    Uri uri = null;
    int arr_index;//앨범 수정할 때 사용하는 위치 값
    ArrayList<AlbumContents> ac_items = new ArrayList<>();//ArrayList로 저장하기 위해 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        /*입력 데이터 위치*/
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_private = (TextView) findViewById(R.id.tv_private);
        tv_albumEdit = (TextView) findViewById(R.id.tv_albumEdit);
        recyclerView =  (RecyclerView)  findViewById(R.id.recyclerView);

        //back버튼 누르면 뒤로(MainActivity5)감
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentsActivity.this, MainActivity5.class);
                startActivity(intent);
            }
        });

        //리싸이클러뷰에 레이아웃 매니저 설정하기 : 수평으로 배치
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //여기에 어댑터를 달아줌 : Activity는 이 최초값을 몽땅 다 받아와서 여기 어댑터에 전달만 함
        adapter = new AlbumContentsAdapter(ac_items);
        recyclerView.setAdapter(adapter);

//        adapter.setOnItemClickListener();

    }
}
