// Generated by view binder compiler. Do not edit!
package com.example.maumalrim.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.example.maumalrim.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEmployeeSignUpBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btCert;

  @NonNull
  public final Button btRegister;

  @NonNull
  public final EditText etNickname;

  @NonNull
  public final EditText etPhoneCert;

  @NonNull
  public final EditText etPhoneId;

  @NonNull
  public final EditText etPwCheck;

  @NonNull
  public final EditText etUserPw;

  @NonNull
  public final LinearLayout llDebug;

  @NonNull
  public final LinearLayout llPhone;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final TextView textViewMainDebug;

  @NonNull
  public final TextView textViewMainResult;

  @NonNull
  public final TextView tvIdAlert;

  @NonNull
  public final TextView tvNickAlert;

  @NonNull
  public final TextView tvPwAlert;

  @NonNull
  public final TextView tvPwCheckAlert;

  @NonNull
  public final TextView tvTitle;

  private ActivityEmployeeSignUpBinding(@NonNull ConstraintLayout rootView, @NonNull Button btCert,
      @NonNull Button btRegister, @NonNull EditText etNickname, @NonNull EditText etPhoneCert,
      @NonNull EditText etPhoneId, @NonNull EditText etPwCheck, @NonNull EditText etUserPw,
      @NonNull LinearLayout llDebug, @NonNull LinearLayout llPhone,
      @NonNull ProgressBar progressBar, @NonNull TextView textViewMainDebug,
      @NonNull TextView textViewMainResult, @NonNull TextView tvIdAlert,
      @NonNull TextView tvNickAlert, @NonNull TextView tvPwAlert, @NonNull TextView tvPwCheckAlert,
      @NonNull TextView tvTitle) {
    this.rootView = rootView;
    this.btCert = btCert;
    this.btRegister = btRegister;
    this.etNickname = etNickname;
    this.etPhoneCert = etPhoneCert;
    this.etPhoneId = etPhoneId;
    this.etPwCheck = etPwCheck;
    this.etUserPw = etUserPw;
    this.llDebug = llDebug;
    this.llPhone = llPhone;
    this.progressBar = progressBar;
    this.textViewMainDebug = textViewMainDebug;
    this.textViewMainResult = textViewMainResult;
    this.tvIdAlert = tvIdAlert;
    this.tvNickAlert = tvNickAlert;
    this.tvPwAlert = tvPwAlert;
    this.tvPwCheckAlert = tvPwCheckAlert;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEmployeeSignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEmployeeSignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_employee_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEmployeeSignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      Button btCert = rootView.findViewById(R.id.bt_cert);
      if (btCert == null) {
        missingId = "btCert";
        break missingId;
      }
      Button btRegister = rootView.findViewById(R.id.bt_register);
      if (btRegister == null) {
        missingId = "btRegister";
        break missingId;
      }
      EditText etNickname = rootView.findViewById(R.id.et_nickname);
      if (etNickname == null) {
        missingId = "etNickname";
        break missingId;
      }
      EditText etPhoneCert = rootView.findViewById(R.id.et_phone_cert);
      if (etPhoneCert == null) {
        missingId = "etPhoneCert";
        break missingId;
      }
      EditText etPhoneId = rootView.findViewById(R.id.et_phone_id);
      if (etPhoneId == null) {
        missingId = "etPhoneId";
        break missingId;
      }
      EditText etPwCheck = rootView.findViewById(R.id.et_pw_check);
      if (etPwCheck == null) {
        missingId = "etPwCheck";
        break missingId;
      }
      EditText etUserPw = rootView.findViewById(R.id.et_user_pw);
      if (etUserPw == null) {
        missingId = "etUserPw";
        break missingId;
      }
      LinearLayout llDebug = rootView.findViewById(R.id.ll_debug);
      if (llDebug == null) {
        missingId = "llDebug";
        break missingId;
      }
      LinearLayout llPhone = rootView.findViewById(R.id.ll_phone);
      if (llPhone == null) {
        missingId = "llPhone";
        break missingId;
      }
      ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
      if (progressBar == null) {
        missingId = "progressBar";
        break missingId;
      }
      TextView textViewMainDebug = rootView.findViewById(R.id.textView_main_debug);
      if (textViewMainDebug == null) {
        missingId = "textViewMainDebug";
        break missingId;
      }
      TextView textViewMainResult = rootView.findViewById(R.id.textView_main_result);
      if (textViewMainResult == null) {
        missingId = "textViewMainResult";
        break missingId;
      }
      TextView tvIdAlert = rootView.findViewById(R.id.tv_id_alert);
      if (tvIdAlert == null) {
        missingId = "tvIdAlert";
        break missingId;
      }
      TextView tvNickAlert = rootView.findViewById(R.id.tv_nick_alert);
      if (tvNickAlert == null) {
        missingId = "tvNickAlert";
        break missingId;
      }
      TextView tvPwAlert = rootView.findViewById(R.id.tv_pw_alert);
      if (tvPwAlert == null) {
        missingId = "tvPwAlert";
        break missingId;
      }
      TextView tvPwCheckAlert = rootView.findViewById(R.id.tv_pw_check_alert);
      if (tvPwCheckAlert == null) {
        missingId = "tvPwCheckAlert";
        break missingId;
      }
      TextView tvTitle = rootView.findViewById(R.id.tv_title);
      if (tvTitle == null) {
        missingId = "tvTitle";
        break missingId;
      }
      return new ActivityEmployeeSignUpBinding((ConstraintLayout) rootView, btCert, btRegister,
          etNickname, etPhoneCert, etPhoneId, etPwCheck, etUserPw, llDebug, llPhone, progressBar,
          textViewMainDebug, textViewMainResult, tvIdAlert, tvNickAlert, tvPwAlert, tvPwCheckAlert,
          tvTitle);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
