package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Count extends UnitOfMeasureAbstract {

    public Count() {
        measurementType = UnitOfMeasureConstants.COUNT_TYPE;
        subtype = UnitOfMeasureConstants.COUNT_SUBTYPE;
        numberOfUnits = UnitOfMeasureConstants.COUNT_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = UnitOfMeasureConstants.COUNT_MAX_MEASUREMENT;
        minMeasurement = UnitOfMeasureConstants.COUNT_MIN_MEASUREMENT;
        unitTwo = UnitOfMeasureConstants.COUNT_UNIT_TWO; // whole (e.g. 1 apple)
        unitOne = UnitOfMeasureConstants.COUNT_UNIT_ONE; // parts of whole (e.g. half an apple)
        unitOneDecimal = UnitOfMeasureConstants.COUNT_UNIT_ONE_DECIMAL;
        smallestUnit = UnitOfMeasureConstants.COUNT_SMALLEST_UNIT;
        isConversionFactorEnabled = UnitOfMeasureConstants.COUNT_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.count;
        subtypeStringResourceId = R.string.count;
        unitOneLabelStringResourceId = R.string.part;
        unitTwoLabelStringResourceId = R.string.whole;
    }

    @Override
    protected double roundDecimal(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        if (decimalFormat instanceof DecimalFormat) {
            ((DecimalFormat) decimalFormat).applyPattern("#.####");
        }

        return Double.parseDouble(decimalFormat.format(valueToRound));
    }

    @Override
    public Pair[] getMaxUnitDigitWidths() {
        // Calculates the max digit width of unit two (whole units)
        int maximumUnitTwoValue = (int) (maxMeasurement / unitTwo);
        int unitTwoDigits = 0;
        while (maximumUnitTwoValue > 0) {
            unitTwoDigits++;
            maximumUnitTwoValue = maximumUnitTwoValue / 10;
        }

        Pair<Integer, Integer> unitTwoDigitsWidth = new Pair<>(unitTwoDigits, 0);
        // unit one uses one digit after decimal (for 10ths of an item)
        Pair<Integer, Integer> unitOneDigitsWidth = new Pair<>(1, 1);
        // No conversion factor used
        Pair<Integer, Integer> conversionFactorDigitWidths = new Pair<>(0, 0);

        Pair[] measurementUnitDigitWidths = new Pair[3];
        measurementUnitDigitWidths[0] = unitOneDigitsWidth;
        measurementUnitDigitWidths[1] = unitTwoDigitsWidth;
        measurementUnitDigitWidths[2] = conversionFactorDigitWidths;

        return measurementUnitDigitWidths;
    }
}
