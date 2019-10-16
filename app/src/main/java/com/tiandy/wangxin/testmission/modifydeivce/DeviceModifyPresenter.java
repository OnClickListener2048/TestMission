package com.tiandy.wangxin.testmission.modifydeivce;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DeviceModifyPresenter implements DeviceModifyContract.IDeviceModifyPresenter {
    private final DeviceModifyContract.IDeviceModifyView deviceModifyView;
    private final DeviceModifyContract.IDeviceModifyModel mDeviceModifyModel;

    public DeviceModifyPresenter(DeviceModifyContract.IDeviceModifyView deviceModifyView) {
        this.deviceModifyView = deviceModifyView;
        mDeviceModifyModel = new DeviceModifyModel(this);

    }

    @Override
    public void updateDevice(DeviceInfo deviceInfo) {
        mDeviceModifyModel.updateDevice(deviceInfo);
    }

    @Override
    public void updateSuccess() {
        deviceModifyView.updateSuccess();
    }

    @Override
    public void updateFail() {
        deviceModifyView.updateFail();
    }
}
