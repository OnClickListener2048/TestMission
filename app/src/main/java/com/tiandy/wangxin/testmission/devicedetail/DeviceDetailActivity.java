package com.tiandy.wangxin.testmission.devicedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.mobile.common.macro.SDKMacro;
import com.mobile.wiget.business.BusinessController;
import com.tiandy.wangxin.testmission.MyApplication;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.base.BaseActivity;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;
import com.tiandy.wangxin.testmission.util.DoubleClickListener;
import com.tiandy.wangxin.testmission.util.LogonUtil;


/**
 * Created by wangxin on 2019/10/15 yeah.
 */

public class DeviceDetailActivity extends BaseActivity implements BusinessController.MainNotifyListener {
    private ConstraintLayout mConstraintLayout;
    private SurfaceView mSurfaceView1;
    private SurfaceView mSurfaceView2;
    private SurfaceView mSurfaceView3;
    private SurfaceView mSurfaceView4;
    private ImageView mTvVolume;
    private ImageView mIvRoutes;
    /**
     * 省流量
     */
    private TextView mTvStream;
    private ImageView mIvFullscreen;
    private RecyclerView mRecyclerView;
    private DeviceInfo mDeviceInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MyApplication.sConstraintLayout);
//        initView();

        BusinessController.getInstance().setMainNotifyListener(this);// 注册主回调
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());


        /**
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTarget(MyApplication.sConstraintLayout);
        getWindow().setSharedElementEnterTransition(transitionSet);
        getWindow().setSharedElementExitTransition(transitionSet);


//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        ArrayList<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            arrayList.add(String.valueOf(i + 1));
//        }
//        mRecyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_route, arrayList) {
//            @Override
//            protected void convert(@NonNull BaseViewHolder helper, String item) {
//
//            }
//        });
////        int loginFlag = logonDDNSDeviceForVideo(mDeviceInfo);
////        LogUtils.d("loginFlag" + loginFlag);
////        mDeviceInfo.setLoginFlag(loginFlag);
////        playAllRoute(mConstraintLayout, mDeviceInfo);
//        BusinessController.getInstance().removeSurfaceView(mSurfaceView2.getId());
//        LogonUtil.fillupSurfaceView(mSurfaceView2, mDeviceInfo.getLoginFlag(), 1);
    }

    private void initView() {
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mSurfaceView1 = findViewById(R.id.surfaceView1);
        mSurfaceView2 = findViewById(R.id.surfaceView2);
        mSurfaceView3 = findViewById(R.id.surfaceView3);
        mSurfaceView4 = findViewById(R.id.surfaceView4);
        mTvVolume = findViewById(R.id.tv_volume);
        mIvRoutes = findViewById(R.id.iv_routes);
        mTvStream = findViewById(R.id.tv_stream);
        mIvFullscreen = findViewById(R.id.iv_fullscreen);
        mRecyclerView = findViewById(R.id.recyclerView);


        mDeviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");
        LogUtils.d(mDeviceInfo);

        int openRoute = mDeviceInfo.getOpenRoute();
        LogUtils.d("openRoute" + openRoute);
        if (mDeviceInfo.isOpen() || openRoute != -1) {
            int childCount = mConstraintLayout.getChildCount();
            for (int j = 0; j < childCount; j++) {
                if (j != openRoute) {
                    mConstraintLayout.getChildAt(j).setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("deviceInfo", mDeviceInfo);
//        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    private void playAllRoute(final ConstraintLayout viewGroup, final DeviceInfo deviceInfo) {
        final int childCount = viewGroup.getChildCount();
        deviceInfo.getPlayFlags().clear();
        for (int i = 0; i < childCount; i++) {
            SurfaceView sView = (SurfaceView) viewGroup.getChildAt(i);
            int playFlag = LogonUtil.fillupSurfaceView(sView, deviceInfo.getLoginFlag(), i);
            deviceInfo.getPlayFlags().add(playFlag);
//                    item.setPlayFlag();
            sView.setSelected(false);

            sView.setOnTouchListener(new DoubleClickListener(new DoubleClickListener.MyClickCallBack() {
                @Override
                public void oneClick(View v) {
                    for (int j = 0; j < childCount; j++) {
                        View view = viewGroup.getChildAt(j);
                        view.setSelected(false);
                    }
                    v.setSelected(true);
                }

                @Override
                public void doubleClick(View v) {
                    for (int j = 0; j < childCount; j++) {
                        View view = viewGroup.getChildAt(j);
                        if (mDeviceInfo.isOpen()) {
                            view.setVisibility(View.VISIBLE);
                            deviceInfo.setOpenRoute(-1);
                        } else {
                            if (view != v) {
                                view.setVisibility(View.GONE);
                            } else {
                                deviceInfo.setOpenRoute(j);
                                view.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    mDeviceInfo.setOpen(!mDeviceInfo.isOpen());
                }
            }) {
            });
        }
    }

    @Override
    public void MainNotifyFun(int i, int i1, String s, int i2, Object o) {
        switch (i1) {
            case SDKMacro.EVENT_LOGIN_SUCCESS: // 登录成功
                Toast.makeText(this, R.string.login_success,
                        Toast.LENGTH_SHORT).show();
                break;

            case SDKMacro.EVENT_LOGIN_FAILED: // 登录失败
                Toast.makeText(this, R.string.login_fail,
                        Toast.LENGTH_SHORT).show();
//                txtPleaseConnect.setText(getResources().getString(
//                        R.string.please_connect));
//                closeVedioPlay();
//                logOff();
//                isLoginSuccess = false;
                break;

            case SDKMacro.EVENT_REALPLAY_STOP:// 播放失败
                Toast.makeText(this, R.string.play_fail, Toast.LENGTH_SHORT)
                        .show();
//                onClickVideoPlay();// 失败后调用播放方法，将状态置回
                break;

            case SDKMacro.EVENT_REALPLAY_START:// 开始播放
                break;

            case SDKMacro.CLIENT_EVENT_REALPLAY_FIRST_FRAME:// 第一帧画面
                Toast.makeText(this, R.string.play_success,
                        Toast.LENGTH_SHORT).show();
                break;

            case SDKMacro.CLIENT_EVENT_TALK_FAILED:// 对讲失败
                Toast.makeText(this, R.string.open_talk_fail,
                        Toast.LENGTH_SHORT).show();
                break;

            case SDKMacro.CLIENT_EVENT_TALK_SUCCESS: // 对讲成功
                // 设置对讲为双向对讲
                Toast.makeText(this, R.string.open_talk_success,
                        Toast.LENGTH_SHORT).show();
//                BusinessController.getInstance().sdkTalkControl(talkFd, 1, 1);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LogUtils.d("onDestroy");
//        ArrayList<Integer> playFlags = mDeviceInfo.getPlayFlags();
//        for (Integer playFlag : playFlags) {
//            LogonUtil.stopPlay(playFlag);
//        }
//        LogonUtil.logoff(mDeviceInfo.getLoginFlag());

    }
}
