package com.azx.myrxjava.test;

import android.util.Log;

import com.azx.myrxjava.utils.MyLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FlowableTest implements BaseTest {

    private static final String TAG = "BackpressureTest";

    @Override
    public void startTest() {
//        flowableTest();
//        actionTest();
    }

    private void flowableTest() {
        LocalFlowableOnSubscribe localFlowableOnSubscribe = new LocalFlowableOnSubscribe();
        LocalSubscriber localSubscriber = new LocalSubscriber();
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.BUFFER); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.DROP); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.LATEST); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.ERROR); // 指定背压策略，也可以通过调用 onBackpressureXX，如 onBackpressureDrop 实现。
        flowable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(localSubscriber);

        // 通过操作符指定背压策略，如果没有指定onBackpressureXXX，默认是 ERROR 模式，直接报错。
//        Flowable<String> flowable = Flowable.create(localFlowableOnSubscribe, BackpressureStrategy.MISSING); // 这里填 MISSING，通过背压操作符指定
//        flowable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).
//                onBackpressureBuffer(). // BackpressureStrategy.BUFFER
//                onBackpressureDrop(). //  BackpressureStrategy.DROP
//                onBackpressureLatest(). // BackpressureStrategy.LATEST
//                subscribe(localSubscriber);
    }

    /**
     * 通过 Consumer 和 Action 模拟 Subscriber 或 Observer 。
     * Action: 无参数类型
     * Consumer<T>: 单一参数类型
     * 如果是多个参数
     * BiConsumer<T1, T2>: 双参数类型
     * Consumer<Obejct[]>: 多参数类型
     */
    private void actionTest() {
        Observable<String> actionObservable = Observable.just("action coming!");
        Disposable subscribe = actionObservable.subscribe(new Consumer<String>() {//相当于 onNext
            @Override
            public void accept(String s) throws Exception {
                MyLog.d(TAG, "action test, onNext - " + s);
            }
        }, new Consumer<Throwable>() {//相当于 onError
            @Override
            public void accept(Throwable throwable) throws Exception {
                MyLog.d(TAG, "action test, onError - " + throwable.getLocalizedMessage());
            }
        }, new Action() {//相当于 onComplete，注意这里是Action
            @Override
            public void run() throws Exception {
                MyLog.d(TAG, "action test, onComplete ");
            }
        }, new Consumer<Disposable>() {//相当于 onSubscribe
            @Override
            public void accept(Disposable subscription) throws Exception {
                MyLog.d(TAG, "action test, onSubscribe " + subscription.toString());
            }
        });
        MyLog.d(TAG, "actionTest - " + subscribe);
    }


    private class LocalFlowableOnSubscribe implements FlowableOnSubscribe<String> {

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
