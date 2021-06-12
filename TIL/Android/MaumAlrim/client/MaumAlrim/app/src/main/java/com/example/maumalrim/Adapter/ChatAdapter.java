package com.example.maumalrim.Adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maumalrim.ChatbotActivity;
import com.example.maumalrim.Item.Chat;
import com.example.maumalrim.R;

import java.util.ArrayList;

/*내가 보낸 것과 상대방이 보낸것의 뷰를 구분하기 위한 어댑터*/
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final String TAG = "ChatAdapter";
    private ArrayList<Chat> mDataset;
    String stMyEmail = "";
    private OnItemClickListener mListener  = null;//리스너 객체 참조를 저장하는 변수


    //  생성자 : mDataset내용이 10줄이면 리사이클러뷰에서 10줄이 보임, 그 내용을 결정함
    public ChatAdapter(ArrayList<Chat> mDataset, String stMyEmail) {
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
    public class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;//view_my_text와 veiw_text 안에 tvChat을 담을 것
        public TextView tvName;
        private ImageView ivCheck;

        public ChatViewHolder(@NonNull final View itemView, final int viewType) {
            super(itemView);
            Log.d(TAG, "5. ChatViewHolder: 아이템뷰 : "+itemView.toString()+" / viewType - "+viewType);
            textView = itemView.findViewById(R.id.tv_chat); // 해당 뷰 안에서 id를 찾아옴
            if (viewType == 0){//내 뷰가 아닐때
                tvName = itemView.findViewById(R.id.tv_name);
            }

            else if (viewType == 2 && ChatbotActivity.isConsultationReception) {
                Log.d(TAG, "ChatViewHolder:  isConsultationReception = "+ChatbotActivity.isConsultationReception);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                   Log.d(TAG, "onClick: 아이템 뷰타입 - "+viewType);
                        int pos = getAdapterPosition();
                        Log.d(TAG, "onClick: ChatAdapter 클릭 위치 : " + pos);
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mListener != null) {
                                // 리스너 객체 전달 메서드와 변수 추가.

                                mListener.onItemClick(v, pos);
                                Log.d(TAG, "onClick: 클릭리스너 pos = " + pos);
                                ivCheck = itemView.findViewById(R.id.iv_check);
                                Log.d(TAG, "onClick: 이미지뷰 선언 pos = " + pos);
//                                    ivCheck.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onClick: 이미지뷰 보이기 pos = " + pos);
//                                    textView.setTextColor(Color.BLACK);
                                Log.d(TAG, "onClick: 텍스트뷰 색 바꾸기 pos = " + pos);

                                Log.d(TAG, "onClick: textView = " + textView.getText().toString());
                                Log.d(TAG, "onClick: pos = " + pos);

//                                StaticValue.chatArrayList.add(new Chat(mDataset.get(pos).getUserChattext(), stMyEmail));

                            }
                        }
                    }
                });
            }
            /*else {
                Log.d(TAG, "ChatViewHolder:  isConsultationReception = "+ChatbotActivity.isConsultationReception);
                textView.setEnabled(false);
                Log.d(TAG, "ChatViewHolder: 버튼 비활성화");
            }*/
        }
    }

    //두번째
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

    //세번째
    @Override  //  뷰를 받아와서 실제 아이템들을 어떤 식으로 보여줄 거냐
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "4. onCreateViewHolder: ViewGroup - "+parent.toString()+" / viewType - "+viewType);

        //다른 사람 채팅뷰
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);

        //내 채팅 뷰
        if (viewType == 1){
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_text, parent, false);
        }
        else if (viewType == 2){
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        }

        ChatViewHolder chatViewHolder = new ChatViewHolder(v, viewType);
        Log.d(TAG, "6. onCreateViewHolder: ChatViewHolder에 뷰(내 채팅 or 다른 사람 채팅) 적용");
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Log.d(TAG, "7. onBindViewHolder: ChatViewHolder - "+holder.toString()+" / position - "+position);
        holder.textView.setText(mDataset.get(position).getUserChattext());
        Log.d(TAG, "8. onBindViewHolder: textView - "+mDataset.get(position).getUserChattext());
        Log.d(TAG, "9. onBindViewHolder: holder.getItemViewType() - "+holder.getItemViewType());
        Log.d(TAG, "10. onBindViewHolder: ID : "+mDataset.get(position).getUserEmail());
        //다른 사람 채팅창일 때
/*        if (holder.getItemViewType()==2 && checkOpetion==1){
            //NullPointerException : MyService.onStartCommand(MyService.java:63)
//            holder.tvName.setText(mDataset.get(position).getUserEmail());
//            StaticValue.chatArrayList.add(new Chat(mDataset.get(position).getUserChattext(), stMyEmail));
            Log.d(TAG, "onClick: 데이터셋 추가");
            checkOpetion++;
        }*/

        if (holder.getItemViewType()==2&&!ChatbotActivity.isConsultationReception){
            Log.d(TAG, "onBindViewHolder: 클릭가능한 버튼 = "+ChatbotActivity.isConsultationReception);

            holder.textView.setEnabled(ChatbotActivity.isConsultationReception);
            holder.textView.setOnClickListener(null);
//            holder.textView.setClickable(ChatbotActivity.isConsultationReception);


        }

    }

    //가장 먼저 시작
    @Override
    public int getItemCount() {
        Log.d(TAG, "1. getItemCount: 데이터셋 크기 : "+mDataset.size());//왜 2번씩 부르지?
        return (null != mDataset ? mDataset.size() : 0);
    }
}
