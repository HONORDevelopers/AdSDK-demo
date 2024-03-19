package com.hihonor.adsdk.demo.external.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.BaseAd;
import com.hihonor.adsdk.base.api.splash.SplashAdLoadListener;
import com.hihonor.adsdk.base.api.splash.SplashExpressAd;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.base.widget.base.CountdownView;
import com.hihonor.adsdk.demo.external.DemoApplication;
import com.hihonor.adsdk.demo.external.MainActivity;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.utils.Constants;
import com.hihonor.adsdk.splash.SplashActionType;
import com.hihonor.adsdk.splash.SplashAdLoad;
import com.hihonor.adsdk.splash.view.SplashAdRootView;

import java.util.List;

public class SplashSelfActivity extends Activity implements ShakeManager.OnShakeListener {

    private static final String TAG = "SplashSelfActivity";

    /**
     * 广告位ID
     */
    private String mSlotId = DemoApplication.getAdUnitId(R.string.splash_1080_1620_unit_id);

    private long mTimeOut = 0;

    /**
     * 是否强制跳转到主页面
     */
    private boolean mForceGoMain;

    private FrameLayout mRootView;

    /**
     * 广告对象
     */
    private SplashExpressAd mSplashExpressAd;

    private SplashAdRootView splashAdRootView;

    private FrameLayout mSplashLayout;

    private ImageView mAdImageView;

    private ImageView mIconImageView;

    private LottieAnimationView lottieAnimationView;

    private LottieAnimationView sweepAnimationView;

    private CountdownView mCountdownView;

    private TextView adMediaNameView;

    private TextView mAdFlagView;

    private TextView mActionTipsView;

    private TextView mTargetTipsView;

    private View mTargetClickLayout;

    private float downY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenUtils.transparentStatusBar(getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_self);
        init();
    }

    public void init() {
        mRootView = findViewById(R.id.root_view);
        obtainAd();
    }

    public void initView(View itemView) {
        splashAdRootView = itemView.findViewById(R.id.splash_root_view);
        mSplashLayout = itemView.findViewById(R.id.ads_splash_layout);
        mAdImageView = itemView.findViewById(R.id.ads_splash_ad_img);
        mCountdownView = itemView.findViewById(R.id.ad_countdown);
        mIconImageView = itemView.findViewById(R.id.ads_splash_icon_img);
        lottieAnimationView = itemView.findViewById(R.id.ad_animation_view);
        sweepAnimationView = itemView.findViewById(R.id.ads_click_button_sweep);
        adMediaNameView = itemView.findViewById(R.id.ads_media_name);
        mAdFlagView = itemView.findViewById(R.id.ad_flag);
        mActionTipsView = itemView.findViewById(R.id.ad_action_prompt);
        mTargetTipsView = itemView.findViewById(R.id.ad_action_result);
        mTargetClickLayout = itemView.findViewById(R.id.ads_click_layout);
        setSplashLayoutParam();
    }

    private void setSplashLayoutParam() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSplashLayout.getLayoutParams();
        params.topMargin = ScreenUtils.getStateBarHeight();
        mSplashLayout.setLayoutParams(params);
    }

    public void obtainAd() {
        // step1：创建广告请求参数对象（AdSlot）。
        AdSlot.Builder builder = new AdSlot.Builder()
            .setSlotId(mSlotId) // 必填，设置广告位ID。
            .setRenderType(1) // 设置渲染类型，0：模板渲染； 1 ：自渲染；
            .setLoadType(0); // 设置媒体请求Type，目前仅为开屏广告使用。0：普通加载方式，会首先去读缓存； 1 ：预缓存加载，将数据保存至缓存； -1：默认请求方式，表示直接进行网络请求，数据不保存缓存
        if (mTimeOut > 0) {
            builder.setTimeOutMillis(mTimeOut);
        }
        // step4：构建广告加载器，传入已创建好的广告请求参数对象与广告加载状态监听器。
        SplashAdLoad load = new SplashAdLoad.Builder()
            .setSplashAdLoadListener(mAdLoadListener) // 必填，注册广告加载状态监听器。
            .setAdSlot(builder.build()) // 必填，设置广告请求参数。
            .build();
        // step5：加载广告
        load.loadAd();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (downY - event.getY() > 400) {
                    startThirdPage(v, Constants.AD_ACTION_TYPE.ACTION_SLIDE_UP);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void onClick(View view) {
        startThirdPage(view, Constants.AD_ACTION_TYPE.ACTION_CLICK);
    }

    @Override
    public void onShake() {
        HiAdsLog.i(TAG, "shake");
        startThirdPage(mAdImageView, Constants.AD_ACTION_TYPE.ACTION_SHAKE);
    }

    public void openClickListener() {
        lottieAnimationView.setVisibility(View.GONE);
        mActionTipsView.setVisibility(View.GONE);
        mActionTipsView.setVisibility(View.GONE);
        mTargetTipsView.setVisibility(View.GONE);
        mTargetClickLayout.setVisibility(View.VISIBLE);
        mTargetClickLayout.setOnClickListener(this::onClick);
        AnimatorUtils.startScaleAndAlphaAnimator(mTargetClickLayout, 300, () -> {
            sweepAnimationView.setVisibility(View.VISIBLE);
        });
    }

    public void openSlideUpListener() {
        lottieAnimationView.setAnimation("slide_up.json");
        splashAdRootView.setOnTouchListener(this::onTouch);
    }

    ShakeManager shakeManager;

    /**
     * 开启摇一摇监听
     */
    private void openShakeListener(double shakeAngle, double shakeAcc, double shakeDuration) {
        lottieAnimationView.setAnimation("shake.json");
        shakeManager = new ShakeManager();
        shakeManager.setOnShakeListener(this);
        shakeManager.setShakeAngle(shakeAngle);
        shakeManager.setShakeAcc(shakeAcc);
        shakeManager.setShakeDuration(shakeDuration);
    }

    public void startThirdPage(View view, int type) {
        splashAdRootView.startThirdPage(view, type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mForceGoMain) {
            startHomeActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    public void startHomeActivity() {
        // 倒计时结束或者点击跳过
        Intent intent = new Intent(SplashSelfActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * step2：实现广告加载状态监听器，加载过程中获取广告的状态变化。
     * <br>
     * 广告加载状态监听器
     */
    private final SplashAdLoadListener mAdLoadListener = new SplashAdLoadListener() {

        /**
         * 广告加载成功回调。
         *
         * @param splashExpressAd 开屏广告模板接口基类
         */
        @Override
        public void onLoadSuccess(SplashExpressAd splashExpressAd) {
            mSplashExpressAd = splashExpressAd;
            HiAdsLog.i(TAG, "onLoadSuccess, ad load success");
            HiAdsLog.i(TAG, "is Have Template View： " + splashExpressAd.getExpressAdView());
            View itemView = LayoutInflater.from(SplashSelfActivity.this).inflate(R.layout.layout_splash_self, null);
            initView(itemView);
            splashAdRootView.setAd(splashExpressAd);

            List<String> images = splashExpressAd.getImages();
            if (images != null && images.size() > 0) {
                GlideUtils.loadImage(SplashSelfActivity.this, images.get(0), mAdImageView, 0);
            }
            // 注册广告事件监听器，您可根据需求实现接口并按需重写您需要接收通知的方法。
            splashExpressAd.setAdListener(new AdListener(){

                /**
                 * 广告曝光时回调
                 */
                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    HiAdsLog.i(TAG, "onAdImpression...");
                    Toast.makeText(SplashSelfActivity.this, getString(R.string.ad_impression_success), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SplashSelfActivity.this, getString(R.string.ad_impression_failed), Toast.LENGTH_SHORT).show();
                }

                /**
                 * 广告被点击时回调
                 */
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    HiAdsLog.i(TAG, "onAdClicked...");
                    Toast.makeText(SplashSelfActivity.this, getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
                }

                /**
                 * 广告关闭时回调
                 */
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    HiAdsLog.i(TAG, "onAdClosed...");
                    Toast.makeText(SplashSelfActivity.this, getString(R.string.app_ad_close_tip), Toast.LENGTH_SHORT).show();
                }

                /**
                 * 开屏广告点击跳过或倒计时结束时回调
                 *
                 * @param type 0：点击跳过、1：倒计时结束
                 */
                @Override
                public void onAdSkip(int type) {
                    super.onAdSkip(type);
                    HiAdsLog.i(TAG, "onAdSkip, type: " + type);
                    Toast.makeText(SplashSelfActivity.this, getString(R.string.ad_skip), Toast.LENGTH_SHORT).show();
                    startHomeActivity();
                }

                /**
                 * 广告成功跳转小程序时回调
                 */
                @Override
                public void onMiniAppStarted() {
                    super.onMiniAppStarted();
                    HiAdsLog.i(TAG, "onMiniAppStarted...");
                    Toast.makeText(SplashSelfActivity.this, getString(R.string.miniapp_start), Toast.LENGTH_SHORT).show();
                }
            });
            // 设置跳过按钮
            mCountdownView.setBaseAd((BaseAd) splashExpressAd);
            mCountdownView.setCountdown(splashExpressAd.getImpDuration());
            mCountdownView.setTextSize(splashExpressAd.getSkipFontSize());
            mCountdownView.setAdListener(splashExpressAd.getAdListener());

            mAdFlagView.setTextSize(splashExpressAd.getAdFlagFontSize());

            String actionType = splashExpressAd.getActionType();
            switch (actionType) {
                case SplashActionType.CLICK:
                    openClickListener();
                    break;
                case SplashActionType.SLIDE_UP:
                    openSlideUpListener();
                    break;
                case SplashActionType.SHAKE:
                    openShakeListener(splashExpressAd.getShakeAngle(), splashExpressAd.getShakeAcc(), splashExpressAd.getShakeDuration());
                    break;
                case SplashActionType.CLICK_OR_SLIDE_UP:
                    openSlideUpListener();
                    lottieAnimationView.setOnClickListener(SplashSelfActivity.this::onClick);
                    break;
                case SplashActionType.CLICK_OR_SHAKE:
                    openShakeListener(splashExpressAd.getShakeAngle(), splashExpressAd.getShakeAcc(), splashExpressAd.getShakeDuration());
                    lottieAnimationView.setOnClickListener(SplashSelfActivity.this::onClick);
                    break;
                default:
                    break;
            }
            mActionTipsView.setText(splashExpressAd.getActionTips());
            mTargetTipsView.setText(splashExpressAd.getTargetTips());

            //媒体设置
            mIconImageView.setImageResource(R.drawable.ic_launcher_background);
            adMediaNameView.setText(getString(R.string.app_market));
            // step3：在请求成功回调里，使用返回的广告对象作渲染处理。
            // 注意： addView前需要把添加广告的容器rootView将控件上所有的view调用removeAllViews方法移除。
            mRootView.removeAllViews();
            // 添加view，进行广告展示
            mRootView.addView(itemView);
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
            Intent intent = new Intent(SplashSelfActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
        if (shakeManager != null) {
            shakeManager.unregister();
        }
        if (mRootView != null) {
            mRootView.removeAllViews();
        }
        if (mSplashExpressAd != null) {
            HiAdsLog.i(TAG, "releaseAd...");
            mSplashExpressAd.release();
        }
    }
}
