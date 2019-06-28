package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class UnitOfMeasureListItemBindingAdapter {

    private static final String TAG = "tkm-UOMListItemAdapter";

    @BindingAdapter(value = {"setLabelForSubtypeInt", "formatBaseUnits", "formatNumberOfItems"})
    public static void setLabelForSubtypeInt(TextView view,
                                             int subtypeInt,
                                             double baseUnits,
                                             int numberOfItems) {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.values()[subtypeInt].getMeasurementClass();
        boolean baseUnitsAreSet = unitOfMeasure.baseUnitsAreSet(baseUnits);
        boolean numberOfItemsAreSet = unitOfMeasure.numberOfProductsIsSet(numberOfItems);
        Resources resources = view.getResources();
        String measurement = "";

        if (baseUnitsAreSet && numberOfItemsAreSet) {
            if (unitOfMeasure instanceof MetricMass || unitOfMeasure instanceof MetricVolume)
                measurement = getMetricMeasurement(unitOfMeasure, resources);

            if (unitOfMeasure instanceof ImperialMass || unitOfMeasure instanceof ImperialVolume)
                measurement = getImperialMeasurement(unitOfMeasure, resources);

            if (unitOfMeasure instanceof Count)
                measurement = getCountMeasurement(unitOfMeasure, resources);
        }
        setMeasurementToView(view, measurement);
    }

    // ToDO change all to use NumberFormat
    private static String getMetricMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        StringBuilder measurement = new StringBuilder();
        double baseUnits = unitOfMeasure.getBaseUnits();
        int numberOfItems = unitOfMeasure.getNumberOfProducts();
        NumberFormat numberFormat = getNumberFormat(resources);
        Log.d(TAG, "getMetricMeasurement: NoItems=" + numberOfItems + " baseUnits=" + baseUnits);

        if (numberOfItems > 1) {

            measurement.
                    append(numberOfItems).append("x");
            baseUnits = baseUnits / numberOfItems;
        }
        if (baseUnits > 999) {
            measurement.
                    append(numberFormat.format(baseUnits / 1000)).
                    append(resources.getString(unitOfMeasure.getUnitTwoLabelStringResourceId()));
        } else {
            measurement.
                    append(numberFormat.format(baseUnits)).
                    append(resources.getString(unitOfMeasure.getUnitOneLabelStringResourceId()));
        }
        return measurement.toString();
    }

    private static String getImperialMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        StringBuilder measurement = new StringBuilder();

        if (unitOfMeasure.getPackMeasurementTwo() > 0)
            measurement.
                    append(unitOfMeasure.getPackMeasurementTwo()).
                    append(resources.getString(unitOfMeasure.getUnitTwoLabelStringResourceId())).
                    append(" ");

        if (unitOfMeasure.getPackMeasurementOne() > 0)
            measurement.
                    append(unitOfMeasure.getPackMeasurementOne()).
                    append(resources.getString(unitOfMeasure.getUnitOneLabelStringResourceId()));

        return measurement.toString();
    }

    private static String getCountMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        StringBuilder measurement = new StringBuilder();
        String baseUnits = String.valueOf(unitOfMeasure.getBaseUnits());
        NumberFormat numberFormat = getNumberFormat(resources);
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

    private static NumberFormat getNumberFormat(Resources resources) {
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

    private static void setMeasurementToView(TextView view, String measurement) {
        int viewId = view.getId();

        if (viewId == R.id.product_list_item_measurement ||
                viewId == R.id.product_viewer_size) {
            view.setText(measurement);
        }
    }
}