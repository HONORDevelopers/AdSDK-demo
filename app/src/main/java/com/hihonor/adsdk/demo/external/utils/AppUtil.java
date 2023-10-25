package com.hihonor.adsdk.demo.external.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.hihonor.adsdk.demo.external.DemoApplication;
import com.hihonor.adsdk.demo.external.common.AppInfo;

import java.util.List;

/**
 * 功能描述
 *
 * @since 2023-05-08
 */
public class AppUtil {

    public static AppInfo getAppInfo(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }
        PackageManager pm = DemoApplication.sContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if(infos != null){
            for (ResolveInfo resolveInfo:infos) {
                if(pkgName.contains(resolveInfo.activityInfo.packageName)){
                    AppInfo appInfo = new AppInfo();
                    appInfo.setIconDrawable(resolveInfo.loadIcon(pm));
                    appInfo.setAppName(resolveInfo.loadLabel(pm).toString());
                    appInfo.setPkgName(resolveInfo.activityInfo.packageName);
                    return appInfo;
                }
            }
        }
        return null;
    }
}
