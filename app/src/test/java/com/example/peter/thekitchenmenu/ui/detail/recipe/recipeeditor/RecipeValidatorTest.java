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
    private RecipeComponentStateModel IDENTITY_INVALID_UNCHANGED = getIdentityModelStatusINVALID_UNCHANGED();
    private RecipeComponentStateModel IDENTITY_VALID_UNCHANGED = getIdentityModelStatusVALID_UNCHANGED();
    private RecipeComponentStateModel IDENTITY_INVALID_CHANGED = getIdentityModelStatusINVALID_CHANGED();
    private RecipeComponentStateModel IDENTITY_VALID_CHANGED = getIdentityModelStatusVALID_CHANGED();
    private RecipeComponentStateModel COURSES_INVALID_UNCHANGED = getCoursesModelStatusINVALID_UNCHANGED();
    private RecipeComponentStateModel COURSES_VALID_UNCHANGED = getCoursesModelStatusVALID_UNCHANGED();
    private RecipeComponentStateModel COURSES_INVALID_CHANGED = getCoursesModelStatusINVALID_CHANGED();
    private RecipeComponentStateModel COURSES_VALID_CHANGED = getCoursesModelStatusVALID_CHANGED();
    private RecipeComponentStateModel DURATION_INVALID_UNCHANGED = getDurationModelStatusINVALID_UNCHANGED();
    private RecipeComponentStateModel DURATION_VALID_UNCHANGED = getDurationModelStatusVALID_UNCHANGED();
    private RecipeComponentStateModel DURATION_INVALID_CHANGED = getDurationModelStatusINVALID_CHANGED();
    private RecipeComponentStateModel DURATION_VALID_CHANGED = getDurationModelStatusVALID_CHANGED();
    private RecipeComponentStateModel PORTIONS_INVALID_UNCHANGED = getPortionsModelStatusINVALID_UNCHANGED();
    private RecipeComponentStateModel PORTIONS_VALID_UNCHANGED = getPortionsModelStatusVALID_UNCHANGED();
    private RecipeComponentStateModel PORTIONS_INVALID_CHANGED = getPortionsModelStatusINVALID_CHANGED();
    private RecipeComponentStateModel PORTIONS_VALID_CHANGED = getPortionsModelStatusVALID_CHANGED();
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