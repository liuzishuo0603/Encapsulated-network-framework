package com.lzs.netwrok.base;

import java.lang.ref.WeakReference;

public class BasePresenter<V> {
    public V mView;
    private WeakReference<V> mVWeakReference;

    public void attach(V view) {
        mVWeakReference = new WeakReference<>(view);
        mView = mVWeakReference.get();
    }

    public void detach() {
        if (mVWeakReference != null) {
            mVWeakReference.clear();
        }
    }
}
