package com.hihonor.ads.group.sdk.base.net;

public interface Constant {
    String NET_TAG = "NET_HTTP";
    interface TYPE {
        String POST = "POST";;
        String GET = "GET";
    }

    interface ERROR {
        String SUCCESS = "0";;
        String IO_ERROR = "10001";
        String RESP_ERROR = "10002";
        String RESP_NULL = "resp is null";
    }


}
