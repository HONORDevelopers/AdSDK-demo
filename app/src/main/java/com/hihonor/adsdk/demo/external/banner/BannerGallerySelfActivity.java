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

    /**
     * 广告位ID
     */
    private String mSlotId = "1698586284462047232";

    private final static int MEDIA_ITEM_COUNT = 5;

    private final static int AD_POSITION = 3;

    private BannerPageAdapter mBannerPageAdapter = new BannerPageAdapter();

    private List<View> mChildViewList = new ArrayList<>();

    private MyViewPager mViewPager;

    private View mAdLoadLayout;

    private TextView mTextErrorInfo;

    /**
     * 广告对象
     */
    private BannerExpressAd mBannerExpressAd;

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
            mAdLoadLayout.setVisibility(View.VISIBLE);
            obtainAd();
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        // step1：创建广告请求参数对象（AdSlot）。
        AdSlot adSlot = new AdSlot.Builder()
            .setSlotId(mSlotId) // 必填，设置广告位ID。
            .setWidth(1280) // 设置广告宽度，目前仅Banner广告使用，作为兜底宽度，一般优先使用服务器下发的宽度。
            .setHeight(720) // 设置广告高度，目前仅Banner广告使用，作为兜底高度，一般优先使用服务器下发的高度。
            .setRenderType(1) // 设置渲染类型，0：模板渲染； 1 ：自渲染；
            .build();
        // step4：构建广告加载器，传入已创建好的广告请求参数对象与广告加载状态监听器。
        BannerAdLoad load = new BannerAdLoad.Builder()
            .setBannerAdLoadListener(mAdLoadListener) // 必填，注册广告加载状态监听器。
            .setAdSlot(adSlot) // 必填，设置广告请求参数。
            .build();
        // step5：加载广告
        load.loadAd();
    }

    /**
     * step2：实现广告加载状态监听器，加载过程中获取广告的状态变化。
     * <br>
     * 广告加载状态监听器
     */
    private final BannerAdLoadListener mAdLoadListener = new BannerAdLoadListener() {
        static final String imgUrl = "https://cs02-pps-drcn.dbankcdn.com/dl/pps/20200725105332993E06F098BAA15E9FDC290386784099.jpg";

        /**
         * 广告加载成功回调
         *
         * @param bannerExpressAd banner广告模板接口基类
         */
        @Override
        public void onLoadSuccess(BannerExpressAd bannerExpressAd) {
            mBannerExpressAd = bannerExpressAd;
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            mBannerPageAdapter.clearData();
            for (int i = 0; i < (MEDIA_ITEM_COUNT + 1); i++) {
                if (AD_POSITION != i) {
                    ImageView imageView = (ImageView) LayoutInflater.from(BannerGallerySelfActivity.this).inflate(R.layout.view_pager_item_image, null, false);
                    Glide.with(BannerGallerySelfActivity.this).load(imgUrl).into(imageView);
                    mChildViewList.add(imageView);
                } else {
                    // step3：在请求成功回调里，使用返回的广告对象作渲染处理。
                    addAdView(bannerExpressAd);
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
    };

    private void addAdView(BannerExpressAd bannerExpressAd) {
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
        // 设置广告负反馈回调监听器
        bannerAdRootView.setDislikeItemClickListener(new DislikeItemClickListener(){
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
        });
        // 注册广告事件监听器，您可根据需求实现接口并按需重写您需要接收通知的方法。
        bannerExpressAd.setAdListener(new AdListener(){

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
                releaseAd();
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
        });
        bannerAdRootView.setAd(bannerExpressAd);
        mChildViewList.add(bannerAdRootView);
    }

    /**
     * 页面不可见需要移除广告view
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseAd();
    }

    /**
     * 销毁广告对象
     */
    private void releaseAd() {
        if (mBannerExpressAd != null) {
            HiAdsLog.i(TAG, "releaseAd...");
            mBannerExpressAd.release();
        }
    }

}
