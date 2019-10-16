package com.tiandy.wangxin.testmission.devicesetting;

import com.tiandy.wangxin.testmission.dbhelper.DbGreenDAOHelper;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DeviceSettingModel implements DeviceSettingContract.IDeviceSettingModel {

    private final DeviceSettingContract.IDeviceSettingPresenter deviceSettingPresenter;

    public DeviceSettingModel(DeviceSettingContract.IDeviceSettingPresenter deviceSettingPresenter) {
        this.deviceSettingPresenter = deviceSettingPresenter;
    }

    @Override
    public void deleteDevice(DeviceInfo deviceInfo) {
        try {
            DbGreenDAOHelper.getInstance().deleteDevice(deviceInfo);
            deviceSettingPresenter.deleteDeviceSuccess();
        } catch (Exception e) {
            deviceSettingPresenter.deleteDeviceFail();
        }

    }
}
