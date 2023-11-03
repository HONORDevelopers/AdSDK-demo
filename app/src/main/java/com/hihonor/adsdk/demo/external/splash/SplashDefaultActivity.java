package com.hihonor.adsdk.demo.external.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hihonor.ads.splash.SplashAdLoad;
import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.splash.SplashAdLoadListener;
import com.hihonor.adsdk.base.api.splash.SplashExpressAd;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.utils.ToastUtil;
import com.hihonor.adsdk.demo.external.MainActivity;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.GlobalConfig;

public class SplashDefaultActivity extends Activity {
    private static final String TAG = "SplashDefaultActivity";
    private String mSlotId = "1698591998676959232";
    private long mTimeOut = 0;

    private FrameLayout mRootView;

    /**
     * 是否强制跳转到主页面
     */
    private boolean mForceGoMain;

    private SplashExpressAd mSplashExpressAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenUtils.transparentStatusBar(getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_default);
        initView();
    }

    public void initView() {
        mRootView = findViewById(R.id.root_view);
        obtainAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mForceGoMain) {
//            startHomeActivity();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    /**
     * 获取广告
     */
    public void obtainAd() {
        AdSlot.Builder builder = new AdSlot.Builder().setSlotId(mSlotId)
            .setLoadType(GlobalConfig.AD_LOAD_TYPE.NORMAL_REQUEST);
        if (mTimeOut > 0) {
            builder.setTimeOutMillis(mTimeOut);
        }
        SplashAdLoad load = new SplashAdLoad.Builder().setSplashAdLoadListener(new AdLoadListener())
            .setAdSlot(builder.build()).build();
        load.loadAd();
    }

    public void startHomeActivity() {
        // 倒计时结束或者点击跳过
        Intent intent = new Intent(SplashDefaultActivity.this, MainActivity.class);
        startActivity(intent);
        if (mRootView != null) {
            mRootView.removeAllViews();
        }
        if (mSplashExpressAd != null) {
            mSplashExpressAd.release();
        }
        finish();
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
            Toast.makeText(SplashDefaultActivity.this,
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
            Toast.makeText(SplashDefaultActivity.this,
                    getString(R.string.ad_impression_failed), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(SplashDefaultActivity.this,
                    getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(SplashDefaultActivity.this,
                    getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SplashDefaultActivity.this,
                    getString(R.string.ad_skip), Toast.LENGTH_SHORT).show();
            startHomeActivity();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(SplashDefaultActivity.this,
                    getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 页面不可见需要移除广告view
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 加载广告的view
        if (mRootView != null) {
            mRootView.removeAllViews();
        }
        if (mSplashExpressAd != null) {
            mSplashExpressAd.release();
        }
    }

    /**
     * 广告加载状态监听器
     */
    private class AdLoadListener implements SplashAdLoadListener {

        /**
         * 广告加载成功回调。
         *
         * @param splashExpressAd 开屏广告模板接口基类
         */
        @Override
        public void onLoadSuccess(SplashExpressAd splashExpressAd) {
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            mSplashExpressAd = splashExpressAd;
            splashExpressAd.setLogoResId(R.drawable.ic_launcher_background);
            splashExpressAd.setMediaNameString(getString(R.string.app_market));
            splashExpressAd.setAdListener(new MyAdListener());
            mRootView.addView(splashExpressAd.getExpressAdView());
        }

        /**
         * 广告加载失败
         *
         * @param code 错误码
         * @param errorMsg 错误提示信息
         */
        @Override
        public void onFailed(String code, String errorMsg) {
            ToastUtil.showShortToast(getString(R.string.splash_request_failed));
            HiAdsLog.i(TAG, "onFailed: code: " + code + ", errorMsg: " + errorMsg);
            Intent intent = new Intent(SplashDefaultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
