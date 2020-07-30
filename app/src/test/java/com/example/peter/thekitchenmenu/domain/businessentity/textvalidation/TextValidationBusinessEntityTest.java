package com.example.peter.thekitchenmenu.domain.businessentity.textvalidation;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity.EntityCallback;
import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity.EntityRequest;
import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity.EntityResponse;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity.TextLength;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TextValidationBusinessEntityTest {

    // region constants
    public static final int SHORT_TEXT_MIN_LENGTH = 3;
    public static final int SHORT_TEXT_MAX_LENGTH = 70;
    public static final int LONG_TEXT_MIN_LENGTH = 0;
    public static final int LONG_TEXT_MAX_LENGTH = 500;
    // endregion constants

    // region helper fields
    private TextValidationModel model;
    private List<FailReasons> failReasons;
    // endregion helper fields

    private TextValidationBusinessEntity SUT;

    @Before
    public void setup() {
        SUT = givenUseCase();
    }

    public TextValidationBusinessEntity givenUseCase() {
        return new TextValidationBusinessEntity(
                SHORT_TEXT_MIN_LENGTH,
                SHORT_TEXT_MAX_LENGTH,
                LONG_TEXT_MIN_LENGTH,
                LONG_TEXT_MAX_LENGTH
        );
    }

    @Test
    public void requestTypeSHORT_TEXT_nullCheck_resultNULL_TEXT() {
        // Arrange
        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(
                        TextLength.SHORT_TEXT,
                        null
                )
        );
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                TextValidationFailReason.TEXT_NULL
        );
        List<FailReasons> actualFailReasons = failReasons;
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void requestTypeSHORT_TEXT_emptyString_resultTOO_SHORT() {
        // Arrange
        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(
                        TextLength.SHORT_TEXT,
                        "")
                );
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                TextValidationFailReason.TEXT_TOO_SHORT
        );
        List<FailReasons> actualFailReasons = failReasons;
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooShort_resultTOO_SHORT() {
        // Arrange
        String textToVerify = new StringMaker().
                makeStringOfLength(SHORT_TEXT_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(
                        TextLength.SHORT_TEXT,
                        textToVerify
                )
        );
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                TextValidationFailReason.TEXT_TOO_SHORT
        );
        List<FailReasons> actualFailReasons = failReasons;
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void requestTypeSHORT_TEXT_textTooLong_resultTOO_LONG() {
        // Arrange
        String textToVerify = new StringMaker().
                makeStringOfLength(SHORT_TEXT_MAX_LENGTH).
                thenAddOneCharacter().
                build();
        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(
                        TextLength.SHORT_TEXT,
                        textToVerify
                )
        );
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                TextValidationFailReason.TEXT_TOO_LONG
        );
        List<FailReasons> actualFailReasons = failReasons;
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void requestTypeLONG_TEXT_emptyString_resultFailReasonsEmpty() {
        // Arrange
        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(TextLength.LONG_TEXT,""));
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        assertEquals(0, failReasons.size());
    }

    @Test
    public void requestTypeLONG_TEXT_singleCharacter_resultFailReasonsEmpty() {
        // Arrange
        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(TextLength.LONG_TEXT,"a"));
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        assertEquals(0, failReasons.size());
    }

    @Test
    public void requestTypeLONG_TEXT_textTooLong_result_TOO_LONG() {
        // Arrange
        String textToVerify = new StringMaker().
                makeStringOfLength(LONG_TEXT_MAX_LENGTH).
                thenAddOneCharacter().
                build();
        EntityRequest<TextValidationModel> request = new EntityRequest<>(
                new TextValidationModel(TextLength.LONG_TEXT, textToVerify)
        );
        // Act
        SUT.execute(request, new EntityCallbackClient());
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(TextValidationFailReason.TEXT_TOO_LONG);
        List<FailReasons> actualFailReasons = failReasons;
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    // region helper methods
    // endregion helper methods

    // region helper classes
    private class EntityCallbackClient
            implements EntityCallback<EntityResponse<TextValidationModel>> {

        @Override
        public void onProcessed(EntityResponse<TextValidationModel> response) {
            model = response.getModel();
            failReasons = response.getFailReasons();
        }
    }
    // endregion helper classes
}