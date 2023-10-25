package com.hihonor.adsdk.demo.external;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.demo.external.common.NativeListAdapter;
import com.hihonor.adsdk.nativead.api.NativeAd;
import com.hihonor.adsdk.nativead.api.NativeAdLoad;
import com.hihonor.adsdk.nativead.api.NativeAdLoadListener;

import java.util.List;

/**
 * 原生广告
 * */
public class NativeADActivity extends BaseActivity {

    private static final String TAG = NativeADActivity.class.getSimpleName();

    private String mSlotId = "1689185894485196800";

    private NativeListAdapter mNativeListAdapter;

    private RecyclerView mNativeAdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_native_ad));
        setContentView(R.layout.activity_native);
        initView();
    }
    private void initView() {
        mNativeAdList = findViewById(R.id.native_ad_list);
        Button btLoadNative = findViewById(R.id.bt_load_native);

        AdSlot adSlot = new AdSlot.Builder()
                .setSlotId(mSlotId)
                .build();

        NativeAdLoad nativeAdLoad = new NativeAdLoad.Builder()
                .setAdSlot(adSlot)
                .setNativeAdLoadListener(new AdLoadListener())
                .build();

        btLoadNative.setOnClickListener(view -> {
            nativeAdLoad.loadAd();
        });
    }

    /**
     * 广告加载状态监听器
     */
    public class AdLoadListener implements NativeAdLoadListener {

        /**
         * 广告加载成功回调。
         *
         * @param adViewList 回调广告数据集合
         */
        @Override
        public void onAdLoaded(List<NativeAd> adViewList) {
            HiAdsLog.i(TAG, "onAdLoaded, ad load success");
            if (adViewList == null || adViewList.isEmpty()) {
                Toast.makeText(NativeADActivity.this, "Request success but data is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            for (NativeAd nativeAd : adViewList) {
                nativeAd.setAdListener(new MyAdListener());
            }
            mNativeListAdapter = new NativeListAdapter(adViewList);
            mNativeAdList.setAdapter(mNativeListAdapter);
            mNativeAdList.setLayoutManager(new LinearLayoutManager(NativeADActivity.this, RecyclerView.VERTICAL, false));
        }

        /**
         * 广告加载失败
         *
         * @param code 错误码
         * @param errorMsg 错误提示信息
         */
        @Override
        public void onFailed(String code, String errorMsg) {
            HiAdsLog.i(TAG, "onFailed: code: " + code + ", errorMsg: " + errorMsg);
            Toast.makeText(NativeADActivity.this, errorMsg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 广告事件监听器
     */
    private class MyAdListener extends AdListener {

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(NativeADActivity.this, "广告关闭", Toast.LENGTH_SHORT).show();
        }

        /**
         * 开屏广告点击跳过或倒计时结束时回调
         *
         * @param type 0：点击跳过、1：倒计时结束
         */
        @Override
        public void onAdSkip(int type) {
            super.onAdSkip(type);
            HiAdsLog.i(TAG, "onAdSkip, type: " + type);
            Toast.makeText(NativeADActivity.this, "跳过广告", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(NativeADActivity.this, "点击广告", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(NativeADActivity.this, "展示成功", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告曝光失败时回调
         *
         * @param msg 曝光失败信息
         */
        @Override
        public void onAdImpressionFailed(String msg) {
            super.onAdImpressionFailed(msg);
            HiAdsLog.i(TAG, "onAdImpressionFailed, msg: " + msg);
            Toast.makeText(NativeADActivity.this, "展示失败", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(NativeADActivity.this, "跳转小程序", Toast.LENGTH_SHORT).show();
        }
    }
}
