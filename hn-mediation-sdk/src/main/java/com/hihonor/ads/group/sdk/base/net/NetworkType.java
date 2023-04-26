package com.hihonor.ads.group.sdk.base.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;




public class NetworkType {
    /**
     * int value: 0
     */
    public static final int NOT_AVAILABLE = 0;
    /**
     * int value: 3
     */
    public static final int WIFI = 3;
    /**
     * int value: 1
     */
    public static final int GPRS_2G = 1;
    /**
     * int value: 2
     */
    public static final int GPRS_3G = 2;
    /**
     * int value: 4
     */
    public static final int GPRS_4G = 4;
    /**
     * int value: 5
     */
    public static final int GPRS_5G = 5;


    /**
     * string value: "wifi"
     */
    private static final String TYPE_WIFI = "wifi";
    /**
     * string value: "unknown"
     */
    private static final String TYPE_NO_NET = "unknown";
    /**
     * string value: "2G"
     */
    private static final String TYPE_2G = "2G";
    /**
     * string value: "3G"
     */
    private static final String TYPE_3G = "3G";
    /**
     * string value: "4G"
     */
    private static final String TYPE_4G = "4G";
    /**
     * string value: "5G"
     */
    private static final String TYPE_5G = "5G";


    private static final int NETWORK_TYPE_WIFI = -101;

    /**
     * for get current available and connected network type
     *
     * @param context
     * @return {@link #NOT_AVAILABLE}<br/>
     * {@link #WIFI}<br/>
     * {@link #GPRS_2G}<br/>
     * {@link #GPRS_3G}<br/>
     * {@link #GPRS_4G}<br/>
     * {@link #GPRS_5G}<br/>
     */
    @SuppressLint("MissingPermission")
    public static int getNetworkType(Context context) {

        int networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
        try {
            ConnectivityManager connectMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null && info.isAvailable() && info.isConnected()) {
                int type = info.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                return NOT_AVAILABLE;
            }

        } catch (Exception ex) {
        }
        return getNetworkClassByType(networkType);
    }


    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_WIFI:
                return WIFI;

            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return GPRS_2G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return GPRS_3G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return GPRS_4G;

            default:
                return GPRS_5G;
        }
    }


    /**
     * check current network is available or not
     *
     * @param context
     * @return if network is available, return true<br/>
     * if network is not available, return false
     */
    public static boolean isNetworkAvailable(Context context) {
        int currType = getNetworkType(context);
        if (currType == NOT_AVAILABLE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * check wifi is available or not
     *
     * @param context
     * @return if wifi is available, return true<br/>
     * if wifi is not available, return false
     */
    public static boolean isWifiAvailable(Context context) {
        int currType = getNetworkType(context);
        if (currType == WIFI) {
            return true;
        } else {
            return false;
        }
    }



    /**
     * for get current available network type description string
     *
     * @param context
     * @return {@link #TYPE_NO_NET}<br/>
     * {@link #TYPE_WIFI}<br/>
     * {@link #TYPE_2G}<br/>
     * {@link #TYPE_3G}<br/>
     */
    public static String getNetTypeString(Context context) {
        int currType = getNetworkType(context);
        switch (currType) {
            case NOT_AVAILABLE:
                return TYPE_NO_NET;
            case WIFI:
                return TYPE_WIFI;
            case GPRS_2G:
                return TYPE_2G;
            case GPRS_3G:
                return TYPE_3G;
            case GPRS_4G:
                return TYPE_4G;
            case GPRS_5G:
                return TYPE_5G;
        }

        return "unknown";
    }
}
