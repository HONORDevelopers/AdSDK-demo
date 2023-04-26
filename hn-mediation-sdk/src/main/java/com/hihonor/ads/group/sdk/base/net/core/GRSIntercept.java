package com.hihonor.ads.group.sdk.base.net.core;

import android.content.Context;
import android.text.TextUtils;


import com.hihonor.ads.group.sdk.base.net.util.GrsUtil;
import com.hihonor.ads.group.sdk.util.log.HiAdsLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class GRSIntercept implements Interceptor {
    private static final String TAG = GRSIntercept.class.getSimpleName();
    private GrsConfig mGrsConfig;
    private Context mContext;
    private String mOriginBaseUrl;
    private XHttpContext mHttpContext;

    public GRSIntercept(Context context, String baseUrl, GrsConfig grsConfig, XHttpContext xHttpContext) {
        this.mContext = context;
        this.mOriginBaseUrl = baseUrl;
        this.mGrsConfig = grsConfig;
        this.mHttpContext = xHttpContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        String originUrl = originRequest.url().toString();
        HiAdsLog.i(TAG, "GRSIntercept start,originUrl:" + originUrl);
        String newBaseUrl = GrsUtil.getBaseUrl(mContext, mGrsConfig.getBizName(), mGrsConfig.getGrsServerName(), mGrsConfig.getGrsKey(), mGrsConfig.getCountryProvider());
        HiAdsLog.i(TAG, "get grs url finish:" + (TextUtils.isEmpty(newBaseUrl) ? "" : newBaseUrl));
        if (TextUtils.isEmpty(newBaseUrl)) {
            throw new IOException("GRS URL is EMPTY");
        }
        mHttpContext.setBaseUrl(newBaseUrl);
        if (newBaseUrl.equalsIgnoreCase(mOriginBaseUrl)) {
            HiAdsLog.e(TAG, "newBaseUrl equals mOriginBaseUrl, return");
            return chain.proceed(originRequest);
        }
        String newUrl = originUrl.replace(mOriginBaseUrl, newBaseUrl);
        if (TextUtils.isEmpty(newUrl)) {
            HiAdsLog.e(TAG, "newUrl is empty, return");
            return chain.proceed(originRequest);
        }
        Request newRequest = originRequest.newBuilder().url(newUrl).build();
        return chain.proceed(newRequest);
    }
}
