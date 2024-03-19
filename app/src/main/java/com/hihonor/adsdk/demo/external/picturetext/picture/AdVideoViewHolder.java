package com.hihonor.adsdk.demo.external.picturetext.picture;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.api.AdVideo;
import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.bean.DislikeInfo;
import com.hihonor.adsdk.base.callback.DislikeItemClickListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.utils.ToastUtil;
import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;
import com.hihonor.adsdk.base.widget.download.HnDownloadButton;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.GlideUtils;
import com.hihonor.adsdk.demo.external.utils.ScreenUtils;
import com.hihonor.adsdk.picturetextad.PictureTextAdRootView;
import com.hihonor.adsdk.picturetextad.open.ViewTagType;
import com.hihonor.adsdk.video.AdVideoSize;
import com.hihonor.adsdk.video.OnVideoPlayListener;
import com.hihonor.adsdk.video.VideoPlayState;

import java.util.Arrays;

public class AdVideoViewHolder extends RecyclerView.ViewHolder {
    private final String LOG_TAG = "AdVideoViewHolder";
    protected RelativeLayout adBrandAutoSizeLayout;
    protected ImageView brandLogoView;
    protected TextView brandNameTextView;
    protected HnDownloadButton downLoadButton;
    private View adPlayerView;
    private final ImageView adVideoStart;
    private final ImageView adVideoVolumeView;
    private final TextView adVideoTime;

    private FrameLayout mAdPlayerContainer;
    protected PictureTextExpressAd mBaseAd;
    private AdVideo mAdVideo;
    private View adReplayView;
    private View adContainerView;

    private TextView titleView;

    private AdFlagCloseView adFlagView;
    private AdFlagCloseView adFlagCloseView;
    private PictureTextAdRootView mPictureTextAdRootView;


    public AdVideoViewHolder(View mRootView) {
        super(mRootView);
        mPictureTextAdRootView = mRootView.findViewById(R.id.root_view);
        adBrandAutoSizeLayout = mRootView.findViewById(R.id.ad_auto_size_layout);
        titleView = mRootView.findViewById(R.id.ad_title);
        brandLogoView = mRootView.findViewById(R.id.ad_brand_logo);
        brandNameTextView = mRootView.findViewById(R.id.ad_brand_name);
        adFlagView = mRootView.findViewById(R.id.ad_flag_view);
        adFlagCloseView = mRootView.findViewById(R.id.ad_close_view);
        downLoadButton = mRootView.findViewById(R.id.ad_download);
        brandLogoView.setVisibility(VISIBLE);

        mAdPlayerContainer = mRootView.findViewById(R.id.ad_player_container);
        adVideoStart = mRootView.findViewById(R.id.ad_video_start);
        adVideoVolumeView = mRootView.findViewById(R.id.ad_video_volume_view);
        adVideoTime = mRootView.findViewById(R.id.ad_video_time);
        adReplayView = mRootView.findViewById(R.id.ad_video_replay);
        adContainerView = mRootView.findViewById(R.id.ad_video_container);
        adVideoTime.setBackground(new ColorDrawable(Color.TRANSPARENT));
        adReplayView.setVisibility(View.GONE);
    }


    public void bindData(PictureTextExpressAd baseAd) {
        if (baseAd == null) {
            HiAdsLog.w(LOG_TAG, "baseAd is null.");
            return;
        }
        this.mBaseAd = baseAd;
        mAdVideo = mBaseAd.getAdVideo();
        if (mAdVideo != null) {
            // 方式一、媒体有需要设置视频尺寸的 可设置尺寸 按一下代码设置并获取视频视图
             AdVideoSize adVideoSize = new AdVideoSize(700, 350);
             adPlayerView = mAdVideo.getVideoView(adVideoSize);

            // 方式二、媒体不设置视频宽高 按一下代码获取视频视图
             adPlayerView = mAdVideo.getVideoView();
        }
        // 获取播放状态
        if (mAdVideo.getPlayerState() == VideoPlayState.STATE_IDLE) {
            adVideoVolumeView.setVisibility(View.GONE);
            adVideoTime.setVisibility(View.GONE);
            adVideoStart.setVisibility(View.VISIBLE);
            adReplayView.setVisibility(View.GONE);
        } else if (mAdVideo.getPlayerState() == VideoPlayState.STATE_COMPLETED) {
            adVideoVolumeView.setVisibility(View.GONE);
            adVideoTime.setVisibility(View.GONE);
            adVideoStart.setVisibility(View.GONE);
            adReplayView.setVisibility(View.VISIBLE);
        }
        mPictureTextAdRootView.setAd(baseAd);
        // 渲染文本视图 标题/品牌名/落地按钮文本/关闭按钮等
        renderTextView(baseAd);
        initVideoPlayerView();
        mPictureTextAdRootView.setAdCloseView(adFlagCloseView);
        mPictureTextAdRootView.setDislikeItemClickListener(new DislikeItemClickListener() {
            @Override
            public void onFeedItemClick(int i, @Nullable DislikeInfo dislikeInfo, @Nullable View view) {
                if (baseAd != null) {
                    baseAd.release();
                }
                ((ViewGroup) itemView).removeAllViews();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onShow() {

            }
        });
        adReplayView.setOnClickListener(view -> {
            setPlayFinishLayout(false);
            mAdVideo.start();
        });
        registerViewClickProxy();
    }

    private void registerViewClickProxy(){
        // ViewTagType setViewTag 对View设置一次即可
        ViewTagType.TITLE.setViewTag(titleView);
        ViewTagType.VIDEO.setViewTag(mAdPlayerContainer);
        if (adBrandAutoSizeLayout != null) {
            adBrandAutoSizeLayout.setOnClickListener(view -> ToastUtil.showShortToast("包含品牌logo，品牌名称，广告标识，下载按钮，负反馈按钮"));
        }
        if (brandNameTextView != null) {
            brandNameTextView.setOnClickListener(view -> ToastUtil.showShortToast("品牌"));
        }
        if (titleView != null) {
            titleView.setOnClickListener(view -> ToastUtil.showShortToast("Title"));
        }
        if (mAdPlayerContainer != null) {
            mAdPlayerContainer.setOnClickListener(view -> ToastUtil.showShortToast("视频"));
        }
        if (adFlagView != null) {
            adFlagView.setOnClickListener(view -> ToastUtil.showShortToast("广告标签"));
        }
        if (adFlagCloseView != null) {
            adFlagCloseView.setOnClickListener(view -> ToastUtil.showShortToast("广告标签-关闭"));
        }

        // pictureTextAdRootView,downLoadButton 这些设置无效
        mPictureTextAdRootView.registerViewForInteraction(Arrays.asList(mPictureTextAdRootView,
                brandLogoView, mAdPlayerContainer, adBrandAutoSizeLayout,
                brandNameTextView, titleView, brandLogoView, adFlagView, adFlagCloseView, downLoadButton));
    }


    protected void renderTextView(@NonNull PictureTextExpressAd baseAd) {
        HiAdsLog.i(LOG_TAG, "render text view, set text data.");
        Context context = itemView.getContext();
        titleView.setText(baseAd.getTitle());
        GlideUtils.loadImage(context, baseAd.getLogo(), brandLogoView, 0);
        brandNameTextView.setText(baseAd.getBrand());
        // 广告标识
        int adFlag = baseAd.getAdFlag();
        adFlagView.setVisibility(adFlag == 0 ? View.INVISIBLE : VISIBLE);
        if (adFlag == 0) {
            setViewWidth(adFlagView, 0);
            setViewHeight(adFlagView, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) downLoadButton.getLayoutParams();
            float marginStart = context.getResources().getDimension(R.dimen.ads_app_magic_dimens_element_horizontal_middle);
            layoutParams.setMarginStart((int) marginStart);
            HiAdsLog.i(LOG_TAG, "adFlag is OFF");
        } else {
            // 广告标签背景色适配深色模式，备注：UX未在参数表中找到对应的资源id，无法使用uikit资源直接适配，需手动判断。
            int color = ScreenUtils.isDarkTheme() ? 0x33FFFFFF : 0x33000000;
            adFlagView.setBgColor(color);
        }

        // 关闭按钮
        int closeFlag = baseAd.getCloseFlag();
        adFlagCloseView.setVisibility(closeFlag == 0 ? View.INVISIBLE : VISIBLE);
        if (closeFlag == 0) {
            setViewWidth(adFlagCloseView, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) downLoadButton.getLayoutParams();
            layoutParams.setMarginEnd(0);
            HiAdsLog.i(LOG_TAG, "closeFlag is OFF");
        } else {
            adFlagCloseView.setBgColor(context.getResources().getColor(R.color.ads_app_magic_button_default));
            Drawable closeIconDrawable = context.getDrawable(
                    R.drawable.ic_app_honor_ads_close_black);
            adFlagCloseView.setCloseIconDrawable(closeIconDrawable);
        }
        downLoadButton.setBaseAd(baseAd);
    }

    private void setViewHeight(View target, int height) {
        HiAdsLog.i(LOG_TAG, "Call set view height.");
        if (target == null) {
            HiAdsLog.w(LOG_TAG, "target view is null!");
            return;
        }
        ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
        layoutParams.height = height;
        target.setLayoutParams(layoutParams);

    }
    private void setViewWidth(View target, int width) {
        HiAdsLog.i(LOG_TAG, "Call set view width.");
        if (target == null) {
            HiAdsLog.w(LOG_TAG, "target view is null!");
            return;
        }
        ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
        layoutParams.width = width;
        target.setLayoutParams(layoutParams);
    }

    private void initVideoPlayerView() {
        mAdPlayerContainer.removeAllViews();
        ViewParent parent = adPlayerView.getParent();
        if (parent != null) {
            ((ViewGroup)parent).removeView(adPlayerView);
        }
        if (adPlayerView != null) {
            mAdPlayerContainer.addView(adPlayerView, 0);
        }
        if (mAdVideo != null) {
            mAdVideo.setVideoListener(onVideoPlayListener);
            adFlagCloseView.setDislikeItemClickListener(new DislikeItemClickListener() {
                @Override
                public void onFeedItemClick(int i, @Nullable DislikeInfo dislikeInfo, @Nullable View view) {
                    mPictureTextAdRootView.removeAllViews();
                    mAdVideo.releasePlayer();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onShow() {

                }
            });
        }
        initListener();
        initVolume();
        String timeText = VideoAdViewFactory.updateTimeText(0,mBaseAd.getVideoDuration() * 1000L);
        adVideoTime.setText(timeText);
//        adPlayerView.setOnClickListener(this::triggerClick);
    }

    private void initListener() {
        // 开始播放
        adVideoStart.setOnClickListener(view -> {
            mAdVideo.start();
            setPlayFinishLayout(false);
        });
    }

    private void initVolume() {
        // 声音
        if (mAdVideo != null) {
            mAdVideo.setMuted(true);
        }
        setVolumeDrawable(true);
        adVideoVolumeView.setOnClickListener(view -> {
            boolean muted = false;
            if (mAdVideo != null) {
                muted = mAdVideo.isMuted();
                HiAdsLog.i(LOG_TAG, "onClick,muted: " + muted);
                mAdVideo.setMuted(!muted);
            }
            setVolumeDrawable(!muted);
        });
    }

    private void setVolumeDrawable(boolean muted){
        if (muted) {
            adVideoVolumeView.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_app_volume_off));
        } else {
            adVideoVolumeView.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_app_volume_on));
        }
    }

    private void setPlayFinishLayout(boolean visible) {
        HiAdsLog.i(LOG_TAG, "setPlayFinishLayout--->visible : " + visible);
        adVideoVolumeView.setVisibility(visible ? View.GONE : View.VISIBLE);
        adVideoTime.setVisibility(visible ? View.GONE : View.VISIBLE);

        adVideoStart.setVisibility(visible ? View.VISIBLE : View.GONE);
        adReplayView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private final OnVideoPlayListener onVideoPlayListener = new OnVideoPlayListener() {
        @Override
        public void onVideoStart() {
            HiAdsLog.i(LOG_TAG, "self--->onVideoStart");
            setPlayFinishLayout(false);
        }

        @Override
        public void onVideoPause() {
            HiAdsLog.i(LOG_TAG, "self--->onVideoPause");
        }

        @Override
        public void onVideoBuffering(boolean isBuffering) {
            HiAdsLog.i(LOG_TAG, "self--->onVideoBuffering,isBuffering:" + isBuffering);
        }

        @Override
        public void onVideoResume() {
            super.onVideoResume();
            setPlayFinishLayout(false);
            HiAdsLog.i(LOG_TAG, "self--->onVideoResume");
        }

        @Override
        public void onVideoEnd() {
            HiAdsLog.i(LOG_TAG, "self--->onVideoEnd");
            setPlayFinishLayout(true);
        }

        @Override
        public void onVideoError(int errorCode, String message) {
            HiAdsLog.i(LOG_TAG, "self--->onVideoError,errorCode: " + errorCode + ",message: " + message);
        }

        @Override
        public void onProgressUpdate(long position, long bufferedPosition, long duration) {
            super.onProgressUpdate(position, bufferedPosition, duration);
            String timeText = VideoAdViewFactory.updateTimeText(position, duration);
            adVideoTime.setText(timeText);
            HiAdsLog.d(LOG_TAG, "self--->onProgressUpdate,timeText: " + timeText);

            setPlayFinishLayout(false);
        }

        @Override
        public void onVideoMute(boolean isMute) {
            super.onVideoMute(isMute);
        }
    };

    public void release() {
        if (mAdVideo != null) {
            mAdVideo.releasePlayer();
        }
    }
}
