package com.tiandy.wangxin.testmission.devicedetail;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobile.common.macro.SDKMacro;
import com.mobile.wiget.business.BusinessController;
import com.tiandy.wangxin.testmission.R;
import com.tiandy.wangxin.testmission.base.BaseActivity;
import com.tiandy.wangxin.testmission.devicelist.bean.DeviceInfo;
import com.tiandy.wangxin.testmission.util.LogonUtil;
import com.zjun.widget.TimeRuleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by wangxin on 2019/10/15 yeah.
 */

public class DeviceDetailActivity extends BaseActivity implements BusinessController.MainNotifyListener, View.OnClickListener {
    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();
    private ImageView mTvVolume;
    private ImageView mIvRoutes;
    /**
     * 省流量
     */
    private TextView mTvStream;
    private ImageView mIvFullscreen;
    private RecyclerView mRecyclerView;
    private DeviceInfo mDeviceInfo;
    private SurfaceView mSurfaceView;
    /**
     * 抓拍
     */
    private TextView mTvCapture;
    /**
     * 录像
     */
    private TextView mTvRecord;
    /**
     * 快退
     */
    private TextView mTvPlayBack;
    /**
     * 暂停
     */
    private TextView mTvPause;
    /**
     * playforward
     */
    private TextView mTvPlayForward;
    /**
     * 单帧步进
     */
    private TextView mTvSingleFrame;
    private TextView mTvDate;
    private int mSelectedRoute;
    private TimeRuleView mTimeRulerView;
    private ArrayList<TimeRuleView.TimePart> mTimeParts = new ArrayList<>();
    private String mStartTime = timeStamp2Date(System.currentTimeMillis() - 60 * 60 * 1000);
    private int mPlayBack;
    private int newTimeValue;
    private String mNowHours;
    private String mNowMinus;
    private String mNowSeconds;
    private String YMD;
    private long mMillis = System.currentTimeMillis();
    private String mEndTime = timeStamp2Date(System.currentTimeMillis());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        initView();

        BusinessController.getInstance().setMainNotifyListener(this);// 注册主回调
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());


        /**
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTarget(mSurfaceView);
        getWindow().setSharedElementEnterTransition(transitionSet);
        getWindow().setSharedElementExitTransition(transitionSet);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final List<RoutePlayStatus> routePlayStatuses = new ArrayList<>();
        for (int i = 1; i <= mDeviceInfo.getChannelNum(); i++) {
            RoutePlayStatus routePlayStatus = new RoutePlayStatus();
            routePlayStatus.setRouteName("通道" + i);

            routePlayStatus.setRoute(i);
            if (mSelectedRoute == i) {
                routePlayStatus.setPlaying(true);
            }

            routePlayStatuses.add(routePlayStatus);
        }
        mRecyclerView.setAdapter(new BaseQuickAdapter<RoutePlayStatus, BaseViewHolder>(R.layout.item_route, routePlayStatuses) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, final RoutePlayStatus item) {
                helper.setText(R.id.tv_device_name, item.getRouteName()).setImageResource(R.id.tv_src, item.isPlaying ? R.mipmap.photo : 0);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RoutePlayStatus routePlayStatus : routePlayStatuses) {
                            routePlayStatus.setPlaying(false);
                        }
                        item.setPlaying(true);
                        notifyDataSetChanged();
                        mSelectedRoute = item.getRoute();
                        play(false);
                    }
                });
            }
        });


    }


    private void initView() {


        mDeviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");
        mSelectedRoute = getIntent().getIntExtra("selectedRoute", 0);
        LogUtils.d("mSelectedRoute" + mSelectedRoute);

        mSurfaceView = findViewById(R.id.surfaceView);
        mTvVolume = findViewById(R.id.tv_volume);
        mTvStream = findViewById(R.id.tv_stream);
        mIvFullscreen = findViewById(R.id.iv_fullscreen);
        mTvCapture = findViewById(R.id.tv_capture);
        mTvRecord = findViewById(R.id.tv_record);
        mTvPlayBack = findViewById(R.id.tv_play_back);
        mTvPause = findViewById(R.id.tv_pause);
        mTvPlayForward = findViewById(R.id.tv_play_forward);
        mTvSingleFrame = findViewById(R.id.tv_single_frame);
        mRecyclerView = findViewById(R.id.recyclerView);
        mTvDate = findViewById(R.id.tv_date);

        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
        mTvDate.setText(TimeUtils.getNowString(getDateFormat("yyyy-MM-dd")));
        mTimeRulerView = findViewById(R.id.timeRulerView);
        YMD = TimeUtils.getNowString(getDateFormat("yyyy-MM-dd"));
        getNowHMS();

        LogUtils.d("nowHours" + mNowHours + "nowMinus" + mNowMinus + "nowSeconds" + mNowSeconds);

        mTimeRulerView.setCurrentTime(getSTime(mNowHours, mNowMinus, mNowSeconds));
        TimeRuleView.TimePart timePart = new TimeRuleView.TimePart();
        timePart.endTime = getSTime(Integer.valueOf(mNowHours) - 2, Integer.valueOf(mNowMinus), Integer.valueOf(mNowSeconds));
        newTimeValue = getSTime(Integer.valueOf(mNowHours) - 1, Integer.valueOf(mNowMinus), Integer.valueOf(mNowSeconds));
        timePart.startTime = getSTime(Integer.valueOf(mNowHours), Integer.valueOf(mNowMinus), Integer.valueOf(mNowSeconds));
        mTimeParts.add(timePart);
        mTimeRulerView.setTimePartList(mTimeParts);

        mTimeRulerView.setOnTimeChangedListener(new TimeRuleView.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int newTimeValue) {
                DeviceDetailActivity.this.newTimeValue = newTimeValue;
                getStartTime();
                getEndTime();
                LogUtils.d("mStartTime" + mStartTime);
                LogUtils.d("mStartTime" + mEndTime);
            }

            @Override
            public void onThumbUp() {


                if (!mTimeRulerView.getScroller().computeScrollOffset()) {
                    LogUtils.d("移动停止");
                    if (mStartTime == null) {
                        return;
                    }
                    play(false);
                }
            }
        });
        mPlayBack = LogonUtil.playBack(mDeviceInfo
                , mStartTime
                , mEndTime
                , mSelectedRoute
                , mSurfaceView);

    }

    private void play(boolean anotherDay) {
        if (anotherDay) {
            mTimeParts.clear();
        }
        TimeRuleView.TimePart timePart = new TimeRuleView.TimePart();
        timePart.endTime = newTimeValue + 60 * 60;

        getNowHMS();
        int sTime = getSTime(mNowHours, mNowMinus, mNowSeconds);
        LogUtils.d("endTimemillis=" + timePart.endTime);
        LogUtils.d("nowTimemillis=" + sTime);
        if (timePart.endTime > sTime && TimeUtils.isToday(mMillis)) {
            timePart.endTime = sTime;
        }

        timePart.startTime = newTimeValue - 3600;
        mTimeParts.add(timePart);
        mTimeRulerView.setTimePartList(mTimeParts);
        int ret = LogonUtil.playBackStop(mPlayBack);
        LogUtils.d("ret" + ret);
        mPlayBack = LogonUtil.playBack(mDeviceInfo
                , mStartTime
                , mEndTime
                , mSelectedRoute
                , mSurfaceView);



        LogUtils.d("mPlayBack" + mPlayBack+"----mSelectedRoute="+mSelectedRoute);
    }

    private void getStartTime() {
        mStartTime = YMD + " " + TimeRuleView.formatTimeHHmmss(DeviceDetailActivity.this.newTimeValue);
    }

    private void getEndTime() {
        mEndTime = YMD + " " + TimeRuleView.formatTimeHHmmss(DeviceDetailActivity.this.newTimeValue + 3600);
    }

    private void getNowHMS() {
        mNowHours = TimeUtils.getNowString(getDateFormat("HH"));
        mNowMinus = TimeUtils.getNowString(getDateFormat("mm"));
        mNowSeconds = TimeUtils.getNowString(getDateFormat("ss"));
    }

    private static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        } else {
            simpleDateFormat.applyPattern(pattern);
        }
        return simpleDateFormat;
    }


    private int getSTime(String hours, String m, String s) {
        return (Integer.valueOf(hours) - 1) * 3600 + Integer.valueOf(m) * 60 + Integer.valueOf(s);
    }

    private int getSTime(int hours, int m, int s) {
        return (hours) * 3600 + m * 60 + s;
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("deviceInfo", mDeviceInfo);
//        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    //打开日历
    private void openCalendar() {
//        if (mTime <= 0) return;
        final Calendar calendar = Calendar.getInstance();
//        calendar.setTime(TimeUtils.millis2Date(mTime));

        final DatePickerDialog picker = new DatePickerDialog(this, null,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.setCancelable(true);
        picker.setCanceledOnTouchOutside(true);
        picker.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = picker.getDatePicker();
                        calendar.set(Calendar.YEAR, datePicker.getYear());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));
                        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
                        mMillis = TimeUtils.date2Millis(calendar.getTime());
                        mTvDate.setText(timeStampDate(mMillis));
                        YMD = timeStampDate(mMillis);
                        getStartTime();
                        getEndTime();
                        play(true);

                    }
                });
        picker.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        picker.show();

    }


    @Override
    public void MainNotifyFun(int i, int i1, String s, int i2, Object o) {
        switch (i1) {
            case SDKMacro.EVENT_LOGIN_SUCCESS: // 登录成功
                Toast.makeText(this, R.string.login_success,
                        Toast.LENGTH_SHORT).show();
                break;

            case SDKMacro.EVENT_LOGIN_FAILED: // 登录失败
                Toast.makeText(this, R.string.login_fail,
                        Toast.LENGTH_SHORT).show();
//                txtPleaseConnect.setText(getResources().getString(
//                        R.string.please_connect));
//                closeVedioPlay();
//                logOff();
//                isLoginSuccess = false;
                break;

            case SDKMacro.EVENT_REALPLAY_STOP:// 播放失败
                Toast.makeText(this, R.string.play_fail, Toast.LENGTH_SHORT)
                        .show();
//                onClickVideoPlay();// 失败后调用播放方法，将状态置回
                break;

            case SDKMacro.EVENT_REALPLAY_START:// 开始播放
                break;

            case SDKMacro.CLIENT_EVENT_REALPLAY_FIRST_FRAME:// 第一帧画面
                Toast.makeText(this, R.string.play_success,
                        Toast.LENGTH_SHORT).show();
                break;

            case SDKMacro.CLIENT_EVENT_TALK_FAILED:// 对讲失败
                Toast.makeText(this, R.string.open_talk_fail,
                        Toast.LENGTH_SHORT).show();
                break;

            case SDKMacro.CLIENT_EVENT_TALK_SUCCESS: // 对讲成功
                // 设置对讲为双向对讲
                Toast.makeText(this, R.string.open_talk_success,
                        Toast.LENGTH_SHORT).show();
//                BusinessController.getInstance().sdkTalkControl(talkFd, 1, 1);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int ret = LogonUtil.playBackStop(mPlayBack);
        LogUtils.d("onDestroy");
//        ArrayList<Integer> playFlags = mDeviceInfo.getPlayFlags();
//        for (Integer playFlag : playFlags) {
//            LogonUtil.stopPlay(playFlag);
//        }
//        LogonUtil.logoff(mDeviceInfo.getLoginFlag());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_volume:
                break;
            case R.id.tv_stream:
                break;
            case R.id.iv_fullscreen:
                break;
            case R.id.recyclerView:
                break;
        }
    }

    public static String timeStamp2Date(long seconds) {
        if (seconds <= 0) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(seconds));
    }


    public static String timeStampDate(long seconds) {
        if (seconds <= 0) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(seconds));
    }

    //通过回调随时更新日期
    private void startUpdateDate(Calendar time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(time.getTime());
//        mTime = TimeUtils.date2Millis(time.getTime());//随时更新时间
//        mRulerView.setCurrentTimeMillis(mTime);

    }

    public void onBackClick(View view) {
        super.onBackPressed();
    }


    public static class RoutePlayStatus {

        private String routeName;

        private int route;

        private boolean isPlaying;

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public String getRouteName() {
            return routeName;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }

        public int getRoute() {
            return route;
        }

        public void setRoute(int route) {
            this.route = route;
        }
    }


}
