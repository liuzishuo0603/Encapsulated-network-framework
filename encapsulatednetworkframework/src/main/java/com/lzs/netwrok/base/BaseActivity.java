package com.lzs.netwrok.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends SimpleActivity {
    public P mPresener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresener = caterPresenter();
        super.onCreate(savedInstanceState);
        if (mPresener != null) {
            mPresener.attach((V) this);
        }
    }

    public void showPresenter() {

    }

    public void hidePresenter() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresener != null) {
            mPresener.detach();
        }
    }

    protected abstract P caterPresenter();
}
