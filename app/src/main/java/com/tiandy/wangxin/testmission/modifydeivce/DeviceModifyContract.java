package com.tiandy.wangxin.testmission.modifydeivce;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public interface DeviceModifyContract {

    interface IDeviceModifyModel{

        void updateDevice(DeviceInfo deviceInfo);
    }

    interface IDeviceModifyView{
        void updateSuccess();

        void updateFail();
    }

    interface IDeviceModifyPresenter{

        void updateDevice(DeviceInfo deviceInfo);

        void updateSuccess();

        void updateFail();
    }
}
