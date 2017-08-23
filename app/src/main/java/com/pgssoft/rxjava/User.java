package com.pgssoft.rxjava;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawidpodolak on 09.08.2017.
 */
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private int age;

    private List<Car> cars = new ArrayList();

    public User(int id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public void addCars(List<Car> cars){
        this.cars.addAll(cars);
    }

    public List<Car> getCars() {
        return cars;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
