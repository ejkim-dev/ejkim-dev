package com.example.maumalrim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maumalrim.Common.StaticValue;
import com.example.maumalrim.Item.EmployeeInfo;
import com.example.maumalrim.Item.UserList;
import com.example.maumalrim.SharedPreference.Information;
import com.example.maumalrim.databinding.ActivityEmployeeMainBinding;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static boolean isActivityStart = false;

    private static final String TAG = "EmployeeMainActivity";
    ActivityEmployeeMainBinding activityEmployeeMainBinding;
    Intent foregroundServiceIntent;
    UserListAdapter userListAdapter;
//    List<UserList> userLists;

    //상담자 대기리스트는 서버에 저장하고, 최초 로그인했을 때만 서버에서 리스트를 불러온다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {//여기다가 Activity 생성시 필요한 코드들을 작성
        /*onCreate는 Acitivity가 처음 실행 되는 상태에 제일 먼저 호출되는 메소드로
        여기에 실행시 필요한 각종 초기화 작업을 적어준다.
        기본적으로 내가 메소드를 정의하지 않더라도 안스에서 Acitivity를 생성하면 자동으로 생성된다.*/
        super.onCreate(savedInstanceState);
        isActivityStart = true;
        Log.d(TAG, "onCreate: 들어옴 | isActivityStart = "+isActivityStart);
        activityEmployeeMainBinding = ActivityEmployeeMainBinding.inflate(getLayoutInflater());
        View view = activityEmployeeMainBinding.getRoot();
        setContentView(view);

        activityEmployeeMainBinding.rcUserState.setHasFixedSize(true);
        activityEmployeeMainBinding.rcUserState.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        userListAdapter = new UserListAdapter(new UserListAdapter.OnUserListClickListener() {
            @Override
            public void onUserLisetClicked(UserList model) {

                if (model.getType().equals("퇴장")||model.getType().equals("상담종료")){
                    Toast.makeText(getApplicationContext(), "선택한 내담자는 ["+model.getType()+"] 상태 입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (model.getType().equals("상담중")){
                    Toast.makeText(getApplicationContext(), "상담중인 내담자 입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                StaticValue.recipientId = model.getUserId();//받는사람 id를 저장함

                Toast.makeText(getApplicationContext(), model.getUserNickName()+"님과 상담 채팅방 입니다.",Toast.LENGTH_SHORT).show();

                //상담사 메세지를 서버로 보내야함
                Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
                ArrayList<EmployeeInfo> employeeInfos = Information.getArr(getApplicationContext());
                serviceIntent.putExtra("myMsg", "[알림] "+ employeeInfos.get(0).getUser_nickname()+"상담사와 연결되었습니다.");//보낼때 아이디랑 닉네임도 보내야함

                startForegroundService(serviceIntent);

                Intent intent = new Intent(getApplicationContext(), MainChatActivity.class);

                //내담자 메세지 보내기
                intent.putExtra("name", model.getUserNickName());
                intent.putExtra("category",model.getUserCategory());
                intent.putExtra("message", model.getUserMessage());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

                //유저리스트에서 찾아서 '상담중'으로 바꾸기
/*                Log.d(TAG, "onUserLisetClicked: 상담할 id 찾기");

                for (int i = StaticValue.userLists.size()-1; i >= 0; i--){
                    //상담할 id와 같은 id를 찾음
                    Log.d(TAG, "onUserLisetClicked: 상담할 id와 같은 id를 찾음 | 현재 id = "+StaticValue.userLists.get(i).getUserId()+" | 찾는 id = "+StaticValue.recipientId);
                    if (StaticValue.userLists.get(i).getUserId().equals(StaticValue.recipientId)){

                        Log.d(TAG, "onUserLisetClicked: 해당하는 id 찾아서 들어옴");
                        String userNickName = StaticValue.userLists.get(i).getUserNickName();
                        String userCategory = StaticValue.userLists.get(i).getUserCategory();
                        String userTime = StaticValue.userLists.get(i).getStartTime();
                        String senderId = StaticValue.userLists.get(i).getUserId();
                        String userMessage = StaticValue.userLists.get(i).getUserMessage();
                        String finishTime = StaticValue.userLists.get(i).getFinishTime();

                        StaticValue.userLists.set(i, new UserList("상담중", senderId, userNickName, userCategory,userMessage,userTime,finishTime));

                        Log.d(TAG, "onUserLisetClicked: 적용 완료 : 타입 - "+StaticValue.userLists.get(i).getType()+" | id - "+StaticValue.userLists.get(i).getUserId()
                                +" | 닉네임 - "+StaticValue.userLists.get(i).getUserNickName());
                        Log.d(TAG, "onUserLisetClicked: 카테고리 - "+StaticValue.userLists.get(i).getUserCategory()+
                                " | 시작시간 - "+StaticValue.userLists.get(i).getStartTime()+" | 종료시간 - "+StaticValue.userLists.get(i).getFinishTime());
                        break;
                    }

                    //현재 유저리스트 확인하기
                    for (int i1 = 0; i1 < StaticValue.userLists.size(); i1++){
                        Log.d(TAG, "receive: 유저리스트 "+(i+1)+" : "+StaticValue.userLists.get(i).toString());
                    }
                }*/

                finish();

            }
        });

        activityEmployeeMainBinding.rcUserState.setAdapter(userListAdapter);
        activityEmployeeMainBinding.cbxSearch.setOnCheckedChangeListener(this);


        //https://salix97.tistory.com/218 리사이클러뷰 새로고침 추가하기
//        StaticValue.userLists = new ArrayList<>();

        //MyService에서 String[유저ID||닉네임||상담내용||현재시간] 형태로 받아온다.
        // 구분자(||)를 기준으로 상담내용과 현재시간을 나누고,
        //현재시간(String)을 date로 파싱한다.
//        StaticValue.userLists.add(new UserList("a","홍길동","기타상담","09:28"));
//        StaticValue.userLists.add(new UserList("b","김철수","가족상담","09:56"));
        userListAdapter.setItems(StaticValue.userLists);

    }

    @Override
    protected void onStart() {//각종 리스너 등록
        /*보통 회원가입 등이 필요한 기능에서 리스너 객체 등을
        onCreate에서 선언하고 onStart에서 선언된 리스너를 등록한 후,
        이미 로그인 된 사용자인지를 구분하여 로그인 화면으로 넘어가지 않고
        바로 메인으로 넘어가게 할 때 사용한다.
        브로드캐스트리시버를 사용할 때도 보통 여기다가 등록한다!
        onStop과 짝을 이루고 다루게 된다.*/
        super.onStart();
        //처음 앱 실행
        isActivityStart = true;

        Log.d(TAG, "onStart: 들어옴 |  isActivityStart = "+isActivityStart);
        if (MyService.serviceIntent == null){

            foregroundServiceIntent = new Intent(this, MyService.class);
            Log.d(TAG, "onStart: foregroundServiceIntent = "+foregroundServiceIntent.toString());

            startForegroundService(foregroundServiceIntent);
            Toast.makeText(getApplicationContext(), "채팅 서비스 시작", Toast.LENGTH_LONG).show();

        }else {//포그라운드 실행중일 때

            foregroundServiceIntent = MyService.serviceIntent;
            Log.d(TAG, "onStart: foregroundServiceIntent = "+foregroundServiceIntent.toString());
            Toast.makeText(getApplicationContext(), "채팅 서비스 중", Toast.LENGTH_LONG).show();
        }

        //내담자가 없을 경우 뜨는 메세지
        if (StaticValue.userLists.size() == 0){
            activityEmployeeMainBinding.tvNoUserMsg.setVisibility(View.VISIBLE);
            activityEmployeeMainBinding.cbxSearch.setVisibility(View.GONE);
        }
        else {
            activityEmployeeMainBinding.tvNoUserMsg.setVisibility(View.GONE);
            activityEmployeeMainBinding.cbxSearch.setVisibility(View.VISIBLE);
        }


    }

    @Override //사용자에게 보여질 데이터 등 가져오기
    protected void onResume() {
        /*onResume는 생명주기상 onStart 다음에 호출된다.
        안드로이드 Document엔 사용자와 상호작용이 가능할 때 호출이 된다고 나와있다.
        onPause와 더불어 onCreate 다음으로 가장 많이 사용되는 메소드이다.
        액티비티는 사용자에 요구사항에 따라 계속해서 변화하게 된다.
        onCreate와 onStart가 사용자와 상호작용 하기 전과 직전에 일어난다면
        onResume는 직접적으로 사용자의 터치 이벤트나 Toast등이 동작할 수 있을 때 호출된다고 생각된다.*/
        super.onResume();
        isActivityStart = true;
        Log.d(TAG, "onResume: 실행 | isActivityStart ="+isActivityStart);

    }

    @Override // onNewIntent() 를 호출 하게 된다. 서비스 -> 액티비티에서 확인하는경우.
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: 들어옴");


        //받은 메세지 처리하기, 메시지 받을때 유저 ID도 같이 받아야함

        if (intent != null){

            userListAdapter.setItems(StaticValue.userLists);

            //리사이클러뷰에 적용
            activityEmployeeMainBinding.rcUserState.setAdapter(userListAdapter);
            //어뎁터에 알려준다
            userListAdapter.notifyDataSetChanged();



        }
    }

    public void onIconClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                Log.d(TAG, "onClick: 이 엑티비티 열기 전 화면으로 돌리기 위해서");
                finish();

                return;
            case R.id.img_mypage:
                Log.d(TAG, "onClick: 마이페이지로 간다");
                Intent intent = new Intent(getApplicationContext(), EmployeeMypageActivity.class);
                startActivity(intent);
//                finish();
                return;
        }
    }


    @Override
    protected void onStop() {//사용자에게 액티비티가 보여지지 않을 때 호출된다.
        /*onStop는 생명주기상 액티비티가 보여지지 않을 때 호출된다. onStart와 짝을 이루며, onPause와 비슷해 보이는데 onPause보다 나중에 호출된다.
        가끔은 onPause와 onStop 중에 어디다 코드를 작성 해야할지 고민이 될 때가 있다. (그럴 땐 일단 구글링부터!)
        보통 객체의 null 체크 후에 값이 있을 경우 자원을 해제할 때 사용한다.
        데이터베이스에 정보를 쓰는 작업과 같이 규모가 크고 CPU를 많이 사용하는 종료 작업을 수행하는 경우 onStop()을 사용해야 함.*/
        super.onStop();
        isActivityStart = false;
        Log.d(TAG, "onStop: 실행 | isActivityStart = "+isActivityStart);

    }

    @Override
    protected void onDestroy() {//액티비티가 소멸될 때 호출할 메소드등 선언됨
        /*마지막으로 자원을 정리하거나 액티비티 소멸시 간단한 노티피케이션 등을 띄울때 사용할 수 있다.
        *  위 onStop()에선 stop만 했다면, onDestroy에서는 shutdown()메소드 까지 호출하여 완전히 자원을 해제해준다.*/
        super.onDestroy();
        isActivityStart = false;
        Log.d(TAG, "onDestroy: 실행 | isActivityStart = "+isActivityStart);

    }

    @Override
    protected void onPause() { //사용자에게 보여지지 않을 때 임시로 뭔가 저장하거나 자원 해제 등 작성
        /*onPause는 생명주기상 사용자에게 보여지지 않을 때 호출된다.
        onResume과 짝을 이루며, 사용자가 Home 버튼을 클릭해서 액티비티가 보여지지 않거나
        다른 액티비티가 보여질 경우, 각종 View나 데이터들을 임시로 저장해야할 필요가 있을 때가 있다.
        이 때 Application Context를 사용해서 저장하거나 Preference를 통해서 값을 저장하게 되는데,
        이러한 코드들을 여기에 작성 하면된다.
        또한, 카메라를 사용하거나 위치 정보 등을 사용할 경우 리스너나 자원 등을 해제할 때도 onPause 밑에 작성해주면 되겠다.*/
        super.onPause();
//        isActivityStart = false;
        Log.d(TAG, "onPause: 실행 | isActivityStart = "+isActivityStart);

        //내담자가 없을 경우 뜨는 메세지
        if (StaticValue.userLists.size() == 0){
            activityEmployeeMainBinding.tvNoUserMsg.setVisibility(View.VISIBLE);
            activityEmployeeMainBinding.cbxSearch.setVisibility(View.GONE);
        }
        else {
            activityEmployeeMainBinding.tvNoUserMsg.setVisibility(View.GONE);
            activityEmployeeMainBinding.cbxSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 체크박스를 클릭해서 상태가 바꾸었을 경우 호출되는 콜백 메서드
        if (activityEmployeeMainBinding.cbxSearch.isChecked()){
            //체크했을 때 리사이클러뷰에서 대기중인 내담자만 보여야함
            Log.d(TAG, "onCheckedChanged: 체크박스 선택");

            ArrayList<UserList> userListArrayList = new ArrayList<>();

            for (int i=0; i < StaticValue.userLists.size(); i++){
                
                if (StaticValue.userLists.get(i).getType().equals("상담중")||StaticValue.userLists.get(i).getType().equals("상담종료")||StaticValue.userLists.get(i).getType().equals("퇴장")){
                    
                }else {
                    String type = StaticValue.userLists.get(i).getType();
                    String id = StaticValue.userLists.get(i).getUserId();
                    String name = StaticValue.userLists.get(i).getUserNickName();
                    String category = StaticValue.userLists.get(i).getUserCategory();
                    String text = StaticValue.userLists.get(i).getUserMessage();
                    String start = StaticValue.userLists.get(i).getStartTime();
                    String end = StaticValue.userLists.get(i).getFinishTime();

                    Log.d(TAG, "onCheckedChanged: 상태 = "+type+" | id = "+id+" | name = "+name+" | category = "+category+" | text = "+text+" | start = "+start+" | end = "+end);
                    userListArrayList.add(new UserList(type, id, name, category,text, start,end));

                }
            }
            if (userListArrayList.size() == 0){
                activityEmployeeMainBinding.tvNoUserMsg.setVisibility(View.VISIBLE);
            }
            userListAdapter.setItems(userListArrayList);


        }else {
            //체크 안했을 때
            Log.d(TAG, "onCheckedChanged: 체크박스 해제");
            if (StaticValue.userLists.size() > 0) {
                activityEmployeeMainBinding.tvNoUserMsg.setVisibility(View.GONE);
            }
            userListAdapter.setItems(StaticValue.userLists);
        }
    }


    private static class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
        interface OnUserListClickListener {
            void onUserLisetClicked(UserList model);
        }

        private OnUserListClickListener mListener;

        private List<UserList> mItems = new ArrayList<>();

        //기본 생성자
        public UserListAdapter() {}

        //리스너 필요한 생성자
        public UserListAdapter(OnUserListClickListener listener) {
            mListener = listener;
        }

        public void setItems(List<UserList> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }
//
//        @Override
//        public void onItemSwipe(int)


        @NonNull
        @Override
        public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_list, parent, false);
            final UserListViewHolder viewHolder = new UserListViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final UserList item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.onUserLisetClicked(item);
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
            UserList item = mItems.get(position);
            if (item.getType().equals("")){
                holder.tvUserName.setText(item.getUserNickName());
                holder.tvUserMsg.setText(item.getUserCategory());
                holder.tvTime.setText(item.getStartTime());
            }
            else {
                holder.tvUserName.setText("("+item.getType()+") "+item.getUserNickName());
                holder.tvUserMsg.setText(item.getUserCategory());
                if (item.getFinishTime().equals("")){
                    holder.tvTime.setText(item.getStartTime());

                }else {
                    holder.tvTime.setText(item.getStartTime()+" - "+item.getFinishTime());
                }
            }
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class UserListViewHolder extends RecyclerView.ViewHolder {
            TextView tvUserName;
            TextView tvUserMsg;
            TextView tvTime;

            public UserListViewHolder(@NonNull View itemView) {
                super(itemView);
                tvUserName = itemView.findViewById(R.id.tv_user_name);
                tvUserMsg = itemView.findViewById(R.id.tv_user_msg);
                tvTime = itemView.findViewById(R.id.tv_time);
            }
        }
    }
}
