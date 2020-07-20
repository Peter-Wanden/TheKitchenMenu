package com.example.peter.thekitchenmenu.domain.businessentity.recipeIdentity;

import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityEntityTest {

    // region constants
    public static final int TITLE_MIN_LENGTH = TextValidatorTest.SHORT_TEXT_MIN_LENGTH;
    public static final int TITLE_MAX_LENGTH = TextValidatorTest.SHORT_TEXT_MAX_LENGTH;
    public static final int DESCRIPTION_MIN_LENGTH = TextValidatorTest.LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = TextValidatorTest.LONG_TEXT_MAX_LENGTH;
    // endregion constants

    // region helper fields
    // endregion helper fields

    RecipeIdentityEntity SUT;

    @Before
    public void setup() {
        SUT = givenUseCase();
    }

    private RecipeIdentityEntity givenUseCase() {
        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(TITLE_MIN_LENGTH).
                setShortTextMaxLength(TITLE_MAX_LENGTH).
                setLongTextMinLength(DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(DESCRIPTION_MAX_LENGTH).
                build();

        return new RecipeIdentityEntity(textValidator);
    }

    @Test
    public void titleValidDescriptionValid_failReasonsEmpty() {
        // Arrange

        // Act
        // Assert
    }

    // region helper methods
    // endregion helper methods

    // region helper classes
    // endregion helper classes
}