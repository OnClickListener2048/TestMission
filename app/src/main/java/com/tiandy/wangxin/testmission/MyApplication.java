package com.tiandy.wangxin.testmission;

import android.app.Application;
import android.content.Context;
import android.transition.TransitionSet;


/**
 * Created by wangxin on 2019/10/14 yeah.
 */

public class MyApplication extends Application {


    public static int duration = 800;
    public static int order = TransitionSet.ORDERING_TOGETHER;



    /**
     * @param a
     * @return a
     * @author wangxin
     * @time 209/10/23  10:03
    * @description
     */


    /**
     * @param
     * @return a
     * @author wangxin     * @time2019/10/23  10:32     * @description a
     */
    @Override
    public void onCreate() {
        /**
         * @author wangxin
         * @time 2019/10/23  10:03
         * @param []
         * @return void
         * @description
         */
        super.onCreate();
//        Stetho.initializeWithDefaults(this);
    }



    /**
     * @author wangxin
     * @time 2019/10/23  10:05
     * @param [base]
     * @return void
     * @description attachBaseContext
     */
    /**
     *
     * @param base
     */


    /**
     * @param base
     * @return a
     * @author wangxin
     * @time 2019/10/23 10:11
     * @dscription
     */
    @Override
    protected void attachBaseContext(Context base) {
        /**
         * @author wangxin
         * @time 2019/10/23  10:05
         * @param [base]
         * @return void
         * @description attachBaseContext
         */
        super.attachBaseContext(base);
    }
}
