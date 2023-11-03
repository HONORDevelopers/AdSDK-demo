package com.hihonor.adsdk.demo.external.banner;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hihonor.ads.banner.api.BannerAdLoad;
import com.hihonor.ads.banner.api.BannerAdLoadListener;
import com.hihonor.ads.banner.api.BannerExpressAd;
import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.bean.DislikeInfo;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.callback.DislikeItemClickListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;

/**
 * Activity for displaying a banner ad.
 */
public class BannerDefaultActivity extends BaseActivity {

    private static final String TAG = "BannerDefaultActivity";

    private FrameLayout mRootView;

    private BannerExpressAd mBannerExpressAd;

    private String mSlotId = "1698586284462047232";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_banner_default));
        setContentView(R.layout.activity_banner_default);
        initView();
    }

    public void initView() {
        mRootView = findViewById(R.id.ad_content);
        Button adLoadButton = findViewById(R.id.bt_load_ad);
        adLoadButton.setOnClickListener(view -> obtainAd());
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder()
            .setSlotId(mSlotId)
            .build();
        BannerAdLoad load = new BannerAdLoad.Builder()
            .setBannerAdLoadListener(new AdLoadListener())
            .setAdSlot(adSlot)
            .build();
        load.loadAd();
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
            Toast.makeText(BannerDefaultActivity.this, getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(BannerDefaultActivity.this, getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(BannerDefaultActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(BannerDefaultActivity.this, getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 广告加载状态监听器
     */
    private class AdLoadListener implements BannerAdLoadListener {

        /**
         * 广告加载成功回调
         *
         * @param bannerExpressAd banner广告模板接口基类
         */
        @Override
        public void onLoadSuccess(BannerExpressAd bannerExpressAd) {
            mBannerExpressAd = bannerExpressAd;
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            // 您可根据需求实现接口并按需重写您需要接收通知的方法。
            bannerExpressAd.setAdListener(new MyAdListener());
            mRootView.removeAllViews();
            // 添加view
            mRootView.addView(bannerExpressAd.getExpressAdView());
            // 设置点击监听
            bannerExpressAd.setDislikeClickListener(new MyDislikeClickListener());
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
            Toast.makeText(BannerDefaultActivity.this, errorMsg,Toast.LENGTH_SHORT).show();
        }
    }

    class MyDislikeClickListener implements DislikeItemClickListener {
        @Override
        public void onItemClick(int position, @Nullable DislikeInfo dislikeInfo, @Nullable View target) {
            mRootView.removeAllViews();
            Toast.makeText(BannerDefaultActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
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
        if (mBannerExpressAd != null) {
            mBannerExpressAd.release();
        }
    }
}
