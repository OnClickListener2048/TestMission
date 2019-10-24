package com.tiandy.wangxin.testmission.devicelist.bean;

/**
 * Created by wangxin on 2019/10/21 yeah.
 */

public class Replay {

    /**
     * ch : 1
     * filename : H00008980001F5DA0000C00.sdv
     * size : 1067883515
     * filetype : 2
     * starttime : 2019-10-21 17:12:18
     * endtime : 2019-10-21 18:20:01
     */

    private int ch;
    private String filename;
    private int size;
    private int filetype;
    private String starttime;
    private String endtime;

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFiletype() {
        return filetype;
    }

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
