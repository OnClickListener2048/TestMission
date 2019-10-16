package com.tiandy.wangxin.testmission.devicelist;

import com.tiandy.wangxin.testmission.dbhelper.DbGreenDAOHelper;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

import java.util.List;

/**
 * Created by wangxin on 2019/10/11.
 */

public class DeviceListModel implements DeviceListContract.IDeviceListModel {
    private final DeviceListPresenter deviceListPresenter;

    public DeviceListModel(DeviceListPresenter deviceListPresenter) {
        this.deviceListPresenter = deviceListPresenter;
    }

    @Override
    public void loadLocalDeviceList() {
        List<DeviceInfo> deviceInfos = DbGreenDAOHelper.getInstance().loadLocalDeviceList();
        deviceListPresenter.loadLocalDeviceListSuccess(deviceInfos);
    }
}
