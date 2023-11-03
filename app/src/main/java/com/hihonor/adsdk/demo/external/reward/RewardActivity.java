package com.hihonor.adsdk.demo.external.reward;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.reward.RewardAdLoadListener;
import com.hihonor.adsdk.base.api.reward.RewardExpressAd;
import com.hihonor.adsdk.base.api.reward.RewardItem;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.reward.RewardAdLoad;

public class RewardActivity extends BaseActivity {

    private static final String TAG = RewardActivity.class.getSimpleName();

    private TextView mTvMsg;
    private Button mBtnRewardContent;
    private String mSlotId = "1698589518466908160";
    private String mRewardName = "Q点券";
    private double mRewardAmount = 1000;

    private RewardExpressAd mRewardExpressAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_reward_ad));
        setContentView(R.layout.activity_reward);
        initView();
    }

    private void initView() {
        Button btnReward = findViewById(R.id.btn_reward);
        mTvMsg = findViewById(R.id.tv_msg);
        mBtnRewardContent = findViewById(R.id.btn_reward_content);
        mBtnRewardContent.setVisibility(View.INVISIBLE);
        btnReward.setOnClickListener(view -> {
            obtainAd();
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder()
                .setSlotId(mSlotId)
                .setRewardAmount(mRewardAmount)
                .setRewardName(mRewardName)
                .build();
        RewardAdLoad load = new RewardAdLoad.Builder()
                .setRewardAdLoadListener(new AdLoadListener())
                .setAdSlot(adSlot)
                .build();
        load.loadAd();
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
            Toast.makeText(RewardActivity.this,
                    getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告曝光失败时回调
         *
         * @param msg 曝光失败信息
         */
        @Override
        public void onAdImpressionFailed(String msg) {
            super.onAdImpressionFailed(msg);
            HiAdsLog.i(TAG, "onAdImpressionFailed, msg: " + msg);
            Toast.makeText(RewardActivity.this,
                    getString(R.string.ad_impression_failed), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(RewardActivity.this,
                    getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告关闭时回调
         */
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            HiAdsLog.i(TAG, "onAdClosed...");
            Toast.makeText(RewardActivity.this,
                    getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(RewardActivity.this,
                    getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 广告加载状态监听器
     */
    public class AdLoadListener implements RewardAdLoadListener {

        /**
         * 广告加载成功回调
         *
         * @param rewardExpressAd 激励广告模板接口基类
         */
        @Override
        public void onLoadSuccess(RewardExpressAd rewardExpressAd) {
            mRewardExpressAd = rewardExpressAd;
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            rewardExpressAd.setAdListener(new MyAdListener());
            rewardExpressAd.show(RewardActivity.this, new RewardExpressAd.RewardAdStatusListener() {
                @Override
                public void onRewardAdClosed() {
                    HiAdsLog.i(TAG,"onRewardAdClosed");
                }

                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    HiAdsLog.i(TAG,"onRewardAdFailedToShow,errorCode: " + errorCode);
                }

                @Override
                public void onRewardAdOpened() {
                    HiAdsLog.i(TAG,"onRewardAdOpened");
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    HiAdsLog.i(TAG,"onRewarded");
                    mBtnRewardContent.setText(getString(R.string.get_reward)
                            + rewardItem.getAmount() + " " + rewardItem.getType());
                    mBtnRewardContent.setVisibility(View.VISIBLE);
                }
            });
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
            mTvMsg.setText(getString(R.string.wrong_msg) + code + " " + errorMsg);
            mTvMsg.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 页面不可见需要移除广告view
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRewardExpressAd != null) {
            mRewardExpressAd.release();
        }
    }
}
