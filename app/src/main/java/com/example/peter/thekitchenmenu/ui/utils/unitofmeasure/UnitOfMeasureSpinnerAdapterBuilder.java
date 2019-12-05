package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import android.content.res.Resources;
import android.widget.SpinnerAdapter;

import androidx.fragment.app.FragmentActivity;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.ui.utils.SpinnerItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitOfMeasureSpinnerAdapterBuilder {

    private FragmentActivity activity;
    private Resources resources;
    private ArrayList<UnitOfMeasureSpinnerItemModel> items = new ArrayList<>();
    private List<String> unitOfMeasureHeaders;


    public static UnitOfMeasureSpinnerAdapterBuilder setActivity(FragmentActivity activity) {
        return new UnitOfMeasureSpinnerAdapterBuilder().
                addActivity(activity).
                setResources(activity.getResources()).
                setSubTypeStringArray(activity.getResources());
    }

    private UnitOfMeasureSpinnerAdapterBuilder addActivity(FragmentActivity activity) {
        this.activity = activity;
        return this;
    }

    private UnitOfMeasureSpinnerAdapterBuilder setResources(Resources resources) {
        this.resources = resources;
        return this;
    }

    private UnitOfMeasureSpinnerAdapterBuilder setSubTypeStringArray(Resources resources) {
        unitOfMeasureHeaders = Arrays.asList(resources.getStringArray(
                R.array.unit_of_measure_subtypes_as_string_array));
        return this;
    }

    public UnitOfMeasureSpinnerAdapterBuilder addMetricMass() {
        String massMetricUnits = removeArrayBraces(Arrays.toString(resources.
                getStringArray(R.array.unit_of_measure_options_mass_metric)));

        UnitOfMeasureSpinnerItemModel headerItem = new UnitOfMeasureSpinnerItemModel(
                SpinnerItemType.LIST_ITEM,
                unitOfMeasureHeaders.get(MeasurementSubtype.METRIC_MASS.asInt()).
                        concat(" (").
                        concat(massMetricUnits).
                        concat(")"));

        items.add(headerItem);
        return this;
    }

    public UnitOfMeasureSpinnerAdapterBuilder addImperialMass() {
        String massImperialUnits = removeArrayBraces(Arrays.toString(resources.
                getStringArray(R.array.unit_of_measure_options_mass_imperial)));

        UnitOfMeasureSpinnerItemModel headerItem = new UnitOfMeasureSpinnerItemModel(
                SpinnerItemType.LIST_ITEM,
                unitOfMeasureHeaders.get(MeasurementSubtype.IMPERIAL_MASS.asInt()).
                        concat(" (").
                        concat(massImperialUnits).
                        concat(")"));

        items.add(headerItem);
        return this;
    }

    public UnitOfMeasureSpinnerAdapterBuilder addMetricVolume() {
        String volumeMetricUnits = removeArrayBraces(Arrays.toString(resources.
                getStringArray(R.array.unit_of_measure_options_volume_metric)));

        UnitOfMeasureSpinnerItemModel headerItem = new UnitOfMeasureSpinnerItemModel(
                SpinnerItemType.LIST_ITEM,
                unitOfMeasureHeaders.get(MeasurementSubtype.METRIC_VOLUME.asInt()).
                        concat(" (").
                        concat(volumeMetricUnits).
                        concat(")"));

        items.add(headerItem);
        return this;
    }

    public UnitOfMeasureSpinnerAdapterBuilder addImperialVolume() {
        String volumeImperialUnits = removeArrayBraces(Arrays.toString(resources.
                getStringArray(R.array.unit_of_measure_options_volume_imperial)));

        UnitOfMeasureSpinnerItemModel headerItem = new UnitOfMeasureSpinnerItemModel(
                SpinnerItemType.LIST_ITEM,
                unitOfMeasureHeaders.get(MeasurementSubtype.IMPERIAL_VOLUME.asInt()).
                        concat(" (").
                        concat(volumeImperialUnits).
                        concat(")"));

        items.add(headerItem);
        return this;
    }

    public UnitOfMeasureSpinnerAdapterBuilder addCount() {
        String countUnits = removeArrayBraces(Arrays.toString(resources.
                getStringArray(R.array.unit_of_measure_options_count)));

        UnitOfMeasureSpinnerItemModel headerItem = new UnitOfMeasureSpinnerItemModel(
                SpinnerItemType.LIST_ITEM,
                unitOfMeasureHeaders.get(MeasurementSubtype.COUNT.asInt()).
                        concat(" (").
                        concat(countUnits).
                        concat(")"));

        items.add(headerItem);
        return this;
    }

    public UnitOfMeasureSpinnerAdapterBuilder addImperialSpoon() {
        String spoonUnits = removeArrayBraces(Arrays.toString(resources.
                getStringArray(R.array.unit_of_measure_options_volume_teaspoon)));

        UnitOfMeasureSpinnerItemModel headerItem = new UnitOfMeasureSpinnerItemModel(
                SpinnerItemType.LIST_ITEM,
                unitOfMeasureHeaders.get(MeasurementSubtype.IMPERIAL_SPOON.asInt()).
                        concat(" (").
                        concat(spoonUnits).
                        concat(")"));

        items.add(headerItem);
        return this;
    }

    private String removeArrayBraces(String stringWithBrackets) {
        return stringWithBrackets.substring(1, stringWithBrackets.length() - 1);
    }

    public SpinnerAdapter build() {
        return new UnitOfMeasureSpinnerAdapter(activity, items);
    }
}
