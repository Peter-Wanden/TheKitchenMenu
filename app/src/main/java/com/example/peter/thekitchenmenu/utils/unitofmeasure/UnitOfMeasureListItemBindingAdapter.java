package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class UnitOfMeasureListItemBindingAdapter {

    private static final String TAG = "tkm-UOMListItemAdapter";

    // Todo send in a measurement model- also use one in measurement viewmodel instead of bindable viewmodel
    @BindingAdapter(value = {"setLabelForSubtypeInt", "formatBaseUnits", "formatNumberOfItems"})
    public static void setLabelForSubtypeInt(TextView view,
                                             int subtypeInt,
                                             double baseUnits,
                                             int numberOfItems) {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.valueOf(subtypeInt).getMeasurementClass();
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

    private static String getMetricMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        StringBuilder formattedMeasurement = new StringBuilder();
        int numberOfItems = unitOfMeasure.getNumberOfProducts();

        if (numberOfItems > 1) {
            formattedMeasurement.append(numberOfItems).append(" x ");

            formattedMeasurement.append(formattedMetricMeasurement(
                    resources,
                    unitOfMeasure,
                    unitOfMeasure.getProductMeasurementTwo(),
                    unitOfMeasure.getProductMeasurementOne()));
            formattedMeasurement.append(" (");
        }

        formattedMeasurement.append(formattedMetricMeasurement(
                resources,
                unitOfMeasure,
                unitOfMeasure.getPackMeasurementTwo(),
                unitOfMeasure.getPackMeasurementOne()));

        if (numberOfItems > 1) {
            formattedMeasurement.append(")");
        }

        if (formattedMeasurement.length() > 0) {
            return formattedMeasurement.toString();
        } else {
            return "";
        }
    }

    private static StringBuilder formattedMetricMeasurement(Resources resources,
                                                            UnitOfMeasure unitOfMeasure,
                                                            int unitTwoValue,
                                                            double unitOneValue) {
        String unitOneLabel = resources.getString(unitOfMeasure.getUnitOneLabelStringResourceId());
        String unitTwoLabel = resources.getString(unitOfMeasure.getUnitTwoLabelStringResourceId());
        StringBuilder unformattedMeasurement = new StringBuilder();
        StringBuilder formattedMeasurement = new StringBuilder();
        NumberFormat numberFormat = getNumberFormat(resources);
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

    private static String getImperialMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        NumberFormat numberFormat = getNumberFormat(resources);
        StringBuilder formattedMeasurement = new StringBuilder();
        int numberOfProducts = unitOfMeasure.getNumberOfProducts();
        int packUnitTwoValue = unitOfMeasure.getPackMeasurementTwo();
        double packUnitOneValue = unitOfMeasure.getPackMeasurementOne();
        int productUnitTwo = unitOfMeasure.getProductMeasurementTwo();
        double productUnitOne = unitOfMeasure.getProductMeasurementOne();
        String unitOneLabel = resources.getString(unitOfMeasure.getUnitOneLabelStringResourceId());
        String unitTwoLabel = resources.getString(unitOfMeasure.getUnitTwoLabelStringResourceId());

        if (numberOfProducts > 1) {
            formattedMeasurement.append(numberOfProducts).append(" x ");
            if (productUnitTwo > 0)
                formattedMeasurement.append(productUnitTwo).append(unitTwoLabel).append(" ");
            if (productUnitOne > 0) {
                formattedMeasurement.append(numberFormat.format(productUnitOne));
                formattedMeasurement.append(unitOneLabel);
            }
            formattedMeasurement.append(" (");
        }
        if (packUnitTwoValue > 0)
            formattedMeasurement.append(packUnitTwoValue).append(unitTwoLabel);
        if (packUnitTwoValue > 0 && packUnitOneValue > 0)
            formattedMeasurement.append(" ");
        if (packUnitOneValue > 0)
            formattedMeasurement.append(numberFormat.format(packUnitOneValue)).append(unitOneLabel);
        if (numberOfProducts > 1)
            formattedMeasurement.append(")");
        if (formattedMeasurement.toString().length() > 0)
            return formattedMeasurement.toString();
        else
            return "";
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