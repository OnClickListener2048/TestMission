package com.tiandy.wangxin.testmission.modifydeivce;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.base.BaseActivity;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

import java.io.Serializable;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DeviceModifyActivity extends BaseActivity implements DeviceModifyContract.IDeviceModifyView {

    /**
     * 192.168.1.160
     */
    private EditText mEtIp;
    /**
     * 1234
     */
    private EditText mEtPort;
    /**
     * 2312
     */
    private EditText mEtDeviceName;
    /**
     * 1123
     */
    private EditText mEtUsername;
    /**
     * 3123123
     */
    private EditText mEtPassword;
    /**
     * 确定
     */
    private Button mBtnConfirm;
    private DeviceInfo mDeviceInfo;
    private DeviceModifyContract.IDeviceModifyPresenter mDeviceModifyPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_modify);
        initView();
    }

    public void onConfirmClick(View view) {

        String ip = mEtIp.getText().toString();
        String port = mEtPort.getText().toString();
        String deviceName = mEtDeviceName.getText().toString();
        String userName = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();


        if (mEtIp.getText() == null || "".equals(ip)) {
            ToastUtils.showShort(getString(R.string.enter_ip));
            return;
        }

        if (!RegexUtils.isIP(mEtIp.getText())) {
            ToastUtils.showShort(getString(R.string.enter_real_ip));
            return;
        }

        if (mEtPort.getText() == null || "".equals(port)) {
            ToastUtils.showShort(getString(R.string.enter_port));
            return;
        }
        Integer p = Integer.valueOf(port);
        if (p < 1 || p > 65535) {
            ToastUtils.showShort(getString(R.string.enter_real_port));
            return;
        }

        if (mEtDeviceName.getText() == null || "".equals(deviceName)) {
            ToastUtils.showShort(getString(R.string.enter_name));
            return;
        }

        if (mEtUsername.getText() == null || "".equals(userName)) {
            ToastUtils.showShort(getString(R.string.enter_username));
            return;
        }

        if (mEtPassword.getText() == null || "".equals(password)) {
            ToastUtils.showShort(getString(R.string.enter_password));
            return;
        }

        mDeviceInfo.setKey(ip+port);
        mDeviceInfo.setIp(ip);
        mDeviceInfo.setPort(port);
        mDeviceInfo.setName(deviceName);
        mDeviceInfo.setUserName(userName);
        mDeviceInfo.setPassword(password);

        mDeviceModifyPresenter.updateDevice(mDeviceInfo);
    }

    public void onBackClick(View view) {
        finish();
    }

    private void initView() {

        mDeviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");

        mEtIp = (EditText) findViewById(R.id.et_ip);
        mEtPort = (EditText) findViewById(R.id.et_port);
        mEtDeviceName = (EditText) findViewById(R.id.et_device_name);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

        mEtIp.setText(mDeviceInfo.getIp());
        mEtPort.setText(mDeviceInfo.getPort());
        mEtDeviceName.setText(mDeviceInfo.getName());
        mEtUsername.setText(mDeviceInfo.getUserName());
        mEtPassword.setText(mDeviceInfo.getPassword());

        mDeviceModifyPresenter = new DeviceModifyPresenter(this);

    }

    @Override
    public void updateSuccess() {
        ToastUtils.showShort("更新设备成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void updateFail() {
        ToastUtils.showShort("更新设备失败");
        finish();
    }
}
