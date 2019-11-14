package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class Count extends UnitOfMeasureAbstract {

    Count() {
        measurementType = COUNT_TYPE;
        subtype = COUNT_SUBTYPE;
        numberOfUnits = COUNT_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = COUNT_MAX_MEASUREMENT;
        minMeasurement = COUNT_MIN_MEASUREMENT;
        unitTwo = COUNT_UNIT_TWO;
        unitOne = COUNT_UNIT_ONE;
        unitOneDecimal = COUNT_UNIT_ONE_DECIMAL;
        smallestUnit = COUNT_SMALLEST_UNIT;
        isConversionFactorEnabled = COUNT_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.count;
        subtypeStringResourceId = R.string.count;
        unitOneLabelStringResourceId = R.string.empty_string; // Unit one is not used for COUNT
        unitTwoLabelStringResourceId = R.string.each;
    }

    @Override
    public double getTotalUnitOne() {
        return 0;
    }

    @Override
    public boolean isTotalUnitOneSet(double totalUnitOne) {
        return false;
    }

    @Override
    public double getItemUnitOne() {
        return 0;
    }

    @Override
    public boolean isItemUnitOneSet(double itemUnitOne) {
        return false;
    }

    @Override
    public Pair[] getMaxUnitDigitWidths() {
        // Calculates the max digit width of unit two
        int maximumUnitTwoValue = (int) (maxMeasurement / unitTwo);
        int unitTwoDigits = 0;
        while (maximumUnitTwoValue > 0) {
            unitTwoDigits++;
            maximumUnitTwoValue = maximumUnitTwoValue / 10;
        }
        Pair<Integer, Integer> unitTwoDigitsWidth = new Pair<>(unitTwoDigits, 0);
        // unit one not used
        Pair<Integer, Integer> unitOneDigitsWidth = new Pair<>(0, 0);
        // Sets the digit widths for the conversion factor
        Pair<Integer, Integer> conversionFactorDigitWidths = new Pair<>(0, 0);

        Pair[] measurementUnitDigitWidths = new Pair[3];
        measurementUnitDigitWidths[0] = unitOneDigitsWidth;
        measurementUnitDigitWidths[1] = unitTwoDigitsWidth;
        measurementUnitDigitWidths[2] = conversionFactorDigitWidths;

        return measurementUnitDigitWidths;
    }
}
