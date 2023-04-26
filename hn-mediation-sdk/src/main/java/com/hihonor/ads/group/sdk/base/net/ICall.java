package com.hihonor.ads.group.sdk.base.net;


public interface ICall<RESP>{
    ApiResult<RESP> execute();

    void enqueue(HttpCallback<RESP> httpCallback);

    void cancel();

    boolean isExecuted();

    boolean isCanceled();
}
