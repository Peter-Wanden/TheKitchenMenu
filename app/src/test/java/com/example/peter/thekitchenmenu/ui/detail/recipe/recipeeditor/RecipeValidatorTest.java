package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecipeValidatorTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeComponentStatusModel IDENTITY_INVALID_UNCHANGED = getIdentityModelStatusUnchangedInvalid();
    private RecipeComponentStatusModel IDENTITY_VALID_UNCHANGED = getIdentityModelStatusUnchangedValid();
    private RecipeComponentStatusModel IDENTITY_INVALID_CHANGED = getIdentityModelStatusChangedInvalid();
    private RecipeComponentStatusModel IDENTITY_VALID_CHANGED = getIdentityModelStatusChangedValid();
    private RecipeComponentStatusModel COURSES_INVALID_UNCHANGED = getCoursesModelStatusUnchangedInvalid();
    private RecipeComponentStatusModel COURSES_VALID_UNCHANGED = getCoursesModelStatusUnchangedValid();
    private RecipeComponentStatusModel COURSES_INVALID_CHANGED = getCoursesModelStatusChangedInvalid();
    private RecipeComponentStatusModel COURSES_VALID_CHANGED = getCoursesModelStatusChangedValid();
    private RecipeComponentStatusModel DURATION_INVALID_UNCHANGED = getDurationModelStatusUnchangedInvalid();
    private RecipeComponentStatusModel DURATION_VALID_UNCHANGED = getDurationModelStatusUnchangedValid();
    private RecipeComponentStatusModel DURATION_INVALID_CHANGED = getDurationModelStatusChangedInvalid();
    private RecipeComponentStatusModel DURATION_VALID_CHANGED = getDurationModelStatusChangedValid();
    private RecipeComponentStatusModel PORTIONS_INVALID_UNCHANGED = getPortionsModelStatusUnchangedInvalid();
    private RecipeComponentStatusModel PORTIONS_VALID_UNCHANGED = getPortionsModelStatusUnchangedValid();
    private RecipeComponentStatusModel PORTIONS_INVALID_CHANGED = getPortionsModelStatusChangedInvalid();
    private RecipeComponentStatusModel PORTIONS_VALID_CHANGED = getPortionsModelStatusChangedValid();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    private RecipeValidation.RecipeEditor recipeEditorMock;
    @Captor
    ArgumentCaptor<RecipeStatus> recipeStatusCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeValidator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeValidator();
        SUT.setRecipeEditor(recipeEditorMock);
    }

    @Test
    public void setModelStatus_oneValidModelNoChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidModelNoChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidModelChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidModelChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_CHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_allInvalidUnchangedModels_INVALID_UNCHANGED() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(COURSES_INVALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(DURATION_INVALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_INVALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_UNCHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidUnchangedRemainderInvalidChanged_INVALID_CHANGED() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(COURSES_INVALID_CHANGED);
        SUT.submitRecipeComponentStatus(DURATION_INVALID_CHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_INVALID_CHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_CHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_allInvalidChanged_INVALID_CHANGED() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_CHANGED);
        SUT.submitRecipeComponentStatus(COURSES_INVALID_CHANGED);
        SUT.submitRecipeComponentStatus(DURATION_INVALID_CHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_INVALID_CHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_CHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidChangedRemainderInvalidUnchanged_INVALID_CHANGED() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        SUT.submitRecipeComponentStatus(COURSES_INVALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(DURATION_INVALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_INVALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_CHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidChangedRemainderValidUnchanged_INVALID_CHANGED() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_CHANGED);
        SUT.submitRecipeComponentStatus(COURSES_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(DURATION_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_VALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_CHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_twoValidUnchanged_VALID_UNCHANGED() {
        // Arrange
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(COURSES_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(DURATION_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_VALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(VALID_UNCHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidChangedRemainderValidUnchanged_VALID_CHANGED() {
        // Arrange
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        SUT.submitRecipeComponentStatus(COURSES_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(DURATION_VALID_UNCHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_VALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(VALID_CHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_allValidChanged_VALID_CHANGED() {
        // Arrange
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        SUT.submitRecipeComponentStatus(COURSES_VALID_CHANGED);
        SUT.submitRecipeComponentStatus(DURATION_VALID_CHANGED);
        SUT.submitRecipeComponentStatus(PORTIONS_VALID_CHANGED);
        // Assert
        verify(recipeEditorMock, times((4))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(VALID_CHANGED, validationStatus);
    }

    @Test
    public void setModelStatus_twoSameValidChangedModels_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        // Assert
        verify(recipeEditorMock, times((2))).setValidationStatus(recipeStatusCaptor.capture());
        RecipeStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}