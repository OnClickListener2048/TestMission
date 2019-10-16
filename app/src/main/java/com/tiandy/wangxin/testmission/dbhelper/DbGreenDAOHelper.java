package com.tiandy.wangxin.testmission.dbhelper;

import android.content.Context;

import com.tiandy.wangxin.testmission.devicelist.bean.DaoMaster;
import com.tiandy.wangxin.testmission.devicelist.bean.DaoSession;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfoDao;
import com.tiandy.wangxin.testmission.exception.DuplicatedInsertException;

import java.util.List;

/**
 * Created by wangxin on 2019/10/12 yeah.
 */

public class DbGreenDAOHelper {

    private static DbGreenDAOHelper instance = new DbGreenDAOHelper();
    private DeviceInfoDao mDeviceInfoDao;

    private DbGreenDAOHelper() {

    }

    public static DbGreenDAOHelper getInstance() {
        return instance;
    }

    public void initDao(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "device.db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        mDeviceInfoDao = daoSession.getDeviceInfoDao();
    }

    public List<DeviceInfo> loadLocalDeviceList() {
        return mDeviceInfoDao.queryBuilder().list();
    }

    public void addDevice(DeviceInfo deviceInfo) throws DuplicatedInsertException {
        List<DeviceInfo> list = mDeviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Key.eq(deviceInfo.getKey())).build().list();
        if (list != null && list.size() == 0) {
            mDeviceInfoDao.insertOrReplace(deviceInfo);
        } else {
            throw new DuplicatedInsertException();
        }




    }

    public void deleteDevice(DeviceInfo deviceInfo) {
        mDeviceInfoDao.delete(deviceInfo);
    }

    public void updateDevice(DeviceInfo deviceInfo) {
        mDeviceInfoDao.update(deviceInfo);
    }


}
