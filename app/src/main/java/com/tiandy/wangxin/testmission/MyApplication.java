package com.tiandy.wangxin.testmission;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.constraint.ConstraintLayout;


/**
 * Created by wangxin on 2019/10/14 yeah.
 */

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static ConstraintLayout sConstraintLayout;

    @Override
    public void onCreate() {
        super.onCreate();
//        Stetho.initializeWithDefaults(this);
    }
}
