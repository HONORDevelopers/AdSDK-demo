package com.hihonor.ads.group.sdk.base.net.util;

import android.content.Context;
import android.text.TextUtils;


import com.hihonor.ads.group.sdk.base.net.core.ICountryProvider;
import com.hihonor.ads.group.sdk.datamonitor.cache.CacheUtil;
import com.hihonor.ads.group.sdk.util.log.HiAdsLog;
import com.hihonor.framework.network.grs.GrsBaseInfo;
import com.hihonor.framework.network.grs.GrsClient;


public class GrsUtil {
    private static final String TAG = GrsUtil.class.getSimpleName();
    private static final String KEY_APP_VERSION = "key_app_version";
    private static volatile boolean hasCheckExpire = false;

    public static String getBaseUrl(Context mContext, String bizName, String grsServer, String grsKey, ICountryProvider countryProvider) {
        GrsBaseInfo grsBaseInfo = new GrsBaseInfo(mContext);
    //    grsBaseInfo.setAppName(bizName); 广告sdk为虚拟应用，此处不能调用setAppName，否则会导致找不到url
        String issueCountry = grsBaseInfo.getIssueCountry();
        if (countryProvider != null) {
            String custCountry = countryProvider.country();
            if (!TextUtils.isEmpty(custCountry)) {
                issueCountry = custCountry;
            }
        }
        grsBaseInfo.setSerCountry(issueCountry);
        HiAdsLog.d(TAG, "issueCountry=" + issueCountry+" bizName="+bizName+" grsServer="+grsServer+" grsKey="+grsKey);
        GrsClient grsClient = new GrsClient(mContext, grsBaseInfo);
        checkUrlExpire(mContext, grsClient);
        String baseUrl = grsClient.synGetGrsUrl(grsServer, grsKey);
        if (!TextUtils.isEmpty(baseUrl) && !baseUrl.startsWith("http")) {
            baseUrl = "http://" + baseUrl;
        }
        return baseUrl;
    }

    private static void checkUrlExpire(Context context, GrsClient grsClient) {
        if (hasCheckExpire) {
            return;
        }
        CacheUtil cacheUtil = SafeUtil.getCache();
        if (cacheUtil == null) {
            return;
        }
        int curVer = PackageUtil.getVersionCode(context);
        int lastVer = cacheUtil.getInt(KEY_APP_VERSION, -1);
        if (curVer > 0 && curVer != lastVer) {
            grsClient.forceExpire();
            cacheUtil.putInt(KEY_APP_VERSION, curVer);
        }
        hasCheckExpire = true;
    }

}