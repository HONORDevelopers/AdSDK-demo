package com.hihonor.adsdk.demo.external.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.HnAds;
import com.hihonor.adsdk.demo.external.DemoApplication;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.AppUtil;
import com.hihonor.adsdk.nativead.api.NativeAd;
import com.hihonor.adsdk.nativead.api.NativeAdView;

import java.util.List;

/**
 * 功能描述
 *
 * @since 2022-12-09
 */
public class SearchNativeListAdapter extends RecyclerView.Adapter<SearchNativeListAdapter.NativeViewHolder>{
    private static final String LOG_TAG = SearchNativeListAdapter.class.getSimpleName();
    private List<MediaDataBean> nativeAdList;

    public SearchNativeListAdapter(List<MediaDataBean> nativeAdList) {
        this.nativeAdList = nativeAdList;
    }

    public List<MediaDataBean> getNativeAdList() {
        return nativeAdList;
    }

    @NonNull
    @Override
    public NativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_search_defualt_style;
        if (viewType == 0) {
            layoutId = R.layout.item_search_ad_style;
        }
        View rootView = LayoutInflater.from(parent.getContext()).inflate(layoutId, null, false);
        return new NativeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull NativeViewHolder holder, int position) {
        holder.bindData(nativeAdList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        MediaDataBean mediaDataBean = nativeAdList.get(position);
        if (mediaDataBean.getNativeAd() != null) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return (nativeAdList != null) ? nativeAdList.size() : 0;
    }


    class NativeViewHolder extends RecyclerView.ViewHolder {
        private TextView appNameTextView;
        private TextView appFlag;
        private ImageView appIconView;
        public NativeViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconView = itemView.findViewById(R.id.app_icon);
            appFlag = itemView.findViewById(R.id.ad_flag);
            appNameTextView = itemView.findViewById(R.id.app_name);
        }

        public void bindData(MediaDataBean mediaDataBean) {
            if (mediaDataBean == null) {
                return;
            }
            if (mediaDataBean.getNativeAd() != null) {
                // 渲染广告视图
                renderAd(mediaDataBean);
                appFlag.setVisibility(View.VISIBLE);
            } else {
                // 渲染媒体自己的视图
                AppInfo appInfo = mediaDataBean.getAppInfo();
                appNameTextView.setText(appInfo.getAppName());
                appIconView.setImageDrawable(appInfo.getIconDrawable());
                itemView.setOnClickListener(v -> Toast.makeText(DemoApplication.sContext, "点击了 " + appInfo.getAppName(), Toast.LENGTH_SHORT).show());
            }
        }

        /**
         * 渲染广告视图
         * @param mediaDataBean
         */
        private void renderAd(MediaDataBean mediaDataBean) {
            NativeAd nativeAd = mediaDataBean.getNativeAd();
            AppInfo appInfo = mediaDataBean.getAppInfo();
            if (appInfo == null) {
                appInfo = AppUtil.getAppInfo(nativeAd.getAppPackage());
                mediaDataBean.setAppInfo(appInfo);
            }
            NativeAdView nativeAdView = itemView.findViewById(R.id.native_ad_layout);
            // 以下set方法，没有对应视图元素，就不用调set，有的话就需要set
            if (appInfo != null) {
                appNameTextView.setText(appInfo.getAppName());
                appIconView.setImageDrawable(appInfo.getIconDrawable());
            }
            nativeAdView.setTitleView(appNameTextView);
            nativeAdView.setIconView(appIconView);
            itemView.setOnClickListener(v -> Toast.makeText(HnAds.get().getContext(),
                    "点击广告item", Toast.LENGTH_SHORT).show());
            // 有关闭按钮的话就需要使用AdCloseView
//            nativeAdView.setAdCloseView(adCloseView);
            // 有关闭的话，需要设置对调
//            nativeAdView.setDislikeItemClickListener((position, dislikeInfo, target) -> {
//                nativeAdList.remove(mediaDataBean);
//                notifyDataSetChanged();
//            });
            // 视图与广告数据绑定
            nativeAdView.setNativeAd(nativeAd);
        }
    }
}
