package com.example.mytraveldiary;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//리스트 데이터들이 들어갈 것을 선언해야함 : 메인사진과 제목
public class MyAlbumAdapter extends RecyclerView.Adapter<MyAlbumAdapter.ViewHolder> implements OnAlbumItemClickListener {

    //리스트뷰의 아이템들의 답을 어레이리스트로 만들예정, MyAlbum을 가져 올 예정
   static ArrayList<MyAlbum> items = new ArrayList<MyAlbum>();
    OnAlbumItemClickListener listener;

    public MyAlbumAdapter(ArrayList<MyAlbum> items){
        this.items = items;
    }


    @NonNull
    @Override//onCreateViewHolder : 리스트뷰 값이 처음으로 생성될때의 생명주기를 뜻함
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.album_item, viewGroup, false);//인플레이션을 통해 뷰 객체 만들기


        return new ViewHolder(itemView, this);//뷰홀더 객체를 생성하면서 뷰 객체를 전달하고 그 뷰홀더 객체를 반환하기;
    }

    @Override//onBindViewHolder 실제 추가될 때에 대한 생명주기; 메서드는 뷰홀더가 재사용될 때 호출되고, 뷰 객체는 기존 것을 그대로 사용하고 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        MyAlbum item = items.get(position);
        viewHolder.setItem(item);

    }

    @Override//getItemCount : 어댑터가 관리하는 아이템의 개수를 반환함, 이 메서드에서  전체 아이템이 몇개(return items.size())인지 확인하고 그 값을 반환
    public int getItemCount() { return (null != items ? items.size() : 0); }

    public void remove(int position){
        try {
            items.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e){ e.printStackTrace();}
    }

    //소스 코드에서 어댑터에 MyAlbum 객체를 넣거나 가져갈 수 있도록 addItem(), setItems(), getItem(), setItem() 메서드를 추가한다
    public void addItem(MyAlbum item) { items.add(item); }

    public void setItems(ArrayList<MyAlbum> items) { this.items = items; }

    public MyAlbum getItem(int position) { return items.get(position); }//position = index 값과 비슷한 느낌, 위치 번호

    public void setItem(int position, MyAlbum item) { items.set(position, item); }

    public void setOnItemClickListener(OnAlbumItemClickListener listener) { this.listener = listener; }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    //ViewHolder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageView_albumMainImg;
        TextView textView_albumTitle;

        public ViewHolder(View itemView, final OnAlbumItemClickListener listener) {
            super(itemView);

            //이곳은 Activity형태의 클래스 파일이 아니기때문에 itemView를 그대로 받아옴
            imageView_albumMainImg = (ImageView) itemView.findViewById(R.id.imageView_albumMainImg);
            textView_albumTitle = (TextView) itemView.findViewById(R.id.textView_albumTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){ listener.onItemClick(ViewHolder.this, v, position ); }
                }
            });
        }
        //현재 뷰홀더에 들어있는 뷰 객체의 데이터를 다른 것으로 보이도록 하는 역할을 함
        public void setItem(MyAlbum item) {
            imageView_albumMainImg.setImageURI(Uri.parse(item.getTitleImgUri()));
            textView_albumTitle.setText(item.title);
        }
    }
}
