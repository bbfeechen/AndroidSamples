package com.androidheroes.tutsplusdagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 2/2/16
 */
@Singleton
@Component(modules = {VehicleModule.class})
public interface VehicleComponent {

    Vehicle provideVehicle();

}
