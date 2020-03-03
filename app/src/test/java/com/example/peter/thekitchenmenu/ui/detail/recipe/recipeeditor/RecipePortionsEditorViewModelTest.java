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
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsEditorViewModelTest {

    private static final String TAG = "tkm-" + RecipePortionsEditorViewModelTest.class.
            getSimpleName() + ": ";

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
    private UseCaseHandler handler;
    private RecipeMacro recipeMacro;
    private RecipeStateListener recipeStateListener;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipePortionsEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = givenViewModel();
    }

    private RecipePortionsEditorViewModel givenViewModel() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock()
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

        recipeMacro = new RecipeMacro(
                handler,
                stateCalculator,
                recipe, identity,
                course,
                duration,
                portions);

        recipeStateListener = new RecipeStateListener();
        recipeMacro.registerStateListener(recipeStateListener);

        return new RecipePortionsEditorViewModel(
                handler, recipeMacro, resourcesMock
        );
    }

    @Test
    public void startNewRecipeId_requestFromAnotherMacroClient_responsePushedToRegisteredCallbackOnce() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        assertEquals(String.valueOf(NEW_EMPTY.getServings()), SUT.getServingsInView());
        assertEquals(String.valueOf(NEW_EMPTY.getSittings()), SUT.getServingsInView());
    }

    @Test
    public void startNewRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidServingsInvalidSittings_errorMessageSet() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        // Assert
        assertEquals(ERROR_MESSAGE_SERVINGS, SUT.servingsErrorMessage.get());

        // Act
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        assertEquals(ERROR_MESSAGE_SITTINGS, SUT.sittingsErrorMessage.get());

    }

    @Test
    public void startNewRecipeId_invalidServingsInvalidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidServingsValidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_INVALID_SERVINGS_VALID_SITTINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_INVALID_SERVINGS_VALID_SITTINGS.getSittings()));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validServingsInvalidSittings_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_VALID_SERVINGS_INVALID_SITTINGS.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID_SERVINGS_INVALID_SITTINGS.getSittings()));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validServingsValidSittings_errorMessageNull() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        assertNull(SUT.servingsErrorMessage.get());
        assertNull(SUT.sittingsErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validServingsValidSittings_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validSittingsValidServings_portionsUpdated() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(recipeId);
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        String expectedPortions = String.valueOf(NEW_VALID.getServings() * NEW_VALID.getSittings());
        assertEquals(expectedPortions, SUT.getPortionsInView());
    }

    @Test
    public void startNewRecipeId_validSittingsValidServings_saved() {
        // Arrange
        String recipeId = NEW_EMPTY.getRecipeId();
        whenIdProviderReturn(NEW_EMPTY.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW_EMPTY.getCreateDate());
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_VALID.getServings()));
        SUT.setSittingsInView(String.valueOf(NEW_VALID.getSittings()));
        // Assert
        verify(repoPortionsMock).save(eq(NEW_VALID));
    }

    @Test
    public void startExistingRecipeId_validRecipeId_valuesSetToObservables() {
        // Arrange
        String recipeId = EXISTING_VALID.getRecipeId();
        // Act
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);
        // Assert
        assertEquals(String.valueOf(EXISTING_VALID.getServings()), SUT.getServingsInView());
        assertEquals(String.valueOf(EXISTING_VALID.getSittings()), SUT.getSittingsInView());
    }

    @Test
    public void startExistingRecipeId_validRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        String recipeId = EXISTING_VALID.getRecipeId();
        // Act
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startExistingRecipeId_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        String recipeId = EXISTING_VALID.getRecipeId();

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(NEW_INVALID.getServings()));
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void startExistingRecipeId_validUpdatedServings_saved() {
        // Arrange
        String recipeId = EXISTING_VALID.getRecipeId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SERVINGS.getLastUpdate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);

        // Act
        SUT.setServingsInView(String.valueOf(EXISTING_VALID_UPDATED_SERVINGS.getServings()));
        // Assert
        verify(repoPortionsMock).save(eq(EXISTING_VALID_UPDATED_SERVINGS));
    }

    @Test
    public void startExistingRecipeId_invalidUpdatedSittings_invalidValueNotSaved() {
        // Arrange
        String recipeId = EXISTING_VALID.getRecipeId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SERVINGS.getLastUpdate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);

        // Act
        SUT.setSittingsInView(String.valueOf(NEW_INVALID.getSittings()));
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void startExistingRecipeId_validUpdatedSittings_saved() {
        // Arrange
        String recipeId = EXISTING_VALID.getRecipeId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(EXISTING_VALID_UPDATED_SITTINGS.getLastUpdate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(recipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(recipeId);

        // Act
        SUT.setSittingsInView(String.valueOf(EXISTING_VALID_UPDATED_SITTINGS.getSittings()));
        // Assert
        verify(repoPortionsMock).save(eq(EXISTING_VALID_UPDATED_SITTINGS));
    }

    @Test
    public void startByCloningModel_existingAndCloneToRecipeId_existingSavedWithNewId() {
        // Arrange
        String cloneFromRecipeId = EXISTING_VALID.getRecipeId();
        String cloneToRecipeId = NEW_EMPTY.getRecipeId();

        whenIdProviderReturn(EXISTING_VALID_CLONE.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(cloneFromRecipeId).
                setCloneToId(cloneToRecipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());
        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(cloneFromRecipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(cloneFromRecipeId);

        // Assert
        verify(repoPortionsMock).save(EXISTING_VALID_CLONE);
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        String cloneFromRecipeId = EXISTING_VALID.getRecipeId();
        String cloneToRecipeId = NEW_EMPTY.getRecipeId();

        whenIdProviderReturn(cloneToRecipeId);
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(cloneFromRecipeId).
                setCloneToId(cloneToRecipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(cloneFromRecipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(cloneFromRecipeId);

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.PORTIONS);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startByCloningModel_existingAndNewRecipeId_cloneSavedWithUpdatedSittingsServings() {
        // Arrange
        String cloneFromRecipeId = EXISTING_VALID.getRecipeId();
        String cloneToRecipeId = NEW_EMPTY.getRecipeId();

        whenIdProviderReturn(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                EXISTING_VALID_CLONE.getLastUpdate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(cloneFromRecipeId).
                setCloneToId(cloneToRecipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnExistingValid(cloneFromRecipeId);
        verifyRepoPortionsCalledAndReturnExistingValid(cloneFromRecipeId);

        // Act
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

    private void verifyAllOtherComponentReposCalledAndReturnDataUnavailable(String recipeId) {
        verifyRepoRecipeCalledAndReturnDataUnavailable(recipeId);
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
    }

    private void verifyRepoRecipeCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoIdentityCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoCoursesCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoDurationCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoPortionsCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataNotAvailable();
    }

    private void verifyAllOtherComponentReposCalledAndReturnExistingValid(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeEntity.getValidExisting());

        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(TestDataRecipeIdentityEntity.
                getValidExistingTitleValidDescriptionValid());

        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.
                getAllByRecipeId(recipeId));

        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(TestDataRecipeDurationEntity.
                getValidExistingComplete());
    }

    private void verifyRepoPortionsCalledAndReturnExistingValid(String recipeId) {
        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(EXISTING_VALID);
    }

    private void whenIdProviderReturn(String id) {
        when(idProviderMock.getUId()).thenReturn(id);
    }

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeStateListener implements RecipeMacro.RecipeStateListener {

        RecipeStateResponse response;

        @Override
        public void recipeStateChanged(RecipeStateResponse response) {
            this.response = response;
        }

        public RecipeStateResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeStateListener{" +
                    "response=" + response +
                    '}';
        }
    }

    private static class RecipeResponseCallback implements UseCase.Callback<RecipeResponse> {

        private static final String TAG = "tkm-" + RecipeResponseCallback.class.getSimpleName() +
                ": ";

        private RecipeResponse response;

        @Override
        public void onSuccess(RecipeResponse response) {
            System.out.println(RecipePortionsEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeResponse response) {
            System.out.println(RecipePortionsEditorViewModelTest.TAG + TAG + "onError:" + response);
            this.response = response;
        }

        public RecipeResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeResponseCallback{" +
                    "response=" + response +
                    '}';
        }
    }
    // endregion helper classes --------------------------------------------------------------------


}