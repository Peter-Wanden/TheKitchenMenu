package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public class UnitOfMeasureConstants {

    public static final double MIN_MASS = 1.; // 1g
    public static final double MAX_MASS = 10000.; // 10kg

    static final double MIN_VOLUME = 1.; // 1ml
    public static final double MAX_VOLUME = 10000.; // 10l

    static final int MIN_COUNT = 1;
    static final int MAX_COUNT = 500;

    static final int MIN_NUMBER_OF_ITEMS = 1;
    static final int MAX_NUMBER_OF_ITEMS = 1000;

    static final int NOT_YET_SET = 0;

    // density
    public static final double MAX_CONVERSION_FACTOR = 1.5;
    public static final double MIN_CONVERSION_FACTOR = .001;

    // region imperial mass constants --------------------------------------------------------------
    static final MeasurementType IMPERIAL_MASS_TYPE = MeasurementType.MASS;
    static final MeasurementSubtype IMPERIAL_MASS_SUBTYPE = MeasurementSubtype.IMPERIAL_MASS;
    static final int IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    static final double IMPERIAL_MASS_MAX_MEASUREMENT = MAX_MASS;
    static final double IMPERIAL_MASS_MIN_MEASUREMENT = MIN_MASS;
    static final double IMPERIAL_MASS_UNIT_TWO = IMPERIAL_MASS_MIN_MEASUREMENT * 453.59237; // 1lb
    static final double IMPERIAL_MASS_UNIT_ONE = IMPERIAL_MASS_UNIT_TWO / 16; // 1oz
    static final double IMPERIAL_MASS_UNIT_ONE_DECIMAL = IMPERIAL_MASS_UNIT_ONE / 10; // tenth oz
    static final double IMPERIAL_MASS_SMALLEST_UNIT = IMPERIAL_MASS_UNIT_ONE_DECIMAL;
    static final boolean IMPERIAL_MASS_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion imperial mass constants -----------------------------------------------------------

    // region imperial spoon constants -------------------------------------------------------------
    static final MeasurementType IMPERIAL_SPOON_TYPE = MeasurementType.VOLUME;
    static final MeasurementSubtype IMPERIAL_SPOON_SUBTYPE = MeasurementSubtype.IMPERIAL_SPOON;
    static final int IMPERIAL_SPOON_NUMBER_OF_MEASUREMENT_UNITS = 2;
    static final double IMPERIAL_SPOON_MAX_MEASUREMENT = MAX_VOLUME;
    static final double IMPERIAL_SPOON_MIN_MEASUREMENT = MIN_VOLUME / 1000; // 1000th gram
    static final double IMPERIAL_SPOON_UNIT_TWO = MIN_VOLUME * 15; // 1Tbsp
    static final double IMPERIAL_SPOON_UNIT_ONE = IMPERIAL_SPOON_UNIT_TWO / 3; // 1tsp
    static final double IMPERIAL_SPOON_UNIT_ONE_DECIMAL = IMPERIAL_SPOON_UNIT_ONE / 10; // 10th of a tsp
    static final double IMPERIAL_SPOON_SMALLEST_UNIT = 0.1;
    static final boolean IMPERIAL_SPOON_IS_CONVERSION_FACTOR_ENABLED = true;
    // endregion imperial spoon constants ----------------------------------------------------------

    // region imperial volume constants --------------------------------------------------------------
    static final MeasurementType IMPERIAL_VOLUME_TYPE = MeasurementType.VOLUME;
    static final MeasurementSubtype IMPERIAL_VOLUME_SUBTYPE = MeasurementSubtype.IMPERIAL_VOLUME;
    static final int IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    static final double IMPERIAL_VOLUME_MAX_MEASUREMENT = MAX_VOLUME;
    static final double IMPERIAL_VOLUME_MIN_MEASUREMENT = MIN_VOLUME;
    static final double IMPERIAL_VOLUME_UNIT_TWO = IMPERIAL_VOLUME_MIN_MEASUREMENT * 568.26125; // 1 Pt
    static final double IMPERIAL_VOLUME_UNIT_ONE = IMPERIAL_VOLUME_UNIT_TWO / 20; // 1fl oz
    static final double IMPERIAL_VOLUME_UNIT_ONE_DECIMAL = IMPERIAL_VOLUME_UNIT_ONE / 10; // 10th fl oz
    static final double IMPERIAL_VOLUME_SMALLEST_UNIT = IMPERIAL_VOLUME_UNIT_ONE_DECIMAL;
    static final boolean IMPERIAL_VOLUME_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion imperial volume constants -----------------------------------------------------------

    // region metric mass constants ----------------------------------------------------------------
    static final MeasurementType METRIC_MASS_TYPE = MeasurementType.MASS;
    static final MeasurementSubtype METRIC_MASS_SUBTYPE = MeasurementSubtype.METRIC_MASS;
    static final int METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    static final double METRIC_MASS_MAX_MEASUREMENT = MAX_MASS;
    static final double METRIC_MASS_MIN_MEASUREMENT = MIN_MASS;
    static final double METRIC_MASS_UNIT_TWO = METRIC_MASS_MIN_MEASUREMENT * 1000; // 1kg
    static final double METRIC_MASS_UNIT_ONE = METRIC_MASS_UNIT_TWO / 1000; // 1g
    static final double METRIC_MASS_UNIT_ONE_DECIMAL = 0; // no fractions of a gram
    static final double METRIC_MASS_SMALLEST_UNIT = METRIC_MASS_UNIT_ONE;
    static final boolean METRIC_MASS_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion metric mass constants -------------------------------------------------------------

    // region metric volume constants --------------------------------------------------------------
    static final MeasurementType METRIC_VOLUME_TYPE = MeasurementType.VOLUME;
    static final MeasurementSubtype METRIC_VOLUME_SUBTYPE = MeasurementSubtype.METRIC_VOLUME;
    static final int METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    static final double METRIC_VOLUME_MAX_MEASUREMENT = MAX_VOLUME;
    static final double METRIC_VOLUME_MIN_MEASUREMENT = MIN_VOLUME;
    static final double METRIC_VOLUME_UNIT_TWO = METRIC_VOLUME_MIN_MEASUREMENT * 1000; // 1l
    static final double METRIC_VOLUME_UNIT_ONE = METRIC_VOLUME_UNIT_TWO / 1000; // 1ml
    static final double METRIC_VOLUME_UNIT_ONE_DECIMAL = 0;
    static final double METRIC_VOLUME_SMALLEST_UNIT = METRIC_VOLUME_UNIT_ONE;
    static final boolean METRIC_VOLUME_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion metric volume constants -----------------------------------------------------------

    // region count constants --------------------------------------------------------------
    static final MeasurementType COUNT_TYPE = MeasurementType.COUNT;
    static final MeasurementSubtype COUNT_SUBTYPE = MeasurementSubtype.COUNT;
    static final int COUNT_NUMBER_OF_MEASUREMENT_UNITS = 1;
    static final double COUNT_MAX_MEASUREMENT = MAX_COUNT;
    static final double COUNT_MIN_MEASUREMENT = MIN_COUNT;
    static final double COUNT_UNIT_TWO = 1; // only unit two is used with count
    static final double COUNT_UNIT_ONE = 1; // unit one not used
    static final double COUNT_UNIT_ONE_DECIMAL = 0;
    static final double COUNT_SMALLEST_UNIT = MIN_COUNT;
    static final boolean COUNT_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion count constants -----------------------------------------------------------
}
