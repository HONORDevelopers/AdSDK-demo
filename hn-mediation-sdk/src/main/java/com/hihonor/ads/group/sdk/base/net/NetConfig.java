package com.hihonor.ads.group.sdk.base.net;

public interface NetConfig {
    //grs相关
    String GRS_FLAVOR_PRODUCT = "product";
    String GRS_BIZ_NAME = "commonService.adSdk";//虚拟应用名称，不参与初始化传值，否则会导致ayncGetGrsUrl失败
    String GRS_REQUEST_SERVER_NAME = "com.hihonor.adsdk.server";//接口请求服务唯一名称
    String GRS_HA_SERVER_NAME = "com.hihonor.adsdk.hianalytics";//HA打点接口请求服务唯一名称
    String GRS_KEY = "ROOT";//服务其地址对应的键值
}
