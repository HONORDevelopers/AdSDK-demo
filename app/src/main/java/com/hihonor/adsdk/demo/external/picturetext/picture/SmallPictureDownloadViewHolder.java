package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.ScreenUtils;

/**
 * 小图文下载view
 */
class SmallPictureDownloadViewHolder extends PictureDownloadViewHolder {
    private static final int DP_VALUE = 4;
    private final ImageView adImageView;

    private final TextView adTitle, adContent;

    public SmallPictureDownloadViewHolder(View mRootView) {
        super(mRootView);
        adImageView = mRootView.findViewById(R.id.ad_image);
        adTitle = mRootView.findViewById(R.id.ad_title);
        adContent = mRootView.findViewById(R.id.ad_content);
        adFlagView = mRootView.findViewById(R.id.ad_flag_view);
        adFlagCloseView = mRootView.findViewById(R.id.ad_close_view);
    }

    @Override
    protected boolean isDisplayLogo() {
        return false;
    }

    @Override
    public void bindData(PictureTextExpressAd baseAd) {
        super.bindData(baseAd);

        Context context = mRootView.getContext();

        setPictureImage(baseAd);
        // 应用下载广告的主标题对应的字段是品牌名称，副标题对应的字段是广告标题
        adTitle.setText(baseAd.getBrand());
        adContent.setText(baseAd.getTitle());

        adFlagView.setRectCornerRadius(ScreenUtils.dpToPx(DP_VALUE));
        adFlagView.setViewPadding(ScreenUtils.dpToPx(DP_VALUE), 0, ScreenUtils.dpToPx(DP_VALUE), 0);
        adFlagView.setVisibility(baseAd.getAdFlag() == 0 ? View.GONE : View.VISIBLE);
        // 广告标签背景色适配深色模式，备注：UX未在参数表中找到对应的资源id，无法使用uikit资源直接适配，需手动判断。
        int color = ScreenUtils.isDarkTheme() ? 0x33FFFFFF : 0x33000000;
        adFlagView.setBgColor(color);

        adFlagCloseView.setVisibility(baseAd.getCloseFlag() == 0 ? View.GONE : View.VISIBLE);
        adFlagCloseView.setBgColor(context.getResources().getColor(R.color.ads_app_magic_color_bg_translucent));
        adFlagCloseView.setViewPadding(0, 0, 0, 0);
        Drawable closeIconDrawable = context.getDrawable(R.drawable.honor_ads_icsvg_public_cancel_regular);
        adFlagCloseView.setCloseIconDrawable(closeIconDrawable);
    }

    private void setPictureImage(PictureTextExpressAd baseAd) {
        mRootView.post(()->{
            Context context = mRootView.getContext();
            // 加载图片
            loadImage(context, baseAd, baseAd.getImages(), 0, adImageView);
        });
    }
}
