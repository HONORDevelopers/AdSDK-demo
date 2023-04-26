package com.hihonor.ads.group.sdk.base.net.core;

import android.content.Context;

import okhttp3.logging.HttpLoggingInterceptor;


public class XHttpContext {
    private String baseUrl = "";
    private String uuid;
    private long tokenCheckDuration;
    private HttpLoggingInterceptor.Level logLevel;
    private Context context;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setBaseUrl(String baseUrl) {
        if (!this.baseUrl.equalsIgnoreCase(baseUrl)) {
            this.baseUrl = baseUrl;
        }
    }

    public long getTokenCheckDuration() {
        return tokenCheckDuration;
    }

    public void setTokenCheckDuration(long tokenCheckDuration) {
        this.tokenCheckDuration = tokenCheckDuration;
    }

    public HttpLoggingInterceptor.Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(HttpLoggingInterceptor.Level logLevel) {
        this.logLevel = logLevel;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
