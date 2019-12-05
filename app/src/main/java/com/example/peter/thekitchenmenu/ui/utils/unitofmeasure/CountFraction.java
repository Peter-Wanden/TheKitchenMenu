package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.R;

import java.util.HashMap;
import java.util.Map;

public enum CountFraction {
    DEFAULT {
        @Override
        public int toInt() {
            return 0;
        }

        @Override
        public double getDecimalValue() {
            return 0;
        }

        @Override
        public int getStringResourceId() {
            return R.string.count_fraction_zero_value;
        }
    },
    ONE_TENTH {
        @Override
        public int toInt() {
            return 1;
        }

        @Override
        public double getDecimalValue() {
            return .1;
        }

        @Override
        public int getStringResourceId() {
            return R.string.one_tenth;
        }
    },
    THREE_TENTHS {
        @Override
        public int toInt() {
            return 4;
        }

        @Override
        public double getDecimalValue() {
            return .3;
        }

        @Override
        public int getStringResourceId() {
            return R.string.three_tenths;
        }
    },
    SEVEN_TENTHS {
        @Override
        public int toInt() {
            return 8;
        }

        @Override
        public double getDecimalValue() {
            return .7;
        }

        @Override
        public int getStringResourceId() {
            return R.string.seven_tenths;
        }
    },
    NINE_TENTHS {
        @Override
        public int toInt() {
            return 11;
        }

        @Override
        public double getDecimalValue() {
            return .9;
        }

        @Override
        public int getStringResourceId() {
            return R.string.nine_tenths;
        }
    },
    ONE_FIFTH {
        @Override
        public int toInt() {
            return 2;
        }

        @Override
        public double getDecimalValue() {
            return .2;
        }

        @Override
        public int getStringResourceId() {
            return R.string.one_fifth;
        }
    },
    TWO_FIFTHS {
        @Override
        public int toInt() {
            return 5;
        }

        @Override
        public double getDecimalValue() {
            return .4;
        }

        @Override
        public int getStringResourceId() {
            return R.string.two_fifths;
        }
    },
    THREE_FIFTHS {
        @Override
        public int toInt() {
            return 7;
        }

        @Override
        public double getDecimalValue() {
            return .6;
        }

        @Override
        public int getStringResourceId() {
            return R.string.three_fifths;
        }
    },
    FOUR_FIFTHS {
        @Override
        public int toInt() {
            return 10;
        }

        @Override
        public double getDecimalValue() {
            return .8;
        }

        @Override
        public int getStringResourceId() {
            return R.string.four_fifths;
        }
    },
    ONE_QUARTER {
        @Override
        public int toInt() {
            return 3;
        }

        @Override
        public double getDecimalValue() {
            return .25;
        }

        @Override
        public int getStringResourceId() {
            return R.string.one_quarter;
        }
    },
    THREE_QUARTERS {
        @Override
        public int toInt() {
            return 9;
        }

        @Override
        public double getDecimalValue() {
            return .75;
        }

        @Override
        public int getStringResourceId() {
            return R.string.three_quarters;
        }
    },
    HALF {
        @Override
        public int toInt() {
            return 6;
        }

        @Override
        public double getDecimalValue() {
            return .5;
        }

        @Override
        public int getStringResourceId() {
            return R.string.half;
        }
    };

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, CountFraction> map = new HashMap<>();

    static {
        for (CountFraction fraction : CountFraction.values())
            map.put(fraction.toInt(), fraction);
    }

    public static CountFraction fromInt(int fractionAsInt) {
        return map.get(fractionAsInt);
    }

    public abstract int toInt();

    public abstract double getDecimalValue();

    public abstract int getStringResourceId();

    public static CountFraction findClosest(double valueToCompare) {
        CountFraction closestFraction = CountFraction.DEFAULT;
        double smallestDiff = Double.MAX_VALUE;

        for (CountFraction thisFraction : CountFraction.values()) {
            double thisDiff = Math.abs(thisFraction.getDecimalValue() - valueToCompare);

            if (thisDiff < smallestDiff) {
                smallestDiff = thisDiff;
                closestFraction = thisFraction;
            }
        }
        return closestFraction;
    }
}
