package com.pgssoft.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {


    public static void main(String... strings) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(4);

        UserProvider userProvider = new UserProvider(es);
        CarProvider carProvider = new CarProvider();

        Observable<Car> carStream = carProvider.getIntervalCar(100)
                .doOnSubscribe(d -> System.out.println("Subscribe car stream"))
                .doFinally(() -> System.out.println("Unsubscribe car stream"))
                .subscribeOn(Schedulers.from(es))
                .observeOn(Schedulers.from(es));
                ;

        Observable<User> userStream = userProvider.getDelayedUser(10,100)
                .doOnSubscribe(d -> System.out.println("Subscribe user stream "))
                .doFinally(() -> System.out.println("Unsubscribe user stream"))
                .subscribeOn(Schedulers.from(es));


        Observable.amb(Arrays.asList(carStream, userStream))
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
