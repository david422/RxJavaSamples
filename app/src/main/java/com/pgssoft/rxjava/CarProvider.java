package com.pgssoft.rxjava;

import io.reactivex.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Created by dawidpodolak on 16.08.2017.
 */
public class CarProvider {

    private static final List<Car> carList = new ArrayList<>();
    static {
        carList.add(new Car(0, 1, "Ford", "Focus", 90));
        carList.add(new Car(1, 2, "Ford", "Mondeo", 190));
        carList.add(new Car(2, 5, "Opel", "Astra", 85));
        carList.add(new Car(3, 0, "Audi", "A4", 300));
        carList.add(new Car(4, 8, "BMW", "e60", 150));
        carList.add(new Car(5, 7, "Volkswagen", "Golf", 140));
        carList.add(new Car(6, 6, "Volkswagen", "Passat", 160));
        carList.add(new Car(7, 4, "Skoda", "Fabia", 90));
        carList.add(new Car(9, 3, "Skoda", "Octavia", 130));
        carList.add(new Car(10, 1, "Mazda", "6", 140));
        carList.add(new Car(11, 7, "Citroen", "C6", 170));
        carList.add(new Car(12, 5, "Skoda", "Superb", 130));
    }

    public Single<Car> findFisrtCar(int userID){
        return Single.create(new SingleOnSubscribe<Car>() {
            @Override
            public void subscribe(SingleEmitter<Car> e) throws Exception {
                for (Car car: carList) {
                    if (car.getUserId() == userID) {
                        e.onSuccess(car);
                        return;
                    }
                }

                e.onError(new NoSuchElementException());
            }
        });
    }

    public Observable<Car> getIntervalCar(int interval){
         return Observable.fromIterable(carList)
                 .zipWith(Observable.interval(0, interval, TimeUnit.MILLISECONDS), (car, along) -> car);
    }

    public Observable<Car> findAllCars(int userID){
        return Observable.create(e -> {
            for (Car car: carList) {
                if (car.getUserId() == userID) {
                    e.onNext(car);
                }
            }
            e.onComplete();
        });
    }
}
