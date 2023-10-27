package com.hihonor.adsdk.demo.external.interstital;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.interstitial.InterstitialAdLoadListener;
import com.hihonor.adsdk.base.api.interstitial.InterstitialExpressAd;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.utils.ToastUtil;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.interstitial.InterstitialAdLoad;

public class InterstitialActivity extends BaseActivity {
    private final static String TAG = "InterstitialActivity";
    private String mSlotId = "1698593762892578816";

    private TextView mTvMsg;
    private InterstitialExpressAd mInterstitialExpressAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_interstital_ad));
        setContentView(R.layout.activity_interstital);
        initView();
    }

    private void initView() {
        mTvMsg = findViewById(R.id.tv_msg);
        Button btnIns = findViewById(R.id.btn_ins);
        btnIns.setOnClickListener(view -> {
            obtainAd();
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder()
                .setSlotId(mSlotId)
                .build();
        InterstitialAdLoad load = new InterstitialAdLoad.Builder()
                .setAdSlot(adSlot)
                .setInterstitialAdLoadListener(new AdLoadListener())
                .build();
        load.loadAd();
    }

    /**
     * 广告加载状态监听器
     */
    private class AdLoadListener implements InterstitialAdLoadListener {

        /**
         * 广告加载成功回调。
         *
         * @param interstitialExpressAd 插屏广告模板接口基类
         */
        @Override
        public void onAdLoaded(InterstitialExpressAd interstitialExpressAd) {
            HiAdsLog.i(TAG, "onAdLoaded, ad load success");
            mInterstitialExpressAd = interstitialExpressAd;
            ToastUtil.showShortToast("加载成功");
            mInterstitialExpressAd.setAdListener(new MyAdListener());
            mInterstitialExpressAd.show(InterstitialActivity.this);
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
            mTvMsg.setVisibility(View.VISIBLE);
            mTvMsg.setText("err : " + code + ", msg is :" + errorMsg);
        }
    }

    /**
     * 广告事件监听器
     */
    private class MyAdListener extends AdListener {

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(InterstitialActivity.this,
                    getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(InterstitialActivity.this,
                    getString(R.string.ad_impression_failed), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(InterstitialActivity.this,
                    getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(InterstitialActivity.this,
                    getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(InterstitialActivity.this,
                    getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiAdsLog.i(TAG, "onDestroy");
        if (mInterstitialExpressAd != null) {
            HiAdsLog.i(TAG, "mInterstitialExpressAd is not null");
            mInterstitialExpressAd.release();
        }
    }
}
