package com.hihonor.ads.group.sdk.base.net;

import android.text.TextUtils;

import com.hihonor.ads.group.sdk.util.JsonUtil;
import com.hihonor.ads.group.sdk.util.log.HiAdsLog;

import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpLogger implements HttpLoggingInterceptor.Logger {
    private static final String LOG_TAG = "XHttp";
    private final StringBuffer mMessage = new StringBuffer();

    @Override
    public void log(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
            mMessage.setLength(0);
            mMessage.append("<<<<<<<<<< Http log start >>>>>>>>>>\n");
            mMessage.append("=============================================================\n");
        }

        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = JsonUtil.formatJson(message);
        }

        mMessage.append(message.concat("\n"));

        if (message.startsWith("--> END POST") || message.startsWith("--> END GET")) {
            mMessage.append(" ┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄\n");
        }

        if (message.startsWith("<-- END HTTP") || message.startsWith("<-- HTTP FAILED")) {
            mMessage.append("=============================================================");
            HiAdsLog.d(LOG_TAG, mMessage.toString());
        }
    }


}
