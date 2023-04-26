package com.hihonor.ads.group.sdk.base.net;



/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */



import com.hihonor.ads.group.sdk.ErrorCode;
import com.hihonor.ads.group.sdk.util.log.HiAdsLog;

import retrofit2.Call;
import retrofit2.Response;

public class ApiCall<RESP> implements ICall<RESP> {
    protected Call<ApiResult<RESP>> mCall;

    ApiCall(Call<ApiResult<RESP>> call) {
        mCall = call;
    }

    @Override
    public ApiResult<RESP> execute() {
        ApiResult errorResp = new ApiResult();
        try {
            Response<ApiResult<RESP>> rspResponse = mCall.execute();
            ApiResult<RESP> resp = rspResponse.body();
            if (resp == null) {
                errorResp.errorCode = Constant.ERROR.RESP_ERROR;
                errorResp.errorMessage = Constant.ERROR.RESP_NULL;
                return errorResp;
            }
            HiAdsLog.i(Constant.NET_TAG, "origin ApiResult#execute#errorMessage = " + resp.errorMessage
                    + ", code=" + resp.errorCode);
            return resp;
        } catch (Exception e) {
            errorResp.errorMessage = "Exception:" + e.getMessage();
            errorResp.errorCode = Constant.ERROR.IO_ERROR;

        }
        return errorResp;
    }

    @Override
    public void enqueue(HttpCallback<RESP> apiCallback) {
        mCall.enqueue(new CallBackWrapper<ApiResult<RESP>>() {
            @Override
            void onResult(Call<ApiResult<RESP>> call, Response<ApiResult<RESP>> response) {
                ApiResult<RESP> resp = response.body();
                HiAdsLog.i(Constant.NET_TAG, "origin ApiResult#onResult#message = "+
                        response.message() + ", code=" + response.code());
                if (apiCallback == null) {
                    return;
                }
                if (resp == null) {
                    HiAdsLog.i(Constant.NET_TAG, "Resp is null");
                    // 响应为空
                    apiCallback.onException(String.valueOf(ErrorCode.RESPONSE_FAIL),  new Throwable(Constant.ERROR.RESP_NULL));
                    return;
                }


                // 网络请求响应成功
                apiCallback.onResult(resp);
            }

            @Override
            void onError(Call<ApiResult<RESP>> call, Throwable t) {
                if (apiCallback != null) {
                    apiCallback.onException(String.valueOf(ErrorCode.TRACK.REQ_ERR),  t);
                }
            }
        });
    }


    @Override
    public void cancel() {
        mCall.cancel();
    }

    @Override
    public boolean isExecuted() {
        if (mCall != null) {
            return mCall.isExecuted();
        }
        return false;
    }

    @Override
    public boolean isCanceled() {
        if (mCall != null) {
            return mCall.isCanceled();
        }
        return false;
    }


}
