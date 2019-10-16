package com.tiandy.wangxin.testmission.modifydeivce;

import com.tiandy.wangxin.testmission.dbhelper.DbGreenDAOHelper;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DeviceModifyModel implements DeviceModifyContract.IDeviceModifyModel {
    private final DeviceModifyContract.IDeviceModifyPresenter deviceModifyPresenter;

    public DeviceModifyModel(DeviceModifyContract.IDeviceModifyPresenter deviceModifyPresenter) {
        this.deviceModifyPresenter = deviceModifyPresenter;
    }

    @Override
    public void updateDevice(DeviceInfo deviceInfo) {
        try {
            DbGreenDAOHelper.getInstance().updateDevice(deviceInfo);
            deviceModifyPresenter.updateSuccess();
        } catch (Exception e) {
            deviceModifyPresenter.updateFail();
        }


    }
}
