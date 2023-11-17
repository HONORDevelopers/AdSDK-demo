package com.hihonor.adsdk.demo.external.banner;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hihonor.adsdk.banner.api.BannerAdLoad;
import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.banner.BannerAdLoadListener;
import com.hihonor.adsdk.base.api.banner.BannerExpressAd;
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

    /**
     * 广告对象
     */
    private BannerExpressAd mBannerExpressAd;

    /**
     * 广告位ID
     */
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
        // step1：创建广告请求参数对象（AdSlot）。
        AdSlot adSlot = new AdSlot.Builder()
            .setSlotId(mSlotId) // 必填，设置广告位ID。
            .build();
        // step4：构建广告加载器，传入已创建好的广告请求参数对象与广告加载状态监听器。
        BannerAdLoad load = new BannerAdLoad.Builder()
            .setBannerAdLoadListener(mAdLoadListener)
            .setAdSlot(adSlot)
            .build();
        load.loadAd();
    }

    /**
     * step2：实现广告加载状态监听器，加载过程中获取广告的状态变化。
     * <br>
     * 广告加载状态监听器
     */
    private final BannerAdLoadListener mAdLoadListener = new BannerAdLoadListener() {

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
            if (mRootView == null) {
                HiAdsLog.e(TAG, "onLoadSuccess, mRootView is null");
                return;
            }
            // 注册广告事件监听器，您可根据需求实现接口并按需重写您需要接收通知的方法。
            bannerExpressAd.setAdListener(new AdListener(){

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
                    releaseAd();
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
            });
            // 设置广告负反馈回调监听器
            bannerExpressAd.setDislikeClickListener(new DislikeItemClickListener(){
                @Override
                public void onItemClick(int position, @Nullable DislikeInfo dislikeInfo, @Nullable View target) {
                    mRootView.removeAllViews();
                    Toast.makeText(BannerDefaultActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
                }
            });
            // step3：在请求成功回调里，使用返回的广告对象作渲染处理。
            // 注意： addView前需要把添加广告的容器rootView将控件上所有的view调用removeAllViews方法移除。
            mRootView.removeAllViews();
            // 添加view，进行广告展示
            mRootView.addView(bannerExpressAd.getExpressAdView());
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
    };

    /**
     * 页面不可见时需要销毁广告
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseAd();
    }

    /**
     * 销毁广告
     */
    private void releaseAd() {
        if (mRootView != null) {
            mRootView.removeAllViews();
        }
        if (mBannerExpressAd != null) {
            HiAdsLog.i(TAG, "releaseAd...");
            mBannerExpressAd.release();
        }
    }
}
