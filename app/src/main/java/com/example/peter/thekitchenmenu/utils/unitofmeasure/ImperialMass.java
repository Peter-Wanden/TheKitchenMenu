package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class ImperialMass implements UnitOfMeasure {

    private static final String TAG = "ImperialMass";

    private static final int IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_POUND = BASE_SI_UNIT_MASS * 453.59237;
    private static final double UNIT_OUNCE = UNIT_POUND / 16;

    private String type;
    private String subType;
    private String measurementUnitOne;
    private String measurementUnitTwo;
    private String measurementUnitThree;

    private Integer numberOfItemsInPack = SINGLE_ITEM;
    private double baseSiUnits;
    private Integer packMeasurementInPounds = 0;
    private Integer packMeasurementInOunces = 0;
    private Integer itemMeasurementInPounds = 0;
    private Integer itemMeasurementInOunces = 0;

    ImperialMass(Context context) {

        type = context.getResources().getString(R.string.mass);
        subType = context.getResources().getString(R.string.sub_type_imperial_mass);
        measurementUnitTwo = context.getResources().getString(R.string.pounds);
        measurementUnitOne = context.getResources().getString(R.string.ounces);
        measurementUnitThree = "";
    }

    @Override
    public String getMeasurementTypeAsString() {
        return type;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_MASS;
    }

    @Override
    public String getMeasurementSubTypeAsString() {
        return null;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_IMPERIAL_MASS;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public String getMeasurementUnitOneLabel() {
        return measurementUnitOne;
    }

    @Override
    public String getMeasurementUnitTwoLabel() {
        return measurementUnitTwo;
    }

    @Override
    public String getMeasurementUnitThreeLabel() {
        return measurementUnitThree;
    }

    @Override
    public ProductMeasurementModel getMinAndMax() {

        ProductMeasurementModel productMeasurementModel = new ProductMeasurementModel();
        productMeasurementModel.setNumberOfItems(numberOfItemsInPack);

        double minMeasurement;

        if (numberOfItemsInPack > SINGLE_ITEM)

            minMeasurement = UNIT_OUNCE * numberOfItemsInPack;

        else minMeasurement = UNIT_OUNCE;

        productMeasurementModel.setMinimumMeasurementOne((int) minMeasurement);
        productMeasurementModel.setPackMeasurementOne(getMeasurementInOunces(MAX_MASS));
        productMeasurementModel.setPackMeasurementTwo(getMeasurementInPounds(MAX_MASS));

        return productMeasurementModel;
    }

    private double convertToBaseSiUnits(ProductMeasurementModel productMeasurementModelToConvert) {

        double ouncesToGrams = productMeasurementModelToConvert.getPackMeasurementOne() * UNIT_OUNCE;
        double poundsToGrams = productMeasurementModelToConvert.getPackMeasurementTwo() * UNIT_POUND;

        return poundsToGrams + ouncesToGrams;
    }

    @Override
    public int getNumberOfItems() {
        Log.d(TAG, "getNumberOfItems: banana");
        return numberOfItemsInPack;
    }

    @Override
    public boolean setNumberOfItems(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems > SINGLE_ITEM &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS) {

            this.numberOfItemsInPack = numberOfItems;
            setItemMeasurement(numberOfItemsInPack);
            Log.d(TAG, "setNumberOfItems: banana" + numberOfItemsInPack);
            return true;
        }
        return false;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean baseSiUnitsAreSet(double baseSiUnits) {

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
        ProductMeasurementModel maxValues = getMinAndMax();
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

        ProductMeasurementModel productMeasurementModel = new ProductMeasurementModel();
        productMeasurementModel.setPackMeasurementOne(packMeasurementOne);
        productMeasurementModel.setPackMeasurementTwo(packMeasurementInPounds);

        return baseSiUnitsAreSet(convertToBaseSiUnits(productMeasurementModel));
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInPounds;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        ProductMeasurementModel productMeasurementModel = new ProductMeasurementModel();
        productMeasurementModel.setPackMeasurementOne(packMeasurementInOunces);
        productMeasurementModel.setPackMeasurementTwo(packMeasurementTwo);

        return baseSiUnitsAreSet(convertToBaseSiUnits(productMeasurementModel));
    }

    @Override
    public int getPackMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementThree(int packMeasurementThree) {
        return false;
    }

    @Override
    public int getItemMeasurementOne() {
        return itemMeasurementInOunces;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {

        ProductMeasurementModel productMeasurementModel = new ProductMeasurementModel();
        productMeasurementModel.setPackMeasurementOne(itemMeasurementOne * numberOfItemsInPack);
        productMeasurementModel.setPackMeasurementTwo(itemMeasurementInPounds * numberOfItemsInPack);

        return baseSiUnitsAreSet(convertToBaseSiUnits(productMeasurementModel));
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementInPounds;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {

        ProductMeasurementModel productMeasurementModel = new ProductMeasurementModel();
        productMeasurementModel.setPackMeasurementOne(itemMeasurementInOunces * numberOfItemsInPack);
        productMeasurementModel.setPackMeasurementTwo(itemMeasurementInPounds * numberOfItemsInPack);

        return baseSiUnitsAreSet(convertToBaseSiUnits(productMeasurementModel));
    }

    @Override
    public int getItemMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementThree(int itemMeasurementThree) {
        return false;
    }

    @Override
    public void resetNumericValues() {

    }
}