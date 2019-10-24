package com.tiandy.wangxin.testmission.adddevice;

import com.tiandy.wangxin.testmission.dbhelper.DbGreenDAOHelper;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;
import com.tiandy.wangxin.testmission.exception.DuplicatedInsertException;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class AddDeviceModel implements AddDeviceContract.IAddDeviceModel {
    private final AddDeviceContract.IAddDevicePresenter addDevicePresenter;

    public AddDeviceModel(AddDeviceContract.IAddDevicePresenter addDevicePresenter) {
        this.addDevicePresenter = addDevicePresenter;
    }


    @Override
    public void addDevice(DeviceInfo deviceInfo) {
        try {
            DbGreenDAOHelper.getInstance().addDevice(deviceInfo);
            addDevicePresenter.addDeviceSuccess(deviceInfo);
        }catch (DuplicatedInsertException d){
            addDevicePresenter.addDeviceDuplicated();
        }catch (Exception e) {
            addDevicePresenter.addDeviceFail();
        }

    }
}
