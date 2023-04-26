/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.base.net;

import android.text.TextUtils;

import com.hihonor.ads.group.sdk.util.log.HiAdsLog;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * 功能描述
 *
 * @since 2022-08-03
 */
public class Header {
    private static final String LOG_TAG = "Header";
    private String nonce;

    private long timestamp;

    private String requestId;

    private String signType;

    private String spId;

    private String token;

    private String mediaPkgName;

    private String mediaAppVersion;

    private String oaid;

    private String honorOaid;

    public Header(Builder builder) {
        this.nonce = builder.nonce;
        this.timestamp = builder.timestamp;
        this.requestId = builder.requestId;
        this.signType = builder.signType;
        this.spId = builder.spId;
        this.token = builder.token;
        this.mediaPkgName = builder.mediaPkgName;
        this.mediaAppVersion = builder.mediaAppVersion;
        this.oaid = builder.oaid;
        this.honorOaid = builder.honorOaid;
    }

    public String getNonce() {
        return nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSignType() {
        return signType;
    }

    public String getSpId() {
        return spId;
    }

    public String getToken() {
        return token;
    }

    public String getMediaPkgName() {
        return mediaPkgName;
    }

    public String getMediaAppVersion() {
        return mediaAppVersion;
    }

    public LinkedHashMap<String, String> getHeaders() {
        // 注意：这里请求头中的字段顺序是按照字典顺序来排序， a-z这个顺序。
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("x-app-version", getNoNullValue(mediaAppVersion));
        headers.put("x-oaid", getNoNullValue(oaid));
        headers.put("x-oaid-honor", getNoNullValue(honorOaid));
        headers.put("x-pkg-name", getNoNullValue(mediaPkgName));
        headers.put("x-ra-nonce", getNoNullValue(nonce));
        headers.put("x-ra-timestamp", timestamp + "");
        headers.put("x-request-id", getNoNullValue(requestId));
        headers.put("x-sign-type", "HmacSHA256");
        headers.put("x-sp-id", getNoNullValue(spId));
        headers.put("x-token", getNoNullValue(token));
//        HnAdConfig cfg = HnGroupAds.get().getCfg();
//        if (cfg != null && cfg.isDebug()) {
//            headers.put("x-visit-mark", "mock");
//        }
        return headers;
    }

    private String getNoNullValue(String value) {
        return value == null ? "" : value;
    }

    public static class Builder {
        private String nonce;

        private long timestamp;

        private String requestId;

        private String signType;

        private String spId;

        private String token;

        private String mediaPkgName;

        private String mediaAppVersion;

        private String oaid;

        private String honorOaid;

        public Builder setNonce(String nonce) {
            this.nonce = getNoNullValue(nonce);
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setRequestId(String requestId) {
            this.requestId = getNoNullValue(requestId);
            return this;
        }

        public Builder setSignType(String signType) {
            this.signType = getNoNullValue(signType);
            return this;
        }

        public Builder setSpId(String spId) {
            this.spId = getNoNullValue(spId);
            return this;
        }

        public Builder setToken(String token) {
            this.token = getNoNullValue(token);
            return this;
        }

        public Builder setMediaPkgName(String mediaPkgName) {
            this.mediaPkgName = getNoNullValue(mediaPkgName);
            return this;
        }

        public Builder setMediaAppVersion(String mediaAppVersion) {
            this.mediaAppVersion = getNoNullValue(mediaAppVersion);
            return this;
        }

        public Builder setOaid(String oaid) {
            this.oaid = getNoNullValue(oaid);
            return this;
        }

        public Builder setHonorOaid(String honorOaid) {
            this.honorOaid = getNoNullValue(honorOaid);
            return this;
        }

        private String getNoNullValue(String value) {
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
            return "";
        }

        public Header build() {
            return new Header(this);
        }

        public String getToken() {
            JSONObject jsonObject = new JSONObject();
            try {
                // 注意：这里请求头中的字段顺序是按照字典顺序来排序， a-z这个顺序。oaid不参与签名
                jsonObject.put("x-app-version", mediaAppVersion);
                jsonObject.put("x-pkg-name", mediaPkgName);
                jsonObject.put("x-ra-nonce", requestId);
                jsonObject.put("x-ra-timestamp", timestamp + "");
                jsonObject.put("x-request-id", requestId);
                jsonObject.put("x-sign-type", "HmacSHA256");
                jsonObject.put("x-sp-id", spId);
                if (!TextUtils.isEmpty(token)) {
                    jsonObject.put("x-token", token);
                }
//                HnAdConfig cfg = HnGroupAds.get().getCfg();
//                if (cfg != null && cfg.isDebug()) {
//                    jsonObject.put("x-visit-mark", "mock");
//                }
            } catch (JSONException e) {
                HiAdsLog.w(LOG_TAG, "Header builder get token parse json exception. msg is %s", e.getMessage());
            }
            return jsonObject.toString();
        }
    }
}
