package com.hihonor.adsdk.demo.external.picturetext.picture;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;

import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;

/**
 * 功能描述
 *
 * @since 2022-11-28
 */
public abstract class PictureBaseAdViewAdapter<VH extends PictureBaseAdViewAdapter.ViewHolder> {
    public int viewType;
    public AdapterDataSetObservable mAdapterDataSetObservable;
    public abstract VH createViewHolder(int viewType, Context context);
    public abstract void onBindDataToHolder(VH viewHolder);

    public PictureBaseAdViewAdapter() {
        mAdapterDataSetObservable = new AdapterDataSetObservable();
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }
    public void notifyDataSetChanged() {
        mAdapterDataSetObservable.notifyChanged();
    }

    public void registerDataObserver(AdapterDataSetObserver dataSetObserver) {
        if (mAdapterDataSetObservable.contains(dataSetObserver)) {
            return;
        }
        mAdapterDataSetObservable.registerObserver(dataSetObserver);
    }

    public void unregisterDataObserver(AdapterDataSetObserver dataSetObserver) {
        if (!mAdapterDataSetObservable.contains(dataSetObserver)) {
            return;
        }
        mAdapterDataSetObservable.unregisterObserver(dataSetObserver);
    }
    
    public static abstract class ViewHolder {
        protected View mRootView;
        protected AdFlagCloseView adFlagCloseView;
        protected View.OnClickListener mClickListener;
        public AdFlagCloseView getAdFlagCloseView() {
            return adFlagCloseView;
        }

        public ViewHolder(View mRootView) {
            this.mRootView = mRootView;
        }
        protected <T extends View> T findViewById(@IdRes int id) {
            if (mRootView == null) {
                return null;
            }
            return mRootView.findViewById(id);
        }
        public View getRootView() {
            return mRootView;
        }

        public void startDownload() {

        }
    }
}
