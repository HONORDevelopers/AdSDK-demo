/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.utils;



import static com.hihonor.ads.group.sdk.util.log.HiLogger.ASSERT;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.DEBUG;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.ERROR;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.INFO;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.VERBOSE;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.WARN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * 为一些常见的操作提供方便的方法
 */
public final class Utils {

    private Utils() {
        // Hidden constructor.
    }

    /**
     * 获取异常堆栈信息
     *
     * @return Stack trace in form of String
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.append("Cause by:");
        pw.println(tr.getMessage());
        pw.flush();
        return sw.toString();
    }

    public static String logLevel(int value) {
        switch (value) {
            case VERBOSE:
                return "V";
            case DEBUG:
                return "D";
            case INFO:
                return "I";
            case WARN:
                return "W";
            case ERROR:
                return "E";
            case ASSERT:
                return "A";
            default:
                return "UNKNOWN";
        }
    }

    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }
}
