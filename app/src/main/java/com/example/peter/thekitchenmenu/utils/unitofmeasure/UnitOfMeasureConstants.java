package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public class UnitOfMeasureConstants {

    // All products sold by weight/mass are converted to grams
    static final double MINIMUM_MASS = 1.; // 1 gram
    static final double MAXIMUM_MASS = 10000.; // 10kg

    // All products sold by volume are converted to millilitres
    static final double MINIMUM_VOLUME = 1.; // 1ml
    static final double MAXIMUM_VOLUME = 10000.; // 10 litres

    // Min and max for products sold by count/each
    static final int MINIMUM_COUNT = 1;
    static final int MAXIMUM_COUNT = 99;

    // Min and max values for products sold in a multi-pack
    static final int MINIMUM_NUMBER_OF_PRODUCTS = 1;
    static final int MAXIMUM_NUMBER_OF_PRODUCTS = 20;

    // Represents measurement that has not yet had a value assigned
    static final int NOT_YET_SET = 0;
}
