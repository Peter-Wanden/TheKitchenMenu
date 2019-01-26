package com.example.peter.thekitchenmenu.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit of measure values:
 * Grams
 * Kilograms
 * Millilitre
 * Centilitre
 * Litre
 * Ounces
 * Pounds
 * each
 * count
 * @see {http://tutorials.jenkov.com/java/enums.html}
 */
public enum UnitOfMeasureValues {

    GRAMS(1) {
        @Override
        public String getString() {
            return "g";
        }
    },

    MILLILITRES(2) {
        @Override
        public String getString() {
            return "ml";
        }
    },

    COUNT(3) {
        @Override
        public String getString() {
            return "each";
        }
    };

    private int enumIntValue;
    private static Map<Integer, UnitOfMeasureValues> unitOfMeasureMap = new HashMap<>();

    UnitOfMeasureValues(int measureTypeRequested) {
        this.enumIntValue = measureTypeRequested;
    }

    static {
        for (UnitOfMeasureValues unitOfMeasure : UnitOfMeasureValues.values()) {
            unitOfMeasureMap.put(unitOfMeasure.enumIntValue, unitOfMeasure);
        }
    }

    public static UnitOfMeasureValues valueOf(int unitOfMeasureType) {
        return unitOfMeasureMap.get(unitOfMeasureType);
    }

    public int getIntValue() {
        return enumIntValue;
    }

    public abstract String getString();
}
