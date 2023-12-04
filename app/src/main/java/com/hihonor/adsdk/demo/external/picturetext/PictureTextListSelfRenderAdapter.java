package com.hihonor.adsdk.demo.external.picturetext;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.api.CustomVideo;
import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;
import com.hihonor.adsdk.base.widget.download.HnDownloadButton;
import com.hihonor.adsdk.demo.external.DemoApplication;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.picturetext.picture.AdVideoViewHolder;
import com.hihonor.adsdk.demo.external.picturetext.picture.PictureMediaDataBean;
import com.hihonor.adsdk.demo.external.picturetext.picture.PictureTextSelfRenderView;
import com.hihonor.adsdk.demo.external.utils.GlideUtils;
import com.hihonor.adsdk.demo.external.widget.CustomVideoView;
import com.hihonor.adsdk.picturetextad.PictureTextAdRootView;
import com.hihonor.adsdk.video.OnVideoPlayListener;

import java.util.List;

/**
 * 功能描述
 *
 * @since 2022-12-09
 */
public class PictureTextListSelfRenderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String LOG_TAG = "PictureTextListSelfRenderAdapter";
    private List<PictureMediaDataBean> mMediaDataBeanList;

    public void setMediaDataBeanList(List<PictureMediaDataBean> mediaDataBeanList) {
        mMediaDataBeanList = mediaDataBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_picture_text_media_item, null, false);
            return new MediaViewHolder(rootView);
        } else if (viewType == 1){
            // 图文
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_picture_text_self_render_item, null, false);
            return new AdViewHolder(rootView);
        } else if (viewType == 2){
            // 使用广告播放器的视频
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.self_render_top_video_bottom_text, null, false);
            return new AdVideoViewHolder(rootView);
        } else {
            // 使用媒体自定义播放器的视频
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.self_item_custom_video_layout, null, false);
            return new AdCustomVideoHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        String logStr = "";
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            logStr = " payloads empty";
        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            PictureMediaDataBean pictureMediaDataBean = (PictureMediaDataBean) bundle.get("update_item");
            if (holder instanceof MediaViewHolder) {
                ((MediaViewHolder)holder).bindData(pictureMediaDataBean);
            } else if (holder instanceof AdViewHolder) {
                ((AdViewHolder)holder).bindData(pictureMediaDataBean);
            } else if (holder instanceof AdCustomVideoHolder) {
                ((AdCustomVideoHolder)holder).bindData(pictureMediaDataBean);
            }
            logStr = pictureMediaDataBean.getItemName();
        }
        HiAdsLog.i(LOG_TAG, "onBindViewHolder three %s ", logStr);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PictureMediaDataBean pictureMediaDataBean = mMediaDataBeanList.get(position);
        if (holder instanceof MediaViewHolder) {
            ((MediaViewHolder)holder).bindData(pictureMediaDataBean);
        } else if(holder instanceof  AdViewHolder) {
            ((AdViewHolder)holder).bindData(pictureMediaDataBean);
        }else if (holder instanceof AdVideoViewHolder) {
            ((AdVideoViewHolder)holder).bindData(pictureMediaDataBean.getExpressAd());
        } else if (holder instanceof AdCustomVideoHolder) {
            ((AdCustomVideoHolder)holder).bindData(pictureMediaDataBean);
        }
        HiAdsLog.i(LOG_TAG, "onBindViewHolder two %s ", pictureMediaDataBean.getItemName());
    }

    @Override
    public int getItemViewType(int position) {
        PictureMediaDataBean pictureMediaDataBean = mMediaDataBeanList.get(position);
        return pictureMediaDataBean.getItemType();
    }

    @Override
    public int getItemCount() {
        return (mMediaDataBeanList != null) ? mMediaDataBeanList.size() : 0;
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        PictureTextSelfRenderView mPictureTextSelfRenderView;
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            mPictureTextSelfRenderView = itemView.findViewById(R.id.self_render_view);
        }

        public void bindData(PictureMediaDataBean pictureMediaDataBean) {
            PictureTextExpressAd expressAd = pictureMediaDataBean.getExpressAd();
            mPictureTextSelfRenderView.setAd(expressAd);
            mPictureTextSelfRenderView.setDislikeItemClickListener((position, dislikeInfo, target) -> {
                ViewGroup viewGroup = (ViewGroup) itemView;
                viewGroup.removeView(mPictureTextSelfRenderView);
            });
        }
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_text_view);
        }
        public void bindData(PictureMediaDataBean pictureMediaDataBean) {
            mTextView.setText(pictureMediaDataBean.getItemName());
        }
    }

    static class AdCustomVideoHolder extends RecyclerView.ViewHolder {
        private PictureTextAdRootView mPictureTextAdRootView;
        private CustomVideoView videoView;
        private TextView titleView;
        private TextView brandView;
        private TextView timeText;
        private ImageView logoView;
        private HnDownloadButton mDownLoadButton;
        private ImageView ivMute;
        private boolean isMute;
        private AdFlagCloseView mAdFlagCloseView;
        private FrameLayout finishLayout;
        private TextView tvReplay;
        private ImageView ivCover;

        public AdCustomVideoHolder(@NonNull View itemView) {
            super(itemView);
            mPictureTextAdRootView = itemView.findViewById(R.id.ad_root_view);
            videoView = itemView.findViewById(R.id.video_view);
            titleView = itemView.findViewById(R.id.title_view);
            brandView = itemView.findViewById(R.id.brand_view);
            timeText = itemView.findViewById(R.id.time_text);
            logoView = itemView.findViewById(R.id.logo_view);
            ivMute = itemView.findViewById(R.id.iv_mute);
            finishLayout = itemView.findViewById(R.id.finish_layout);
            tvReplay = itemView.findViewById(R.id.tv_replay);
            mDownLoadButton = itemView.findViewById(R.id.ad_download);
            ivCover = itemView.findViewById(R.id.iv_cover);
            mAdFlagCloseView = itemView.findViewById(R.id.ad_close_view);
        }

        public void bindData(PictureMediaDataBean pictureMediaDataBean) {
            PictureTextExpressAd expressAd = pictureMediaDataBean.getExpressAd();
            mPictureTextAdRootView.setAd(expressAd);
            titleView.setText(expressAd.getTitle() + "-custom-video");
            brandView.setText(expressAd.getTitle());
            GlideUtils.loadImage(DemoApplication.sContext, expressAd.getLogo(), logoView, 0);
            GlideUtils.loadImage(DemoApplication.sContext, expressAd.getCoverUrl(), ivCover, 0);
            mDownLoadButton.setBaseAd(expressAd);
            CustomVideo customVideo = expressAd.getCustomVideo();
            isMute = false;
            ivMute.setImageResource(R.drawable.ic_app_volume_off);
            // 设置静音点击事件
            ivMute.setOnClickListener(v -> {
                isMute = !isMute;
                if (isMute) {
                    ivMute.setImageResource(R.drawable.ic_app_volume_on);
                } else {
                    ivMute.setImageResource(R.drawable.ic_app_volume_off);
                }
                videoView.setMute(isMute);
            });
            tvReplay.setOnClickListener(v -> {
                // 重播
                if (customVideo != null) {
                    videoView.setVideoURI(Uri.parse(customVideo.getVideoUrl()));
                }
            });

            mAdFlagCloseView.setDislikeItemClickListener((position, dislikeInfo, target) -> {
                videoView.stopPlayback();
                ((ViewGroup)itemView).removeAllViews();
            });
            if(customVideo != null) {
                videoView.setCustomVideo(customVideo);
                videoView.setVideoURI(Uri.parse(customVideo.getVideoUrl()));
                videoView.setOnVideoPlayListener(new OnVideoPlayListener() {
                    @Override
                    public void onVideoStart() {
                        super.onVideoStart();
                        videoView.setMute(isMute);
                        finishLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onProgressUpdate(long position, long bufferedPosition, long duration) {
                        super.onProgressUpdate(position, bufferedPosition, duration);
                        HiAdsLog.i(LOG_TAG, "---->onProgressUpdate");
                        timeText.setText(updateTimeText(position, duration));
                        ivCover.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                        ViewGroup.LayoutParams layoutParams = finishLayout.getLayoutParams();
                        layoutParams.width = videoView.getWidth();
                        layoutParams.height = videoView.getHeight();
                        finishLayout.setLayoutParams(layoutParams);
                        finishLayout.setVisibility(View.VISIBLE);
                        ivCover.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        private static final String DEFAULT_MINUTE = "00";
        private static final String TIME_SECTION = ":";

        public String updateTimeText(long position, long duration) {
            String timeText;
            long leftPosition = duration - position;
            if (leftPosition >= (60 * 1000)) {
                long minute = leftPosition / (60 * 1000); // 分
                long second = (leftPosition - minute * 60 * 1000) / 1000; // 秒
                timeText = (minute < 10 ? ("0" + minute) : minute) + TIME_SECTION + (second < 10 ? ("0" + second) : second);
            } else {
                long second = (leftPosition / 1000);
                timeText = DEFAULT_MINUTE + TIME_SECTION + (second < 10 ? ("0" + second) : second);
            }
            return timeText;
        }
    }
}
