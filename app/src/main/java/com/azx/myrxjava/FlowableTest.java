package com.azx.myrxjava;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.schedulers.Schedulers;

public class FlowableTest implements BaseTest{

    private static final String TAG = "BackpressureTest";

    @Override
    public void startTest() {
        LocalFlowableOnSubscribe localFlowableOnSubscribe = new LocalFlowableOnSubscribe();
        LocalSubscriber localSubscriber = new LocalSubscriber();
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.BUFFER); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.DROP); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.LATEST); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.ERROR); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
        flowable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(localSubscriber);

        // 通过操作符指定背压策略，如果没有指定onBackpressureXXX，默认是 ERROR 模式，直接报错。
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.MISSING); // 这里填 MISSING，通过背压操作符指定
//        flowable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).
//                onBackpressureBuffer(). // BackpressureStrategy.BUFFER
//                onBackpressureDrop(). //  BackpressureStrategy.DROP
//                onBackpressureLatest(). // BackpressureStrategy.LATEST
//                subscribe(localSubscriber);
    }


    private class LocalFlowableOnSubscribe implements FlowableOnSubscribe<String>{

        @Override
        public void subscribe(FlowableEmitter<String> emitter) throws Exception {
            for (int i = 0; i < 1000; i++) {
                log("发送事件 " + i + " | 当前可接收事件数量" + emitter.requested());
                emitter.onNext("事件 " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            emitter.onComplete();
        }
    }

    private class LocalSubscriber implements Subscriber<String> {

        private Subscription mSubscription;

        @Override
        public void onSubscribe(Subscription s) {
            log("onSubscribe " + this);
            mSubscription = s;
            mSubscription.request(1);

        }

        @Override
        public void onNext(String s) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("接收事件 " + s + " class : " + this);
            mSubscription.request(1);
        }

        @Override
        public void onError(Throwable t) {
            log("接收onError " + t.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg + " | Thread id : " + Thread.currentThread().getId()
                + " | Thread size : " + Thread.activeCount());
    }
}
