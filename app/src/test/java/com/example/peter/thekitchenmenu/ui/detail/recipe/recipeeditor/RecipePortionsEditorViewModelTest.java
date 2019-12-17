package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final String ERROR_MESSAGE_SERVINGS = "ERROR_MESSAGE_SERVINGS";
    private static final String ERROR_MESSAGE_SITTINGS = "ERROR_MESSAGE_SITTINGS";
    private final int MIN_SERVINGS = TestDataRecipePortionsEntity.getMinServings();
    private final int MAX_SERVINGS = TestDataRecipePortionsEntity.getMaxServings();
    private final int MIN_SITTINGS = TestDataRecipePortionsEntity.getMinSittings();
    private final int MAX_SITTINGS = TestDataRecipePortionsEntity.getMaxSittings();

    private final RecipePortionsEntity NEW_EMPTY =
            TestDataRecipePortionsEntity.getNewValidEmpty();
    private final RecipePortionsEntity NEW_INVALID =
            TestDataRecipePortionsEntity.getNewInvalidServingsInvalidSittings();
    private final RecipePortionsEntity NEW_INVALID_SERVINGS_VALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewInvalidServingsValidSittings();
    private final RecipePortionsEntity NEW_VALID_SERVINGS_INVALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewValidServingsInvalidSittings();
    private final RecipePortionsEntity NEW_VALID =
            TestDataRecipePortionsEntity.getNewValidServingsValidSittings();
    private final RecipePortionsEntity EXISTING_VALID =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private final RecipePortionsEntity EXISTING_VALID_UPDATED_SERVINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedServings();
    private final RecipePortionsEntity EXISTING_VALID_UPDATED_SITTINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedSittings();
    private final RecipePortionsEntity EXISTING_VALID_CLONE =
            TestDataRecipePortionsEntity.getExistingValidClone();
    private final RecipePortionsEntity EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS =
            TestDataRecipePortionsEntity.getExistingClonedUpdatedSittingsServings();
    private final RecipePortionsEntity EXISTING_VALID_FROM_ANOTHER_USER =
            TestDataRecipePortionsEntity.getValidCloneFromAnotherUser();
    private static final RecipeComponentStatus INVALID_UNCHANGED =
            TestDataRecipeValidator.getPortionsModelStatusUnchangedInvalid();
    private static final RecipeComponentStatus INVALID_CHANGED =
            TestDataRecipeValidator.getPortionsModelStatusChangedInvalid();
    private static final RecipeComponentStatus VALID_UNCHANGED =
            TestDataRecipeValidator.getPortionsModelStatusUnchangedValid();
    private static final RecipeComponentStatus VALID_CHANGED =
            TestDataRecipeValidator.getPortionsModelStatusChangedValid();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private RepositoryRecipePortions repoRecipePortions;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getEntityCallbackArgumentCaptor;
    @Mock
    private TimeProvider timeProviderMock;
    @Mock
    private Resources resourcesMock;
    @Mock
    private UniqueIdProvider idProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipePortionsEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipePortionsEditorViewModel(
                repoRecipePortions,
                timeProviderMock,
                idProviderMock,
                resourcesMock);

        SUT.setModelValidationSubmitter(modelValidationSubmitterMock);
    }

    @Test
    public void startNewRecipeId_emptyValuesSetToObservers() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(String.valueOf(NEW_EMPTY.getServings()), SUT.getServingsInView());
        assertEquals(String.valueOf(NEW_EMPTY.getSittings()), SUT.getServingsInView());
    }

    @Test
    public void startNewRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_UNCHANGED));
    }

    @Test
    public void startNewRecipeId_invalidServingsInvalidSittings_errorMessageSet() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        assertEquals(ERROR_MESSAGE_SERVINGS, SUT.servingsErrorMessage.get());
        assertEquals(ERROR_MESSAGE_SITTINGS, SUT.sittingsErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidServingsInvalidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_invalidServingsValidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID_SERVINGS_VALID_SITTINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID_SERVINGS_VALID_SITTINGS.getSittings()));
        // Assert
        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validServingsInvalidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID_SERVINGS_INVALID_SITTINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID_SERVINGS_INVALID_SITTINGS.getSittings()));
        // Assert
        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validServingsValidSittings_errorMessageNull() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        assertNull(SUT.servingsErrorMessage.get());
        assertNull(SUT.sittingsErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validServingsValidSittings_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitRecipeComponentStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validSittingsValidServings_portionsUpdated() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        String expectedPortions = String.valueOf(NEW_VALID.getServings() * NEW_VALID.getSittings());
        assertEquals(expectedPortions, SUT.getPortionsInView());
    }

    @Test
    public void startNewRecipeId_validSittingsValidServings_saved() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        verify(repoRecipePortions).save(eq(NEW_VALID));
    }

    @Test
    public void startExisting_validRecipePortionId_valuesSetToObservables() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        assertEquals(String.valueOf(EXISTING_VALID.getServings()), SUT.getServingsInView());
        assertEquals(String.valueOf(EXISTING_VALID.getSittings()), SUT.getSittingsInView());
    }

    @Test
    public void startExisting_validRecipePortionId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_UNCHANGED));
    }

    @Test
    public void startExisting_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        // Assert
        verifyNoMoreInteractions(repoRecipePortions);
    }

    @Test
    public void startExisting_validUpdatedServings_validValueSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SERVINGS.getLastUpdate());
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(EXISTING_VALID_UPDATED_SERVINGS.getServings()));
        // Assert
        verify(repoRecipePortions).save(eq(EXISTING_VALID_UPDATED_SERVINGS));
    }

    @Test
    public void startExisting_invalidUpdatedSittings_invalidValueNotSaved() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        verifyNoMoreInteractions(repoRecipePortions);
    }

    @Test
    public void startExisting_validUpdatedSittings_validValueSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SITTINGS.getLastUpdate());
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setSittingsInView(String.valueOf(EXISTING_VALID_UPDATED_SITTINGS.getSittings()));
        // Assert
        verify(repoRecipePortions).save(eq(EXISTING_VALID_UPDATED_SITTINGS));
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_existingSavedWithNewId() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());
        // Act
        SUT.startByCloningModel(EXISTING_VALID.getRecipeId(), NEW_EMPTY.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        verify(repoRecipePortions).save(EXISTING_VALID_CLONE);
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());
        // Act
        SUT.startByCloningModel(EXISTING_VALID.getRecipeId(), NEW_EMPTY.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_UNCHANGED));
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_cloneSavedWithUpdatedSittingsServings() {
        // Arrange
        whenIdProviderReturnNewEmptyId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());
        // Act
        SUT.startByCloningModel(EXISTING_VALID.getRecipeId(), NEW_EMPTY.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(EXISTING_VALID_UPDATED_SERVINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(EXISTING_VALID_UPDATED_SITTINGS.getSittings()));
        // Assert
        verify(repoRecipePortions).save(eq(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS));
    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_min_servings)).thenReturn(MIN_SERVINGS);
        when(resourcesMock.getInteger(R.integer.recipe_max_servings)).thenReturn(MAX_SERVINGS);
        when(resourcesMock.getInteger(R.integer.recipe_min_sittings)).thenReturn(MIN_SITTINGS);
        when(resourcesMock.getInteger(R.integer.recipe_max_sittings)).thenReturn(MAX_SITTINGS);

        when(resourcesMock.getString(eq(R.string.input_error_recipe_servings),
                anyInt(), anyInt())).
                thenReturn(ERROR_MESSAGE_SERVINGS);
        when(resourcesMock.getString(eq(R.string.input_error_recipe_sittings),
                anyInt(), anyInt())).
                thenReturn(ERROR_MESSAGE_SITTINGS);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoRecipePortions).getPortionsForRecipe(eq(NEW_EMPTY.getRecipeId()),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

    private void simulateExistingValidReturnedFromDatabase() {
        verify(repoRecipePortions).getPortionsForRecipe(eq(EXISTING_VALID.getRecipeId()),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(EXISTING_VALID);
    }

    private void whenIdProviderReturnNewEmptyId() {
        when(idProviderMock.getUId()).thenReturn(NEW_EMPTY.getId());
    }

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}