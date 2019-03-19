package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class ImperialMass implements UnitOfMeasure {

    private static final String TAG = "ImperialMass";

    private static final double UNIT_POUND = BASE_SI_UNIT_MASS * 453.59237;
    private static final double UNIT_OUNCE = UNIT_POUND / 16;

    private String type;
    private String subType;
    private String unitOunce;
    private String unitPound;

    private Integer numberOfItemsInPack = 0;
    private double baseSiUnits;
    private Integer packMeasurementInPounds = 0;
    private Integer packMeasurementInOunces = 0;
    private Integer itemMeasurementInPounds = 0;
    private Integer itemMeasurementInOunces = 0;

    ImperialMass(Context context) {

        type = context.getResources().getString(R.string.mass);
        subType = context.getResources().getString(R.string.sub_type_imperial_mass);
        unitPound = context.getResources().getString(R.string.pounds);
        unitOunce = context.getResources().getString(R.string.ounces);
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_MASS;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_IMPERIAL_MASS;
    }

    @Override
    public String getMeasurementUnitOne() {
        return unitOunce;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return unitPound;
    }

    @Override
    public ObservableMeasurement getMinAndMax() {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setNumberOfPacksInPack(numberOfItemsInPack);

        double minMeasurement;

        if (numberOfItemsInPack >= MULTI_PACK_MINIMUM_NO_OF_PACKS)

            minMeasurement = UNIT_OUNCE * numberOfItemsInPack;

        else minMeasurement = UNIT_OUNCE;

        observableMeasurement.setMinimumMeasurementOne((int) minMeasurement);
        observableMeasurement.setPackMeasurementOne(getMeasurementInOunces(MAX_MASS));
        observableMeasurement.setPackMeasurementTwo(getMeasurementInPounds(MAX_MASS));

        return observableMeasurement;
    }

    @Override
    public void setNewMeasurementValuesTo(ObservableMeasurement observableMeasurement) {
    }

    @Override
    public boolean getValuesFromObservableProductModel(ObservableProductModel productModel) {
        return false;
    }

    private double convertToBaseSiUnits(ObservableMeasurement observableMeasurementToConvert) {

        double ouncesToGrams = observableMeasurementToConvert.getPackMeasurementOne() * UNIT_OUNCE;
        double poundsToGrams = observableMeasurementToConvert.getPackMeasurementTwo() * UNIT_POUND;

        return poundsToGrams + ouncesToGrams;
    }

    @Override
    public int getNumberOfPacksInPack() {
        Log.d(TAG, "getNumberOfPacks: banana");
        return numberOfItemsInPack;
    }

    @Override
    public boolean setNumberOfPacksInPack(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_PACKS &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_PACKS) {

            this.numberOfItemsInPack = numberOfItems;
            setItemMeasurement(numberOfItemsInPack);
            Log.d(TAG, "setNumberOfPacks: banana" + numberOfItemsInPack);
            return true;
        }
        return false;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean setBaseSiUnits(double baseSiUnits) {

        if (numberOfItemsInPack > 1) {

            if (baseSiUnits <= MAX_MASS && baseSiUnits >= UNIT_OUNCE * numberOfItemsInPack) {

                this.baseSiUnits = baseSiUnits;
                setPackMeasurement();
                setItemMeasurement(numberOfItemsInPack);
                packMeasurementInPounds = (int) (this.baseSiUnits / UNIT_POUND);
                packMeasurementInOunces = (int) (this.baseSiUnits % UNIT_POUND / UNIT_OUNCE);
                return true;
            }

        }
        return false;
    }

    private void setPackMeasurement() {

        packMeasurementInOunces = getMeasurementInOunces(baseSiUnits);
        packMeasurementInPounds = getMeasurementInPounds(baseSiUnits);
    }

    private boolean setItemMeasurement(int numberOfItemsInPack) {

        // TODO - validate
        double itemSizeInBaseUnits = baseSiUnits / numberOfItemsInPack;

        if (itemSizeInBaseUnits < UNIT_OUNCE) return false;

        itemMeasurementInOunces = getMeasurementInOunces(itemSizeInBaseUnits);
        itemMeasurementInPounds = getMeasurementInPounds(itemSizeInBaseUnits);

        return true;
    }

    private int getMeasurementInOunces(double baseSiUnits) {
        return (int) (baseSiUnits % UNIT_POUND / UNIT_OUNCE);
    }

    private int getMeasurementInPounds(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_POUND);
    }

    @Override
    public int[] getInputFilterFormat() {

        int[] inputFilterFormat = new int[3];
        ObservableMeasurement maxValues = getMinAndMax();
        int maxOunceValue = maxValues.getPackMeasurementOne();
        int maxPoundValue = maxValues.getPackMeasurementTwo();

        int poundDigits = 0;
        while (maxPoundValue > 0) {
            poundDigits++;
            maxPoundValue = maxPoundValue / 10;
        }

        int ounceDigits = 0;
        while (maxOunceValue > 0) {
            ounceDigits++;
            maxOunceValue = maxOunceValue / 10;
        }

        inputFilterFormat[MeasurementUnits.POUNDS.getIntValue()] = poundDigits;
        inputFilterFormat[MeasurementUnits.OUNCES.getIntValue()] = ounceDigits;

        return inputFilterFormat;
    }

    @Override
    public int getPackMeasurementOne() {
        return packMeasurementInOunces;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setPackMeasurementOne(packMeasurementOne);
        observableMeasurement.setPackMeasurementTwo(packMeasurementInPounds);

        return setBaseSiUnits(convertToBaseSiUnits(observableMeasurement));
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInPounds;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setPackMeasurementOne(packMeasurementInOunces);
        observableMeasurement.setPackMeasurementTwo(packMeasurementTwo);

        return setBaseSiUnits(convertToBaseSiUnits(observableMeasurement));
    }

    @Override
    public int getSinglePackMeasurementOne() {
        return itemMeasurementInOunces;
    }

    @Override
    public boolean setSinglePackMeasurementOne(int itemMeasurementOne) {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setPackMeasurementOne(itemMeasurementOne * numberOfItemsInPack);
        observableMeasurement.setPackMeasurementTwo(itemMeasurementInPounds * numberOfItemsInPack);

        return setBaseSiUnits(convertToBaseSiUnits(observableMeasurement));
    }

    @Override
    public int getSinglePackMeasurementTwo() {
        return itemMeasurementInPounds;
    }

    @Override
    public boolean setSinglePackMeasurementTwo(int itemMeasurementTwo) {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setPackMeasurementOne(itemMeasurementInOunces * numberOfItemsInPack);
        observableMeasurement.setPackMeasurementTwo(itemMeasurementInPounds * numberOfItemsInPack);

        return setBaseSiUnits(convertToBaseSiUnits(observableMeasurement));
    }

    @Override
    public void resetNumericValues() {

    }
}