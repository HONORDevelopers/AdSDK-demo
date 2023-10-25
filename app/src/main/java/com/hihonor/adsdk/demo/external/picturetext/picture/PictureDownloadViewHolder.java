package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.Style;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.DensityUtil;
import com.hihonor.adsdk.demo.external.utils.GlideUtils;

import java.util.List;

abstract class PictureDownloadViewHolder extends PictureBaseAdViewAdapter.ViewHolder {
   private static final String LOG_TAG = "PictureDownloadViewHolder";
   protected float cornerRadius;
   protected TextView titleView;
   protected AdFlagCloseView adFlagView;

   public PictureDownloadViewHolder(View mRootView) {
       super(mRootView);
   }

   protected void bindData(PictureTextExpressAd baseAd) {
       if (baseAd == null) {
           return;
       }
       Context context = mRootView.getContext();
       // 设置圆角
       cornerRadius = context.getResources().getDimension(R.dimen.ads_app_magic_corner_radius_small);
       Style style = baseAd.getStyle();
       if (style != null){
           cornerRadius = DensityUtil.dip2px(context, style.getBorderRadius());
       }
   }

   protected abstract boolean isDisplayLogo();

   protected void setViewHeight(View target, int height) {
       HiAdsLog.i(LOG_TAG, "Call set view height.");
       if (target == null) {
           HiAdsLog.w(LOG_TAG, "target view is null!");
           return;
       }
       ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
       layoutParams.height = height;
       target.setLayoutParams(layoutParams);

   }
   protected void setViewWidth(View target, int width) {
       HiAdsLog.i(LOG_TAG, "Call set view width.");
       if (target == null) {
           HiAdsLog.w(LOG_TAG, "target view is null!");
           return;
       }
       ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
       layoutParams.width = width;
       target.setLayoutParams(layoutParams);
   }

   protected void loadImage(Context context, PictureTextExpressAd baseAd, List<String> images, int position, ImageView adImageView) {
       HiAdsLog.i(LOG_TAG, "Call load image.");
       if (baseAd == null || adImageView == null) {
           HiAdsLog.w(LOG_TAG, "baseAd or adImageView is null!");
           return;
       }
       String imgUrl = null;
       if (images != null && images.size() > position) {
           imgUrl = images.get(position);
       }
       GlideUtils.loadImage(context, imgUrl, adImageView, (int) cornerRadius);
   }
}
