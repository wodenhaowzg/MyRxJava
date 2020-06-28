package com.azx.myrxjava.test;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class WatchTest implements BaseTest {

    private static final String TAG = "WatchTest";

    @Override
    public void startTest() {
        LocalFlowableOnSubscribe localFlowableOnSubscribe = new LocalFlowableOnSubscribe();
        // 通过操作符指定背压策略，如果没有指定onBackpressureXXX，默认是 ERROR 模式，直接报错。
        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.MISSING) // 这里填 MISSING，通过背压操作符指定
                .onBackpressureLatest() // BackpressureStrategy.LATEST
                .subscribeOn(Schedulers.io())
//                .subscribeOn(Schedulers.computation())
//                .subscribeOn(Schedulers.single())
//                .subscribeOn(Schedulers.trampoline())
//                .subscribeOn(Schedulers.newThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io());

        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalSubscriber mLocalSubscriber = new LocalSubscriber();
            localFlowableOnSubscribe.setEventName("事件" + i);
            flowable.subscribe(mLocalSubscriber);
        }
    }

    private class LocalFlowableOnSubscribe implements FlowableOnSubscribe<String> {

        private String mEventName;

        public void setEventName(String mEventName) {
            this.mEventName = mEventName;
        }

        @Override
        public void subscribe(FlowableEmitter<String> emitter) throws Exception {
            String eventName = mEventName;
            log("发送事件: " + eventName + " | 线程 :" + Thread.currentThread().getId() + " | 线程数量 : " + Thread.activeCount());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            emitter.onNext(eventName);
            emitter.onComplete();
        }
    }

    private class LocalSubscriber implements Subscriber<String>, Disposable {

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
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            log("接收事件 " + s + " | 线程 :" + Thread.currentThread().getId());
            mSubscription.request(1);
        }

        @Override
        public void onError(Throwable t) {
            log("接收onError " + t.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
        }

        @Override
        public void dispose() {

        }

        @Override
        public boolean isDisposed() {
            return false;
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg + " | Thread id : " + Thread.currentThread().getId()
                + " | Thread size : " + Thread.activeCount());
    }
}
