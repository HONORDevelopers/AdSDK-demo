package com.hihonor.ads.group.sdk;

import android.content.Context;

public class HnGroupAds {
    private Context mContext;

    private HnGroupAds() {

    }

    private static final class HOLDER {
        private static final HnGroupAds instance = new HnGroupAds();
    }

    public static HnGroupAds get() {
        return HOLDER.instance;
    }

    public Context getContext() {
        return mContext;
    }
}
