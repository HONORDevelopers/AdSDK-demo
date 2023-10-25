package com.hihonor.adsdk.demo.external.picturetext.picture;


import com.hihonor.adsdk.base.api.feed.PictureTextExpressAd;

import java.io.Serializable;

/**
 * 功能描述
 *
 * @since 2023-06-07
 */
public class PictureMediaDataBean implements Serializable {
    private String itemName;
    private PictureTextExpressAd mExpressAd;
    private int itemType = 0;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public PictureTextExpressAd getExpressAd() {
        return mExpressAd;
    }

    public void setExpressAd(PictureTextExpressAd expressAd) {
        mExpressAd = expressAd;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
