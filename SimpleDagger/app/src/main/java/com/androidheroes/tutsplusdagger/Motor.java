package com.androidheroes.tutsplusdagger;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 2/2/16
 */
public class Motor {

    private int rpm;

    public Motor(){
        this.rpm = 0;
    }

    public int getRpm(){
        return rpm;
    }

    public void accelerate(int value){
        rpm = rpm + value;
    }

    public void brake(){
        rpm = 0;
    }
}
