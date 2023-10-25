package com.hihonor.adsdk.demo.external.picturetext;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hihonor.adsdk.base.AdSlot;
import com.hihonor.adsdk.base.api.feed.PictureTextAdLoadListener;
import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;
import com.hihonor.adsdk.base.callback.AdListener;
import com.hihonor.adsdk.base.log.HiAdsLog;
import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;
import com.hihonor.adsdk.demo.external.picturetext.picture.PictureMediaDataBean;
import com.hihonor.adsdk.demo.external.utils.GlobalConfig;
import com.hihonor.adsdk.picturetextad.PictureTextAdLoad;

import java.util.ArrayList;
import java.util.List;

public class PictureTextSelfRenderActivity extends BaseActivity {

    private static final String TAG = "PictureTextSelfTAG";
    private String mSlotId = "1698588952478875648";

    private PictureTextListSelfRenderAdapter mAdapter;
    private RecyclerView mPictureTextRecyclerView;

    private List<PictureMediaDataBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HiAdsLog.i(TAG, "onCreate");
        setTitle(getString(R.string.app_picture_text_self_render_ad));
        setContentView(R.layout.activity_picture_text_self_render);
        initView();
    }

    private void initView() {
        HiAdsLog.i(TAG, "initView");
        mPictureTextRecyclerView = findViewById(R.id.picture_text_self_render_recycler_view);
        Button adLoadButton = findViewById(R.id.bt_load_ad);
        mAdapter = new PictureTextListSelfRenderAdapter();
        mPictureTextRecyclerView.setAdapter(mAdapter);
        mPictureTextRecyclerView.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false)
        );
        adLoadButton.setOnClickListener((v) -> {
            releaseAd();
            obtainAd();
        });
    }

    /**
     * 获取广告
     */
    private void obtainAd() {
        AdSlot adSlot = new AdSlot.Builder().setSlotId(mSlotId)
            .setRenderType(GlobalConfig.AD_RENDERING_TYPE.SELF_RENDERING).build();
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
                Toast.makeText(PictureTextSelfRenderActivity.this, "Request success but data is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            List<PictureMediaDataBean> oldDatas = new ArrayList<>(mDatas);
            mDatas.clear();
            for (int i = 0; i < adViewList.size(); i++) {
                createMediaData(i);
                PictureTextExpressAd expressAd = adViewList.get(i);
                HiAdsLog.i(TAG, "是否有模板视图 %s position=%s,： ", expressAd.getExpressAdView(), i);
                PictureMediaDataBean pictureMediaDataBean = new PictureMediaDataBean();
                String itemName = expressAd.getRequestId();
                pictureMediaDataBean.setItemName(itemName);
                pictureMediaDataBean.setExpressAd(expressAd);
                Log.i(TAG,"应用名称:" + expressAd.getBrand()
                        + "\t\n应用版本号：" + expressAd.getAppVersion()
                        + "\t\n应用包名：" + expressAd.getAppPackage()
                        + "\t\n开发者公司名称：" + expressAd.getDeveloperName()
                        + "\t\n隐私协议超链：" + expressAd.getPermissionsUrl()
                        + "\t\n权限列表超链：" + expressAd.getPrivacyAgreementUrl()
                        + "\r\n主页超链：" + expressAd.getHomePage());
                if (expressAd.hasVideo()) {
                    // 视频广告
                    if (i % 2 == 0) {
                        pictureMediaDataBean.setItemType(2);
                    } else {
                        pictureMediaDataBean.setItemType(3);
                    }
                } else {
                    // 信息流广告
                    pictureMediaDataBean.setItemType(1);
                }
                mDatas.add(pictureMediaDataBean);
                expressAd.setAdListener(new MyAdListener());
            }
            mAdapter = new PictureTextListSelfRenderAdapter();
            mPictureTextRecyclerView.setAdapter(mAdapter);
            mAdapter.setMediaDataBeanList(mDatas);
            mAdapter.notifyDataSetChanged();
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
            Toast.makeText(PictureTextSelfRenderActivity.this, "广告关闭", Toast.LENGTH_SHORT)
                .show();
        }

        /**
         * 广告被点击时回调
         */
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            HiAdsLog.i(TAG, "onAdClicked...");
            Toast.makeText(PictureTextSelfRenderActivity.this, "点击广告", Toast.LENGTH_SHORT)
                .show();
        }

        /**
         * 广告曝光时回调
         */
        @Override
        public void onAdImpression() {
            super.onAdImpression();
            HiAdsLog.i(TAG, "onAdImpression...");
            Toast.makeText(PictureTextSelfRenderActivity.this, "展示成功", Toast.LENGTH_SHORT)
                .show();
        }

        /**
         * 广告成功跳转小程序时回调
         */
        @Override
        public void onMiniAppStarted() {
            super.onMiniAppStarted();
            HiAdsLog.i(TAG, "onMiniAppStarted...");
            Toast.makeText(PictureTextSelfRenderActivity.this, "跳转小程序", Toast.LENGTH_SHORT)
                .show();
        }

    }

    private void createMediaData(int number) {
        for (int i = 0; i < 3; i++) {
            PictureMediaDataBean pictureMediaDataBean = new PictureMediaDataBean();
            String itemName = String.format("Media-Data-item-%s-%s", number, i);
            pictureMediaDataBean.setItemName(itemName);
            mDatas.add(pictureMediaDataBean);
        }
    }

    private class DiffCallBack extends DiffUtil.Callback {
        List<PictureMediaDataBean> mNewDatas;
        List<PictureMediaDataBean> mOldDatas;

        public DiffCallBack(List<PictureMediaDataBean> oldDatas, List<PictureMediaDataBean> newDatas) {
            mNewDatas = newDatas;
            mOldDatas = oldDatas;
        }

        @Override
        public int getOldListSize() {
            return mOldDatas != null ? mOldDatas.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewDatas != null ? mNewDatas.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            String oldItemName = mOldDatas.get(oldItemPosition).getItemName();
            String newItemName = mNewDatas.get(newItemPosition).getItemName();
            return TextUtils.equals(oldItemName, newItemName);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            String oldItemName = mOldDatas.get(oldItemPosition).getItemName();
            String newItemName = mNewDatas.get(newItemPosition).getItemName();
            if (!TextUtils.equals(oldItemName, newItemName)) {
                return false;
            }
            PictureTextExpressAd oldExpressAd = mOldDatas.get(oldItemPosition).getExpressAd();
            PictureTextExpressAd newExpressAd = mNewDatas.get(newItemPosition).getExpressAd();
            if (newExpressAd != oldExpressAd) {
                return false;
            }
            return true; //默认两个data内容是相同的
        }

        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            PictureMediaDataBean oldStudent = mOldDatas.get(oldItemPosition);
            PictureMediaDataBean newStudent = mNewDatas.get(newItemPosition);
            Bundle diffBundle = new Bundle();
            if (oldStudent != newStudent) {
                diffBundle.putSerializable("update_item", newStudent);
            }
            return diffBundle;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseAd();
    }

    private void releaseAd() {
        // 销毁资源
        if (mDatas != null && !mDatas.isEmpty()) {
            for (PictureMediaDataBean data : mDatas) {
                PictureTextExpressAd expressAd = data.getExpressAd();
                if (expressAd != null) {
                    expressAd.release();
                }
            }
        }
    }
}
