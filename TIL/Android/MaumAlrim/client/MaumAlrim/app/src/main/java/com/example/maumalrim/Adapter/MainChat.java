package com.example.maumalrim.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.R;

import java.util.ArrayList;

public class MainChat extends RecyclerView.Adapter<MainChat.ChatViewHolder> {

    private ArrayList<Chat> mDataset;
    String stMyEmail = "";

    //  생성자 : mDataset내용이 10줄이면 리사이클러뷰에서 10줄이 보임, 그 내용을 결정함
    public MainChat(ArrayList<Chat> mDataset, String stMyEmail) {
        this.mDataset = mDataset;
        this.stMyEmail = stMyEmail;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;//view_my_text와 veiw_text 안에 tvChat을 담을 것

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_chat); // 해당 뷰 안에서 id를 찾아옴
        }
    }

    public int getItemViewType(int position){
        //  email이 stMyEmail와 같으면 내 거 : 내 뷰와 상대방 뷰를 구분하기 위해 뷰타입을 구분해준다.
        if (mDataset.get(position).getUserEmail().equals(stMyEmail)){
            return 1;//내거는 1을 반환하고,
        }
        else {
            return 0; // 내거가 아니면 0을 반환
        }

    }

    @Override  //  뷰를 받아와서 실제 아이템들을 어떤 식으로 보여줄 거냐
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //다른 사람 채팅뷰
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_2, parent, false);

        //내 채팅 뷰
        if (viewType == 1){
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_text, parent, false);
        }

        ChatViewHolder chatViewHolder = new ChatViewHolder(v);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position).getUserChattext());
    }

    @Override
    public int getItemCount() {
        return (null != mDataset ? mDataset.size() : 0);
    }
}
