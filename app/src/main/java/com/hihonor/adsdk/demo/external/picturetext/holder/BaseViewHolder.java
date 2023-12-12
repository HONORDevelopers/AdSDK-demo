package com.hihonor.adsdk.demo.external.picturetext.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.Style;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;
import com.hihonor.adsdk.base.widget.download.DownLoadButton;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.DensityUtil;
import com.hihonor.adsdk.demo.external.utils.GlideUtils;
import com.hihonor.adsdk.demo.external.utils.ScreenUtils;

import java.util.List;

/**
 * 功能描述
 *
 * @since 2023-11-21
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = "BaseViewHolder";
    protected View mRootView;
    protected RelativeLayout adBrandAutoSizeLayout;
    protected TextView brandNameTextView;
    protected View adLandingLayout;
    protected DownLoadButton downLoadButton;
    protected float cornerRadius;
    protected TextView titleView;
    protected AdFlagCloseView adFlagView;
    protected AdFlagCloseView adFlagCloseView;
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mRootView = itemView;
        adBrandAutoSizeLayout = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_auto_size_layout);
        brandNameTextView = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_brand_name);
        adLandingLayout = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_landing);
        downLoadButton = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_download);
        titleView = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_title);
        adFlagView = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_flag_view);
        adFlagCloseView = itemView.findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_close_view);
    }

    protected void bindData(@NonNull PictureTextExpressAd baseAd) {
        if (baseAd == null) {
            return;
        }
        adFlagCloseView.setDislikeItemClickListener((i, dislikeInfo, view) -> {
            ViewParent parent = mRootView.getParent();
            if (parent != null) {
                ((ViewGroup)parent).removeView(mRootView);
            }
        });
        renderTextView(baseAd);
    }
    protected  <T extends View> T findViewById(@IdRes int id) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }
    protected void renderTextView(@NonNull PictureTextExpressAd baseAd) {
        HiAdsLog.i(LOG_TAG, "render text view, set text data.");
        Context context = mRootView.getContext();
        brandNameTextView.setText(baseAd.getBrand());
        // 设置圆角
        cornerRadius = context.getResources().getDimension(R.dimen.ads_app_magic_corner_radius_small);
        Style style = baseAd.getStyle();
        if (style != null){
            cornerRadius = DensityUtil.dip2px(context, style.getBorderRadius());
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

    public void setViewHeight(View target, int height) {
        HiAdsLog.i(LOG_TAG, "Call set view height.");
        if (target == null) {
            HiAdsLog.w(LOG_TAG, "target view is null!");
            return;
        }
        ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
        layoutParams.height = height;
        target.setLayoutParams(layoutParams);

    }
    public void setViewWidth(View target, int width) {
        HiAdsLog.i(LOG_TAG, "Call set view width.");
        if (target == null) {
            HiAdsLog.w(LOG_TAG, "target view is null!");
            return;
        }
        ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
        layoutParams.width = width;
        target.setLayoutParams(layoutParams);
    }

    public void loadImage(Context context, PictureTextExpressAd baseAd, List<String> images, int position, ImageView adImageView, int cornerRadius) {
        HiAdsLog.i(LOG_TAG, "Call load image.");
        if (baseAd == null || adImageView == null) {
            HiAdsLog.w(LOG_TAG, "baseAd or adImageView is null!");
            return;
        }
        String imgUrl = null;
        if (images != null && images.size() > position) {
            imgUrl = images.get(position);
        }
        GlideUtils.loadImage(context, imgUrl, adImageView, cornerRadius);
    }
}
