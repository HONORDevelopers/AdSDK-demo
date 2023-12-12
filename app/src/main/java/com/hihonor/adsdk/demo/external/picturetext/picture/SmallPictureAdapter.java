package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.Style;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.Constants;

/**
 * 小图适配器
 *
 * @since 2022-11-28
 */
public class SmallPictureAdapter extends PictureBaseAdViewAdapter<PictureDownloadViewHolder> {
    private static final String LOG_TAG = "SmallPictureAdapter";
    private final PictureTextExpressAd pictureTextAd;

    public SmallPictureAdapter(@NonNull PictureTextExpressAd pictureTextAd) {
        this.pictureTextAd = pictureTextAd;
    }

    @Override
    public PictureDownloadViewHolder createViewHolder(int viewType, Context context) {
        int adSpecTemplateType = pictureTextAd.getAdSpecTemplateType();
        int layoutRes = R.layout.self_render_left_text_right_picture;
        if (adSpecTemplateType == Constants.TEMPLATE_TYPE.TEMPLATE_1) {
            Style style = pictureTextAd.getStyle();
            if (style != null) {
                int layout = style.getLayout();
                if (layout == Constants.LAYOUT.RIGHT_TEXT_LEFT_PICTURE) {
                    layoutRes = R.layout.self_render_left_picture_right_text;
                }
            }
        } else if (adSpecTemplateType == Constants.TEMPLATE_TYPE.TEMPLATE_2) {
            layoutRes = R.layout.self_render_left_picture_right_download;
            View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
            return new SmallPictureDownloadViewHolder(inflate);
        }
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        return new SmallPictureViewHolder(inflate);
    }

    @Override
    public void onBindDataToHolder(PictureDownloadViewHolder viewHolder) {
        viewHolder.bindData(pictureTextAd);
    }
}
