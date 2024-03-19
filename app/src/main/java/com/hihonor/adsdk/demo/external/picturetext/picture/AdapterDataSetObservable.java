package com.hihonor.adsdk.demo.external.picturetext.picture;


import android.database.Observable;

/**
 * 功能描述
 *
 * @since 2022-11-28
 */
public class AdapterDataSetObservable extends Observable<AdapterDataSetObserver> {
    public void notifyChanged() {
        if (mObservers == null) {
            return;
        }
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onChanged();
        }
    }
    public boolean contains(AdapterDataSetObserver dataSetObserver) {
        return mObservers.contains(dataSetObserver);
    }
}
