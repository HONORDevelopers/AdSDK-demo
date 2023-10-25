package com.hihonor.adsdk.demo.external;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.feed.PictureTextAdLoadListener;
import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.demo.external.common.InstallPictureTextListAdapter;
import com.hihonor.adsdk.picturetextad.PictureTextAdLoad;

import java.util.List;


/**
 * 功能描述 更新器集成参考页面
 * 更新器广告目前使用信息流广告模板
 * 使用 RecyclerView 加载广告列表
 *
 * @since 2022-10-20
 */
public class InstallMangerActivity extends BaseActivity {
    private static final String TAG = InstallMangerActivity.class.getSimpleName();

    /**
     * 广告位ID
     */
    private String mSlotId = "1645712748299026432";

    private InstallPictureTextListAdapter mAdapter;

    private RecyclerView mPictureTextRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_picture_text_install_ad));

        setContentView(R.layout.activity_picture_text_install);
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
        AdSlot adSlot = new AdSlot.Builder()
            .setSlotId(mSlotId)
            .setAdContext("{\"userInstalledPkgName\": \"com.hihonor.app\", \"userInstalledPkgSize\": 60, \"userInstalledPkgIndustryCode\": \"10001\", \"mediaLmt\": 0}")
            .build();
        PictureTextAdLoad pictureTextAdLoad = new PictureTextAdLoad.Builder()
            .setAdSlot(adSlot)
            .setPictureTextAdLoadListener(new AdLoadListener())
            .build();
        pictureTextAdLoad.loadAd();
    }

    /**
     * 广告加载状态监听器
     */
    public class AdLoadListener implements PictureTextAdLoadListener {

        /**
         * 广告加载失败
         *
         * @param code 错误码
         * @param errorMsg 错误提示信息
         */
        @Override
        public void onFailed(String code, String errorMsg) {
            HiAdsLog.i(TAG, "onFailed: code: " + code + ", errorMsg: " + errorMsg);
            Toast.makeText(InstallMangerActivity.this, errorMsg,
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
                Toast.makeText(InstallMangerActivity.this, "Request success but data is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < adViewList.size(); i++) {
                PictureTextExpressAd expressAd = adViewList.get(i);
                expressAd.setAdListener(new MyAdListener());
            }
            mAdapter = new InstallPictureTextListAdapter(adViewList);
            mPictureTextRecyclerView.setAdapter(mAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    InstallMangerActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false);

            // 设置 recycler_view 的间隔线
            int[] ATTRS = new int[]{android.R.attr.listDivider};
            TypedArray a = InstallMangerActivity.this.obtainStyledAttributes(ATTRS);
            Drawable divider = a.getDrawable(0);

            // 设置间隔线边距
            int inset = getResources().getDimensionPixelSize(R.dimen.fab_margin_84);
            int insetRight = getResources().getDimensionPixelSize(R.dimen.fab_margin_12);
            //设置各个边距 int insetLeft, int insetTop, int insetRight, int insetBottom
            InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, insetRight, 0);
            a.recycle();
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mPictureTextRecyclerView.getContext(),linearLayoutManager.getOrientation());
            dividerItemDecoration.setDrawable(insetDivider);
            mPictureTextRecyclerView.addItemDecoration(dividerItemDecoration);

            mPictureTextRecyclerView.setLayoutManager(linearLayoutManager);
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
            Toast.makeText(InstallMangerActivity.this, "广告关闭", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(InstallMangerActivity.this, "跳过广告", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(InstallMangerActivity.this, "点击广告", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(InstallMangerActivity.this, "展示成功", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(InstallMangerActivity.this, "展示失败", Toast.LENGTH_SHORT).show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(InstallMangerActivity.this, "跳转小程序", Toast.LENGTH_SHORT).show();
        }
    }

}
