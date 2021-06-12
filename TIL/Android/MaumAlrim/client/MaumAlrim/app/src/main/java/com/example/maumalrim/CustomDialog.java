package com.example.maumalrim;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maumalrim.databinding.ActivityCustomDialogBinding;

public class CustomDialog extends Dialog {

    private String mTitle;
    private String mText;

    private View.OnClickListener mNegativeClickListener;
    private View.OnClickListener mPositiveClickListener;
    private ActivityCustomDialogBinding activityCustomDialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityCustomDialogBinding = ActivityCustomDialogBinding.inflate(getLayoutInflater());
        View view = activityCustomDialogBinding.getRoot();

        //다이얼로그 외부화면 흐르게 표현
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(view);

        //제목과 내용을 생성자에서 셋팅한다
        activityCustomDialogBinding.titleTextView.setText(mTitle);
        activityCustomDialogBinding.messageTextView.setText(mText);

        //클릭 이벤트 세팅
        if (mNegativeClickListener != null && mPositiveClickListener != null){
            activityCustomDialogBinding.negativeButton.setOnClickListener(mNegativeClickListener);
            activityCustomDialogBinding.positiveButton.setOnClickListener(mPositiveClickListener);
        } else if (mNegativeClickListener != null && mPositiveClickListener == null){
            activityCustomDialogBinding.negativeButton.setOnClickListener(mNegativeClickListener);
        }else {

        }
    }

    //클릭버튼이 하나일 때 생성자 함수로 클릭이벤트를 받는다.
    public CustomDialog(@NonNull Context context, String title, View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mNegativeClickListener = singleListener;
    }

    //클릭 버튼이 확인과 취소 뒤개일 때 생성자 함수로 이벤트를 받는다.
    public CustomDialog(@NonNull View.OnClickListener context, String title, String text, View.OnClickListener negativeListener, View.OnClickListener positiveListener) {
        super((Context) context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mText = text;
        this.mNegativeClickListener = negativeListener;
        this.mPositiveClickListener = positiveListener;

    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
