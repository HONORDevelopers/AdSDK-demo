package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.Style;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.Constants;
import com.hihonor.adsdk.demo.external.utils.DensityUtil;

/**
 * 大图适配器
 *
 * @since 2022-11-28
 */
public class BigPictureAdapter extends PictureBaseAdViewAdapter<BigPictureAdapter.BigPictureAdViewHolder> {
    private static final String LOG_TAG = "BigPictureAdapter";
    private final PictureTextExpressAd pictureTextAd;

    public BigPictureAdapter(@NonNull PictureTextExpressAd pictureTextAd) {
        this.pictureTextAd = pictureTextAd;
    }

    @Override
    public BigPictureAdViewHolder createViewHolder(int viewType, Context context) {
        Style style = pictureTextAd.getStyle();
        int layoutRes = R.layout.self_render_top_picture_bottom_text;
        if (style != null) {
            int layout = style.getLayout();
            if (layout == Constants.LAYOUT.TOP_TEXT_BOTTOM_PICTURE) {
                layoutRes = R.layout.self_render_top_text_bottom_picture;
            }
        }
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        return new BigPictureAdViewHolder(inflate);
    }

    @Override
    public void onBindDataToHolder(BigPictureAdViewHolder viewHolder) {
        viewHolder.bindData(pictureTextAd);
    }

    static class BigPictureAdViewHolder extends PictureBaseViewHolder {
        private final ImageView adImageView;

        public BigPictureAdViewHolder(View mRootView) {
            super(mRootView);
            adImageView = findViewById(com.hihonor.adsdk.picturetextad.R.id.ad_image);

        }

        @Override
        protected boolean isDisplayLogo() {
            return true;
        }

        public void bindData(PictureTextExpressAd baseAd) {
            super.bindData(baseAd);
            if (baseAd == null) {
                HiAdsLog.w(LOG_TAG, "baseAd is null.");
                return;
            }
            // 这里设置默认图片高度是防止广告被连续加入以RecyclerView为列表的容器中时，初始图片未加载时高度为0，
            // 导致本来不应该展示在屏幕的item，在刚addView时展示了一瞬间（速度很快，但是用户看不到）但实际从代码角度看
            // 是展示了的，实际这些item应该是在屏幕外的，这种情况会导致多曝光。
            int imgHeight = baseAd.getImgHeight();
            if (imgHeight == 0) {
                HiAdsLog.w(LOG_TAG, "server res image height is zero!");
                imgHeight = (int) (DensityUtil.getScreenWidth(mRootView.getContext()) / (16f / 9f));
            }
            setViewHeight(adImageView, imgHeight);
            // 渲染文本视图 标题/品牌名/落地按钮文本/关闭按钮等
            renderTextView(baseAd);
            mRootView.post(() -> {
                Context context = mRootView.getContext();
                // 获取图片比例，如果获取不到，则大图默认是16:9
                float proportion = baseAd.getProportion() <= 0 ? (16f / 9f) : baseAd.getProportion();
                // 大图根布局左padding边距
                float paddingMaxStart = context.getResources().getDimension(R.dimen.ads_app_magic_dimens_max_start);
                // 大图根布局右padding边距
                float paddingMaxEnd = context.getResources().getDimension(R.dimen.ads_app_magic_dimens_max_end);
                int paddingHorLength = (int) (paddingMaxStart + paddingMaxEnd);
                // 图片宽度 = 父控件测量宽度 - 左右边距。
                int width = mRootView.getMeasuredWidth() - paddingHorLength;
                setViewWidth(adImageView, width);
                setViewHeight(adImageView, (int) (width / proportion));

                setViewWidth(adBrandAutoSizeLayout, width);
                setViewWidth(titleView, width);
                // 加载图片
                loadImage(context, baseAd, baseAd.getImages(), 0, adImageView);
            });
        }
    }
}
