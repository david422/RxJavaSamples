package com.pgssoft.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main{


    public static void main(String... strings) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(2);


        UserProvider userProvider = new UserProvider(es);


        Observable<String> stream1 = userProvider.getRandomUser().map(user -> "Stream1: " + user.toString());
        Observable<String> stream2 = userProvider.getDelayedUser(150).map(user -> "Stream2: " + user.toString());
        Observable<String> stream3 = userProvider.getDelayedUser(200).map(user -> "Stream3: " + user.toString());

        Observable.concat(stream1, stream2, stream3)
                .observeOn(Schedulers.from(es))
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.print("User: " + o + "\n");
                    }


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete users stream");
                        es.shutdownNow();
                    }
                });

        while (!es.isShutdown());
        System.out.println("Complete main thread");
    }
}
