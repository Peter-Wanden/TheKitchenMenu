package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import java.util.HashMap;
import java.util.Map;

/**
 * @see {http://tutorials.jenkov.com/java/enums.html}
 */
public enum UnitOfMeasureUnits {

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

    OUNCES(3) {
        @Override
        public String getString() {
            return "each";
        }
    },

    POUNDS(4) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    POUNDS_AND_OUNCES(5) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    MILLILITRES(6) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    LITRES(7) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    FLUID_OUNCES(8) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    PINTS(9) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    GALLONS(10) {
        @Override
        public int getIntValue() {
            return super.getIntValue();
        }

        @Override
        public String getString() {
            return null;
        }
    },

    COUNT(11) {
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
    private static Map<Integer, UnitOfMeasureUnits> unitOfMeasureMap = new HashMap<>();

    UnitOfMeasureUnits(int measureTypeRequested) {
        this.enumIntValue = measureTypeRequested;
    }

    static {
        for (UnitOfMeasureUnits unitOfMeasure : UnitOfMeasureUnits.values()) {
            unitOfMeasureMap.put(unitOfMeasure.enumIntValue, unitOfMeasure);
        }
    }

    public static UnitOfMeasureUnits valueOf(int unitOfMeasureType) {
        return unitOfMeasureMap.get(unitOfMeasureType);
    }

    public int getIntValue() {
        return enumIntValue;
    }

    public abstract String getString();
}
