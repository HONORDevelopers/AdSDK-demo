package com.hihonor.adsdk.demo.external.reward;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.reward.RewardAdLoadListener;
import com.hihonor.adsdk.base.api.reward.RewardExpressAd;
import com.hihonor.adsdk.base.api.reward.RewardItem;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.DemoApplication;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.reward.RewardAdLoad;

public class RewardActivity extends BaseActivity {

    private static final String TAG = "RewardActivity";

    private TextView mTvMsg;

    private Button mBtnRewardContent;
    private Switch mAdSwitch;

    /**
     * 广告位ID
     */
    private String mSlotId = DemoApplication.getAdUnitId(R.string.reward_720_1280_land_unit_id);

    private String mRewardName = "Q点券";

    private double mRewardAmount = 1000;

    /**
     * 广告对象
     */
    private RewardExpressAd mRewardExpressAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_reward_ad));
        setContentView(R.layout.activity_reward);
        initView();
    }

    private void initView() {
        mTvMsg = findViewById(R.id.tv_msg);
        mBtnRewardContent = findViewById(R.id.btn_reward_content);
        mBtnRewardContent.setVisibility(View.INVISIBLE);
        mAdSwitch = findViewById(R.id.ad_switch);
        findViewById(R.id.btn_reward_landscape_1280_720).setOnClickListener(view -> {
            mSlotId = DemoApplication.getAdUnitId(R.string.reward_1280_720_land_unit_id);
            obtainAd();
        });
        findViewById(R.id.btn_reward_portrait_1280_720).setOnClickListener(view -> {
            mSlotId = DemoApplication.getAdUnitId(R.string.reward_1280_720_portrait_unit_id);
            obtainAd();
        });
        findViewById(R.id.btn_reward_landscape_720_1280).setOnClickListener(view -> {
            mSlotId = DemoApplication.getAdUnitId(R.string.reward_720_1280_land_unit_id);
            obtainAd();
        });
        findViewById(R.id.btn_reward_portrait_720_1280).setOnClickListener(view -> {
            mSlotId = DemoApplication.getAdUnitId(R.string.reward_720_1280_portrait_unit_id);
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
                .setRewardAmount(mRewardAmount) // 设置激励视频广告奖励数量
                .setRewardName(mRewardName) // 设置激励视频广告奖励名称
                .setPreCacheVideo(mAdSwitch.isChecked())
                .build();
        // step4：构建广告加载器，传入已创建好的广告请求参数对象与广告加载状态监听器。
        RewardAdLoad load = new RewardAdLoad.Builder()
                .setRewardAdLoadListener(mAdLoadListener) // 必填，注册广告加载状态监听器。
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
    private final RewardAdLoadListener mAdLoadListener = new RewardAdLoadListener() {

        /**
         * 广告加载成功回调
         *
         * @param rewardExpressAd 激励广告模板接口基类
         */
        @Override
        public void onLoadSuccess(RewardExpressAd rewardExpressAd) {
            mRewardExpressAd = rewardExpressAd;
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            // 注册广告事件监听器，您可根据需求实现接口并按需重写您需要接收通知的方法。
            rewardExpressAd.setAdListener(new AdListener(){

                /**
                 * 广告曝光时回调
                 */
                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    HiAdsLog.i(TAG, "onAdImpression...");
                    Toast.makeText(RewardActivity.this, getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
                }

                /**
                 * 广告曝光失败时回调
                 *
                 * @param msg 曝光失败信息
                 */
                @Override
                public void onAdImpressionFailed(int errCode, String msg) {
                    super.onAdImpressionFailed(errCode, msg);
                    HiAdsLog.i(TAG, "onAdImpressionFailed, msg: " + msg);
                    Toast.makeText(RewardActivity.this, getString(R.string.ad_impression_failed), Toast.LENGTH_SHORT).show();
                }

                /**
                 * 广告被点击时回调
                 */
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    HiAdsLog.i(TAG, "onAdClicked...");
                    Toast.makeText(RewardActivity.this, getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
                }

                /**
                 * 广告关闭时回调
                 */
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    HiAdsLog.i(TAG, "onAdClosed...");
                    Toast.makeText(RewardActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
                    releaseAd();
                }

                /**
                 * 广告成功跳转小程序时回调
                 */
                @Override
                public void onMiniAppStarted() {
                    super.onMiniAppStarted();
                    HiAdsLog.i(TAG, "onMiniAppStarted...");
                    Toast.makeText(RewardActivity.this, getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
                }
            });
            // step3：在请求成功回调里，使用返回的广告对象作渲染处理。
            rewardExpressAd.show(RewardActivity.this, new RewardExpressAd.RewardAdStatusListener() {

                /**
                 * 激励广告关闭
                 */
                @Override
                public void onRewardAdClosed() {
                    HiAdsLog.i(TAG,"onRewardAdClosed");
                    releaseAd();
                }

                @Override
                public void onVideoError(int errorCode) {
                    HiAdsLog.i(TAG,"onRewardAdFailedToShow,errorCode: " + errorCode);
                }


                /**
                 * 激励广告打开时
                 */
                @Override
                public void onRewardAdOpened() {
                    HiAdsLog.i(TAG,"onRewardAdOpened");
                }

                /**
                 * 获得奖励
                 *
                 * @param rewardItem 激励奖励类
                 */
                @Override
                public void onRewarded(RewardItem rewardItem) {
                    HiAdsLog.i(TAG, "onRewarded, 成功获得奖励, amount: " + rewardItem.getAmount() + ", type: " + rewardItem.getType());
                    mBtnRewardContent.setText(getString(R.string.get_reward) + rewardItem.getAmount() + " " + rewardItem.getType());
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
        if (mRewardExpressAd != null) {
            HiAdsLog.i(TAG, "releaseAd...");
            mRewardExpressAd.release();
        }
    }

}
