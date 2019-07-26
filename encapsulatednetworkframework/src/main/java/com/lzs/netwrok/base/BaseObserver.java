package com.lzs.netwrok.base;

import com.lzs.netwrok.http.ApiException;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class BaseObserver<T> implements Observer<T> {
    //管理每一次的网络请求
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onSubscribe(Disposable d) {
        mCompositeDisposable.add(d);
    }

    @Override
    public void onNext(T value) {
        onSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            int errorCode = apiException.getErrorCode();
            switch (errorCode) {
                case 1:
            }
            onFail(apiException.getErrorMsg());
            //网络请求失败
        } else if (e instanceof HttpException) {
            onFail(e.getMessage());
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void onComplete() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    abstract <T> void onSuccess(T data);

    abstract <T> void onFail(T error);
}
