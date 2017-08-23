package com.pgssoft.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by dawidpodolak on 09.08.2017.
 */
public class UserProvider {

    PublishSubject<User> userPublishSubject = PublishSubject.create();

    private List<User> userList = new ArrayList<>();

    public UserProvider(ExecutorService es) {
        inflateUsers();
        es.execute(() -> {
            while (!es.isShutdown()) {
                Random r = new Random();
                int position = r.nextInt(userList.size());
                userPublishSubject.onNext(userList.get(position));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        });

    }

    private void inflateUsers() {
        userList.add(new User(0, "Jan", "Kowalski", 32));
        userList.add(new User(1, "Joanna", "Nowak", 31));
        userList.add(new User(2, "Wojciech", "Kowalczyk", 54));
        userList.add(new User(3, "Elżbieta", "Woźniak", 13));
        userList.add(new User(4, "Aleksander", "Dąbrowski", 45));
        userList.add(new User(5, "Jacek", "Mazur", 63));
        userList.add(new User(6, "Andrzej", "Grabowski", 11));
        userList.add(new User(7, "Piotr", "Adamczyk", 23));
        userList.add(new User(8, "Konrad", "Kamiński", 41));
        userList.add(new User(9, "Robert", "Lewandowski", 18));
    }

    public Observable<User> getUsers() {
        return Observable.fromIterable(userList);
    }

    public Observable<User> getDelayedUser(int delay) {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                for (User u :
                        userList) {
                    e.onNext(u);
                    Thread.sleep(delay);
                }

                e.onComplete();
            }
        });
    }


    public Observable<User> getRandomUser() {
        return userPublishSubject;
    }

}


