package com.tiandy.wangxin.testmission.devicelist;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
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
import com.tiandy.wangxin.testmission.devicesetting.DeviceSettingActivity;
import com.tiandy.wangxin.testmission.util.DoubleClickListener;
import com.tiandy.wangxin.testmission.util.LogonUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.tiandy.wangxin.testmission.util.LogonUtil.clearSelected;
import static com.tiandy.wangxin.testmission.util.LogonUtil.logoff;
import static com.tiandy.wangxin.testmission.util.LogonUtil.logonDDNSDeviceForVideo;
import static com.tiandy.wangxin.testmission.util.LogonUtil.stopAll;

public class DeviceListActivity extends BaseActivity implements DeviceListContract.IDeviceListView, BusinessController.MainNotifyListener {

    private DeviceListPresenter mDeviceListPresenter;
    /**
     * 添加新设备
     */
    private AppCompatTextView mTvAddDevice;
    private AppCompatImageView mIvAdd;
    private RecyclerView mRecyclerView;
    List<DeviceInfo> deviceInfoList;
    ArrayList<ViewGroup> mViewGroups = new ArrayList<>();
    ArrayList<View> mViews = new ArrayList<>();
    private BaseQuickAdapter<DeviceInfo, BaseViewHolder> mDeviceInfoBaseViewHolderBaseQuickAdapter;
    private AppCompatImageView mIvAddBig;
    /**
     * 设备列表
     */
    private TextView mTvDeviceList;
    private int loginFlag;
    private DeviceInfo restraintDevice;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("restraintDevice" + restraintDevice);
        if (restraintDevice != null) {
            loginFlag = logonDDNSDeviceForVideo(restraintDevice);
            playAllRoute(constraintLayout, restraintDevice);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mIvAddBig = findViewById(R.id.iv_add_big);
        mTvAddDevice = findViewById(R.id.tv_add_device);
        mIvAdd = findViewById(R.id.iv_add);
        mRecyclerView = findViewById(R.id.recyclerView);
        mTvDeviceList = findViewById(R.id.tv_device_list);
        mDeviceInfoBaseViewHolderBaseQuickAdapter = new BaseQuickAdapter<DeviceInfo, BaseViewHolder>(R.layout.item_device, deviceInfoList) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void convert(@NonNull final BaseViewHolder helper, final DeviceInfo item) {
                helper.setText(R.id.tv_device_name, item.getName());
                helper.addOnClickListener(R.id.tv_setting, R.id.tv_disconnect);

                loginFlag = logonDDNSDeviceForVideo(item);
                item.setLoginFlag(loginFlag);
                mViews.add(helper.getView(R.id.iv_play));
                mViewGroups.add((ViewGroup) helper.getView(R.id.constraintLayout));
                ChannelNum ddnsChannels = LogonUtil.getDDNSChannels(loginFlag);
                LogUtils.d("loginFlag" + loginFlag);
                LogUtils.d("ddnsChannels" + ddnsChannels.num);
                helper.getView(R.id.tv_disconnect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Integer playFlag : item.getPlayFlags()) {
                            int ret = LogonUtil.stopPlay(playFlag);
                            LogUtils.d("PlayFlag" + ret);
                        }
                        helper.setVisible(R.id.iv_play, true);
                        clearSelected((ViewGroup) helper.getView(R.id.constraintLayout));
                    }
                });

                helper.getView(R.id.iv_play).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogonUtil.stopAll(deviceInfoList, mViewGroups, mViews);
                        helper.setVisible(R.id.iv_play, false);
                        helper.getView(R.id.constraintLayout).setClickable(true);
                        playAllRoute((ConstraintLayout) helper.getView(R.id.constraintLayout), item);
                    }
                });

                helper.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeviceListActivity.this.restraintDevice = item;
                        DeviceListActivity.this.constraintLayout = helper.getView(R.id.constraintLayout);
//                        stopAll(deviceInfoList, mViewGroups, mViews);
                        helper.setVisible(R.id.iv_play, false);
//                        logoff(item.getLoginFlag());
                        Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                        intent.putExtra("deviceInfo", item);
                        Pair<View, String> pair = new Pair<>(helper.getView(R.id.constraintLayout), ViewCompat.getTransitionName(helper.getView(R.id.constraintLayout)));
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(DeviceListActivity.this, pair);
                        ActivityCompat.startActivityForResult(DeviceListActivity.this, intent, 5000, activityOptionsCompat.toBundle());
                    }
                });

                if (helper.getAdapterPosition() == 0) {
                    helper.setVisible(R.id.iv_play, false);
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
                        bundle.putExtra("deviceInfo", deviceInfoList.get(position));
                        bundle.setClass(DeviceListActivity.this, DeviceSettingActivity.class);
                        startActivityForResult(bundle, 4000);
                        break;
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mDeviceInfoBaseViewHolderBaseQuickAdapter);
        mDeviceInfoBaseViewHolderBaseQuickAdapter.addFooterView(View.inflate(this, R.layout.item_list_footer, null), 0);
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
            restraintDevice = (DeviceInfo) data.getSerializableExtra("deviceInfo");
            LogUtils.d("restraintDevice" + restraintDevice);
            int openRoute = restraintDevice.getOpenRoute();
            LogUtils.d("openRoute" + openRoute);
            if (restraintDevice.isOpen() || openRoute != -1) {
                int childCount = constraintLayout.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    if (j != openRoute) {
                        constraintLayout.getChildAt(j).setVisibility(View.GONE);
                    }
                }
            }
        } else if (resultCode == RESULT_OK) {
            mDeviceListPresenter.loadLocalDeviceList();
        }


    }

    @Override
    public void MainNotifyFun(int i, int i1, String s, int i2, Object o) {
        LogUtils.d("SDKMacro-------"+i1);
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
