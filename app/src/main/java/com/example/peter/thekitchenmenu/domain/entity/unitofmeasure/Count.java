package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.R;

/**
 * The count class uses the variable totalUnitTwo to receive the number of items for a count
 * measurement.
 * The variable NoOfItems takes
 * Item unit one is the integer and ItemUnit one the decimal part of a count being apportioned,
 *
 */
public class Count extends UnitOfMeasureAbstract {

    public Count() {
        measurementType = UnitOfMeasureConstants.COUNT_TYPE;
        subtype = UnitOfMeasureConstants.COUNT_SUBTYPE;
        numberOfUnits = UnitOfMeasureConstants.COUNT_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = UnitOfMeasureConstants.COUNT_MAX_MEASUREMENT;
        minMeasurement = UnitOfMeasureConstants.COUNT_MIN_MEASUREMENT;
        unitTwo = UnitOfMeasureConstants.COUNT_UNIT_TWO;
        unitOne = UnitOfMeasureConstants.COUNT_UNIT_ONE;
        unitOneDecimal = UnitOfMeasureConstants.COUNT_UNIT_ONE_DECIMAL;
        smallestUnit = UnitOfMeasureConstants.COUNT_SMALLEST_UNIT;
        isConversionFactorEnabled = UnitOfMeasureConstants.COUNT_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.count;
        subtypeStringResourceId = R.string.count;
        unitOneLabelStringResourceId = R.string.empty_string; // Unit one is not used for COUNT
        unitTwoLabelStringResourceId = R.string.each;
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
