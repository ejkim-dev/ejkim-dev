package com.example.maumalrim.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.R;

import java.util.ArrayList;

public class ChatDailyAdapter extends RecyclerView.Adapter<ChatDailyAdapter.ChatDailyViewHolder> {

    private static final String TAG = "ChatDailyAdapter";
    private ArrayList<Chat> mDataset;
    String stMyEmail = "";
    private OnItemClickListener mListener  = null;//리스너 객체 참조를 저장하는 변수

    public ChatDailyAdapter(ArrayList<Chat> mDataset, String stMyEmail) {
        this.mDataset = mDataset;
        this.stMyEmail = stMyEmail;
    }

    // 어댑터 내에서 커스텀 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public int getItemViewType(int position){
        //  email이 stMyEmail와 같으면 내 거 : 내 뷰와 상대방 뷰를 구분하기 위해 뷰타입을 구분해준다.
        Log.d(TAG, "2. getItemViewType: position - "+position);
        if (mDataset.get(position).getUserEmail().equals(stMyEmail)){

            Log.d(TAG, "3. getItemViewType: 내 이메일과 일치");
            return 1;//내거는 1을 반환하고,
        }
        else if (mDataset.get(position).getUserEmail().equals("유형옵션")){
            //유형 뷰타입 하나 더 추가
            Log.d(TAG, "3. getItemViewType: 유형옵션");
            return 2;
        }
        else {
            Log.d(TAG, "3. getItemViewType: 내 이메일과 불일치");
            return 0; // 내거가 아니면 0을 반환
        }

    }

    @NonNull
    @Override
    public ChatDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //다른 사람 채팅뷰
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatbot_daily, parent, false);

        //내 채팅 뷰
        if (viewType == 1){
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_text, parent, false);
        }
        else if (viewType == 2){
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_2, parent, false);
        }

        ChatDailyViewHolder chatDailyViewHolder = new ChatDailyViewHolder(v, viewType);
        return chatDailyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDailyViewHolder holder, int position) {

        Log.d(TAG, "7. onBindViewHolder: ChatViewHolder - "+holder.toString()+" / position - "+position);
        holder.textView.setText(mDataset.get(position).getUserChattext());
        Log.d(TAG, "8. onBindViewHolder: textView - "+mDataset.get(position).getUserChattext());
        Log.d(TAG, "9. onBindViewHolder: holder.getItemViewType() - "+holder.getItemViewType());
        Log.d(TAG, "10. onBindViewHolder: ID : "+mDataset.get(position).getUserEmail());

    }

    @Override
    public int getItemCount() {
        return (null != mDataset ? mDataset.size() : 0);
    }

    public class ChatDailyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;//view_my_text와 veiw_text 안에 tvChat을 담을 것
//        public TextView tvName;

        public ChatDailyViewHolder(@NonNull View itemView, final int viewType) {
            super(itemView);

            Log.d(TAG, "5. ChatViewHolder: 아이템뷰 : "+itemView.toString()+" / viewType - "+viewType);
            textView = itemView.findViewById(R.id.tv_chat); // 해당 뷰 안에서 id를 찾아옴
/*            if (viewType == 0){//내 뷰가 아닐때
                tvName = itemView.findViewById(R.id.tv_name);
            }*/

            if (viewType == 2) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = getAdapterPosition();
                        Log.d(TAG, "onClick: ChatAdapter 클릭 위치 : " + pos);
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mListener != null) {
                                // 리스너 객체 전달 메서드와 변수 추가.

                                mListener.onItemClick(v, pos);
                                Log.d(TAG, "onClick: 클릭리스너 pos = " + pos);

                                Log.d(TAG, "onClick: textView = " + textView.getText().toString());
                                Log.d(TAG, "onClick: pos = " + pos);


                            }
                        }
                    }
                });
            }
        }
    }
}
