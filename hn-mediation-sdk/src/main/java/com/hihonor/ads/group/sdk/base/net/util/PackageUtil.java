package com.hihonor.ads.group.sdk.base.net.util;

import android.content.Context;


public class PackageUtil {
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
        }
        return versionCode;
    }
}
