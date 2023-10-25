package com.hihonor.adsdk.demo.external.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.nativead.api.NativeAd;
import com.hihonor.adsdk.nativead.api.NativeAdView;

import java.util.List;

/**
 * 功能描述
 *
 * @since 2022-12-09
 */
public class NativeListAdapter extends RecyclerView.Adapter<NativeListAdapter.NativeViewHolder>{
    private static final String LOG_TAG = NativeListAdapter.class.getSimpleName();
    private List<NativeAd> nativeAdList;

    public NativeListAdapter(List<NativeAd> nativeAdList) {
        this.nativeAdList = nativeAdList;
    }

    public void setNativeAdList(List<NativeAd> nativeAdList) {
        this.nativeAdList = nativeAdList;
    }

    public List<NativeAd> getNativeAdList() {
        return nativeAdList;
    }

    @NonNull
    @Override
    public NativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_native_big_img, null, false);
        return new NativeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull NativeViewHolder holder, int position) {
        holder.bindData(nativeAdList.get(position));
    }

    @Override
    public int getItemCount() {
        return (nativeAdList != null) ? nativeAdList.size() : 0;
    }

    class NativeViewHolder extends RecyclerView.ViewHolder {

        public NativeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(NativeAd nativeAd) {
            if (nativeAd == null) {
                return;
            }
            AdFlagCloseView adCloseView = itemView.findViewById(R.id.iv_close_ad);
            NativeAdView nativeAdView = itemView.findViewById(R.id.native_ad_layout);
            // 以下set方法，没有对应视图元素，就不用调set，有的话就需要set
            nativeAdView.setTitleView(itemView.findViewById(R.id.ad_title));
            nativeAdView.setIconView(itemView.findViewById(R.id.app_icon));
            nativeAdView.setBrandNameView(itemView.findViewById(R.id.ad_brand_name));
            // 有关闭按钮的话就需要使用AdCloseView
//            nativeAdView.setAdCloseView(adCloseView);
//            // 有关闭的话，需要设置对调
//            nativeAdView.setDislikeItemClickListener((position, dislikeInfo, target) -> {
//                nativeAdList.remove(nativeAd);
//                notifyDataSetChanged();
//            });
            // 视图与广告数据绑定
            nativeAdView.setNativeAd(nativeAd);
        }
    }
}
