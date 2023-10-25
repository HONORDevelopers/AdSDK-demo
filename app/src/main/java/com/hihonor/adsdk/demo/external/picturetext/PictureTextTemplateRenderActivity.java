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
import com.hihonor.adsdk.picturetextad.PictureTextAdLoad;

import java.util.List;

/**
 * 信息流广告标准集成页面
 *
 * @since 2022-10-20
 */
public class PictureTextTemplateRenderActivity extends BaseActivity {
    private static final String TAG = PictureTextTemplateRenderActivity.class.getSimpleName();
    private String mSlotId = "1698587684968857600";
    private PictureTextListAdapter mAdapter;
    private RecyclerView mPictureTextRecyclerView;

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
        adLoadButton.setOnClickListener((v) ->{
            obtainAd();
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder().setSlotId(mSlotId).build();
        PictureTextAdLoad pictureTextAdLoad = new PictureTextAdLoad.Builder().setAdSlot(adSlot)
            .setPictureTextAdLoadListener(new AdLoadListener()).build();
        pictureTextAdLoad.loadAd();
    }

    /**
     * 广告加载状态监听器
     */
    private class AdLoadListener implements PictureTextAdLoadListener {

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
            HiAdsLog.i(TAG, "onAdLoaded, ad load success");
            if (adViewList == null || adViewList.isEmpty()) {
                Toast.makeText(PictureTextTemplateRenderActivity.this, "Request success but data is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            for (PictureTextExpressAd pictureTextExpressAd : adViewList) {
                pictureTextExpressAd.setAdListener(new MyAdListener());
            }
            mAdapter = new PictureTextListAdapter(adViewList);
            mPictureTextRecyclerView.setAdapter(mAdapter);
            mPictureTextRecyclerView.setLayoutManager(new LinearLayoutManager(
                    PictureTextTemplateRenderActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false)
            );
        }
    }

    /**
     * 广告事件监听器
     */
    private class MyAdListener extends AdListener {

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(PictureTextTemplateRenderActivity.this, "广告关闭", Toast.LENGTH_SHORT)
                .show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(PictureTextTemplateRenderActivity.this, "点击广告", Toast.LENGTH_SHORT)
                .show();
        }

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(PictureTextTemplateRenderActivity.this, "展示成功", Toast.LENGTH_SHORT)
                .show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(PictureTextTemplateRenderActivity.this, "跳转小程序", Toast.LENGTH_SHORT)
                .show();
        }

    }

}
