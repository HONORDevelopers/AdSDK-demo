package com.hihonor.ads.group.sdk.base.net;

import android.content.Context;
import android.text.TextUtils;


import com.hihonor.ads.group.sdk.base.net.core.GRSIntercept;
import com.hihonor.ads.group.sdk.base.net.core.GrsConfig;
import com.hihonor.ads.group.sdk.base.net.core.XHttpContext;
import com.hihonor.ads.group.sdk.base.net.util.SafeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class XHttp {

    private Retrofit mRetrofit;
    private static final String TAG = "XHttp";
    private final boolean mGrsSupport;
    private final GrsConfig mGrsConfig;
    private final Context mContext;
    private final List<Interceptor> mInterceptorList;
    private final List<Converter.Factory> mConverterFactoryList;
    private final List<CallAdapter.Factory> mCallAdapterFactories;
    private final HttpLoggingInterceptor.Level logLevel;
    private final XHttpContext mXhttpContext;
    private final HostnameVerifier mHostnameVerifier;
    private final SSLSocketFactory mSSLSocketFactory;
    private final X509TrustManager mX509TrustManager;

    private XHttp(Builder builder) {
        mContext = builder.context.getApplicationContext();
        mXhttpContext = new XHttpContext();
        mGrsSupport = builder.supportGrs;
        mXhttpContext.setContext(mContext);
        mXhttpContext.setTokenCheckDuration(builder.tokenCheckDuration);
        mXhttpContext.setUuid(builder.uuid);
        mXhttpContext.setLogLevel(builder.logLevel);
        mInterceptorList = builder.interceptorList;
        mConverterFactoryList = builder.converterFactoryList;
        mCallAdapterFactories = builder.callAdapterFactories;
        mHostnameVerifier = builder.hostnameVerifier;
        mSSLSocketFactory = builder.sslsocketFactory;
        mX509TrustManager = builder.x509TrustManager;
        logLevel = builder.logLevel;
        mGrsConfig = builder.grsConfig;
        SafeUtil.init(builder.context);
        OkHttpClient client = initOkHttpClient(builder);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        String baseUrl = builder.baseUrl;
        retrofitBuilder.baseUrl(baseUrl);
        mXhttpContext.setBaseUrl(baseUrl);

        retrofitBuilder.client(client);
        if (mConverterFactoryList != null && !mConverterFactoryList.isEmpty()) {
            for (Converter.Factory factory : mConverterFactoryList) {
                if (!(factory instanceof GsonConverterFactory)) {
                    retrofitBuilder.addConverterFactory(factory);
                }
            }
        }

        if (mCallAdapterFactories != null && !mCallAdapterFactories.isEmpty()) {
            for (CallAdapter.Factory factory : mCallAdapterFactories) {
                retrofitBuilder.addCallAdapterFactory(factory);
            }
        }

        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        mRetrofit = retrofitBuilder.build();
    }

    public final Context getContext() {
        return mContext;
    }

    public final Retrofit getRetrofit() {
        return mRetrofit;
    }

    private boolean isLogDebug() {
        if (logLevel == null) {
            return false;
        }
        return logLevel == HttpLoggingInterceptor.Level.BODY;
    }


    private OkHttpClient initOkHttpClient(Builder xHttpBuilder) {
        OkHttpClient.Builder okhttpBuilder = xHttpBuilder.okHttpBuilder;
        if (okhttpBuilder == null) {
            okhttpBuilder = new OkHttpClient.Builder();
            okhttpBuilder.connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            if (mSSLSocketFactory != null && mX509TrustManager != null) {
                okhttpBuilder.sslSocketFactory(mSSLSocketFactory, mX509TrustManager);
            } else if (mSSLSocketFactory != null) {
                okhttpBuilder.sslSocketFactory(mSSLSocketFactory);
            }
            if (mHostnameVerifier != null) {
                okhttpBuilder.hostnameVerifier(mHostnameVerifier);
            }
        }

        if (mGrsSupport) {
            okhttpBuilder.addInterceptor(new GRSIntercept(mContext, xHttpBuilder.baseUrl, mGrsConfig, mXhttpContext));
        }

        if (mInterceptorList != null && !mInterceptorList.isEmpty()) {
            for (Interceptor interceptor : mInterceptorList) {
                okhttpBuilder.addInterceptor(interceptor);
            }
        }

        return okhttpBuilder.build();
    }

    public static class Builder {
        private Context context;
        //安全相关
        private HostnameVerifier hostnameVerifier;
        private SSLSocketFactory sslsocketFactory;
        private X509TrustManager x509TrustManager;
        //grs相关参数配置
        private boolean supportGrs;
        private String baseUrl;
        private GrsConfig grsConfig;
        //huks相关参数配置
        private String uuid;
        private int apiVersion;
        private long tokenCheckDuration;

        //Retorfit构建相关配置
        private List<Interceptor> interceptorList;
        private List<Converter.Factory> converterFactoryList;
        private List<CallAdapter.Factory> callAdapterFactories;
        private HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.NONE;
        private OkHttpClient.Builder okHttpBuilder;

        public Builder(Context context) {
            this.context = context;
            this.supportGrs = true;
            this.apiVersion = 0;
        }

        public Builder setApiVersion(int version) {
            this.apiVersion = version;
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslsocketFactory) {
            this.sslsocketFactory = sslsocketFactory;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public Builder sslSocketFactory(X509TrustManager x509TrustManager) {
            this.x509TrustManager = x509TrustManager;
            return this;
        }

        public Builder disableGrs() {
            this.supportGrs = false;
            return this;
        }

        public Builder setGrsConfig(GrsConfig grsConfig) {
            this.grsConfig = grsConfig;
            return this;
        }

        /**
         * 设置Retrofit的baseUrl；如果grs关闭，该方法必须调用
         *
         * @param baseUrl 请求的基础url
         * @return
         */
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }


        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setTokenCheckDuration(long duration) {
            this.tokenCheckDuration = duration;
            return this;
        }


        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptorList == null) {
                interceptorList = new ArrayList<>();
            }
            interceptorList.add(interceptor);
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            if (converterFactoryList == null) {
                converterFactoryList = new ArrayList<>();
            }
            converterFactoryList.add(factory);
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            if (callAdapterFactories == null) {
                callAdapterFactories = new ArrayList<>();
            }
            callAdapterFactories.add(factory);
            return this;
        }

        public Builder setLogLevel(HttpLoggingInterceptor.Level level) {
            this.logLevel = level;
            return this;
        }

        public Builder setOkHttpClientBuilder(OkHttpClient.Builder builder) {
            this.okHttpBuilder = builder;
            return this;
        }

        public XHttp build() {
            return new XHttp(this);
        }
    }
}
