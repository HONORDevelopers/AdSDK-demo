package com.hihonor.adsdk.demo.external;

import android.app.Application;
import android.content.Context;

import com.hihonor.adsdk.base.HnAds;
import com.hihonor.adsdk.base.init.HnAdConfig;
import com.hihonor.adsdk.base.init.HnCustomController;

public class DemoApplication extends Application {

    private static final String TAG = "DemoApplication";

    /**
     * 媒体ID
     */
    private final static String APPId = "1640545857217757184";

    /**
     * 媒体appKey
     */
    private final static String APPKey = "PuWq5LFla2vJ8XvZdHxsD7s6oYFg3zAGnL9ACczccvU=";

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 强烈建议在应用对应的Application#onCreate()方法中调用，避免出现context为null的异常
        doInit(DemoApplication.this);
        sContext = this;
    }

    private static void doInit(Context context) {
        HnAds.get().init(context, buildConfig());
    }

    private static HnAdConfig buildConfig() {
        return new HnAdConfig.Builder()
                .setAppId(APPId) // 设置您的媒体id，媒体id是您在荣耀广告平台注册的媒体id
                .setAppKey(APPKey) // 设置您的appKey，appKey是您在荣耀广告平台注册的媒体id对应的密钥:
                .setSupportMultiProcess(false)// 媒体多进程场景需要设置该参数为true，非多进程场景不需要设置。
                // .setWxOpenAppId("Your WeChat AppId") // 如果您的推广目标有小程序推广的话，此处需要设置。
                .setCustomController(new HnCustomController() {
                    @Override
                    public boolean isCanUseLocation() {
                        //允许获取地理位置（城市信息）
                        return super.isCanUseLocation();
                    }

                    @Override
                    public boolean isCanGetAllPackages() {
                        //允许获取设备应用安装列表
                        return super.isCanGetAllPackages();
                    }
                })
                .build();
    }
}
