package com.example.peter.thekitchenmenu.ui.utils;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum CountFraction {
    ONE_TENTH {
        @Override
        public int asInt() {
            return 0;
        }

        @Override
        public double getValue() {
            return .1;
        }
    },
    ONE_FIFTH {
        @Override
        public int asInt() {
            return 1;
        }

        @Override
        public double getValue() {
            return .2;
        }
    },
    THREE_TENTHS {
        @Override
        public int asInt() {
            return 2;
        }

        @Override
        public double getValue() {
            return .3;
        }
    },
    TWO_FIFTHS {
        @Override
        public int asInt() {
            return 3;
        }

        @Override
        public double getValue() {
            return .4;
        }
    },
    HALF {
        @Override
        public int asInt() {
            return 4;
        }

        @Override
        public double getValue() {
            return .5;
        }
    },
    THREE_FIFTHS {
        @Override
        public int asInt() {
            return 5;
        }

        @Override
        public double getValue() {
            return .6;
        }
    },
    SEVEN_TENTHS {
        @Override
        public int asInt() {
            return 6;
        }

        @Override
        public double getValue() {
            return .7;
        }
    },
    FOUR_FIFTHS {
        @Override
        public int asInt() {
            return 7;
        }

        @Override
        public double getValue() {
            return .8;
        }
    },
    NINE_TENTHS {
        @Override
        public int asInt() {
            return 8;
        }

        @Override
        public double getValue() {
            return .9;
        }
    };

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, CountFraction> map = new HashMap<>();

    static {
        for (CountFraction fraction : CountFraction.values())
            map.put(fraction.asInt(), fraction);
    }

    public static CountFraction fromInt(int fractionAsInt) {
        return map.get(fractionAsInt);
    }

    public abstract int asInt();

    public abstract double getValue();
}
