package com.pgssoft.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {


    public static void main(String... strings) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(2);

        UserProvider userProvider = new UserProvider(es);

        Observable<String> stream1 = userProvider.getDelayedUser(10)
                .doOnSubscribe(d -> System.out.println("Subscribe stream 1"))
                .map(user -> "Stream1: " + user.toString()).subscribeOn(Schedulers.from(es));

        Observable<String> stream2 = userProvider.getDelayedUser(150)
                .doOnSubscribe(d -> System.out.println("Subscribe stream 2"))
                .map(user -> "Stream2: " + user.toString()).subscribeOn(Schedulers.from(es));

        List<Observable<String>> streamList = new ArrayList<>();
        streamList.add(stream1);
        streamList.add(stream2);

        Observable[] streamArray = new Observable[]{stream1, stream2};
        Observable.zipArray(new Function<Object[], String>() {
            @Override
            public String apply(Object[] objects) throws Exception {
                return objects[0].toString() + " " + objects[1].toString();
            }
        }, true, 1, streamArray)
                .observeOn(Schedulers.from(es))
                .doOnTerminate(es::shutdownNow)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String o) {
                        System.out.print(o + "\n");
                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete users stream");

                    }
                });

        while (!es.isShutdown()) ;
        System.out.println("Complete main thread");
    }
}
