package com.azx.myrxjava;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class FlowableTest {

    private static final String TAG = "BackpressureTest";

    public void startFlowableTest() {
        Flowable flowable = createFlowable(BackpressureStrategy.MISSING);
        LocalSubscriber mLocalSubscriber = new LocalSubscriber();
        // 测试10000次订阅下，背压的策略
//        for (int i = 0; i < 1000; i++) {
        flowable.subscribeOn(Schedulers.newThread()).subscribe(mLocalSubscriber);
//        }

//        LocalSubscriber mLocalSubscriber2 = new LocalSubscriber();
//        log(" ------------------------------------------------------------");
//        for (int i = 0; i < 1000; i++) {
//            flowable.subscribeOn(Schedulers.io()).subscribe(mLocalSubscriber2);
//        }
//
//        log(" ------------------------------------------------------------");
//        LocalSubscriber mLocalSubscriber3 = new LocalSubscriber();
//        for (int i = 0; i < 1000; i++) {
//            flowable.subscribeOn(Schedulers.single()).subscribe(mLocalSubscriber3);
//        }
    }

    public void startFlowableTest2() {
        Flowable flowable = createFlowable2(BackpressureStrategy.MISSING);
        LocalSubscriber mLocalSubscriber = new LocalSubscriber();
        flowable.subscribeOn(Schedulers.newThread()).
                observeOn(Schedulers.io()).
                subscribe(mLocalSubscriber);
    }

    private Flowable createFlowable(BackpressureStrategy backpressureStrategy) {
        return Flowable.create(new FlowableOnSubscribe<String>() {

            public void subscribe(FlowableEmitter<String> emitter) {
                emitter.onNext("send event");
                emitter.onComplete();
            }
        }, backpressureStrategy); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
    }

    private Flowable createFlowable2(BackpressureStrategy backpressureStrategy) {
        return Flowable.create(new FlowableOnSubscribe<String>() {

            public void subscribe(FlowableEmitter<String> emitter) {
                for (int i = 0; i < 1000; i++) {
                    log("发送事件 " + i + " | " +  emitter.requested());
                    emitter.onNext("事件 " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        }, backpressureStrategy); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
    }

    private class LocalSubscriber implements Subscriber<String> {

        private volatile int mReceiveEventNum;
        private Subscription mSubscription;

        @Override
        public void onSubscribe(Subscription s) {
            log("onSubscribe " + this);
//            mSubscription.request(1);
        }

        @Override
        public void onNext(String s) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("接收事件 " + s + " class : " + this);
//            mSubscription.request(1);
//            synchronized (LocalSubscriber.class) {
//                mReceiveEventNum++;
//            }
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.d(TAG, "onNext invoked! " + s + " | Thread id : " + Thread.currentThread().getId() + " class : " + this);
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            log("接收onError " + t.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
//            synchronized (LocalSubscriber.class) {
//                log("complete invoked! " + mReceiveEventNum + " class : " + this);
//            }
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg + " | Thread id : " + Thread.currentThread().getId()
                + " | Thread size : " + Thread.activeCount());
    }
}
