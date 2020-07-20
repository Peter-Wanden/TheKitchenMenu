package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModelBuilder;

public class UnitOfMeasureConstants {

    public static final double MIN_MASS = 1.; // 1g
    public static final double MAX_MASS = 10000.; // 10kg

    public static final double MIN_VOLUME = 1.; // 1ml
    public static final double MAX_VOLUME = 10000.; // 10l

    public static final double MIN_COUNT = 0.1; // 1 tenth of a whole item
    public static final int MAX_COUNT = 500;

    public static final int MIN_NUMBER_OF_ITEMS = 1;
    public static final int MAX_NUMBER_OF_ITEMS = 1000;

    public static final int NOT_SET = 0;

    // density
    public static final double MIN_CONVERSION_FACTOR = .001;
    public static final double DEFAULT_CONVERSION_FACTOR = 1;
    public static final double MAX_CONVERSION_FACTOR = 1.5;

    // region imperial mass constants --------------------------------------------------------------
    public static final MeasurementType IMPERIAL_MASS_TYPE = MeasurementType.MASS;
    public static final MeasurementSubtype IMPERIAL_MASS_SUBTYPE = MeasurementSubtype.IMPERIAL_MASS;
    public static final int IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    public static final double IMPERIAL_MASS_MAX_MEASUREMENT = MAX_MASS;
    public static final double IMPERIAL_MASS_MIN_MEASUREMENT = MIN_MASS;
    public static final double IMPERIAL_MASS_UNIT_TWO = IMPERIAL_MASS_MIN_MEASUREMENT * 453.59237; // 1lb
    public static final double IMPERIAL_MASS_UNIT_ONE = IMPERIAL_MASS_UNIT_TWO / 16; // 1oz
    public static final double IMPERIAL_MASS_UNIT_ONE_DECIMAL = IMPERIAL_MASS_UNIT_ONE / 10; // tenth oz
    public static final double IMPERIAL_MASS_SMALLEST_UNIT = IMPERIAL_MASS_UNIT_ONE_DECIMAL;
    public static final boolean IMPERIAL_MASS_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion imperial mass constants -----------------------------------------------------------

    // region imperial spoon constants -------------------------------------------------------------
    public static final MeasurementType IMPERIAL_SPOON_TYPE = MeasurementType.VOLUME;
    public static final MeasurementSubtype IMPERIAL_SPOON_SUBTYPE = MeasurementSubtype.IMPERIAL_SPOON;
    public static final int IMPERIAL_SPOON_NUMBER_OF_MEASUREMENT_UNITS = 2;
    public static final double IMPERIAL_SPOON_MAX_MEASUREMENT = MAX_VOLUME;
    public static final double IMPERIAL_SPOON_MIN_MEASUREMENT = MIN_VOLUME / 1000; // 1000th gram
    public static final double IMPERIAL_SPOON_UNIT_TWO = MIN_VOLUME * 15; // 1Tbsp
    public static final double IMPERIAL_SPOON_UNIT_ONE = IMPERIAL_SPOON_UNIT_TWO / 3; // 1tsp
    public static final double IMPERIAL_SPOON_UNIT_ONE_DECIMAL = IMPERIAL_SPOON_UNIT_ONE / 10; // 10th of a tsp
    public static final double IMPERIAL_SPOON_SMALLEST_UNIT = 0.1;
    public static final boolean IMPERIAL_SPOON_IS_CONVERSION_FACTOR_ENABLED = true;
    // endregion imperial spoon constants ----------------------------------------------------------

    // region imperial volume constants --------------------------------------------------------------
    public static final MeasurementType IMPERIAL_VOLUME_TYPE = MeasurementType.VOLUME;
    public static final MeasurementSubtype IMPERIAL_VOLUME_SUBTYPE = MeasurementSubtype.IMPERIAL_VOLUME;
    public static final int IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    public static final double IMPERIAL_VOLUME_MAX_MEASUREMENT = MAX_VOLUME;
    public static final double IMPERIAL_VOLUME_MIN_MEASUREMENT = MIN_VOLUME;
    public static final double IMPERIAL_VOLUME_UNIT_TWO = IMPERIAL_VOLUME_MIN_MEASUREMENT * 568.26125; // 1 Pt
    public static final double IMPERIAL_VOLUME_UNIT_ONE = IMPERIAL_VOLUME_UNIT_TWO / 20; // 1fl oz
    public static final double IMPERIAL_VOLUME_UNIT_ONE_DECIMAL = IMPERIAL_VOLUME_UNIT_ONE / 10; // 10th fl oz
    public static final double IMPERIAL_VOLUME_SMALLEST_UNIT = IMPERIAL_VOLUME_UNIT_ONE_DECIMAL;
    public static final boolean IMPERIAL_VOLUME_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion imperial volume constants -----------------------------------------------------------

    // region metric mass constants ----------------------------------------------------------------
    public static final MeasurementType METRIC_MASS_TYPE = MeasurementType.MASS;
    public static final MeasurementSubtype METRIC_MASS_SUBTYPE = MeasurementSubtype.METRIC_MASS;
    public static final int METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    public static final double METRIC_MASS_MAX_MEASUREMENT = MAX_MASS;
    public static final double METRIC_MASS_MIN_MEASUREMENT = MIN_MASS;
    public static final double METRIC_MASS_UNIT_TWO = METRIC_MASS_MIN_MEASUREMENT * 1000; // 1kg
    public static final double METRIC_MASS_UNIT_ONE = METRIC_MASS_UNIT_TWO / 1000; // 1g
    public static final double METRIC_MASS_UNIT_ONE_DECIMAL = 0.1; // 10ths of a gram
    public static final double METRIC_MASS_SMALLEST_UNIT = METRIC_MASS_UNIT_ONE_DECIMAL;
    public static final boolean METRIC_MASS_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion metric mass constants -------------------------------------------------------------

    // region metric volume constants --------------------------------------------------------------
    public static final MeasurementType METRIC_VOLUME_TYPE = MeasurementType.VOLUME;
    public static final MeasurementSubtype METRIC_VOLUME_SUBTYPE = MeasurementSubtype.METRIC_VOLUME;
    public static final int METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    public static final double METRIC_VOLUME_MAX_MEASUREMENT = MAX_VOLUME;
    public static final double METRIC_VOLUME_MIN_MEASUREMENT = MIN_VOLUME;
    public static final double METRIC_VOLUME_UNIT_TWO = METRIC_VOLUME_MIN_MEASUREMENT * 1000; // 1l
    public static final double METRIC_VOLUME_UNIT_ONE = METRIC_VOLUME_UNIT_TWO / 1000; // 1ml
    public static final double METRIC_VOLUME_UNIT_ONE_DECIMAL = 0.1;
    public static final double METRIC_VOLUME_SMALLEST_UNIT = METRIC_VOLUME_UNIT_ONE_DECIMAL;
    public static final boolean METRIC_VOLUME_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion metric volume constants -----------------------------------------------------------

    // region count constants --------------------------------------------------------------
    public static final MeasurementType COUNT_TYPE = MeasurementType.COUNT;
    public static final MeasurementSubtype COUNT_SUBTYPE = MeasurementSubtype.COUNT;
    public static final int COUNT_NUMBER_OF_MEASUREMENT_UNITS = 2;
    public static final double COUNT_MAX_MEASUREMENT = MAX_COUNT;
    public static final double COUNT_MIN_MEASUREMENT = MIN_COUNT;
    public static final double COUNT_UNIT_TWO = 1;
    public static final double COUNT_UNIT_ONE = 1;
    public static final double COUNT_UNIT_ONE_DECIMAL = 0.1;
    public static final double COUNT_SMALLEST_UNIT = COUNT_UNIT_ONE_DECIMAL / 100;
    public static final boolean COUNT_IS_CONVERSION_FACTOR_ENABLED = false;
    // endregion count constants -----------------------------------------------------------

    public static final UnitOfMeasure DEFAULT_UNIT_OF_MEASURE =
            MeasurementSubtype.METRIC_MASS.getMeasurementClass();

    public static final MeasurementModel DEFAULT_MEASUREMENT_MODEL =
            MeasurementModelBuilder.
                    basedOnUnitOfMeasure(DEFAULT_UNIT_OF_MEASURE).
                    build();
}
