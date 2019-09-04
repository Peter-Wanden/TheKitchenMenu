package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData.getIdentityModelStatusChangedValid;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;
import static org.junit.Assert.*;

public class RecipeValidatorTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeModelStatus IDENTITY_MODEL_UNCHANGED_INVALID = getIdentityModelStatusUnChangedInValid();
    private RecipeModelStatus IDENTITY_MODEL_UNCHANGED_VALID = getIdentityModelStatusUnChangedValid();
    private RecipeModelStatus IDENTITY_MODEL_CHANGED_INVALID = getIdentityModelStatusChangedInvalid();
    private RecipeModelStatus IDENTITY_MODEL_CHANGED_VALID = getIdentityModelStatusChangedValid();
    private RecipeModelStatus COURSES_MODEL_UNCHANGED_INVALID = getCoursesModelStatusUnchangedInvalid();
    private RecipeModelStatus COURSES_MODEL_UNCHANGED_VALID = getCoursesModelStatusUnchangedValid();
    private RecipeModelStatus COURSES_MODEL_CHANGED_INVALID = getCoursesModelStatusChangedInvalid();
    private RecipeModelStatus COURSES_MODEL_CHANGED_VALID = getCoursesModelStatusChangedValid();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeValidator SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeValidator();
    }

    @Test
    public void setModelStatus_oneValidModelNoChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_UNCHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_MISSING_MODELS, validator);
    }

    @Test
    public void setModelStatus_oneInvalidModelNoChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_UNCHANGED_INVALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_MISSING_MODELS, validator);
    }

    @Test
    public void setModelStatus_oneValidModelChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_MISSING_MODELS, validator);
    }

    @Test
    public void setModelStatus_oneInvalidModelChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_INVALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_MISSING_MODELS, validator);
    }

    @Test
    public void setModelStatus_twoInvalidUnchangedModels_INVALID_NO_CHANGES() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_UNCHANGED_INVALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_NO_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_oneInvalidUnchangedOneInvalidChanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_CHANGED_INVALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_HAS_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_twoInvalidChanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_CHANGED_INVALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_HAS_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_oneValidChangedOneInvalidUnchanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_UNCHANGED_INVALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_HAS_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_oneInvalidChangedOneValidUnchanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_UNCHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_HAS_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_twoValidUnchanged_VALID_NO_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_UNCHANGED_VALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_UNCHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.VALID_NO_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_oneValidChangedOneValidUnchanged_VALID_HAS_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_UNCHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.VALID_HAS_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_twoValidChanged_VALID_HAS_CHANGES() throws Exception {
        // Arrange
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(
                IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(
                COURSES_MODEL_CHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.VALID_HAS_CHANGES, validator2);
    }

    @Test
    public void setModelStatus_multipleChangesToModels_resultVALID_HAS_CHANGES() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_UNCHANGED_VALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator3 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator4 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator5 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator6 = SUT.getRecipeValidationStatus(COURSES_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator7 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator8 = SUT.getRecipeValidationStatus(COURSES_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator9 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator10 = SUT.getRecipeValidationStatus(COURSES_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator11 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator12 = SUT.getRecipeValidationStatus(COURSES_MODEL_UNCHANGED_INVALID);
        RecipeValidationStatus validator13 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_INVALID);
        RecipeValidationStatus validator14 = SUT.getRecipeValidationStatus(COURSES_MODEL_UNCHANGED_VALID);
        RecipeValidationStatus validator15 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_UNCHANGED_VALID);
        RecipeValidationStatus validator16 = SUT.getRecipeValidationStatus(COURSES_MODEL_UNCHANGED_VALID);
        RecipeValidationStatus validator17 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator18 = SUT.getRecipeValidationStatus(COURSES_MODEL_UNCHANGED_VALID);
        RecipeValidationStatus validator19 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator20 = SUT.getRecipeValidationStatus(COURSES_MODEL_CHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.VALID_HAS_CHANGES, validator20);
        // Assert
    }

    @Test
    public void setModelStatus_twoSameValidChangedModels_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        // Act
        RecipeValidationStatus validator1 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_VALID);
        RecipeValidationStatus validator2 = SUT.getRecipeValidationStatus(IDENTITY_MODEL_CHANGED_VALID);
        // Assert
        assertEquals(RecipeValidationStatus.INVALID_MISSING_MODELS, validator2);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}