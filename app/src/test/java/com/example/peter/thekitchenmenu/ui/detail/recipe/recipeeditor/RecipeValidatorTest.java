package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecipeValidatorTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeComponentStatus IDENTITY_INVALID_UNCHANGED = getIdentityModelStatusUnchangedInvalid();
    private RecipeComponentStatus IDENTITY_VALID_UNCHANGED = getIdentityModelStatusUnchangedValid();
    private RecipeComponentStatus IDENTITY_INVALID_CHANGED = getIdentityModelStatusChangedInvalid();
    private RecipeComponentStatus IDENTITY_VALID_CHANGED = getIdentityModelStatusChangedValid();
    private RecipeComponentStatus COURSES_INVALID_UNCHANGED = getCoursesModelStatusUnchangedInvalid();
    private RecipeComponentStatus COURSES_VALID_UNCHANGED = getCoursesModelStatusUnchangedValid();
    private RecipeComponentStatus COURSES_INVALID_CHANGED = getCoursesModelStatusChangedInvalid();
    private RecipeComponentStatus COURSES_VALID_CHANGED = getCoursesModelStatusChangedValid();
    private RecipeComponentStatus DURATION_INVALID_UNCHANGED = getDurationModelStatusUnchangedInvalid();
    private RecipeComponentStatus DURATION_VALID_UNCHANGED = getDurationModelStatusUnchangedValid();
    private RecipeComponentStatus DURATION_INVALID_CHANGED = getDurationModelStatusChangedInvalid();
    private RecipeComponentStatus DURATION_VALID_CHANGED = getDurationModelStatusChangedValid();
    private RecipeComponentStatus PORTIONS_INVALID_UNCHANGED = getPortionsModelStatusUnchangedInvalid();
    private RecipeComponentStatus PORTIONS_VALID_UNCHANGED = getPortionsModelStatusUnchangedValid();
    private RecipeComponentStatus PORTIONS_INVALID_CHANGED = getPortionsModelStatusChangedInvalid();
    private RecipeComponentStatus PORTIONS_VALID_CHANGED = getPortionsModelStatusChangedValid();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    private RecipeValidation.RecipeEditor recipeEditorMock;
    @Captor
    ArgumentCaptor<RecipeValidationStatus> recipeStatusCaptor;
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidModelNoChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_UNCHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneValidModelChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_VALID_CHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    @Test
    public void setModelStatus_oneInvalidModelChanges_INVALID_MISSING_MODELS() {
        // Arrange
        // Act
        SUT.submitRecipeComponentStatus(IDENTITY_INVALID_CHANGED);
        // Assert
        verify(recipeEditorMock).setValidationStatus(recipeStatusCaptor.capture());
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
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
        RecipeValidationStatus validationStatus = recipeStatusCaptor.getValue();
        assertEquals(INVALID_MISSING_MODELS, validationStatus);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}