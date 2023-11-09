package com.hihonor.adsdk.demo.external.picturetext;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.feed.PictureTextAdLoadListener;
import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.demo.external.common.PictureTextListAdapter;
import com.hihonor.adsdk.demo.external.utils.CollectionUtils;
import com.hihonor.adsdk.picturetextad.PictureTextAdLoad;

import java.util.List;

/**
 * 信息流广告标准集成页面
 *
 * @since 2022-10-20
 */
public class PictureTextTemplateRenderActivity extends BaseActivity {

    private static final String TAG = PictureTextTemplateRenderActivity.class.getSimpleName();

    /**
     * 广告位ID
     */
    private String mSlotId = "1698587684968857600";

    private PictureTextListAdapter mAdapter;

    private RecyclerView mPictureTextRecyclerView;

    private List<PictureTextExpressAd> mAdViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_picture_text_template_render_ad));
        setContentView(R.layout.activity_picture_text_template_render);
        initView();
    }

    private void initView() {
        mPictureTextRecyclerView = findViewById(R.id.picture_text_recycler_view);
        Button adLoadButton = findViewById(R.id.bt_load_ad);
        adLoadButton.setOnClickListener((v) -> {
            releaseAd();
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
            .build();
        // step4：构建广告加载器，传入已创建好的广告请求参数对象与广告加载状态监听器。
        PictureTextAdLoad load = new PictureTextAdLoad.Builder()
            .setPictureTextAdLoadListener(mAdLoadListener) // 必填，注册广告加载状态监听器。
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
    private final PictureTextAdLoadListener mAdLoadListener = new PictureTextAdLoadListener() {

        /**
         * 广告加载失败
         *
         * @param code 错误码
         * @param errorMsg 错误提示信息
         */
        @Override
        public void onFailed(String code, String errorMsg) {
            HiAdsLog.i(TAG, "onFailed: code: " + code + ", errorMsg: " + errorMsg);
            Toast.makeText(PictureTextTemplateRenderActivity.this, errorMsg,
                    Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告加载成功回调。
         *
         * @param adViewList 回调广告数据集合
         */
        @Override
        public void onAdLoaded(List<PictureTextExpressAd> adViewList) {
            mAdViewList = adViewList;
            HiAdsLog.i(TAG, "onAdLoaded, ad load success");
            if (adViewList == null || adViewList.isEmpty()) {
                Toast.makeText(PictureTextTemplateRenderActivity.this, "Request success but data is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            for (PictureTextExpressAd pictureTextExpressAd : adViewList) {
                pictureTextExpressAd.setAdListener(new AdListener(){

                    /**
                     * 广告关闭时回调
                     */
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        HiAdsLog.i(TAG, "onAdClosed...");
                        Toast.makeText(PictureTextTemplateRenderActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 广告被点击时回调
                     */
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        HiAdsLog.i(TAG, "onAdClicked...");
                        Toast.makeText(PictureTextTemplateRenderActivity.this, getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 广告曝光时回调
                     */
                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        HiAdsLog.i(TAG, "onAdImpression...");
                        Toast.makeText(PictureTextTemplateRenderActivity.this, getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 广告成功跳转小程序时回调
                     */
                    @Override
                    public void onMiniAppStarted() {
                        super.onMiniAppStarted();
                        HiAdsLog.i(TAG, "onMiniAppStarted...");
                        Toast.makeText(PictureTextTemplateRenderActivity.this, getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
                    }

                });
            }
            // step3：在请求成功回调里，使用返回的广告对象作渲染处理。
            mAdapter = new PictureTextListAdapter(adViewList);
            mPictureTextRecyclerView.setAdapter(mAdapter);
            mPictureTextRecyclerView.setLayoutManager(new LinearLayoutManager(
                    PictureTextTemplateRenderActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false)
            );
        }
    };

    /**
     * 页面不可见时需要销毁广告
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseAd();
    }

    /**
     * 销毁广告
     */
    private void releaseAd() {
        if (CollectionUtils.isNotEmpty(mAdViewList)) {
            for (PictureTextExpressAd expressAd : mAdViewList) {
                if (expressAd != null) {
                    HiAdsLog.i(TAG, "releaseAd...");
                    expressAd.release();
                }
            }
        }
    }

}
