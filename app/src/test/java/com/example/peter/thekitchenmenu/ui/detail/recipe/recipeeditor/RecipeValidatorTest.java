package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData.getIdentityModelStatusChangedValid;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    @Mock
    private RecipeValidation.RecipeEditor recipeEditorMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeValidator SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeValidator();
        SUT.setRecipeEditor(recipeEditorMock);
    }

    @Test
    public void setModelStatus_oneValidModelNoChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_UNCHANGED_VALID);
        // Assert
        verify(recipeEditorMock).setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidModelNoChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_UNCHANGED_INVALID);
        // Assert
        verify(recipeEditorMock).setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidModelChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
        // Assert
        verify(recipeEditorMock).setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidModelChanges_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_INVALID);
        // Assert
        verify(recipeEditorMock).setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_twoInvalidUnchangedModels_INVALID_NO_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_UNCHANGED_INVALID);
        SUT.submitModelStatus(COURSES_MODEL_UNCHANGED_INVALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(INVALID_NO_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidUnchangedOneInvalidChanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_UNCHANGED_INVALID);
        SUT.submitModelStatus(COURSES_MODEL_CHANGED_INVALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(INVALID_HAS_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_twoInvalidChanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_INVALID);
        SUT.submitModelStatus(COURSES_MODEL_CHANGED_INVALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(INVALID_HAS_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidChangedOneInvalidUnchanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
        SUT.submitModelStatus(COURSES_MODEL_UNCHANGED_INVALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(INVALID_HAS_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidChangedOneValidUnchanged_INVALID_HAS_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_INVALID);
        SUT.submitModelStatus(COURSES_MODEL_UNCHANGED_VALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(INVALID_HAS_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_twoValidUnchanged_VALID_NO_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        SUT.submitModelStatus(IDENTITY_MODEL_UNCHANGED_VALID);
        SUT.submitModelStatus(COURSES_MODEL_UNCHANGED_VALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(VALID_NO_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidChangedOneValidUnchanged_VALID_HAS_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
        SUT.submitModelStatus(COURSES_MODEL_UNCHANGED_VALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(VALID_HAS_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_twoValidChanged_VALID_HAS_CHANGES() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
        SUT.submitModelStatus(COURSES_MODEL_CHANGED_VALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(VALID_HAS_CHANGES, validationStatus);
    }

    @Test
    public void setModelStatus_twoSameValidChangedModels_INVALID_MISSING_MODELS() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeValidationStatus> ac = ArgumentCaptor.forClass(
                RecipeValidationStatus.class);
        // Act
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
        SUT.submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
        // Assert
        verify(recipeEditorMock, times(2)).
                setRecipeValidationStatus(ac.capture());
        RecipeValidationStatus validationStatus = ac.getAllValues().get(1);
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}