package com.example.peter.thekitchenmenu.ui.bindingadapters.unitofmeasure;

import android.content.res.Resources;
import android.os.Build;

import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MeasurementToStringFormatter {
    private Resources resources;
    private MeasurementModel model;
    private UnitOfMeasure unitOfMeasure;
    private String measurementErrorText = "There is a measurement error";
    boolean measurementError;

    public MeasurementToStringFormatter(Resources resources) {
        this.resources = resources;
    }

    public String formatMeasurement(MeasurementModel model) {
        this.model = model;
        return convertModelToString();
    }

    private String convertModelToString() {
        unitOfMeasure = model.getSubtype().getMeasurementClass();
        String measurement = "";

        boolean isBaseUnitsSet = isBaseUnitsSet();
        boolean isNumberOfItemSet = isNumberOfItemsSet();
        boolean isConversionFactorSet = isConversionFactorSet();

        if (isBaseUnitsSet && isNumberOfItemSet && isConversionFactorSet) {

            if (isMetricMeasurement()) {
                measurement = getMetricMeasurement();

            } else if (isImperialMeasurement()) {
                measurement = getImperialMeasurement();

            } else if (unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.COUNT) {
                measurement = getCountMeasurement();
            }

        } else {
            measurement = measurementErrorText;
        }
        return measurement;
    }

    private boolean isBaseUnitsSet() {
        if (isTotalBaseUnitsSupplied()) {
            return unitOfMeasure.isTotalBaseUnitsSet(model.getTotalBaseUnits());
        } else {
            return unitOfMeasure.isItemBaseUnitsSet(model.getItemBaseUnits());
        }
    }

    private boolean isTotalBaseUnitsSupplied() {
        return model.getTotalBaseUnits() > 0;
    }

    private boolean isNumberOfItemsSet() {
        if (isNumberOfItemsSetToDefault()) {
            return true;
        } else {
            return unitOfMeasure.isNumberOfItemsSet(model.getNumberOfItems());
        }
    }

    private boolean isNumberOfItemsSetToDefault() {
        return model.getNumberOfItems() == UnitOfMeasureConstants.MIN_NUMBER_OF_ITEMS;
    }

    private boolean isConversionFactorSet() {
        if (unitOfMeasure.isConversionFactorEnabled() && hasConversionFactor()) {
            return unitOfMeasure.isConversionFactorSet(model.getConversionFactor());
        } else {
            return true;
        }
    }

    private boolean hasConversionFactor() {
        return Double.compare(model.getConversionFactor(),
                UnitOfMeasureConstants.NO_CONVERSION_FACTOR) != 0;
    }

    private boolean isMetricMeasurement() {
        return unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.METRIC_MASS ||
                unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.METRIC_VOLUME;
    }

    private boolean isImperialMeasurement() {
        return unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.IMPERIAL_MASS ||
                unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.IMPERIAL_VOLUME ||
                unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.IMPERIAL_SPOON;
    }

    private String getMetricMeasurement() {
        StringBuilder formattedMeasurement = new StringBuilder();
        int numberOfItems = unitOfMeasure.getNumberOfItems();

        if (numberOfItems > 1) {
            formattedMeasurement.append(numberOfItems).append(" x ");

            formattedMeasurement.append(formattedMetricMeasurement(
                    unitOfMeasure.getItemUnitTwo(),
                    unitOfMeasure.getItemUnitOne()));
            formattedMeasurement.append(" (");
        }

        formattedMeasurement.append(formattedMetricMeasurement(
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getTotalUnitOne()));

        if (numberOfItems > 1) {
            formattedMeasurement.append(")");
        }

        if (formattedMeasurement.length() > 0) {
            return formattedMeasurement.toString();

        } else {
            return "";
        }
    }

    private StringBuilder formattedMetricMeasurement(int unitTwoValue, double unitOneValue) {
        String unitOneLabel = resources.getString(unitOfMeasure.getUnitOneLabelResourceId());
        String unitTwoLabel = resources.getString(unitOfMeasure.getUnitTwoLabelResourceId());

        StringBuilder unformattedMeasurement = new StringBuilder();
        StringBuilder formattedMeasurement = new StringBuilder();

        NumberFormat numberFormat = getNumberFormat();

        if (unitTwoValue > 0) {
            unformattedMeasurement.append(unitTwoValue);
        }
        if (unitTwoValue > 0 && unitOneValue > 0) {
            unformattedMeasurement.append(".");
        }
        if (unitOneValue > 0) {
            unformattedMeasurement.append(numberFormat.format(unitOneValue));
        }

        formattedMeasurement.append(numberFormat.format(
                Double.parseDouble(unformattedMeasurement.toString())));

        if (unitTwoValue > 0) {
            formattedMeasurement.append(unitTwoLabel);
            return formattedMeasurement;

        } else {
            formattedMeasurement.append(unitOneLabel);
            return formattedMeasurement;
        }
    }

    private String getImperialMeasurement() {
        NumberFormat numberFormat = getNumberFormat();
        StringBuilder formattedMeasurement = new StringBuilder();

        int numberOfItems = unitOfMeasure.getNumberOfItems();
        int totalUnitTwo = unitOfMeasure.getTotalUnitTwo();
        double totalUnitOne = unitOfMeasure.getTotalUnitOne();
        int itemUnitTwo = unitOfMeasure.getItemUnitTwo();
        double itemUnitOne = unitOfMeasure.getItemUnitOne();
        String unitOneLabel = resources.getString(unitOfMeasure.getUnitOneLabelResourceId());
        String unitTwoLabel = resources.getString(unitOfMeasure.getUnitTwoLabelResourceId());

        if (numberOfItems > 1) {
            formattedMeasurement.append(numberOfItems).append(" x ");

            if (itemUnitTwo > 0) {
                formattedMeasurement.append(itemUnitTwo).append(unitTwoLabel).append(" ");
            }

            if (itemUnitOne > 0) {
                formattedMeasurement.append(numberFormat.format(itemUnitOne));
                formattedMeasurement.append(unitOneLabel);
            }
            formattedMeasurement.append(" (");
        }
        if (totalUnitTwo > 0)
            formattedMeasurement.append(totalUnitTwo).append(unitTwoLabel);

        if (totalUnitTwo > 0 && totalUnitOne > 0)
            formattedMeasurement.append(" ");

        if (totalUnitOne > 0)
            formattedMeasurement.append(numberFormat.format(totalUnitOne)).append(unitOneLabel);

        if (numberOfItems > 1)
            formattedMeasurement.append(")");

        if (formattedMeasurement.toString().length() > 0) {
            return formattedMeasurement.toString();

        } else {
            return "";
        }
    }

    private String getCountMeasurement() {
        StringBuilder measurement = new StringBuilder();
        String baseUnits = String.valueOf(unitOfMeasure.getTotalBaseUnits());
        NumberFormat numberFormat = getNumberFormat();
        int parsedValue = 0;

        try {
            parsedValue = numberFormat.parse(baseUnits).intValue();

            if (parsedValue < 2)
                return "";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return measurement.append(parsedValue).append("s").toString();
    }

    @SuppressWarnings("deprecation")
    private NumberFormat getNumberFormat() {
        Locale locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = resources.getConfiguration().getLocales().get(0);

        } else {
            locale = resources.getConfiguration().locale;
        }

        NumberFormat format = NumberFormat.getNumberInstance(locale);

        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setGroupingUsed(false);
        }
        return format;
    }
}
