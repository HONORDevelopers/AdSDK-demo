package com.hihonor.ads.group.sdk.base.net;

import android.os.Handler;
import android.os.Looper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CallBackWrapper<T> implements Callback<T> {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
   final public void onResponse(Call<T> call, Response<T> response) {
        handler.post(() -> onResult(call, response));
    }

    @Override
    final public void onFailure(Call<T> call, Throwable t) {
        handler.post(() -> onError(call, t));
    }

    abstract void onResult(Call<T> call, Response<T> response);

    abstract void onError(Call<T> call, Throwable t);
}
