package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.callback.DislikeItemClickListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.download.DownLoadButton;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.GlobalConfig;
import com.hihonor.adsdk.picturetextad.PictureTextAdRootView;

/**
 * 功能描述
 *
 * @since 2023-06-07
 */
public class PictureTextSelfRenderView extends FrameLayout {
    AdapterDataSetObserver mDataSetObserver;
    PictureBaseAdViewAdapter mAdapter;
    PictureTextAdRootView pictureTextAdRootView;
    DislikeItemClickListener dislikeItemClickListener;

    public PictureTextSelfRenderView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PictureTextSelfRenderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PictureTextSelfRenderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PictureTextSelfRenderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.mDataSetObserver = new DataSetObserver();
        this.pictureTextAdRootView = new PictureTextAdRootView(context);
        addView(pictureTextAdRootView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mAdapter != null) {
            this.mAdapter.registerDataObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataObserver(mDataSetObserver);
        }
    }


    private void setAdapter(PictureBaseAdViewAdapter<? extends PictureBaseAdViewAdapter.ViewHolder> adapter) {
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mAdapter.registerDataObserver(mDataSetObserver);
        }
        notifyAllChildView();
    }

    private void notifyAllChildView() {
        int itemType = mAdapter.getViewType();
        PictureBaseAdViewAdapter.ViewHolder holder = mAdapter.createViewHolder(itemType, getContext());
        View childView;
        if (holder != null && (childView = holder.getRootView()) != null) {
            DownLoadButton downLoadButton = childView.findViewById(R.id.ad_download);
            pictureTextAdRootView.addView(childView);
            pictureTextAdRootView.setAdCloseView(holder.getAdFlagCloseView());
            pictureTextAdRootView.setDislikeItemClickListener((position, dislikeInfo, target) -> {
                if (dislikeItemClickListener != null) {
                    dislikeItemClickListener.onItemClick(position, dislikeInfo, target);
                }
            });
            pictureTextAdRootView.setDownLoadButton(downLoadButton);
            mAdapter.onBindDataToHolder(holder);
        }
    }


    public void setAd(@NonNull PictureTextExpressAd ad) throws IllegalArgumentException {
        int subType = ad.getSubType();
        if(!isSupportAdType(subType)){
            HiAdsLog.i("PictureTextAdView", "setAd but subType goto default : " + subType);
            return;
        }
        pictureTextAdRootView.removeAllViews();
        pictureTextAdRootView.setAd(ad);
        switch (subType) {
            case GlobalConfig.SUB_TYPE.BIG_PICTURE:
                setAdapter(new BigPictureAdapter(ad));
                break;
            case GlobalConfig.SUB_TYPE.SMALL_PICTURE:
                setAdapter(new SmallPictureAdapter(ad));
                break;
            case GlobalConfig.SUB_TYPE.THREE_PICTURE:
                setAdapter(new ThreePictureAdapter(ad));
                break;
            case GlobalConfig.SUB_TYPE.APP_PICTURE:
                setAdapter(new AppPictureAdapter(ad));
                break;
        }
    }

    public void setDislikeItemClickListener(DislikeItemClickListener dislikeItemClickListener) {
        this.dislikeItemClickListener = dislikeItemClickListener;
    }

    private boolean isSupportAdType(int subType){
        if(subType == GlobalConfig.SUB_TYPE.BIG_PICTURE || subType == GlobalConfig.SUB_TYPE.SMALL_PICTURE
                || subType == GlobalConfig.SUB_TYPE.THREE_PICTURE || subType == GlobalConfig.SUB_TYPE.APP_PICTURE){
            return true;
        }
        return false;
    }


    public class DataSetObserver implements AdapterDataSetObserver {
        @Override
        public void onChanged() {
            notifyAllChildView();
        }
    }
}
