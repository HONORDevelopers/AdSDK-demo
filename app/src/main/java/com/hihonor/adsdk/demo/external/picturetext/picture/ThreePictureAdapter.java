package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.Style;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.Constants;
import com.hihonor.adsdk.demo.external.utils.DensityUtil;

/**
 * 三图适配器
 *
 * @since 2022-11-28
 */
public class ThreePictureAdapter extends PictureBaseAdViewAdapter<ThreePictureAdapter.ThreePictureAdViewHolder> {
    private static final String LOG_TAG = "ThreePictureAdapter";
    private final PictureTextExpressAd pictureTextAd;

    public ThreePictureAdapter(@NonNull PictureTextExpressAd pictureTextAd) {
        this.pictureTextAd = pictureTextAd;
    }

    @Override
    public ThreePictureAdViewHolder createViewHolder(int viewType, Context context) {
        Style style = pictureTextAd.getStyle();
        int layoutRes = R.layout.self_render_three_top_picture_bottom_text;
        if (style != null) {
            int layout = style.getLayout();
            if (layout == Constants.LAYOUT.TOP_TEXT_BOTTOM_PICTURE) {
                layoutRes = R.layout.self_render_three_top_text_bottom_picture;
            }
        }
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        return new ThreePictureAdViewHolder(inflate);
    }

    @Override
    public void onBindDataToHolder(ThreePictureAdViewHolder viewHolder) {
        viewHolder.bindData(pictureTextAd);
    }

    static class ThreePictureAdViewHolder extends PictureBaseViewHolder {
        private final LinearLayout adThreePictureLayout;
        private final ImageView adImageView;
        private final ImageView adImageView2;
        private final ImageView adImageView3;

        public ThreePictureAdViewHolder(View mRootView) {
            super(mRootView);
            adThreePictureLayout = findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_three_picture_layout);
            adImageView = findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_image);
            adImageView2 = findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_image2);
            adImageView3 = findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_image3);
        }

        @Override
        protected boolean isDisplayLogo() {
            return false;
        }

        public void bindData(PictureTextExpressAd baseAd) {
            super.bindData(baseAd);
            if (baseAd == null) {
                return;
            }
            // 这里设置默认图片高度是防止广告被连续加入以RecyclerView为列表的容器中时，初始图片未加载时高度为0，
            // 导致本来不应该展示在屏幕的item，在刚addView时展示了一瞬间（速度很快，但是用户看不到）但实际从代码角度看
            // 是展示了的，实际这些item应该是在屏幕外的，这种情况会导致多曝光。
            int imgHeight = baseAd.getImgHeight();
            if (imgHeight == 0) {
                HiAdsLog.w(LOG_TAG, "server res image height is zero!");
                imgHeight = DensityUtil.px2dip(mRootView.getContext(), 150);
            }
            setViewHeight(adImageView, imgHeight);
            setViewHeight(adImageView2, imgHeight);
            setViewHeight(adImageView3, imgHeight);
            // 渲染文本视图 标题/品牌名/落地按钮文本/关闭按钮等
            renderTextView(baseAd);
            mRootView.post(() -> {
                Context context = mRootView.getContext();
                // 获取图片比例，如果获取不到，则三图默认是3:2
                float proportion = baseAd.getProportion() <= 0 ? (3f / 2f) : baseAd.getProportion();
                // 三图根布局左padding边距
                float paddingMaxStart = context.getResources().getDimension(R.dimen.ads_app_magic_dimens_max_start);
                // 三图根布局左padding边距
                float paddingMaxEnd = context.getResources().getDimension(R.dimen.ads_app_magic_dimens_max_end);
                int paddingHorLength = (int) (paddingMaxStart + paddingMaxEnd);
                // 三图父布局宽度 = 广告布局宽度 - 左右Padding
                int width = mRootView.getMeasuredWidth() - paddingHorLength;
                setViewWidth(adThreePictureLayout, width);
                // 三图每个图片的间距
                float horizontalImagePadding = context.getResources()
                        .getDimension(R.dimen.ads_app_magic_dimens_element_horizontal_middle);
                // 三图父布局宽度 / 3 = 单个图片宽度
                int imageWidth = (int) ((width - (horizontalImagePadding * 2)) / 3);
                int imageHeight = (int) (imageWidth / proportion);
                // 设置图1宽高
                setViewWidth(adImageView, imageWidth);
                setViewHeight(adImageView, imageHeight);
                // 设置图2宽高
                setViewWidth(adImageView2, imageWidth);
                setViewHeight(adImageView2, imageHeight);
                // 设置图3宽高
                setViewWidth(adImageView3, imageWidth);
                setViewHeight(adImageView3, imageHeight);

                setViewWidth(titleView, width);
                setViewWidth(adBrandAutoSizeLayout, width);

                // 加载图片
                loadImage(context, baseAd, baseAd.getImages(), 0, adImageView);
                loadImage(context, baseAd, baseAd.getImages(), 1, adImageView2);
                loadImage(context, baseAd, baseAd.getImages(), 2, adImageView3);
            });
        }
    }
}
