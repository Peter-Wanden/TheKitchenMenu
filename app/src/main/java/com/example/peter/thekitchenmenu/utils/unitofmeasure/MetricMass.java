package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.GRAMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.KILOGRAMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_PACKS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_PACKS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NO_INPUT;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    // Unit description strings
    private String typeAsString;
    private String subTypeAsString;
    private String unitGram;
    private String unitKilogram;

    // Unit values as they relate to the International System of Units, or SI
    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;

    // Min and max measurements
    private double minimumBaseSiMeasurement = 0; // TODO - set this to one when all is said and done
    private double maximumBaseSiMeasurement = MAX_MASS;
    private int minimumNumberOfPacks = MULTI_PACK_MINIMUM_NO_OF_PACKS;
    private int maximumNumberOfPacks = MULTI_PACK_MAXIMUM_NO_OF_PACKS;

    // Current measurements
    private int numberOfPacksInPack = 1;
    private double singlePackSizeInBaseUnits = minimumBaseSiMeasurement;
    private double baseSiUnits = 0.;
    private double packMeasurementInKilograms = 0;
    private double packMeasurementInGrams = 0;
    private double singlePackMeasurementInKilograms = 0;
    private double singlePackMeasurementInGrams = 0;

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
    public boolean setNumberOfPacksInPack(int numberOfPacksInPack) {

        Log.d(TAG, "setNumberOfPacks: " + numberOfPacksInPack);

        if (newUnitOfMeasureIsBeingInitialised() &&
                numberOfPacksInAPackAreWithinBounds(numberOfPacksInPack)) {

            Log.d(TAG, "setNumberOfPacks: New unit of measure is being initialised " +
                    "AND numberOfPacksInAPackAreWithinBounds - Setting number " +
                    "of packs in pack to: " + numberOfPacksInPack);

            this.numberOfPacksInPack = numberOfPacksInPack;

            return true;

        } else {

            if (singlePackSizeNotLessThanSmallestUnit(numberOfPacksInPack) &&
                    numberOfPacksInAPackAreWithinBounds(numberOfPacksInPack)) {

                setPacksInPack(numberOfPacksInPack);

                return true;
            }
        }
        return false;
    }

    private boolean newUnitOfMeasureIsBeingInitialised() {

        return baseSiUnits == NO_INPUT;
    }

    private boolean numberOfPacksInAPackAreWithinBounds(int numberOfPacksInPack) {

        boolean result = numberOfPacksInPack >= minimumNumberOfPacks &&
                numberOfPacksInPack <= maximumNumberOfPacks;

        Log.d(TAG, "numberOfPacksInAPackAreWithinBounds: " + result);

        return result;
    }

    private boolean singlePackSizeNotLessThanSmallestUnit(int numberOfPacks) {

        boolean result = numberOfPacks / singlePackSizeInBaseUnits >= minimumBaseSiMeasurement;
        Log.d(TAG, "singlePackSizeNotLessThanSmallestUnit: " + result);

        return result;
    }

    private void setPacksInPack(int numberOfPacksInPack) {

        this.numberOfPacksInPack = numberOfPacksInPack;
        singlePackSizeInBaseUnits = baseSiUnits / numberOfPacksInPack;
        singlePackMeasurementInGrams = getMeasurementInGrams(singlePackSizeInBaseUnits);
        singlePackMeasurementInKilograms = getMeasurementInKilograms(singlePackSizeInBaseUnits);

        // This needs updating every time the pack or single pack measurements change
        maximumNumberOfPacks = (int) singlePackSizeInBaseUnits / MAX_MASS;

        Log.d(TAG, "setNumberOfPacks: single packSize: " + singlePackSizeInBaseUnits + " " +
                singlePackMeasurementInKilograms + "kg " +
                singlePackMeasurementInGrams + "grams");
    }

    @Override
    public ObservableMeasurement getMinAndMax() {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setNumberOfPacksInPack(numberOfPacksInPack);

        if (numberOfPacksInPack >= MULTI_PACK_MINIMUM_NO_OF_PACKS)
            observableMeasurement.setMinimumMeasurementOne((int) minimumMultiPackMeasurement());
        else observableMeasurement.setMinimumMeasurementOne((int) minimumBaseSiMeasurement);

        observableMeasurement.setMinimumMeasurementTwo(0);
        observableMeasurement.setMaximumMeasurementOne(0);
        observableMeasurement.setMaximumMeasurementTwo(getMeasurementInKilograms(MAX_MASS));

        return observableMeasurement;
    }

    private double minimumMultiPackMeasurement() {
        return minimumBaseSiMeasurement * numberOfPacksInPack;
    }

    @Override
    public boolean getValuesFromObservableProductModel(ObservableProductModel productModel) {

        boolean baseSiUnitsAreSet = setBaseSiUnits(productModel.getBaseSiUnits());
        boolean numberOfPacksAreSet = setNumberOfPacksInPack(productModel.getNumberOfPacks());

        Log.d(TAG, "getValuesFromObservableProductModel: number of packs is set: " +
                numberOfPacksAreSet);
        Log.d(TAG, "getValuesFromObservableProductModel: base units are set: " +
                baseSiUnitsAreSet);

        return numberOfPacksAreSet && baseSiUnitsAreSet;
    }

    @Override
    public int getNumberOfPacksInPack() {
        return numberOfPacksInPack;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean setBaseSiUnits(double baseSiUnits) {

        Log.d(TAG, "setBaseSiUnits: measurement received: " + baseSiUnits);
        Log.d(TAG, "setBaseSiUnits: " +
                "max is: " + maximumBaseSiMeasurement + " min is: " + minimumBaseSiMeasurement);

        if (isMultiPack()) {

            if (baseSiUnitsAreWithinBounds(baseSiUnits)) {

                Log.d(TAG, "setBaseSiUnits: setting base Si for multiPack to: " + baseSiUnits);
                this.baseSiUnits = baseSiUnits;
                setPackMeasurement();
                setPacksInPack(numberOfPacksInPack);

                return true;
            }
            return false;

        } else if (baseSiUnitsAreWithinBounds(baseSiUnits)) {

            Log.d(TAG, "setBaseSiUnits: setting baseSi for single pack");
            this.baseSiUnits = baseSiUnits;
            setPackMeasurement();

            return true;

        } else {

            resetNumericValues();
        }
        return false;
    }

    private boolean baseSiUnitsAreWithinBounds(double baseSiUnits) {
        boolean result = baseSiUnits <= maximumBaseSiMeasurement &&
                baseSiUnits >= minimumBaseSiMeasurement;
        Log.d(TAG, "baseSiUnitsAreWithinBounds: " + result);
        return baseSiUnits <= maximumBaseSiMeasurement && baseSiUnits >= minimumBaseSiMeasurement;
    }

    private boolean isMultiPack() {
        return numberOfPacksInPack >= MULTI_PACK_MINIMUM_NO_OF_PACKS;
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
    public int getPackMeasurementOne() {
        Log.d(TAG, "getPackMeasurementOne: " + packMeasurementInGrams);
        return (int) packMeasurementInGrams;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setPackMeasurementOne(packMeasurementOne);
        observableMeasurement.setPackMeasurementTwo((int) packMeasurementInKilograms);

        Log.d(TAG, "setPackMeasurementOne: observableMeasurement is: " + packMeasurementOne);
        Log.d(TAG, "setPackMeasurementOne: observableMeasurement sent for processing is: " +
                observableMeasurement.getPackMeasurementTwo() + " kg, " + observableMeasurement.getPackMeasurementOne() + " grams");

        double baseSiUnits = convertToBaseSiUnits(observableMeasurement);
        Log.d(TAG, "setPackMeasurementOne: base Si to set is: " + baseSiUnits);

        return setBaseSiUnits(convertToBaseSiUnits(observableMeasurement));
    }

    @Override
    public int getPackMeasurementTwo() {
        Log.d(TAG, "getPackMeasurementTwo: " + packMeasurementInKilograms);
        return (int) packMeasurementInKilograms;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setPackMeasurementOne((int) packMeasurementInGrams);
        observableMeasurement.setPackMeasurementTwo(packMeasurementTwo);

        Log.d(TAG, "setPackMeasurementTwo: observableMeasurement is: " + packMeasurementTwo);
        Log.d(TAG, "setPackMeasurementTwo: observableMeasurement sent for processing is: " +
                observableMeasurement.getPackMeasurementTwo() + " kg, " +
                observableMeasurement.getPackMeasurementOne() + " grams");

        return setBaseSiUnits(convertToBaseSiUnits(observableMeasurement));
    }

    @Override
    public int getSinglePackMeasurementOne() {
        return (int) this.singlePackMeasurementInGrams;
    }

    @Override
    public boolean setSinglePackMeasurementOne(int singlePackMeasurementOne) {

        ObservableMeasurement singleToTotalPackMeasurement = new ObservableMeasurement();
        singleToTotalPackMeasurement.setPackMeasurementOne(singlePackMeasurementOne * numberOfPacksInPack);
        singleToTotalPackMeasurement.setPackMeasurementTwo((int) singlePackMeasurementInKilograms * numberOfPacksInPack);

        Log.d(TAG, "setSinglePackMeasurementOne: measurement is: " + singlePackMeasurementOne);
        Log.d(TAG, "setSinglePackMeasurementOne: measurement sent for processing is: " +
                singleToTotalPackMeasurement.getSinglePackMeasurementTwo() + " kg" +
                singleToTotalPackMeasurement.getSinglePackMeasurementOne() + " grams");

        return setBaseSiUnits(convertToBaseSiUnits(singleToTotalPackMeasurement));
    }

    @Override
    public int getSinglePackMeasurementTwo() {
        return (int) singlePackMeasurementInKilograms;
    }

    @Override
    public boolean setSinglePackMeasurementTwo(int singlePackMeasurementTwo) {

        ObservableMeasurement singlePackMeasurement = new ObservableMeasurement();
        singlePackMeasurement.setPackMeasurementOne((int) singlePackMeasurementInGrams * numberOfPacksInPack);
        singlePackMeasurement.setPackMeasurementTwo(singlePackMeasurementTwo * numberOfPacksInPack);

        Log.d(TAG, "setSinglePackMeasurementTwo: measurement is: " + singlePackMeasurementTwo);
        Log.d(TAG, "setSinglePackMeasurementTwo: measurement sent for processing is: " +
                singlePackMeasurement.getPackMeasurementTwo() + " kg " +
                singlePackMeasurement.getPackMeasurementOne() + " grams");

        return setBaseSiUnits(convertToBaseSiUnits(singlePackMeasurement));
    }

    private double convertToBaseSiUnits(ObservableMeasurement observableMeasurementToConvert) {

        double gramsToGrams = observableMeasurementToConvert.getPackMeasurementOne();
        double kilogramsToGrams = observableMeasurementToConvert.getPackMeasurementTwo() * UNIT_KILOGRAM;

        Log.d(TAG, "convertToBaseSiUnits: kg to grams: " + kilogramsToGrams +
                ", grams to grams: " + gramsToGrams + " Total: " + (kilogramsToGrams + gramsToGrams));

        return kilogramsToGrams + gramsToGrams;
    }

    @Override
    public void setNewMeasurementValuesTo(ObservableMeasurement observableMeasurement) {

        if (observableMeasurement.getMeasurementSubType() != getMeasurementSubType())
            observableMeasurement.setMeasurementSubType(getMeasurementSubType());

        if (observableMeasurement.getNumberOfPacksInPack() != getNumberOfPacksInPack())
            observableMeasurement.setNumberOfPacksInPack(getNumberOfPacksInPack());

        if (observableMeasurement.getPackMeasurementOne() != getPackMeasurementOne())
            observableMeasurement.setPackMeasurementOne(getPackMeasurementOne());

        if (observableMeasurement.getPackMeasurementTwo() != getPackMeasurementTwo())
            observableMeasurement.setPackMeasurementTwo(getPackMeasurementTwo());

        if (observableMeasurement.getSinglePackMeasurementOne() != getSinglePackMeasurementOne())
            observableMeasurement.setSinglePackMeasurementOne(getSinglePackMeasurementOne());

        if (observableMeasurement.getSinglePackMeasurementTwo() != getSinglePackMeasurementTwo())
            observableMeasurement.setSinglePackMeasurementTwo(getSinglePackMeasurementTwo());
    }
    @Override
    public void resetNumericValues() {

        baseSiUnits = 0;
        numberOfPacksInPack = 0;
        packMeasurementInKilograms = 0;
        packMeasurementInGrams = 0;
        singlePackMeasurementInKilograms = 0;
        singlePackMeasurementInGrams = 0;
    }

    @Override
    public int[] getInputFilterFormat() {

        int[] inputFilterFormat = new int[5];
        inputFilterFormat[GRAMS.getIntValue()] = 3;
        inputFilterFormat[KILOGRAMS.getIntValue()] = 3;

        return inputFilterFormat;
    }

    @Override
    public String toString() {
        return "MetricMass{" +
                "\ntype='" + typeAsString + '\'' +
                "\n, subType='" + subTypeAsString + '\'' +
                "\n, unitKilogram='" + unitKilogram + '\'' +
                "\n, unitGram='" + unitGram + '\'' +
                "\n, numberOfPacksInPack=" + numberOfPacksInPack +
                "\n, baseSiUnits=" + baseSiUnits +
                "\n, packMeasurementInKilograms=" + packMeasurementInKilograms +
                "\n, packMeasurementInGrams=" + packMeasurementInGrams +
                "\n, singlePackMeasurementInKilograms=" + singlePackMeasurementInKilograms +
                "\n, singlePackMeasurementInGrams=" + singlePackMeasurementInGrams +
                '}';
    }
}