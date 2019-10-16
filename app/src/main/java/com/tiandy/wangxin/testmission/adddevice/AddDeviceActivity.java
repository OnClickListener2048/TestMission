package com.tiandy.wangxin.testmission.adddevice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.base.BaseActivity;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class AddDeviceActivity extends BaseActivity implements AddDeviceContract.IAddDeviceView {


    /**
     * 请输入IP
     */
    private EditText mEtIp;
    /**
     * 请输入端口号（1-65535）
     */
    private EditText mEtPort;
    /**
     * 请输入自定义名称
     */
    private EditText mEtDeviceName;
    /**
     * 请输入设备登录用户名
     */
    private EditText mEtUsername;
    /**
     * 请输入设备登录密码
     */
    private EditText mEtPassword;
    private AddDevicePresenter mAddDevicePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initView();


    }

    public void onBackClick(View view) {
        finish();
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

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setKey(ip+port);
        deviceInfo.setIp(ip);
        deviceInfo.setPort(port);
        deviceInfo.setName(deviceName);
        deviceInfo.setUserName(userName);
        deviceInfo.setPassword(password);

        mAddDevicePresenter.addDevice(deviceInfo);
    }

    private void initView() {
        mEtIp = (EditText) findViewById(R.id.et_ip);
        mEtPort = (EditText) findViewById(R.id.et_port);
        mEtDeviceName = (EditText) findViewById(R.id.et_device_name);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);

        mAddDevicePresenter = new AddDevicePresenter(this);

    }

    @Override
    public void addDeviceSuccess() {
        ToastUtils.showShort("添加设备成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void addDeviceFail() {
        ToastUtils.showShort("添加设备失败");
        finish();
    }

    @Override
    public void addDeviceDuplicated() {
        ToastUtils.showShort("重复添加设备");
    }
}
