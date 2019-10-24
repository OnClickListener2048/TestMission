package com.tiandy.wangxin.testmission.devicedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tiandy.wangxin.testmission.MyApplication;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.base.BaseActivity;

/**
 * PROJECT_NAME TestMission
 * 项目名：com.tiandy.wangxin.testmission.devicedetail
 * Created by wangxin on 2019/10/24 15:12.
 *
 * @description
 */
public class AnimatorActivity extends BaseActivity {

    private EditText mEtDuration;
    /**
     * 同时播放
     */
    private RadioButton mSequential;
    /**
     * 顺序播放
     */
    private RadioButton mTogether;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animator);
        initView();
        mEtDuration.setText(String.valueOf(MyApplication.duration));
    }

    private void initView() {
        mEtDuration = findViewById(R.id.et_duration);
        mSequential = findViewById(R.id.sequential);
        mTogether = findViewById(R.id.together);
        RadioGroup radioGroup = findViewById(R.id.rb);

        if (MyApplication.order == TransitionSet.ORDERING_SEQUENTIAL) {
            mSequential.toggle();
        } else {
            mTogether.toggle();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.sequential) {
                    MyApplication.order = TransitionSet.ORDERING_SEQUENTIAL;
                }
                if (checkedId==R.id.together) {
                    MyApplication.order = TransitionSet.ORDERING_TOGETHER;
                }
            }
        });


        mEtDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    MyApplication.duration = Integer.valueOf(String.valueOf(s));
                }

            }
        });
    }
}
