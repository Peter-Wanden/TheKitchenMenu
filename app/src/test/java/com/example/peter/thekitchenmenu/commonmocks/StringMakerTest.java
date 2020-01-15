package com.example.peter.thekitchenmenu.commonmocks;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;

public class StringMakerTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private StringMaker SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new StringMaker();
    }

    @Test
    public void makeStringOfExactLength_lengthCorrect() {
        // Arrange
        int stringLength = 10;
        // Act
        String string = SUT.makeStringOfExactLength(stringLength).build();
        // Assert
        assertEquals(stringLength, string.length());
    }

    @Test
    public void makeStringOfExactLength_thenRemoveOneCharacter_lengthCorrect() {
        // Arrange
        int stringLength = 10;
        // Act
        String string = SUT.makeStringOfExactLength(stringLength).thenRemoveOneCharacter().build();
        // Assert
        assertEquals(stringLength - 1, string.length());
    }

    @Test
    public void makeStringOfExactLength_thenAddOneCharacter_lengthCorrect() {
        // Arrange
        int stringLength = 10;
        // Act
        String string = SUT.makeStringOfExactLength(stringLength).thenAddOneCharacter().build();
        // Assert
        assertEquals(stringLength + 1, string.length());
    }

    @Test
    public void makeStringOfExactLength_includeStringAddedAfter_lengthCorrect() {
        // Arrange
        String includeString = "string";
        int stringLength = 10;
        // Act
        String string = SUT.
                makeStringOfExactLength(stringLength).
                includeStringAtStart(includeString).
                build();
        // Assert
        assertEquals(stringLength, string.length());
    }

    @Test
    public void makeStringOfExactLength_includeStringAddedBefore_lengthCorrect() {
        // Arrange
        String stringAtStart = "string";
        int stringLength = 10;
        // Act
        String string = SUT.
                includeStringAtStart(stringAtStart).
                makeStringOfExactLength(stringLength).
                build();
        // Assert
        assertEquals(stringLength, string.length());
    }

    @Test
    public void makeStringOfExactLength_includeStringThenRemoveOneCharacter_lengthCorrect() {
        // Arrange
        String stringAtStart = "string";
        int stringLength = 10;
        // Act
        String string = SUT.
                makeStringOfExactLength(stringLength).
                includeStringAtStart(stringAtStart).
                thenRemoveOneCharacter().
                build();
        // Assert
        assertEquals(stringLength -1, string.length());
    }

    @Test
    public void makeStringOfExactLength_includeStringThenAddOneCharacter_lengthCorrect() {
        // Arrange
        String stringAtStart = "string";
        int stringLength = 10;
        // Act
        String string = SUT.
                makeStringOfExactLength(stringLength).
                includeStringAtStart(stringAtStart).
                thenAddOneCharacter().
                build();
        // Assert
        assertEquals(stringLength +1, string.length());
    }

    @Test
    public void makeStringOfExactLength_includeStringAddedAfter_startStringLongerThanExactLength_lengthOfStartStringReduced() {
        // Arrange
        String stringAtStartTooLong = "stringAtStartTooLong";
        int stringLengthTooShort = stringAtStartTooLong.length() - stringAtStartTooLong.length() / 2;
        // Act
        String string = SUT.
                makeStringOfExactLength(stringLengthTooShort).
                includeStringAtStart(stringAtStartTooLong).
                build();
        // Assert
        assertEquals(stringLengthTooShort, string.length());
    }

    @Test
    public void makeStringOfExactLength_includeStringAddedBefore_startStringLongerThanExactLength_lengthOfStartStringReduced() {
        // Arrange
        String stringAtStartTooLong = "stringAtStartTooLong";
        int stringLengthTooShort = stringAtStartTooLong.length() - stringAtStartTooLong.length() / 2;
        // Act
        String string = SUT.
                includeStringAtStart(stringAtStartTooLong).
                makeStringOfExactLength(stringLengthTooShort).
                build();
        // Assert
        assertEquals(stringLengthTooShort, string.length());
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}