package com.hihonor.ads.group.sdk.base.net.util;

import android.content.Context;
import android.text.TextUtils;

import com.hihonor.ads.group.sdk.datamonitor.cache.CacheUtil;

import java.util.UUID;


public class SafeUtil {
    private static CacheUtil sCacheUtil;
    private static volatile boolean hasInit = false;
    private static Context sGlobContext;
    private static volatile String sUUID;
    private static final String KEY_UUID = "KEY_UUID";

    public static void init(Context context) {
        if (!hasInit) {
            sGlobContext = context.getApplicationContext();
            sCacheUtil = new CacheUtil(sGlobContext);
            hasInit = true;
        }
    }

    public static CacheUtil getCache() {
        return sCacheUtil;
    }

    public static String getUUID() {
        if (!TextUtils.isEmpty(sUUID)) {
            return sUUID;
        }
        if (sCacheUtil == null) {
            return null;
        }
        sUUID = sCacheUtil.getString(KEY_UUID, null);
        if (!TextUtils.isEmpty(sUUID)) {
            return sUUID;
        }
        sUUID = UUID.randomUUID().toString();
        sCacheUtil.putString(KEY_UUID, sUUID);
        return sUUID;
    }


    public static void remove(String key) {
        if (sCacheUtil == null) {
            return;
        }
        sCacheUtil.remove(key);
    }


}
