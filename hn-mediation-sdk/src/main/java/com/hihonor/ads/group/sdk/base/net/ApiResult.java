/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.base.net;


import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ApiResult<T> {
    @Expose
    @SerializedName("code")
    @Keep
    public String errorCode;

    @Expose
    @SerializedName("message")
    @Keep
    public String errorMessage;

    @Keep
    public T data;

    public boolean isSuccess() {
        return Constant.ERROR.SUCCESS.equals(this.errorCode);
    }
}
