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

        ExecutorService es = Executors.newFixedThreadPool(2);

        UserProvider userProvider = new UserProvider(es);
        CarProvider carProvider = new CarProvider();

        Observable<Car> carStream = carProvider.getIntervalCar(100)
                .doOnSubscribe(d -> System.out.println("Subscribe car stream"))
                .doFinally(() -> System.out.println("Unsubscribe car stream"))
                .subscribeOn(Schedulers.from(es));

        Observable<User> userStream = userProvider.getDelayedUser(50,500)
                .doOnSubscribe(d -> System.out.println("Subscribe user stream "))
                .doFinally(() -> System.out.println("Unsubscribe user stream"))
                .subscribeOn(Schedulers.from(es));



        Observable.combineLatest(carStream, userStream, new BiFunction<Car, User, String>() {
            @Override
            public String apply(Car car, User user) throws Exception {
                return String.format("User %s gets car %s" , user.getFirstName() + " " + user.getLastName(),  car.getManufacturerName() + " " + car.getModel());
            }
        })
                .observeOn(Schedulers.from(es), true)
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
