package com.hihonor.ads.group.sdk.base.net;


public interface HttpCallback<OUT> {
    void onResult(ApiResult<OUT> resp);

    void onException(String errorCode, Throwable throwable);
}
