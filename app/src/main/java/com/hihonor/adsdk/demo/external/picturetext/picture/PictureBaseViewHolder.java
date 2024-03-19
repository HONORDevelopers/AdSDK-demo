/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2023. All rights reserved.
 */

/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2023. All rights reserved.
 */

package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.Style;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.download.HnDownloadButton;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.GlideUtils;
import com.hihonor.adsdk.demo.external.utils.ScreenUtils;

/**
 * 功能描述
 *
 * @since 2022-12-23
 */
 abstract class PictureBaseViewHolder extends PictureDownloadViewHolder {
    private static final String LOG_TAG = "PictureBaseViewHolder";
    protected RelativeLayout adBrandAutoSizeLayout;
    protected ImageView brandLogoView;
    protected TextView brandNameTextView;
    protected View adLandingLayout;
    protected HnDownloadButton downLoadButton;

    public PictureBaseViewHolder(View mRootView) {
        super(mRootView);
        adBrandAutoSizeLayout = findViewById(R.id.ad_auto_size_layout);
        titleView = findViewById(R.id.ad_title);
        brandLogoView = findViewById(R.id.ad_brand_logo);
        brandNameTextView = findViewById(R.id.ad_brand_name);
        adLandingLayout = findViewById(R.id.ad_landing);
        adFlagView = findViewById(R.id.ad_flag_view);
        adFlagCloseView = findViewById(R.id.ad_close_view);
        downLoadButton = findViewById(R.id.ad_download);
        brandLogoView.setVisibility(isDisplayLogo() ? View.VISIBLE : View.GONE);
    }

    protected void renderTextView(@NonNull PictureTextExpressAd baseAd) {
        HiAdsLog.i(LOG_TAG, "render text view, set text data.");
        Context context = mRootView.getContext();
        brandNameTextView.setText(baseAd.getBrand());

        if (isDisplayLogo()) {
            Style style = baseAd.getStyle();
            int radius = 0;
            if (style != null) {
                radius = style.getBorderRadius();
            }
            GlideUtils.loadImage(context, baseAd.getLogo(), brandLogoView, radius);
        }
        // 设置title
        titleView.setText(baseAd.getTitle());

        int adFlag = baseAd.getAdFlag();
        adFlagView.setVisibility(adFlag == 0 ? View.INVISIBLE : View.VISIBLE);
        if (adFlag == 0) {
            setViewWidth(adFlagView, 0);
            setViewHeight(adFlagView, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) adLandingLayout.getLayoutParams();
            float marginStart = context.getResources().getDimension(R.dimen.ads_app_magic_dimens_element_horizontal_middle);
            layoutParams.setMarginStart((int) marginStart);
            HiAdsLog.i(LOG_TAG, "adFlag is OFF");
        } else {
            // 广告标签背景色适配深色模式，备注：UX未在参数表中找到对应的资源id，无法使用uikit资源直接适配，需手动判断。
            int color = ScreenUtils.isDarkTheme() ? 0x33FFFFFF : 0x33000000;
            adFlagView.setBgColor(color);
        }

        int closeFlag = baseAd.getCloseFlag();
        adFlagCloseView.setVisibility(closeFlag == 0 ? View.INVISIBLE : View.VISIBLE);
        if (closeFlag == 0) {
            setViewWidth(adFlagCloseView, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) adLandingLayout.getLayoutParams();
            layoutParams.setMarginEnd(0);
            HiAdsLog.i(LOG_TAG, "closeFlag is OFF");
        } else {
            adFlagCloseView.setBgColor(context.getResources().getColor(R.color.ads_app_magic_button_default));
            Drawable closeIconDrawable = context.getDrawable(
                    com.hihonor.adsdk.base.R.drawable.ic_honor_ads_close_black);
            adFlagCloseView.setCloseIconDrawable(closeIconDrawable);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) adLandingLayout.getLayoutParams();
            float marginEnd = context.getResources().getDimension(com.hihonor.adsdk.picturetextad.R.dimen.honor_ads_magic_dimens_element_horizontal_middle);
            layoutParams.setMarginEnd((int) marginEnd);
        }
        showDownloadButton(baseAd);
    }

    private void showDownloadButton(PictureTextExpressAd baseAd) {
        downLoadButton.setVisibility(View.VISIBLE);
        downLoadButton.setBaseAd(baseAd);
    }
}
