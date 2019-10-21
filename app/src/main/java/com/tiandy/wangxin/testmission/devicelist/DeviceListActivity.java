package com.tiandy.wangxin.testmission.devicelist;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobile.common.macro.SDKMacro;
import com.mobile.common.po.ChannelNum;
import com.mobile.wiget.business.BusinessController;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.adddevice.AddDeviceActivity;
import com.tiandy.wangxin.testmission.base.BaseActivity;
import com.tiandy.wangxin.testmission.dbhelper.DbGreenDAOHelper;
import com.tiandy.wangxin.testmission.devicedetail.DeviceDetailActivity;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;
import com.tiandy.wangxin.testmission.devicelist.fragment.DeviceDetailFragment;
import com.tiandy.wangxin.testmission.devicesetting.DeviceSettingActivity;
import com.tiandy.wangxin.testmission.util.DoubleClickListener;
import com.tiandy.wangxin.testmission.util.LogonUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.tiandy.wangxin.testmission.util.LogonUtil.clearSelected;
import static com.tiandy.wangxin.testmission.util.LogonUtil.logonDDNSDeviceForVideo;
import static com.tiandy.wangxin.testmission.util.LogonUtil.stopAll;
import static com.tiandy.wangxin.testmission.util.LogonUtil.stopOthers;

public class DeviceListActivity extends BaseActivity implements DeviceListContract.IDeviceListView, BusinessController.MainNotifyListener {

    private DeviceListPresenter mDeviceListPresenter;
    /**
     * 添加新设备
     */
    private AppCompatTextView mTvAddDevice;
    private AppCompatImageView mIvAdd;
    private RecyclerView mRecyclerView;
    List<DeviceInfo> deviceInfoList;
    ArrayList<View> mViews = new ArrayList<>();
    ArrayList<View> containers = new ArrayList<>();
    ArrayList<ViewGroup> mViewGroups = new ArrayList<>();
    private BaseQuickAdapter<DeviceInfo, BaseViewHolder> mDeviceInfoBaseViewHolderBaseQuickAdapter;
    private AppCompatImageView mIvAddBig;
    /**
     * 设备列表
     */
    private TextView mTvDeviceList;
    private int loginFlag;
    private FrameLayout mFrameLayout;
    private boolean canScrollVertically = true;
    private RelativeLayout mRlContent;
    private boolean isOpen = false;
    private ObjectAnimator mSurfaceViewObjectAnimator;
    private ObjectAnimator mTitleAlphaObjectAnimator;
    private ObjectAnimator mFlAlphaObjectAnimator;
    private DeviceDetailFragment mDeviceDetailFragment;
    private ConstraintLayout replayConstraintLayout;
    private DeviceInfo replayDevice;
    private View playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BusinessController.getInstance().initSdkDemo();
        BusinessController.getInstance().setMainNotifyListener(this);// 注册主回调
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        initView();
                        DbGreenDAOHelper.getInstance().initDao(DeviceListActivity.this);
                        mDeviceListPresenter = new DeviceListPresenter(DeviceListActivity.this);
                        mDeviceListPresenter.loadLocalDeviceList();
                    }
                });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mIvAddBig = findViewById(R.id.iv_add_big);
        mTvAddDevice = findViewById(R.id.tv_add_device);
        mIvAdd = findViewById(R.id.iv_add);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRlContent = findViewById(R.id.rl_content);
        mTvDeviceList = findViewById(R.id.tv_device_list);
        mFrameLayout = findViewById(R.id.fl_container);
        mDeviceInfoBaseViewHolderBaseQuickAdapter = new BaseQuickAdapter<DeviceInfo, BaseViewHolder>(R.layout.item_device, deviceInfoList) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void convert(@NonNull final BaseViewHolder helper, final DeviceInfo item) {
                containers.add(helper.itemView);
                helper.setText(R.id.tv_device_name, item.getName());
                helper.addOnClickListener(R.id.tv_setting, R.id.tv_disconnect);
                final ViewGroup constraintLayout = helper.getView(R.id.constraintLayout);
                mViewGroups.add(constraintLayout);
                loginFlag = logonDDNSDeviceForVideo(item);
                item.setLoginFlag(loginFlag);
                mViews.add(helper.getView(R.id.iv_play));
                ChannelNum ddnsChannels = LogonUtil.getDDNSChannels(loginFlag);
                item.setChannelNum(ddnsChannels.num);
                LogUtils.d("loginFlag" + loginFlag);
                LogUtils.d("ddnsChannels" + ddnsChannels.num);
                helper.getView(R.id.tv_disconnect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogonUtil.clearSelected(mViewGroups);
                        if (item.isPlaying()) {
                            for (Integer playFlag : item.getPlayFlags()) {
                                LogUtils.d("PlayFlag" + item.getPlayFlags());
                                int ret = LogonUtil.stopPlay(playFlag);
                            }
                        }
                        helper.setVisible(R.id.iv_play, true);
                    }
                });

                helper.getView(R.id.iv_play).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogonUtil.clearSelected(mViewGroups);
                        stopOthers(deviceInfoList, mViews, (ConstraintLayout) helper.getView(R.id.constraintLayout), helper.getAdapterPosition());
                        helper.setVisible(R.id.iv_play, false);
                        DeviceListActivity.this.playButton = helper.getView(R.id.iv_play);
                        playAllRoute((ConstraintLayout) helper.getView(R.id.constraintLayout), item);
                    }
                });

                helper.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isOpen = true;
                        LogonUtil.clearSelected(mViewGroups);
                        LogUtils.d("helper.getAdapterPosition()" + helper.getAdapterPosition());
                        mRlContent.setVisibility(View.VISIBLE);
                        canScrollVertically = false;
                        int[] location = new int[2];
                        DeviceListActivity.this.playButton = helper.getView(R.id.iv_play);
                        constraintLayout.getLocationOnScreen(location);
                        LogUtils.d("location" + location[0]);
                        LogUtils.d("location" + location[1]);
                        int height = location[1] - LogonUtil.getStatusBarHeight(DeviceListActivity.this);
                        LogUtils.d("getStatusBarheight" + LogonUtil.getStatusBarHeight(DeviceListActivity.this));
                        mSurfaceViewObjectAnimator = ObjectAnimator.ofFloat(mRecyclerView, "translationY", -height);
                        mSurfaceViewObjectAnimator.start();
                        mTitleAlphaObjectAnimator = ObjectAnimator.ofFloat(mRlContent, "alpha", 1f, 0f);
                        mTitleAlphaObjectAnimator.start();

                        mFlAlphaObjectAnimator = ObjectAnimator.ofFloat(mFrameLayout, "alpha", 0.0f, 1.0f);
                        mFlAlphaObjectAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                LogUtils.d("onAnimationStart");
                                mFrameLayout.setVisibility(View.VISIBLE);
                                addFragment();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation, boolean isReverse) {
                                LogUtils.d("onAnimationEnd");
                            }
                        });
                        mFlAlphaObjectAnimator.start();

                    }
                });

                helper.getView(R.id.tv_flashback).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = constraintLayout.getChildCount();
                        int selectedRoute = -1;
                        for (int i = 0; i < count; i++) {
                            if (constraintLayout.getChildAt(i).isSelected()) {
                                selectedRoute = i + 1;
                            }
                        }
                        if (selectedRoute == -1) {
                            return;
                        }

                        clearSelected(mViewGroups);
                        stopAll(deviceInfoList, mViews, (ConstraintLayout) helper.getView(R.id.constraintLayout));
                        DeviceListActivity.this.playButton = helper.getView(R.id.iv_play);
                        Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                        intent.putExtra("deviceInfo", item);
                        intent.putExtra("selectedRoute", selectedRoute);
                        Pair<View, String> pair = new Pair<>(helper.getView(R.id.constraintLayout), ViewCompat.getTransitionName(helper.getView(R.id.constraintLayout)));
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(DeviceListActivity.this, pair);
                        ActivityCompat.startActivityForResult(DeviceListActivity.this, intent, 5000, activityOptionsCompat.toBundle());
                    }
                });

                if (helper.getAdapterPosition() == 0) {
                    helper.setVisible(R.id.iv_play, false);
                    DeviceListActivity.this.playButton = helper.getView(R.id.iv_play);
                    helper.getView(R.id.constraintLayout).setClickable(true);
                    playAllRoute((ConstraintLayout) helper.getView(R.id.constraintLayout), item);
                }
            }
        };
        mDeviceInfoBaseViewHolderBaseQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_setting:
                        Intent bundle = new Intent();
                        bundle.putExtra("deviceInfo", deviceInfoList.get(position));
                        bundle.setClass(DeviceListActivity.this, DeviceSettingActivity.class);
                        startActivityForResult(bundle, 4000);
                        break;
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return canScrollVertically;
            }
        };
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDeviceInfoBaseViewHolderBaseQuickAdapter);
        mDeviceInfoBaseViewHolderBaseQuickAdapter.addFooterView(View.inflate(this, R.layout.item_list_footer, null), 0);

    }


    private void addFragment() {
        LogUtils.d("addFragment");
        mDeviceDetailFragment = new DeviceDetailFragment();
        FragmentUtils.add(getSupportFragmentManager(), mDeviceDetailFragment, R.id.fl_container);
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            if (mSurfaceViewObjectAnimator != null) {
                mSurfaceViewObjectAnimator.reverse();
            }
            if (mTitleAlphaObjectAnimator != null) {
                mTitleAlphaObjectAnimator.reverse();
            }
            if (mFlAlphaObjectAnimator != null) {
                mFlAlphaObjectAnimator.reverse();
            }
            isOpen = false;
            canScrollVertically = true;
            FragmentUtils.remove(mDeviceDetailFragment);
        } else {
            super.onBackPressed();
        }


    }

    private void playAllRoute(final ConstraintLayout viewGroup, final DeviceInfo deviceInfo) {
        this.replayConstraintLayout = viewGroup;
        this.replayDevice = deviceInfo;
        final int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SurfaceView sView = (SurfaceView) viewGroup.getChildAt(i);
            int playFlag = LogonUtil.fillupSurfaceView(sView, deviceInfo.getLoginFlag(), i);
            LogUtils.d("playFlag" + playFlag);
            deviceInfo.getPlayFlags().add(playFlag);
            deviceInfo.setPlaying(true);
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
                        if (deviceInfo.isOpen()) {
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
                    deviceInfo.setOpen(!deviceInfo.isOpen());
                }
            }) {
            });
        }
    }

    @Override
    public void showToast(String message) {

    }


    @Override
    public void refreshList(List<DeviceInfo> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
        if (deviceInfoList != null && deviceInfoList.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mDeviceInfoBaseViewHolderBaseQuickAdapter.setNewData(deviceInfoList);
            mIvAddBig.setVisibility(View.GONE);
            mTvAddDevice.setVisibility(View.GONE);
            mTvDeviceList.setVisibility(View.VISIBLE);
        } else {
            mIvAddBig.setVisibility(View.VISIBLE);
            mTvAddDevice.setVisibility(View.VISIBLE);
            mDeviceInfoBaseViewHolderBaseQuickAdapter.setNewData(null);
            mTvDeviceList.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
//            mDeviceInfoBaseViewHolderBaseQuickAdapter.removeAllHeaderView();
        }
        mDeviceInfoBaseViewHolderBaseQuickAdapter.notifyDataSetChanged();

    }


    public void onAddClick(View view) {
        ActivityUtils.startActivityForResult(this, AddDeviceActivity.class, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5000) {
//            restraintDevice = (DeviceInfo) data.getSerializableExtra("deviceInfo");
//            LogUtils.d("restraintDevice" + restraintDevice);
//            int openRoute = restraintDevice.getOpenRoute();
//            LogUtils.d("openRoute" + openRoute);
//            if (restraintDevice.isOpen() || openRoute != -1) {
//                int childCount = constraintLayout.getChildCount();
//                for (int j = 0; j < childCount; j++) {
//                    if (j != openRoute) {
//                        constraintLayout.getChildAt(j).setVisibility(View.GONE);
//                    }
//                }
//            }
        } else if (resultCode == RESULT_OK) {
            mDeviceListPresenter.loadLocalDeviceList();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (replayConstraintLayout != null && replayDevice != null) {
            playAllRoute(replayConstraintLayout, replayDevice);
            playButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void MainNotifyFun(int i, int i1, String s, int i2, Object o) {
        LogUtils.d("SDKMacro-i=" + i + "----i1=" + i1 + "----i2=" + i2);

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
        for (DeviceInfo deviceInfo : deviceInfoList) {
            ArrayList<Integer> playFlags = deviceInfo.getPlayFlags();
            for (Integer playFlag : playFlags) {
                LogonUtil.stopPlay(playFlag);
            }
            LogonUtil.logoff(deviceInfo.getLoginFlag());
        }

    }
}
