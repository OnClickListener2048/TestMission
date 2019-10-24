package com.tiandy.wangxin.testmission.devicedetail;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.util.Map;

/**
 * PROJECT_NAME TestMission
 * 项目名：com.tiandy.wangxin.testmission.devicedetail
 * Created by wangxin on 2019/10/24 16:06.
 *
 * @description
 */
class FlipTransition2 extends Transition {
    private static final String TOP = "top";
    private static final String HEIGHT = "height";

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        Rect rect = new Rect();
        view.getHitRect(rect);

        transitionValues.values.put(TOP, rect.top);
        transitionValues.values.put(HEIGHT, view.getHeight());
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put(TOP, 0);
        transitionValues.values.put(HEIGHT, transitionValues.view.getHeight());

    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }

        final View startView = startValues.view;
        final View endView = endValues.view;
        Map<String, Object> startMap = startValues.values;
        Map<String, Object> endMap = endValues.values;
        LogUtils.d(endMap.get(TOP));
        LogUtils.d(startMap.get(TOP));


        ObjectAnimator scaleX = ObjectAnimator.ofFloat(startView, "scaleX", 1, 0.6f, 1);//从原始状态放大2倍再回到原始状态
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(startView, "scaleY", 1, 0.6f, 1);
        ObjectAnimator startRotationY = ObjectAnimator.ofFloat(startView, "rotationY", 180, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        startRotationY.setStartDelay(200);

        animatorSet.playTogether(scaleX,scaleY,startRotationY);



        return animatorSet;

    }
}
