package com.pgssoft.rxjava;

/**
 * Created by dawidpodolak on 16.08.2017.
 */
public class Car {

    private int id;
    private int userId;
    private String manufacturerName;
    private String model;
    private int powerHP;

    public Car(int id, int userId, String manufacturerName, String model, int powerHP) {
        this.id = id;
        this.userId = userId;
        this.manufacturerName = manufacturerName;
        this.model = model;
        this.powerHP = powerHP;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getModel() {
        return model;
    }

    public int getPowerHP() {
        return powerHP;
    }

    @Override
    public String toString() {
        return "Car{" +
                "manufacturerName='" + manufacturerName + '\'' +
                ", model='" + model + '\'' +
                ", powerHP=" + powerHP +
                '}';
    }
}
