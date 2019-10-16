package com.tiandy.wangxin.testmission.util;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangxin on 2019/10/15 yeah.
 */

public abstract class DoubleClickListener implements View.OnTouchListener {
    private int clickCount = 0;//记录连续点击次数
    private Handler handler;
    private MyClickCallBack myClickCallBack;

    public interface MyClickCallBack {
        void oneClick(View view);//点击一次的回调

        void doubleClick(View view);//连续点击两次的回调

    }


    public DoubleClickListener(MyClickCallBack myClickCallBack) {
        this.myClickCallBack = myClickCallBack;
        handler = new Handler();
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clickCount++;
            int timeout = 400;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (clickCount == 1) {
                        myClickCallBack.oneClick(v);
                    } else if (clickCount == 2) {
                        myClickCallBack.doubleClick(v);
                    }
                    handler.removeCallbacksAndMessages(null);
                    //清空handler延时，并防内存泄漏
                    clickCount = 0;//计数清零
                }
            }, timeout);//延时timeout后执行run方法中的代码
        }
        return false;//让点击事件继续传播，方便再给View添加其他事件监听
    }
}
