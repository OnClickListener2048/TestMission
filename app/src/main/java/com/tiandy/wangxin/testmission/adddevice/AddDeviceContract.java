package com.tiandy.wangxin.testmission.adddevice;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public interface AddDeviceContract {

    interface IAddDeviceModel{
        void addDevice(DeviceInfo deviceInfo);
    }

    interface IAddDeviceView{
        void addDeviceSuccess();

        void addDeviceFail();

        void addDeviceDuplicated();
    }

    interface IAddDevicePresenter{

        void addDevice(DeviceInfo deviceInfo);

        void addDeviceSuccess();

        void addDeviceFail();

        void addDeviceDuplicated();
    }
}
