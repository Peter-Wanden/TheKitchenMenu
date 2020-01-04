package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;

public class TextValidatorTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private TextValidatorResponse actualResponse;
    private int[] textLengthValues = {3, 70, 0, 500};
    // endregion helper fields ---------------------------------------------------------------------

    private TextValidator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new TextValidator.Builder().
                setShortTextMinLength(textLengthValues[0]).
                setShortTextMaxLength(textLengthValues[1]).
                setLongTextMinLength(textLengthValues[2]).
                setLongTextMaxLength(textLengthValues[3]).
                build();
    }

    @Test
    public void requestTypeSHORT_TEXT_emptyString_resultTOO_SHORT() {
        // Arrange
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.RequestType.SHORT_TEXT,
                new TextValidatorModel(""));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.Result.TOO_SHORT, actualResponse.getResult());
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooShort_resultTOO_SHORT() {
        // Arrange
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.RequestType.SHORT_TEXT,
                new TextValidatorModel("ti"));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.Result.TOO_SHORT, actualResponse.getResult());
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooLong_resultTOO_LONG() {
        // Arrange
        String text = getTooLongString(textLengthValues[1]);

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.RequestType.SHORT_TEXT,
                new TextValidatorModel(text));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.Result.TOO_LONG, actualResponse.getResult());
    }

    @Test
    public void requestTypeLONG_TEXT_emptyString_resultVALID() {
        // Arrange
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.RequestType.LONG_TEXT,
                new TextValidatorModel(""));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.Result.VALID, actualResponse.getResult());
    }

    @Test
    public void requestTypeLONG_TEXT_singleCharacter_resultVALID() {
        // Arrange
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.RequestType.LONG_TEXT,
                new TextValidatorModel("1"));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.Result.VALID, actualResponse.getResult());
    }

    @Test
    public void requestTypeLONG_TEXT_textTooLong_result_TOO_LONG() {
        // Arrange
        String text = getTooLongString(textLengthValues[3]);

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.RequestType.LONG_TEXT,
                new TextValidatorModel(text));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(TextValidator.Result.TOO_LONG, actualResponse.getResult());
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCaseInteractor.Callback<TextValidatorResponse> getCallback() {
        return new UseCaseInteractor.Callback<TextValidatorResponse>() {
            @Override
            public void onSuccess(TextValidatorResponse response) {
                actualResponse = response;
            }

            @Override
            public void onError(TextValidatorResponse response) {
                actualResponse = response;
            }
        };
    }

    private String getTooLongString(int length) {
        StringBuilder builder = new StringBuilder();
        String a="a";
        for (int i=0; i<length; i++) {
            builder.append(a);
        }
        builder.append(a);
        return builder.toString();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}