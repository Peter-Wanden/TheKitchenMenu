package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.ImperialVolume;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.*;

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
        MeasurementSubtype subtype = MeasurementSubtype.METRIC_VOLUME;
        // Act
        int result = subtype.asInt();
        // Assert
        assertEquals(integerForSubtypeMetricVolume, result);
    }

    @Test
    public void returnSubtypeFromInt() {
        // Arrange
        int integerForSubtypeImperialMass = 1;
        MeasurementSubtype expectedResult = MeasurementSubtype.IMPERIAL_MASS;
        // Act
        MeasurementSubtype actualResult = MeasurementSubtype.fromInt(integerForSubtypeImperialMass);
        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void returnUnitOfMeasureClassFromInt() {
        // Arrange
        int intForImperialVolume = 3;
        // Act
        UnitOfMeasure result = MeasurementSubtype.fromInt(intForImperialVolume).getMeasurementClass();
        // Assert
        assertTrue(result instanceof ImperialVolume);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}