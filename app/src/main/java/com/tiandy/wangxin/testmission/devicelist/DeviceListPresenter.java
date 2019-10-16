package com.tiandy.wangxin.testmission.devicelist;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

import java.util.List;

/**
 * Created by wangxin on 2019/10/11 yeah.
 */

public class DeviceListPresenter implements DeviceListContract.IDevicePresenter {

    private final DeviceListModel mDeviceListModel;
    private final DeviceListContract.IDeviceListView deviceListView;

    public DeviceListPresenter(DeviceListContract.IDeviceListView deviceListView) {
        this.deviceListView = deviceListView;
        mDeviceListModel = new DeviceListModel(this);
    }


    @Override
    public void loadLocalDeviceList() {
        mDeviceListModel.loadLocalDeviceList();
    }

    @Override
    public void loadLocalDeviceListSuccess(List<DeviceInfo> deviceInfoList) {
        deviceListView.refreshList(deviceInfoList);
    }
}
