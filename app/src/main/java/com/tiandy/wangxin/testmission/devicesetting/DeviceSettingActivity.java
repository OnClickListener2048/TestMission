package com.tiandy.wangxin.testmission.devicesetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.base.BaseActivity;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;
import com.tiandy.wangxin.testmission.modifydeivce.DeviceModifyActivity;

import java.io.Serializable;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DeviceSettingActivity extends BaseActivity implements DeviceSettingContract.IDeviceSettingView {

    private TextView mTvDeviceName;
    private RelativeLayout mRlDeviceModify;
    /**
     * 删除设备
     */
    private TextView mTvDeleteDevice;
    private DeviceSettingPresenter mDeviceSettingPresenter;
    private DeviceInfo mDeviceInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");

        setContentView(R.layout.activity_device_setting);
        initView();


    }


    private void initView() {
        mTvDeviceName = (TextView) findViewById(R.id.tv_device_name);
        mRlDeviceModify = (RelativeLayout) findViewById(R.id.rl_device_modify);
        mTvDeleteDevice = (TextView) findViewById(R.id.tv_delete_device);
        mTvDeviceName.setText(mDeviceInfo.getName());

        mDeviceSettingPresenter = new DeviceSettingPresenter(this);
    }

    public void onDeviceDelete(View view) {
        if (mDeviceInfo != null) {
            mDeviceSettingPresenter.deleteDevice(mDeviceInfo);
        }

    }

    public void onDeviceModify(View view) {
        Intent intent = new Intent();
        intent.setClass(this, DeviceModifyActivity.class);
        intent.putExtra("deviceInfo", mDeviceInfo);
        startActivityForResult(intent, 3000);
    }

    public void onBackClick(View view) {
        finish();
    }

    @Override
    public void deleteDeviceSuccess() {
        ToastUtils.showShort("删除设备成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void deleteDeviceFail() {
        ToastUtils.showShort("删除设备失败");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
