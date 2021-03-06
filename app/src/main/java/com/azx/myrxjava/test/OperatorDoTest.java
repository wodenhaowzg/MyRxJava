package com.azx.myrxjava.test;

import android.util.Log;

import com.azx.myrxjava.ConsumerImpl;
import com.azx.myrxjava.ObserverImpl;
import com.azx.myrxjava.bean.News;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OperatorDoTest implements BaseTest {

    private static final String TAG = "OperatorDoTest";
    private ObserverImpl mObserverImpl = new ObserverImpl();
    private ConsumerImpl<Object> mConsumerImpl = new ConsumerImpl<>();
    private List<News> mNews;

    @Override
    public void startTest() {
        mNews = createNews();
//        createOperator();
//        transformOperator();
        polymerizationOperator();
        otherOperator();
    }

    private void otherOperator() {
        Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {

            }
        });
    }

    private void polymerizationOperator() {
        // concat 将多个 Observable 按顺序连接起来
        Observable<String> first = Observable.just("first");
        Observable<String> second = Observable.just("second");
        Observable<String> concat = Observable.concat(first, second);
        Disposable subscribe = concat.subscribe(new Consumer<String>() {//相当于onNext
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "concat accept : " + s);
            }
        });
    }

    private List<News> createNews() {
        long startTime = System.currentTimeMillis();
        List<News> temp = new ArrayList<>();
        temp.add(new News(startTime, "aaa", "a_author"));
        temp.add(new News(startTime + 1000, "bbb", "b_author"));
        temp.add(new News(startTime + 2000, "ccc", "c_author"));
        temp.add(new News(startTime + 3000, "ddd", "d_author"));
        temp.add(new News(startTime + 4000, "eee", "e_author"));
        return temp;
    }

    private void transformOperator() {
        // flatMap 将集合拆分为一个一个的 Observable 并发送
        Observable.just(mNews).flatMap(new Function<List<News>, ObservableSource<News>>() {
            @Override
            public ObservableSource<News> apply(List<News> news) throws Exception {
                return Observable.fromIterable(news);
            }
        }).subscribe(mObserverImpl);

        // map 仅在发送事件前支持一个函数进行前处理
        Observable.just(mNews).map(new Function<List<News>, ObservableSource<News>>() {
            @Override
            public ObservableSource<News> apply(List<News> news) throws Exception {
                return Observable.fromIterable(news);
            }
        }).subscribe(mObserverImpl);

    }

    private void createOperator() {
//        final String[] strArray = new String[]{"from operator aaa", "from operator bbb", "from operator ccc"};
//        // create
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                emitter.onNext("create operator aaa");
//                emitter.onNext("create operator bbb");
//                emitter.onNext("create operator ccc");
//                emitter.onComplete();
//            }
//        }).subscribe(mLocalObserver);

        // 将集合转换为 Observable 对象集合，依次发送。
//        News[] news = new News[mNews.size()];
//        mNews.toArray(news);
//        Observable.fromArray(news).subscribe(mLocalObserver);

        // 将某个对象转换为 Observable 对象。
//        Observable.just(news).subscribe(mLocalObserver);

//        Observable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                Log.d(TAG, "interval Consumer accept : " + aLong);
//            }
//        });
    }
}
