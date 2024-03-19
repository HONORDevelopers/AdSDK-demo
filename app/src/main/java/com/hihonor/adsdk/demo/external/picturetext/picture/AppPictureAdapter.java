package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.demo.external.R;

public class AppPictureAdapter extends PictureBaseAdViewAdapter<PictureDownloadViewHolder> {
    private static final String LOG_TAG = "AppPictureAdapter";
    private final PictureTextExpressAd pictureTextAd;

    public AppPictureAdapter(@NonNull PictureTextExpressAd pictureTextAd) {
        this.pictureTextAd = pictureTextAd;
    }

    @Override
    public PictureDownloadViewHolder createViewHolder(int viewType, Context context) {
        int layoutRes = R.layout.self_render_left_picture_right_download;
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        return new SmallPictureDownloadViewHolder(inflate);
    }

    @Override
    public void onBindDataToHolder(PictureDownloadViewHolder viewHolder) {
        viewHolder.bindData(pictureTextAd);
    }
}