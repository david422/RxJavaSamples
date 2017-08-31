package com.pgssoft.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {


    public static void main(String... strings) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(4);

        UserProvider userProvider = new UserProvider(es);

        Observable<String> stream1 = userProvider.getDelayedUser(1000)
                .map(user -> "Stream1: " + user.toString()).subscribeOn(Schedulers.from(es));

        Observable<String> stream2 = userProvider.getDelayedUser(150)
                .map(user -> "Stream2: " + user.toString()).subscribeOn(Schedulers.from(es))
//                .doOnNext(System.out::println)
                ;

        Observable.zip(stream1, stream2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String stream, String stream2) throws Exception {
                return "Zipped user : " + stream + ", user: " + stream2;
            }
        })
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
