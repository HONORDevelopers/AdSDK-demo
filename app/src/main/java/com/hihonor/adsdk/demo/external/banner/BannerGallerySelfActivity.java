package com.hihonor.adsdk.demo.external.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hihonor.ads.banner.api.BannerAdLoad;
import com.hihonor.ads.banner.api.BannerAdLoadListener;
import com.hihonor.ads.banner.api.BannerAdRootView;
import com.hihonor.ads.banner.api.BannerExpressAd;
import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.ErrorCode;
import com.hihonor.adsdk.base.HnAds;
import com.hihonor.adsdk.base.bean.DislikeInfo;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.callback.DislikeItemClickListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.utils.ToastUtil;
import com.hihonor.adsdk.base.widget.base.AdFlagCloseView;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BannerPageAdapter;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.demo.external.utils.GlideUtils;
import com.hihonor.adsdk.demo.external.widget.MyViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BannerGallerySelfActivity extends BaseActivity {

    private static final String TAG = "BannerGallerySelfActivity";

    private String mSlotId = "1698586284462047232";
    private final static int MEDIA_ITEM_COUNT = 5;
    private final static int AD_POSITION = 3;
    private BannerPageAdapter mBannerPageAdapter = new BannerPageAdapter();
    private List<View> mChildViewList = new ArrayList<>();

    private MyViewPager mViewPager;
    private View mAdLoadLayout;
    private TextView mTextErrorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_banner_gallery_self));
        setContentView(R.layout.activity_banner_gallery_self);
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setPageMargin(50);
        mViewPager.setAdapter(mBannerPageAdapter);
        mAdLoadLayout = findViewById(R.id.ad_load_layout);
        mTextErrorInfo = findViewById(R.id.text_error_info);
        findViewById(R.id.bt_load_ad).setOnClickListener(v -> {
            if(HnAds.get().isSdkConfigSuccess()){
                mAdLoadLayout.setVisibility(View.VISIBLE);
                obtainAd();
            }else {
                Toast.makeText(this, getString(R.string.init_falied), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder()
            .setSlotId(mSlotId)
            .setWidth(1280)
            .setHeight(720)
            .setRenderType(1)
            .build();
        BannerAdLoad load = new BannerAdLoad.Builder()
            .setBannerAdLoadListener(new AdLoadListener())
            .setAdSlot(adSlot)
            .build();
        load.loadAd();
    }

    private class MyDislikeClickListener implements DislikeItemClickListener {
        @Override
        public void onItemClick(int position, @Nullable DislikeInfo dislikeInfo, @Nullable View target) {
            Iterator<View> iterator = mChildViewList.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view instanceof BannerAdRootView) {
                    iterator.remove();
                    mBannerPageAdapter.updateAndNotify(mChildViewList);
                    mViewPager.setCurrentItem(0);
                    ToastUtil.showShortToast(R.string.app_ad_close_tip);
                    break;
                }
            }
        }
    }

    /**
     * 广告事件监听器
     */
    private class MyAdListener extends AdListener {

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(BannerGallerySelfActivity.this, getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(BannerGallerySelfActivity.this, getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(BannerGallerySelfActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(BannerGallerySelfActivity.this, getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 广告加载状态监听器
     */
    private class AdLoadListener implements BannerAdLoadListener {
        static final String imgUrl = "https://cs02-pps-drcn.dbankcdn.com/dl/pps/20200725105332993E06F098BAA15E9FDC290386784099.jpg";

        /**
         * 广告加载成功回调
         *
         * @param bannerExpressAd banner广告模板接口基类
         */
        @Override
        public void onLoadSuccess(BannerExpressAd bannerExpressAd) {
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            mBannerPageAdapter.clearData();
            for (int i = 0; i < (MEDIA_ITEM_COUNT + 1); i++) {
                if (AD_POSITION != i) {
                    ImageView imageView = (ImageView) LayoutInflater.from(BannerGallerySelfActivity.this).inflate(R.layout.view_pager_item_image, null, false);
                    Glide.with(BannerGallerySelfActivity.this).load(imgUrl).into(imageView);
                    mChildViewList.add(imageView);
                } else {
                    View inflate = LayoutInflater.from(BannerGallerySelfActivity.this).inflate(R.layout.page_ad_layout, null);
                    BannerAdRootView bannerAdRootView = inflate.findViewById(R.id.ad_layout);
                    ImageView adImageView = inflate.findViewById(R.id.ad_banner_img);
                    List<String> images = bannerExpressAd.getImages();
                    int radius = 0;
                    if (bannerExpressAd.getStyle() != null) {
                        radius = bannerExpressAd.getStyle().getBorderRadius();
                    }
                    if (images != null && !images.isEmpty()) {
                        GlideUtils.loadImage(BannerGallerySelfActivity.this, images.get(0),adImageView, radius);
                    }
                    AdFlagCloseView adFlagCloseView = inflate.findViewById(R.id.ad_flag_close_view);
                    bannerAdRootView.setAdCloseView(adFlagCloseView);
                    bannerAdRootView.setDislikeItemClickListener(new MyDislikeClickListener());
                    bannerAdRootView.setAd(bannerExpressAd);
                    mChildViewList.add(bannerAdRootView);
                    // 您可根据需求实现接口并按需重写您需要接收通知的方法。
                    bannerExpressAd.setAdListener(new MyAdListener());
                }
            }
            mBannerPageAdapter.updateAndNotify(mChildViewList);
            mViewPager.setCurrentItem(0);
            mAdLoadLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
        }

        /**
         * 广告加载失败
         *
         * @param code 错误码
         * @param errorMsg 错误提示信息
         */
        @Override
        public void onFailed(String code, String errorMsg) {
            HiAdsLog.i(TAG, "onFailed: code: " + code + ", errorMsg: " + errorMsg);
            mTextErrorInfo.setText(errorMsg);
            mAdLoadLayout.setVisibility(View.GONE);
            mTextErrorInfo.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            if (code.equals(String.valueOf(ErrorCode.AD_SLOT_ID_EMPTY))) {
                ToastUtil.showShortToast(getString(R.string.slotid_null));
            }
        }
    }

}
