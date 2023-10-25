package com.hihonor.adsdk.demo.external.common;

import com.hihonor.adsdk.nativead.api.NativeAd;

/**
 * 模拟媒体数据bean，这里简单实现了。
 *
 * @since 2023-05-08
 */
public class MediaDataBean {
    private AppInfo mAppInfo;
    private NativeAd nativeAd;

    public MediaDataBean() {
    }

    public MediaDataBean(AppInfo appInfo) {
        mAppInfo = appInfo;
    }

    public MediaDataBean(AppInfo appInfo, NativeAd nativeAd) {
        mAppInfo = appInfo;
        this.nativeAd = nativeAd;
    }

    public AppInfo getAppInfo() {
        return mAppInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        mAppInfo = appInfo;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
