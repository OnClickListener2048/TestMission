package com.tiandy.wangxin.testmission.devicelist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiandy.wangxin.testmission.R;

import java.util.ArrayList;

/**
 * Created by wangxin on 2019/10/17 yeah.
 */

public class DeviceDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView mTvVolume;
    private ImageView mIvRoutes;
    /**
     * 省流量
     */
    private TextView mTvStream;
    private ImageView mIvFullscreen;
    private RecyclerView mRecyclerView;
    /**
     * 对讲
     */
    private TextView mTvTalkRemote;
    /**
     * 录像
     */
    private TextView mTvRecord;
    /**
     * 抓拍
     */
    private TextView mTvCapture;
    /**
     * 云台
     */
    private TextView mTvCloudPlatform;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_in);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out);
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_device_detail, null);
        initView(inflate);
        return inflate;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {


        mTvVolume = view.findViewById(R.id.tv_volume);
        mTvVolume.setOnClickListener(this);
        mIvRoutes = view.findViewById(R.id.iv_routes);
        mIvRoutes.setOnClickListener(this);
        mTvStream = view.findViewById(R.id.tv_stream);
        mTvStream.setOnClickListener(this);
        mIvFullscreen = view.findViewById(R.id.iv_fullscreen);
        mIvFullscreen.setOnClickListener(this);
        mTvTalkRemote = view.findViewById(R.id.tv_talk_remote);
        mTvTalkRemote.setOnClickListener(this);
        mTvRecord = view.findViewById(R.id.tv_record);
        mTvRecord.setOnClickListener(this);
        mTvCapture = view.findViewById(R.id.tv_capture);
        mTvCapture.setOnClickListener(this);
        mTvCloudPlatform = view.findViewById(R.id.tv_cloud_platform);
        mTvCloudPlatform.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i + 1));
        }
        mRecyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_route, arrayList) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, String item) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        LogUtils.d("onClick");
        switch (v.getId()) {
            case R.id.tv_volume:
                ToastUtils.showShort("声音");
                break;
            case R.id.iv_routes:
                ToastUtils.showShort("分屏");
                break;
            case R.id.tv_stream:
                ToastUtils.showShort("流量");
                break;
            case R.id.iv_fullscreen:
                ToastUtils.showShort("全屏");
                break;
            case R.id.tv_talk_remote:
                ToastUtils.showShort("对话");
                break;
            case R.id.tv_record:
                ToastUtils.showShort("录像");
                break;
            case R.id.tv_capture:
                ToastUtils.showShort("抓拍");
                break;
            case R.id.tv_cloud_platform:
                ToastUtils.showShort("云台");
                break;
            case R.id.recyclerView:
                break;
            default:
                break;
        }
    }
}
