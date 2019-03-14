package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.Product;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.GRAMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.KILOGRAMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NO_INPUT;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;

    private String typeAsString;
    private String subTypeAsString;
    private String unitGram;
    private String unitKilogram;
    private double minimumMeasurement = UNIT_GRAM;

    private int numberOfItemsInPack = 0;
    private double baseSiUnits;
    private int packMeasurementInKilograms = 0;
    private int packMeasurementInGrams = 0;
    private int itemMeasurementInKilograms = 0;
    private int itemMeasurementInGrams = 0;

    MetricMass(Context context) {

        Resources resources = context.getResources();
        typeAsString = resources.getString(R.string.mass);
        subTypeAsString = resources.getString(R.string.sub_type_metric_mass);
        unitGram = resources.getString(R.string.grams);
        unitKilogram = resources.getString(R.string.kilograms);
    }

    @Override
    public String getTypeAsString() {
        return typeAsString;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_MASS;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_METRIC_MASS;
    }

    @Override
    public String getMeasurementUnitOne() {
        return unitGram;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return unitKilogram;
    }


    @Override
    public Measurement getMinAndMax() {

        Measurement measurement = new Measurement();

        measurement.setTypeAsString(typeAsString);
        measurement.setNumberOfItemsInPack(numberOfItemsInPack);
        measurement.setMeasurementUnitOne(unitGram);
        measurement.setMeasurementUnitTwo(unitKilogram);

        if (numberOfItemsInPack >= MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

            double minimumMultiPackMeasurement = minimumMeasurement * numberOfItemsInPack;
            measurement.setMinimumMeasurementOne((int) minimumMultiPackMeasurement);
        } else measurement.setMinimumMeasurementOne((int) minimumMeasurement);

        measurement.setMinimumMeasurementTwo(0);
        measurement.setMaximumMeasurementOne(0);
        measurement.setMaximumMeasurementTwo(getMeasurementInKilograms(MAX_MASS));

        return measurement;
    }

    @Override
    public Measurement setNewMeasurementValuesTo(Measurement measurement) {

        measurement.setTypeAsString(getTypeAsString());
        measurement.setMeasurementSubType(getMeasurementSubType());
        measurement.setNumberOfItemsInPack(getNumberOfItemsInPack());
        measurement.setMeasurementUnitOne(unitGram);
        measurement.setMeasurementUnitTwo(unitKilogram);
        measurement.setPackMeasurementOne(getPackMeasurementOne());
        measurement.setPackMeasurementTwo(getPackMeasurementTwo());
        measurement.setItemMeasurementOne(getItemMeasurementOne());
        measurement.setItemMeasurementTwo(getItemMeasurementTwo());

        return measurement;
    }

    @Override
    public boolean setValuesFromProduct(Product product) {

        boolean baseSiUnitsAreSet = setBaseSiUnits(product.getBaseSiUnits());
        boolean numberOfItemsAreSet = setNumberOfItemsInPack(product.getNumberOfItems());
        Log.d(TAG, "setValuesFromProduct: number of items are set: " + numberOfItemsAreSet);
        Log.d(TAG, "setValuesFromProduct: base units are set: " + baseSiUnitsAreSet);

        return numberOfItemsAreSet && baseSiUnitsAreSet;
    }

    @Override
    public boolean setNumberOfItemsInPack(int numberOfItemsInPack) {

        Log.d(TAG, "setNumberOfItemsInPack: " + numberOfItemsInPack);

        boolean numberOfItemsInPackAreWithinBounds =
                numberOfItemsInPack >= MULTI_PACK_MINIMUM_NO_OF_ITEMS &&
                        numberOfItemsInPack <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS;

        if (numberOfItemsInPackAreWithinBounds) {

            boolean newUnitOfMeasureBeingInitialised = baseSiUnits == NO_INPUT;

            if (newUnitOfMeasureBeingInitialised) {

                this.numberOfItemsInPack = numberOfItemsInPack;

                return true;

            } else {

                boolean numberOfItemsInPackAreWithinMinimumMeasurementBounds =
                        setItemsInPack(numberOfItemsInPack);

                if (numberOfItemsInPackAreWithinMinimumMeasurementBounds) {

                    this.numberOfItemsInPack = numberOfItemsInPack;

                    return true;
                }
            }
        }
        return false;
    }

    private boolean setItemsInPack(int numberOfItemsInPack) {

        double itemSizeInBaseUnits = baseSiUnits / numberOfItemsInPack;

        if (itemSizeInBaseUnits < minimumMeasurement) return false;

        itemMeasurementInGrams = getMeasurementInGrams(itemSizeInBaseUnits);
        itemMeasurementInKilograms = getMeasurementInKilograms(itemSizeInBaseUnits);

        Log.d(TAG, "setItemsInPack: itemSize " +
                itemMeasurementInKilograms + "kg " +
                itemMeasurementInGrams + "grams");

        return true;
    }

    @Override
    public int getNumberOfItemsInPack() {
        return numberOfItemsInPack;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean setBaseSiUnits(double baseSiUnits) {

        Log.d(TAG, "setBaseSiUnits: measurement received: " + baseSiUnits);
        Measurement minAndMax = getMinAndMax();
        boolean multiPack = numberOfItemsInPack >= MULTI_PACK_MINIMUM_NO_OF_ITEMS;
        double maximumMeasurement = MAX_MASS;
//        double minimumMeasurement = minAndMax.getMinimumMeasurementOne();
        minimumMeasurement = NO_INPUT;

        Log.d(TAG, "setBaseSiUnits: " +
                "max is: " + maximumMeasurement + " min is: " + minimumMeasurement);

        boolean baseSiUnitsAreWithinBounds =
                baseSiUnits <= maximumMeasurement && baseSiUnits >= minimumMeasurement;

        Log.d(TAG, "setBaseSiUnits: within bounds: " + baseSiUnitsAreWithinBounds);

        if (multiPack) {

            if (baseSiUnitsAreWithinBounds) {

                Log.d(TAG, "setBaseSiUnits: setting base Si for multiPack to: " + baseSiUnits);
                this.baseSiUnits = baseSiUnits;
                setPackMeasurement();
                setItemsInPack(numberOfItemsInPack);

                return true;
            }
            return false;

        } else if (baseSiUnitsAreWithinBounds) {

            Log.d(TAG, "setBaseSiUnits: setting baseSi for single pack");
            this.baseSiUnits = baseSiUnits;
            setPackMeasurement();

            return true;

        } else {

            resetNumericValues();
        }
        return false;
    }

    private void setPackMeasurement() {

        packMeasurementInGrams = getMeasurementInGrams(baseSiUnits);
        packMeasurementInKilograms = getMeasurementInKilograms(baseSiUnits);

        Log.d(TAG, "setPackMeasurement: Two: " + packMeasurementInKilograms +
                " One: " + packMeasurementInGrams);
    }

    private int getMeasurementInKilograms(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_KILOGRAM);
    }

    private int getMeasurementInGrams(double baseSiUnits) {
        return (int) (baseSiUnits % UNIT_KILOGRAM);
    }

    @Override
    public int[] getInputFilterFormat() {

        int[] inputFilterFormat = new int[5];
        inputFilterFormat[GRAMS.getIntValue()] = 3;
        inputFilterFormat[KILOGRAMS.getIntValue()] = 3;

        return inputFilterFormat;
    }

    @Override
    public int getPackMeasurementOne() {
        Log.d(TAG, "getPackMeasurementOne: " + packMeasurementInGrams);
        return packMeasurementInGrams;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        Measurement measurement = new Measurement();
        measurement.setPackMeasurementOne(packMeasurementOne);
        measurement.setPackMeasurementTwo(packMeasurementInKilograms);

        Log.d(TAG, "setPackMeasurementOne: measurement is: " + packMeasurementOne);
        Log.d(TAG, "setPackMeasurementOne: measurement sent for processing is: " +
                measurement.getPackMeasurementTwo() + " kg, " + measurement.getPackMeasurementOne() + " grams");

        double baseSiUnits = convertToBaseSiUnits(measurement);
        Log.d(TAG, "setPackMeasurementOne: base Si to set is: " + baseSiUnits);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getPackMeasurementTwo() {
        Log.d(TAG, "getPackMeasurementTwo: " + packMeasurementInKilograms);
        return packMeasurementInKilograms;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        Measurement measurement = new Measurement();
        measurement.setPackMeasurementOne(packMeasurementInGrams);
        measurement.setPackMeasurementTwo(packMeasurementTwo);

        Log.d(TAG, "setPackMeasurementTwo: measurement is: " + packMeasurementTwo);
        Log.d(TAG, "setPackMeasurementTwo: measurement sent for processing is: " +
                measurement.getPackMeasurementTwo() + " kg, " +
                measurement.getPackMeasurementOne() + " grams");

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getItemMeasurementOne() {
        return this.itemMeasurementInGrams;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {

        Measurement itemToPackMeasurement = new Measurement();
        itemToPackMeasurement.setPackMeasurementOne(itemMeasurementOne * numberOfItemsInPack);
        itemToPackMeasurement.setPackMeasurementTwo(itemMeasurementInKilograms * numberOfItemsInPack);

        Log.d(TAG, "setItemMeasurementOne: measurement is: " + itemMeasurementOne);
        Log.d(TAG, "setItemMeasurementOne: measurement sent for processing is: " +
                itemToPackMeasurement.getItemMeasurementTwo() + " kg" +
                itemToPackMeasurement.getItemMeasurementOne() + " grams");

        return setBaseSiUnits(convertToBaseSiUnits(itemToPackMeasurement));
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementInKilograms;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {

        Measurement itemToPackMeasurement = new Measurement();
        itemToPackMeasurement.setPackMeasurementOne(itemMeasurementInGrams * numberOfItemsInPack);
        itemToPackMeasurement.setPackMeasurementTwo(itemMeasurementTwo * numberOfItemsInPack);

        Log.d(TAG, "setItemMeasurementTwo: measurement is: " + itemMeasurementTwo);
        Log.d(TAG, "setItemMeasurementTwo: measurement sent for processing is: " +
                itemToPackMeasurement.getPackMeasurementTwo() + " kg " +
                itemToPackMeasurement.getPackMeasurementOne() + " grams");

        return setBaseSiUnits(convertToBaseSiUnits(itemToPackMeasurement));
    }

    private double convertToBaseSiUnits(Measurement measurementToConvert) {

        double gramsToGrams = measurementToConvert.getPackMeasurementOne();
        double kilogramsToGrams = measurementToConvert.getPackMeasurementTwo() * UNIT_KILOGRAM;

        Log.d(TAG, "convertToBaseSiUnits: kg to grams: " + kilogramsToGrams +
                ", grams to grams: " + gramsToGrams + " Total: " + (kilogramsToGrams + gramsToGrams));

        return kilogramsToGrams + gramsToGrams;
    }

    @Override
    public void resetNumericValues() {

        baseSiUnits = 0;
        numberOfItemsInPack = 0;
        packMeasurementInKilograms = 0;
        packMeasurementInGrams = 0;
        itemMeasurementInKilograms = 0;
        itemMeasurementInGrams = 0;
    }

    @Override
    public String toString() {
        return "MetricMass{" +
                "\ntype='" + typeAsString + '\'' +
                "\n, subType='" + subTypeAsString + '\'' +
                "\n, unitKilogram='" + unitKilogram + '\'' +
                "\n, unitGram='" + unitGram + '\'' +
                "\n, numberOfItemsInPack=" + numberOfItemsInPack +
                "\n, baseSiUnits=" + baseSiUnits +
                "\n, packMeasurementInKilograms=" + packMeasurementInKilograms +
                "\n, packMeasurementInGrams=" + packMeasurementInGrams +
                "\n, itemMeasurementInKilograms=" + itemMeasurementInKilograms +
                "\n, itemMeasurementInGrams=" + itemMeasurementInGrams +
                '}';
    }
}