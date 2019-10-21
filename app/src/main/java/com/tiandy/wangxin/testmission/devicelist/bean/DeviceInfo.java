package com.tiandy.wangxin.testmission.devicelist.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangxin on 2019/10/11 yeah.
 */
@Entity
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = -6122522939947466082L;
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String key;
    private String ip;
    private String name;
    private String port;
    private String userName;
    private String password;
    private int channelNum;
    @Transient
    private int loginFlag;
    @Transient
    private
    ArrayList<Integer> playFlags = new ArrayList<>();
    @Transient
    private boolean isOpen;
    @Transient
    private int openRoute = -1;
    @Transient
    boolean isPlaying;
    @Transient
    private String uuid;




    @Generated(hash = 1269205985)
    public DeviceInfo(Long id, String key, String ip, String name, String port,
            String userName, String password, int channelNum) {
        this.id = id;
        this.key = key;
        this.ip = ip;
        this.name = name;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.channelNum = channelNum;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLoginFlag() {
        return this.loginFlag;
    }

    public void setLoginFlag(int loginFlag) {
        this.loginFlag = loginFlag;
    }

    public ArrayList<Integer> getPlayFlags() {
        return playFlags;
    }

    public void setPlayFlags(ArrayList<Integer> playFlags) {
        this.playFlags = playFlags;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getOpenRoute() {
        return openRoute;
    }

    public void setOpenRoute(int openRoute) {
        this.openRoute = openRoute;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", loginFlag=" + loginFlag +
                ", playFlags=" + playFlags +
                '}';
    }
}
