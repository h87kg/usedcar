package com.jason.usedcar.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.usedcar.R;
import com.jason.usedcar.interfaces.IJobListener;
import com.jason.usedcar.model.User;
import com.jason.usedcar.presenter.UserRegisterFragmentPresenter;
import com.jason.usedcar.presenter.UserRegisterFragmentPresenter.UserRegisterFragmentUi;

public class UserRegisterFragment
        extends
        BaseFragment<UserRegisterFragmentPresenter, UserRegisterFragmentPresenter.UserRegisterFragmentUi>
        implements UserRegisterFragmentPresenter.UserRegisterFragmentUi {
    private static final String TAG = "UserRegisterFragment";

    private EditText mEditPhone;
    private EditText mEditValidateCode;
    private EditText mEditPwd;
    private EditText mEditRePwd;
    private CheckBox mChkAcceptRule;
    private Button mBtnRegister;
    private Button mBtnObtainCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_register_layout, container, false);
        mEditPhone = (EditText) rootView.findViewById(R.id.edit_register_phone);
        mEditValidateCode = (EditText) rootView.findViewById(R.id.edit_validate_code);
        mEditPwd = (EditText) rootView.findViewById(R.id.edit_pwd);
        mEditRePwd = (EditText) rootView.findViewById(R.id.edit_repwd);
        mChkAcceptRule = (CheckBox) rootView.findViewById(R.id.chk_accept);
        mBtnRegister = (Button) rootView.findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(mOnClickListener);
        mBtnObtainCode = (Button) rootView.findViewById(R.id.btn_obtain_code);
        mBtnObtainCode.setOnClickListener(mOnBtnObtainCodeClickListener);
        return rootView;
    }

    private OnClickListener mOnBtnObtainCodeClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getPresenter().obtainValidateCode(getActivity(), mEditPhone.getText().toString(),
                    "1234");
        }
    };
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            User user = new User();
            user.setPhone(mEditPhone.getText().toString());
            user.setPhoneVerifyCode(mEditValidateCode.getText().toString());
            user.setPassword(mEditPwd.getText().toString());
            user.setRepassword(mEditRePwd.getText().toString());
            user.setAcceptTerm(mChkAcceptRule.isChecked());
            user.setNickname("nickname");
            user.setAccountType(1);
            getPresenter().registerUser(getActivity().getApplicationContext(), user);
        }
    };

    @Override
    public UserRegisterFragmentPresenter createPresenter() {
        return new UserRegisterFragmentPresenter();
    }

    @Override
    public UserRegisterFragmentUi getUi() {
        return this;
    }

    @Override
    public void onUserRegistered() {
        Log.d(TAG, "onUserRegistered");
        Toast.makeText(getActivity(), "onUserRegistered", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVerifyCodeObtained(String code) {
        Log.d(TAG, "onVerifyCodeObtained code is : " + code);
        mEditValidateCode.setText(code);
    }
}
