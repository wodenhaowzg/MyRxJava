package com.azx.myrxjava;

import com.azx.myrxjava.utils.MyLog;

import io.reactivex.functions.Consumer;

public class ConsumerImpl<T> implements Consumer<T> {
    private static final String TAG = "ConsumerImpl";

    @Override
    public void accept(T o) throws Exception {
        MyLog.d(TAG, "accept : " + o.toString());
    }
}
