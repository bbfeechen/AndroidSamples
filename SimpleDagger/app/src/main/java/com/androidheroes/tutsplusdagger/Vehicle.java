package com.androidheroes.tutsplusdagger;

import javax.inject.Inject;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 2/2/16
 */
public class Vehicle {

    private Motor motor;

    @Inject
    public Vehicle(Motor motor){
        this.motor = motor;
    }

    public void increaseSpeed(int value){
        motor.accelerate(value);
    }

    public void stop(){
        motor.brake();
    }

    public int getSpeed(){
        return motor.getRpm();
    }
}
