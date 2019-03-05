package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import java.util.HashMap;
import java.util.Map;

/**
 * @link {http://tutorials.jenkov.com/java/enums.html}
 */
public enum MeasurementUnits {

    GRAMS(1) {
        @Override
        public String getString() {
            return "g";
        }
        public int getMeasureType() {
            return 1;
        }
    },

    KILOGRAMS(2) {
        @Override
        public String getString() {
            return "ml";
        }
    },

    OUNCES(1) {
        @Override
        public String getString() {
            return "oz";
        }
    },

    POUNDS(2) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    MILLILITRES(1) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    LITRES(2) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    FLUID_OUNCES(1) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    PINTS(2) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    COUNT(1) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    };

    private int enumIntValue;

    public int getIntValue() {
        return enumIntValue;
    }

    MeasurementUnits(int measureTypeRequested) {
        this.enumIntValue = measureTypeRequested;
    }

    private static Map<Integer, MeasurementUnits> unitOfMeasureMap = new HashMap<>();

    static {
        for (MeasurementUnits unitOfMeasure : MeasurementUnits.values()) {
            unitOfMeasureMap.put(unitOfMeasure.enumIntValue, unitOfMeasure);
        }
    }

    public static MeasurementUnits valueOf(int unitOfMeasureType) {
        return unitOfMeasureMap.get(unitOfMeasureType);
    }

    public abstract String getString();
}
