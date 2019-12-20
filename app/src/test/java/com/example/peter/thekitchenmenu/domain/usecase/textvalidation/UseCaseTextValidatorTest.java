package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;

public class UseCaseTextValidatorTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private UseCaseTextValidator.Response actualResponse;
    private int[] textLengthValues = {3, 70, 0, 500};
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseTextValidator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new UseCaseTextValidator.Builder().
                setShortTextMinLength(textLengthValues[0]).
                setShortTextMaxLength(textLengthValues[1]).
                setLongTextMinLength(textLengthValues[2]).
                setLongTextMaxLength(textLengthValues[3]).
                build();
    }

    @Test
    public void requestTypeSHORT_TEXT_emptyString_resultTOO_SHORT() {
        // Arrange
        UseCaseTextValidator.Request request = new UseCaseTextValidator.Request(
                UseCaseTextValidator.RequestType.SHORT_TEXT,
                new UseCaseTextValidator.Model(""));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(UseCaseTextValidator.Result.TOO_SHORT, actualResponse.getResult());
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooShort_resultTOO_SHORT() {
        // Arrange
        UseCaseTextValidator.Request request = new UseCaseTextValidator.Request(
                UseCaseTextValidator.RequestType.SHORT_TEXT,
                new UseCaseTextValidator.Model("ti"));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(UseCaseTextValidator.Result.TOO_SHORT, actualResponse.getResult());
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooLong_resultTOO_LONG() {
        // Arrange
        String text = getTooLongString(textLengthValues[1]);

        UseCaseTextValidator.Request request = new UseCaseTextValidator.Request(
                UseCaseTextValidator.RequestType.SHORT_TEXT,
                new UseCaseTextValidator.Model(text));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(UseCaseTextValidator.Result.TOO_LONG, actualResponse.getResult());
    }

    @Test
    public void requestTypeLONG_TEXT_emptyString_resultVALID() {
        // Arrange
        UseCaseTextValidator.Request request = new UseCaseTextValidator.Request(
                UseCaseTextValidator.RequestType.LONG_TEXT,
                new UseCaseTextValidator.Model(""));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(UseCaseTextValidator.Result.VALID, actualResponse.getResult());
    }

    @Test
    public void requestTypeLONG_TEXT_singleCharacter_resultVALID() {
        // Arrange
        UseCaseTextValidator.Request request = new UseCaseTextValidator.Request(
                UseCaseTextValidator.RequestType.LONG_TEXT,
                new UseCaseTextValidator.Model("1"));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(UseCaseTextValidator.Result.VALID, actualResponse.getResult());
    }

    @Test
    public void requestTypeLONG_TEXT_textTooLong_result_TOO_LONG() {
        // Arrange
        String text = getTooLongString(textLengthValues[3]);

        UseCaseTextValidator.Request request = new UseCaseTextValidator.Request(
                UseCaseTextValidator.RequestType.LONG_TEXT,
                new UseCaseTextValidator.Model(text));
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        assertEquals(UseCaseTextValidator.Result.TOO_LONG, actualResponse.getResult());
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCaseInteractor.Callback<UseCaseTextValidator.Response> getCallback() {
        return new UseCaseInteractor.Callback<UseCaseTextValidator.Response>() {
            @Override
            public void onSuccess(UseCaseTextValidator.Response response) {
                actualResponse = response;
            }

            @Override
            public void onError(UseCaseTextValidator.Response response) {
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