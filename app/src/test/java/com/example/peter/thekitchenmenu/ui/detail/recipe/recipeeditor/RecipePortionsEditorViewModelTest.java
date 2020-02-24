package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
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
            TestDataRecipePortionsEntity.getNewInvalidTooHighServingsInvalidTooHighSittings();
    private final RecipePortionsEntity NEW_INVALID_SERVINGS_VALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewInvalidTooHighServingsValidSittings();
    private final RecipePortionsEntity NEW_VALID_SERVINGS_INVALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewValidServingsInvalidTooHighSittings();
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

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipe repoRecipeMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    private Resources resourcesMock;
    @Mock
    private TimeProvider timeProviderMock;
    @Mock
    private UniqueIdProvider idProviderMock;

    private RecipePortionsEditorViewModel SUT;
    // endregion helper fields ---------------------------------------------------------------------

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = givenViewModel();
    }

    private RecipePortionsEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock()
        );

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        Recipe recipe = new Recipe(
                repoRecipeMock,
                timeProviderMock
        );

        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock,
                handler,
                textValidator
        );

        RecipeCourse course = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );

        RecipeDuration duration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                RecipeDurationTest.MAX_PREP_TIME,
                RecipeDurationTest.MAX_COOK_TIME
        );

        RecipePortions portions = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );

        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        RecipeMacro recipeMacro = new RecipeMacro(
                handler,
                stateCalculator,
                recipe, identity,
                course,
                duration,
                portions);

        return new RecipePortionsEditorViewModel(
                handler, recipeMacro, resourcesMock
        );
    }

    @Test
    public void startNewRecipeId_emptyValuesSetToObservers() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
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
        whenIdProviderReturn(NEW_EMPTY.getId());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_UNCHANGED));
    }

    @Test
    public void startNewRecipeId_invalidServingsInvalidSittings_errorMessageSet() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        assertEquals(ERROR_MESSAGE_SERVINGS, SUT.servingsErrorMessage.get());
//        assertEquals(ERROR_MESSAGE_SITTINGS, SUT.sittingsErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidServingsInvalidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
//        verify(modelValidationSubmitterMock, times((2))).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_invalidServingsValidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_INVALID_SERVINGS_VALID_SITTINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID_SERVINGS_VALID_SITTINGS.getSittings()));
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validServingsInvalidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        // Act
        SUT.setServingsInView(String.valueOf(NEW_VALID_SERVINGS_INVALID_SITTINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID_SERVINGS_INVALID_SITTINGS.getSittings()));
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validServingsValidSittings_errorMessageNull() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
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
        whenIdProviderReturn(NEW_EMPTY.getId());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
//        verify(modelValidationSubmitterMock, times((2))).submitRecipeComponentStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validSittingsValidServings_portionsUpdated() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
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
        whenIdProviderReturn(NEW_EMPTY.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(NEW_EMPTY.getRecipeId());
        simulateNothingReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        verify(repoPortionsMock).save(eq(NEW_VALID));
    }

    @Test
    public void startExistingRecipeId_validRecipeId_valuesSetToObservables() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        assertEquals(String.valueOf(EXISTING_VALID.getServings()), SUT.getServingsInView());
        assertEquals(String.valueOf(EXISTING_VALID.getSittings()), SUT.getSittingsInView());
    }

    @Test
    public void startExistingRecipeId_validRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_UNCHANGED));
    }

    @Test
    public void startExistingRecipeId_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Act
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void startExistingRecipeId_validUpdatedServings_saved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SERVINGS.getLastUpdate());
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(EXISTING_VALID_UPDATED_SERVINGS.getServings()));
        // Assert
        verify(repoPortionsMock).save(eq(EXISTING_VALID_UPDATED_SERVINGS));
    }

    @Test
    public void startExistingRecipeId_invalidUpdatedSittings_invalidValueNotSaved() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void startExistingRecipeId_validUpdatedSittings_saved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SITTINGS.getLastUpdate());
        // Act
        SUT.start(EXISTING_VALID.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setSittingsInView(String.valueOf(EXISTING_VALID_UPDATED_SITTINGS.getSittings()));
        // Assert
        verify(repoPortionsMock).save(eq(EXISTING_VALID_UPDATED_SITTINGS));
    }

    @Test
    public void startByCloningModel_existingAndCloneToRecipeId_existingSavedWithNewId() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());
        // Act
        SUT.startByCloningModel(EXISTING_VALID.getRecipeId(), NEW_EMPTY.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        verify(repoPortionsMock).save(EXISTING_VALID_CLONE);
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());
        // Act
        SUT.startByCloningModel(EXISTING_VALID.getRecipeId(), NEW_EMPTY.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_UNCHANGED));
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_cloneSavedWithUpdatedSittingsServings() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());
        // Act
        SUT.startByCloningModel(EXISTING_VALID.getRecipeId(), NEW_EMPTY.getRecipeId());
        simulateExistingValidReturnedFromDatabase();
        SUT.setServingsInView(String.valueOf(EXISTING_VALID_UPDATED_SERVINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(EXISTING_VALID_UPDATED_SITTINGS.getSittings()));
        // Assert
        verify(repoPortionsMock).save(eq(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS));
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
        verify(repoPortionsMock).getPortionsForRecipe(eq(NEW_EMPTY.getRecipeId()),
                repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataNotAvailable();
    }

    private void simulateExistingValidReturnedFromDatabase() {
        verify(repoPortionsMock).getPortionsForRecipe(eq(EXISTING_VALID.getRecipeId()),
                repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(EXISTING_VALID);
    }

    private void whenIdProviderReturn(String id) {
        when(idProviderMock.getUId()).thenReturn(id);
    }

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}