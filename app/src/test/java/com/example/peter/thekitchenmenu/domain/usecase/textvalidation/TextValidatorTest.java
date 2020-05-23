package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;

public class TextValidatorTest {

    // region constants ----------------------------------------------------------------------------
    public static final int SHORT_TEXT_MIN_LENGTH = 3;
    public static final int SHORT_TEXT_MAX_LENGTH = 70;
    public static final int LONG_TEXT_MIN_LENGTH = 0;
    public static final int LONG_TEXT_MAX_LENGTH = 500;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private TextValidatorResponse actualResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private TextValidator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new TextValidator.Builder().
                setShortTextMinLength(SHORT_TEXT_MIN_LENGTH).
                setShortTextMaxLength(SHORT_TEXT_MAX_LENGTH).
                setLongTextMinLength(LONG_TEXT_MIN_LENGTH).
                setLongTextMaxLength(LONG_TEXT_MAX_LENGTH).
                build();
    }

    @Test
    public void requestTypeSHORT_TEXT_emptyString_resultTOO_SHORT() {
        // Arrange
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.SHORT_TEXT,
                new TextValidatorModel(""));
        // Act
        handler.executeAsync(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.FailReason.TOO_SHORT, actualResponse.getFailReason());
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooShort_resultTOO_SHORT() {
        // Arrange
        String shortTextToShort = new StringMaker().
                makeStringOfExactLength(SHORT_TEXT_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.SHORT_TEXT,
                new TextValidatorModel(shortTextToShort));
        // Act
        handler.executeAsync(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.FailReason.TOO_SHORT, actualResponse.getFailReason());
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooLong_resultTOO_LONG() {
        // Arrange
        String shortTextTooLong = new StringMaker().
                makeStringOfExactLength(SHORT_TEXT_MAX_LENGTH).
                thenAddOneCharacter().
                build();

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.SHORT_TEXT,
                new TextValidatorModel(shortTextTooLong));
        // Act
        handler.executeAsync(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.FailReason.TOO_LONG, actualResponse.getFailReason());
    }

    @Test
    public void requestTypeLONG_TEXT_emptyString_resultVALID() {
        // Arrange
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.LONG_TEXT,
                new TextValidatorModel(""));
        // Act
        handler.executeAsync(SUT, request, getCallback());
        // Assert
        assertEquals(CommonFailReason.NONE, actualResponse.getFailReason());
    }

    @Test
    public void requestTypeLONG_TEXT_singleCharacter_resultVALID() {
        // Arrange
        String longTextMinLengthPlusOne = new StringMaker().
                makeStringOfExactLength(LONG_TEXT_MIN_LENGTH).
                thenAddOneCharacter().
                build();

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.LONG_TEXT,
                new TextValidatorModel(longTextMinLengthPlusOne));
        // Act
        handler.executeAsync(SUT, request, getCallback());
        // Assert
        assertEquals(CommonFailReason.NONE, actualResponse.getFailReason());
    }

    @Test
    public void requestTypeLONG_TEXT_textTooLong_result_TOO_LONG() {
        // Arrange
        String longTextTooLong = new StringMaker().
                makeStringOfExactLength(LONG_TEXT_MAX_LENGTH).
                thenAddOneCharacter().
                build();

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.LONG_TEXT,
                new TextValidatorModel(longTextTooLong));
        // Act
        handler.executeAsync(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.FailReason.TOO_LONG, actualResponse.getFailReason());
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCaseBase.Callback<TextValidatorResponse> getCallback() {
        return new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onSuccessResponse(TextValidatorResponse response) {
                actualResponse = response;
            }

            @Override
            public void onErrorResponse(TextValidatorResponse response) {
                actualResponse = response;
            }
        };
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}