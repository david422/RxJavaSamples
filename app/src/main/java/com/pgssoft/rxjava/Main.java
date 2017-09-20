package com.pgssoft.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {


    public static void main(String... strings) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(5);

        UserProvider userProvider = new UserProvider(es);

        Observable<String> userErrorStream = userProvider.getDelayedUser(10, 100)
                .doOnSubscribe(d -> System.out.println("Subscribe user stream "))
                .doFinally(() -> System.out.println("Unsubscribe user stream"))
                .doOnNext(u -> {
                    if (u.getAge() >= 50) {
                        throw new IllegalStateException("User is too old");
                    }
                })
                .map(u -> " Stream 1 " + u.toString())
                .subscribeOn(Schedulers.from(es));

        Observable<String> userStream = userProvider.getDelayedUser(10, 100)
                .doOnSubscribe(d -> System.out.println("Subscribe user stream "))
                .doFinally(() -> System.out.println("Unsubscribe user stream"))
                .map(u -> " Stream 2 " + u.toString())
                .subscribeOn(Schedulers.from(es));


        userErrorStream
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        return "Error occurred: " + throwable.getMessage();
                    }
                })
//                .onErrorReturnItem("Error occurred")

                .observeOn(Schedulers.from(es), true)
                .doOnTerminate(es::shutdownNow)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
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
