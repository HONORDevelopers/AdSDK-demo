package com.hihonor.adsdk.demo.external;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.StringRes;

import com.hihonor.adsdk.base.HnAds;
import com.hihonor.adsdk.base.init.HnAdConfig;
import com.hihonor.adsdk.base.init.HnCustomController;
import com.hihonor.adsdk.base.init.HnRewardListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.utils.ToastUtil;

public class DemoApplication extends Application {
    public static Context sContext;
    private static final String TAG = "MyApplication";

    /**
     * 媒体ID
     */
    private static String APPId;

    /**
     * 媒体appKey
     */
    private static String APPKey;

    /**
     * 激励点事件
     */
    private static final String REWARD_ACTION = "reward_action";

    /**
     * 单条广告id
     */
    private static final String AD_ID = "ad_id";

    /**
     * 请求id，请求广告时产生的id
     */
    private static final String REQUEST_ID = "request_id";

    /**
     * 广告位id
     */
    private static final String ADUNIT_ID = "adunit_id";

    /**
     * 下载应用的包名
     */
    private static final String APP_PACKAGE = "app_package";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        APPId = getString(R.string.app_id);
        APPKey = getString(R.string.app_key);
        // 强烈建议在应用对应的Application#onCreate()方法中调用，避免出现context为null的异常
        doInit(DemoApplication.this);
    }

    public static String getAdUnitId(@StringRes int id) {
        if (sContext == null) {
            return "";
        }
        return sContext.getString(id);
    }

    private static void doInit(Context context) {
        HnAds.get().init(context, buildConfig());
    }

    private static HnAdConfig buildConfig() {
        return new HnAdConfig.Builder()
                .setAppId(APPId) // 设置您的媒体id，媒体id是您在荣耀广告平台注册的媒体id
                .setAppKey(APPKey) // 设置您的appKey，appKey是您在荣耀广告平台注册的媒体id对应的密钥:
                .setSupportMultiProcess(false) // 媒体多进程场景需要设置该参数为true，非多进程场景不需要设置。
                .setRewardListener(new HnRewardListener() {
                    @Override
                    public void onReward(Bundle bundle) {
                        if (null == bundle) {
                            return;
                        }
                        int action = bundle.getInt(REWARD_ACTION);
                        String adId = bundle.getString(AD_ID);
                        String reqId = bundle.getString(REQUEST_ID);
                        String adUnitId = bundle.getString(ADUNIT_ID);
                        String packageName = bundle.getString(APP_PACKAGE);
                        HiAdsLog.i(TAG, "onReward reqId:" + reqId + "; adUnitId:" + adUnitId
                                + "; adId:" + adId + "; action:" + action);
                        ToastUtil.showShortToast("AD：" + packageName + "get reward action：" + action);
                    }
                })
                // .setWxOpenAppId("Your WeChat AppId") // 如果您的推广目标有小程序推广的话，此处需要设置。
                .setCustomController(new HnCustomController() {
                    @Override
                    public boolean isCanUseLocation() {
                        // 允许获取地理位置（城市信息）
                        return true;
                    }

                    @Override
                    public boolean isCanGetAllPackages() {
                        // 允许获取设备应用安装列表
                        return true;
                    }
                })
                .build();
    }
}
