package com.hihonor.adsdk.demo.external;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.common.AppInfo;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.demo.external.common.MediaDataBean;
import com.hihonor.adsdk.demo.external.common.SearchNativeListAdapter;
import com.hihonor.adsdk.demo.external.utils.AppUtil;
import com.hihonor.adsdk.nativead.api.NativeAd;
import com.hihonor.adsdk.nativead.api.NativeAdLoad;
import com.hihonor.adsdk.nativead.api.NativeAdLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 原生广告
 */
public class SearchNativeADActivity extends BaseActivity {

    private static final String TAG = SearchNativeADActivity.class.getSimpleName();

    private String mSlotId = "1655413403812626432";

    private SearchNativeListAdapter mSearchNativeListAdapter;

    private RecyclerView mNativeAdList;

    private final List<MediaDataBean> mDataBeanList = new ArrayList<>();

    private static final String[] sAppPackages = {
            "com.hihonor.appmarket",
            "com.tencent.mm",
            "com.ss.android.ugc.aweme",
            "com.hihonor.email",
            "com.hihonor.search",
            "com.hihonor.photos",
            "com.hihonor.hicloud",
            "com.huawei.browser",
            "com.huawei.gamebox",
            "com.huawei.health",
            "com.huawei.himovie",
            "com.huawei.music"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_native_ad));
        setContentView(R.layout.activity_native);
        initView();
        initData();

    }

    private void initData() {
        // 构造媒体数据
        for (int i = 0; i < sAppPackages.length; i++) {
            String appPackage = sAppPackages[i];
            AppInfo appInfo = AppUtil.getAppInfo(appPackage);
            if (appInfo != null) {
                mDataBeanList.add(new MediaDataBean(appInfo));
            }
        }
    }

    private void initView() {
        mNativeAdList = findViewById(R.id.native_ad_list);
        Button btLoadNative = findViewById(R.id.bt_load_native);

        btLoadNative.setOnClickListener(view -> {
            mDataBeanList.clear();
            initData();
            obtainAd();
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder()
                .setSlotId(mSlotId)
                .setTimeOutMillis(1500)
                .build();
        NativeAdLoad nativeAdLoad = new NativeAdLoad.Builder()
                .setAdSlot(adSlot)
                .setNativeAdLoadListener(new AdLoadListener())
                .build();
        nativeAdLoad.loadAd();
    }

    /**
     * 广告加载状态监听器
     */
    private class AdLoadListener implements NativeAdLoadListener {

        /**
         * 广告加载成功回调。
         *
         * @param adViewList 回调广告数据集合
         */
        @Override
        public void onAdLoaded(List<NativeAd> adViewList) {
            HiAdsLog.i(TAG, "onAdLoaded, ad load success");
            if (adViewList == null || adViewList.isEmpty()) {
                Toast.makeText(SearchNativeADActivity.this, "Request success but data is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            for (NativeAd nativeAd : adViewList) {
                nativeAd.setAdListener(new MyAdListener());
            }
            mSearchNativeListAdapter = new SearchNativeListAdapter(parseAdData(adViewList));
            mNativeAdList.setAdapter(mSearchNativeListAdapter);
            mNativeAdList.setLayoutManager(new GridLayoutManager(SearchNativeADActivity.this,
                    4, RecyclerView.VERTICAL, false));
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
            Toast.makeText(SearchNativeADActivity.this, errorMsg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private List<MediaDataBean> parseAdData(List<NativeAd> nativeAdList) {
        for (NativeAd nativeAd : nativeAdList) {
            MediaDataBean mediaDataBean = new MediaDataBean();
            AppInfo appInfo = AppUtil.getAppInfo(nativeAd.getAppPackage());
            if (appInfo != null) {
                mediaDataBean.setAppInfo(appInfo);
            }
            mediaDataBean.setNativeAd(nativeAd);
            mDataBeanList.add(mediaDataBean);
        }
        return mDataBeanList;
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
            Toast.makeText(SearchNativeADActivity.this, "广告关闭", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SearchNativeADActivity.this, "跳过广告", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(SearchNativeADActivity.this, "点击广告", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(SearchNativeADActivity.this, "展示成功", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SearchNativeADActivity.this, "展示失败", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(SearchNativeADActivity.this, "跳转小程序", Toast.LENGTH_SHORT).show();
        }
    }
}
