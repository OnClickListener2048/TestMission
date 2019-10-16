package com.tiandy.wangxin.testmission.devicelist;

import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

import java.util.List;

/**
 * Created by wangxin on 2019/10/11.
 */

public interface DeviceListContract {
    interface IDeviceListModel {
        void loadLocalDeviceList();
    }


    interface IDeviceListView {

        void showToast(String message);

        void refreshList(List<DeviceInfo> deviceInfoList);
    }

    interface IDevicePresenter {

        void loadLocalDeviceList();


        void loadLocalDeviceListSuccess(List<DeviceInfo> deviceInfoList);
    }
}
