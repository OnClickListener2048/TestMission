package com.tiandy.wangxin.testmission.devicesetting;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public interface DeviceSettingContract {

    interface IDeviceSettingModel{
        void deleteDevice(DeviceInfo deviceInfo);
    }

    interface IDeviceSettingView{

        void deleteDeviceSuccess();

        void deleteDeviceFail();
    }

    interface IDeviceSettingPresenter{

        void deleteDevice(DeviceInfo deviceInfo);

        void deleteDeviceSuccess();

        void deleteDeviceFail();
    }
}
