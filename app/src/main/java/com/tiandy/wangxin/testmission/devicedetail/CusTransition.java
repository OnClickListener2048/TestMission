package com.tiandy.wangxin.testmission.devicedetail;

import android.animation.Animator;
import android.transition.ChangeTransform;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

/**
 * PROJECT_NAME TestMission
 * 项目名：com.tiandy.wangxin.testmission.devicedetail
 * Created by wangxin on 2019/10/24 13:41.
 *
 * @description
 */
public class CusTransition extends ChangeTransform {
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {

        View start = startValues.view;
        View end = endValues.view;
        LogUtils.d(start);
        LogUtils.d(end);

        return super.createAnimator(sceneRoot, startValues, endValues);

    }
}
