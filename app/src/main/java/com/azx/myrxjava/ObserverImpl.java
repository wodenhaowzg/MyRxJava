package com.azx.myrxjava;

import com.azx.myrxjava.utils.MyLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObserverImpl implements Observer {

    private static final String TAG = "ObserverImpl";

    @Override
    public void onSubscribe(Disposable d) {
        MyLog.e(TAG, "onSubscribe");
    }

    @Override
    public void onNext(Object o) {
        MyLog.d(TAG, "onNext : " + o.toString());
    }

    @Override
    public void onError(Throwable e) {
        MyLog.e(TAG, "onError : " + e.getLocalizedMessage());
    }

    @Override
    public void onComplete() {
        MyLog.d(TAG, "onComplete");
    }
}
