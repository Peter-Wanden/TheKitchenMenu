package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MeasurementSubtypeTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private MeasurementSubtype SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void returnIntFromSubtype() {
        // Arrange
        int integerForSubtypeMetricVolume = 2;
        MeasurementSubtype subtype = MeasurementSubtype.TYPE_METRIC_VOLUME;
        // Act
        int result = subtype.asInt();
        // Assert
        assertEquals(integerForSubtypeMetricVolume, result);
    }

    @Test
    public void returnSubtypeFromInt() {
        // Arrange
        int integerForSubtypeImperialMass = 1;
        MeasurementSubtype expectedResult = MeasurementSubtype.TYPE_IMPERIAL_MASS;
        // Act
        MeasurementSubtype actualResult = MeasurementSubtype.fromInt(integerForSubtypeImperialMass);
        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void returnUnitOfMeasureClassFromInt() {
        // Arrange
        int integerForImperialVolume = 3;
        // Act
        UnitOfMeasure result = MeasurementSubtype.fromInt(integerForImperialVolume).getMeasurementClass();
        // Assert
        assertTrue(result instanceof ImperialVolume);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}