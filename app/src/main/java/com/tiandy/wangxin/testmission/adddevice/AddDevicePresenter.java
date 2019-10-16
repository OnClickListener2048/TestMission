package com.tiandy.wangxin.testmission.adddevice;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class AddDevicePresenter implements AddDeviceContract.IAddDevicePresenter {
    private final AddDeviceContract.IAddDeviceView iAddDeviceView;
    private final AddDeviceModel mAddDeviceModel;

    public AddDevicePresenter(AddDeviceContract.IAddDeviceView iAddDeviceView) {
        this.iAddDeviceView = iAddDeviceView;
        mAddDeviceModel = new AddDeviceModel(this);
    }

    @Override
    public void addDevice(DeviceInfo deviceInfo) {
        mAddDeviceModel.addDevice(deviceInfo);
    }

    @Override
    public void addDeviceSuccess() {
        iAddDeviceView.addDeviceSuccess();
    }

    @Override
    public void addDeviceFail() {
        iAddDeviceView.addDeviceFail();
    }

    @Override
    public void addDeviceDuplicated() {
        iAddDeviceView.addDeviceDuplicated();
    }
}
