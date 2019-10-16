package com.tiandy.wangxin.testmission.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.jaeger.library.StatusBarUtil;


/**
 * Created by wangxin on 2019/10/14 yeah.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

//        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setColor(this, Color.BLACK);
    }
}
