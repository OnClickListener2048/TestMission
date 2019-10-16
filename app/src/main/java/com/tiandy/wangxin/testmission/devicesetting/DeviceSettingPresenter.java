package com.tiandy.wangxin.testmission.devicesetting;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DeviceSettingPresenter implements DeviceSettingContract.IDeviceSettingPresenter {
    private final DeviceSettingContract.IDeviceSettingView deviceSettingView;
    private final DeviceSettingModel mDeviceSettingModel;

    public DeviceSettingPresenter(DeviceSettingContract.IDeviceSettingView deviceSettingView) {
        this.deviceSettingView = deviceSettingView;
        mDeviceSettingModel = new DeviceSettingModel(this);
    }

    @Override
    public void deleteDevice(DeviceInfo deviceInfo) {
        mDeviceSettingModel.deleteDevice(deviceInfo);
    }

    @Override
    public void deleteDeviceSuccess() {
        deviceSettingView.deleteDeviceSuccess();
    }

    @Override
    public void deleteDeviceFail() {
        deviceSettingView.deleteDeviceFail();
    }
}
