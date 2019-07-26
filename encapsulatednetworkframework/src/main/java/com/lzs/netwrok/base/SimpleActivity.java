package com.lzs.netwrok.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class SimpleActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(caterLayout());
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initViewAndData();
    }

    protected abstract void initViewAndData();

    protected abstract int caterLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder!=null){
            mUnbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }
}
