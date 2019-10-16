package com.tiandy.wangxin.testmission.util;

import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.common.macro.SDKMacro;
import com.mobile.common.po.ChannelNum;
import com.mobile.common.po.LogonHostInfo;
import com.mobile.common.po.RealPlayInfo;
import com.mobile.wiget.business.BusinessController;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wangxin on 2019/10/15 yeah.
 */

public class LogonUtil {
    /**
     * @Title: getDDNSChannels
     * @Description: 获取设备（DDNS）通道个数,初始化（DDNS）通道信息
     */
    public static ChannelNum getDDNSChannels(int logonFd) {


        //调取SDK接口
        ChannelNum channelNumJni = new ChannelNum();
        int ret = BusinessController.getInstance().sdkGetConfig(
                logonFd, 0, SDKMacro.GET_CHANNEL_NUM, channelNumJni);

        return channelNumJni;
        //调取SDK接口
//        List<Channel> channelList = new ArrayList<Channel>();
//        Channel[] channelsArray = new Channel[channelNumJni.num];
//        for (int j = 0; j < channelsArray.length; j++) {
//            Channel channel = new Channel();
//            channel.setStrCaption("Channel" + (j + 1));
//            channel.setStrId(host.getStrId() + j);
//            channel.setiNum(j);
//            channel.setIndex(j);
//            channel.setStrThumbnailPath(Environment.getExternalStorageDirectory() + channel.getStrId() + "-c.jpg");
//            channelList.add(channel);
//            channelsArray[j] = channel;
//        }
//
//        //插入数据库
//        int addChannelRet = BusinessController.getInstance().addChannels(host.getStrId(), channelsArray);
//        if (addChannelRet == 0) {
//            host.setChannels(channelList);
//        }

    }

    public static int fillupSurfaceView(SurfaceView surfaceView, int loginFlag, int route) {
        RealPlayInfo realPlayInfo = new RealPlayInfo();
        realPlayInfo.factory_index = 1;
        realPlayInfo.m_iStream_type = 0;// 码流
        realPlayInfo.m_lHwnd = surfaceView.getId();
        realPlayInfo.m_iChannel = route + 1;
        return BusinessController.getInstance().sdkRealplayStart(loginFlag,
                realPlayInfo, surfaceView);
    }

    public static int stopPlay(int playFlag) {
        return BusinessController.getInstance().sdkRealplayStop(playFlag);
    }

    public static void stopAll(List<DeviceInfo> deviceInfoList, ArrayList<ViewGroup> viewGroups, ArrayList<View> views) {
        for (ViewGroup viewGroup : viewGroups) {
            clearSelected(viewGroup);
        }
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
        for (DeviceInfo deviceInfo : deviceInfoList) {
            ArrayList<Integer> playFlags = deviceInfo.getPlayFlags();
            for (Integer playFlag : playFlags) {
                stopPlay(playFlag);
            }
        }
    }

    public static void clearSelected(ViewGroup viewGroup) {
        viewGroup.setClickable(false);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            viewGroup.getChildAt(i).setSelected(false);
        }
    }

    public static void logoff(int logonFd) {
        BusinessController.getInstance().sdkLogoffHost(logonFd);
    }



    public static int logonDDNSDeviceForVideo(DeviceInfo deviceInfo) {

        LogonHostInfo logonInfo = new LogonHostInfo();
        logonInfo.type = 1;
        logonInfo.account_name = "admin";
        logonInfo.account_passwd = "QQQQ";
        logonInfo.dns_server_port = 3000;
        logonInfo.port = 3000;
        logonInfo.nvs_ip = deviceInfo.getIp();
        logonInfo.username = deviceInfo.getUserName();
        logonInfo.password = deviceInfo.getPassword();

        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");

        return BusinessController.getInstance().sdkLogonHostByType(
                uuidStr, 1, logonInfo);

    }


}
