package com.androidheroes.tutsplusdagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 2/2/16
 */
@Module
public class VehicleModule {

    @Provides
    @Singleton
    Motor provideMotor(){
        return new Motor();
    }

    @Provides
    @Singleton
    Vehicle provideVehicle(){
        return new Vehicle(new Motor());
    }
}
