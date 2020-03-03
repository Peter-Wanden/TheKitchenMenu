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
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RecipeDurationEditorViewModelTest {

    private static final String TAG = "tkm-" + RecipeDurationEditorViewModelTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeDurationEntity VALID_NEW_EMPTY =
            TestDataRecipeDurationEntity.getValidNewEmpty();
    private static final RecipeDurationEntity INVALID_NEW_PREP_TIME_INVALID =
            TestDataRecipeDurationEntity.getInvalidNewPrepTimeInvalid();
    private static final RecipeDurationEntity INVALID_NEW_COOK_TIME_INVALID =
            TestDataRecipeDurationEntity.getInvalidNewCookTimeInvalid();
    private static final RecipeDurationEntity VALID_NEW_PREP_TIME_VALID =
            TestDataRecipeDurationEntity.getValidNewPrepTimeValid();
    private static final RecipeDurationEntity VALID_NEW_COOK_TIME_VALID =
            TestDataRecipeDurationEntity.getValidNewCookTimeValid();
    private static final RecipeDurationEntity VALID_NEW_COMPLETE =
            TestDataRecipeDurationEntity.getValidNewComplete();
    private static final RecipeDurationEntity INVALID_EXISTING_COMPLETE =
            TestDataRecipeDurationEntity.getInvalidExistingComplete();
    private static final RecipeDurationEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipeDurationEntity VALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeDurationEntity.getValidCompleteFromAnotherUser();
    private static final RecipeDurationEntity INVALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeDurationEntity.getInvalidCompleteFromAnotherUser();
    private static final RecipeDurationEntity VALID_NEW_CLONED =
            TestDataRecipeDurationEntity.getValidNewCloned();
    private static final RecipeDurationEntity INVALID_NEW_CLONED =
            TestDataRecipeDurationEntity.getInvalidNewCloned();
    private static final RecipeDurationEntity VALID_NEW_CLONED_PREP_TIME_UPDATED =
            TestDataRecipeDurationEntity.getValidNewClonedPrepTimeUpdated();

    private static final int MAX_PREP_TIME = TestDataRecipeDurationEntity.getMaxPrepTime();
    private static final int MAX_COOK_TIME = TestDataRecipeDurationEntity.getMaxCookTime();
    private static final String ERROR_MESSAGE_TIME_TOO_LONG = "error_message_time_too_long";

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
    Resources resourcesMock;
    @Captor
    ArgumentCaptor<RecipeDurationEntity> durationEntityCaptor;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private UseCaseHandler handler;
    private RecipeMacro recipeMacro;
    private RecipeStateListener recipeStateListener;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDurationEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = givenViewModel();
    }

    private RecipeDurationEditorViewModel givenViewModel() {
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
                recipe,
                identity,
                course,
                duration,
                portions);

        recipeStateListener = new RecipeStateListener();
        recipeMacro.registerStateListener(recipeStateListener);

        return new RecipeDurationEditorViewModel(
                handler, recipeMacro, resourcesMock
        );
    }

    @Test
    public void startNewRecipeId_newEmptyValuesSetToObservers() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        // Assert
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() / 60), SUT.getPrepHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() % 60), SUT.getPrepMinutesInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() / 60), SUT.getCookHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() % 60), SUT.getCookMinutesInView());
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        // Assert
        ComponentState expectedState = ComponentState.DATA_UNAVAILABLE;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60 + 1));
        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60 + 1));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validPrepHours_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepHours_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validPrepHours_prepHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));

        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));

        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));

        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_prepTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() % 60));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60));
        SUT.setPrepMinutesInView(String.valueOf(MAX_PREP_TIME % 60 + 1));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validCookHours_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHours_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validCookHours_cookHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));

        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));

        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void startNewRecipeId_validCookMinutes_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validCookMinutes_cookTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);

    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));

        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_cookTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));

        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_COOK_TIME_INVALID.getCreateDate());

        // Act
        setupForNewValidNewEmptyRecipe(recipeId);

        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));

        // Assert
        verify(repoDurationMock).save(VALID_NEW_COOK_TIME_VALID);
    }

    @Test
    public void start_validExistingRecipeId_prepHoursSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        String prepHours = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() / 60);

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(prepHours, SUT.getPrepHoursInView());
    }

    @Test
    public void start_validExistingRecipeId_prepMinutesSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        String prepMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() % 60);

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(prepMinutes, SUT.getPrepMinutesInView());
    }

    @Test
    public void start_validExistingRecipeId_cookHoursSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        String cookHours = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() / 60);

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(cookHours, SUT.getCookHoursInView());
    }

    @Test
    public void start_validExistingRecipeId_cookMinutesSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        String cookMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() % 60);

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(cookMinutes, SUT.getCookMinutesInView());
    }

    @Test
    public void start_validExistingRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();
        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        // Assert
        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = recipeStateListener.
                getResponse().
                getComponentStates().
                get(ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
        // Arrange
        String cloneFromId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToId = VALID_NEW_EMPTY.getId();

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(cloneFromId).
                setCloneToId(cloneToId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        verifyAllOtherReposCalledAndReturnValidExisting(cloneFromId);
        verifyReposDurationCalledAndReturnValidExisting(cloneFromId);

        // Assert
        verify(repoDurationMock).getById(eq(cloneFromId), repoDurationCallback.capture());
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_existingFromAnotherUserCopiedAndSavedWithNewId() {
        // Arrange
        String cloneFromId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(cloneFromId).
                setCloneToId(cloneToId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        verifyAllOtherReposCalledAndReturnValidExisting(cloneFromId);
        verifyRepoDurationCalledAndReturnValidFromAnotherUser();

        // Assert
        verify(repoDurationMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_prepTimeChanged_savedWithUpdatedPrepTime() {
        // Arrange

        // Arrange
        String cloneFromId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToId = VALID_NEW_EMPTY.getId();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());

        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(cloneFromId).
                setCloneToId(cloneToId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());
        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(cloneFromId);
        verifyRepoDurationCalledAndReturnValidFromAnotherUser();

        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60));

        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_CLONED_PREP_TIME_UPDATED, durationEntityCaptor.getValue());
    }


    // region helper methods -----------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
    }

    private void setupForNewValidNewEmptyRecipe(String recipeId) {
        // Arrange
        // An external request that starts/loads the recipe
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeId).
                setCloneToId(DO_NOT_CLONE).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
    }

    private void verifyAllComponentReposCalledAndReturnDataUnavailable(String recipeId) {
        verifyRepoRecipeCalledAndReturnDataUnavailable(recipeId);
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);
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

    private void verifyAllOtherReposCalledAndReturnValidExisting(String recipeId) {

        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeEntity.getValidFromAnotherUser());

        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(TestDataRecipeIdentityEntity.
                getValidExistingTitleValidDescriptionValid());

        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAllByRecipeId(recipeId));

        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(TestDataRecipePortionsEntity.
                getExistingValidNinePortions());
    }

    private void verifyReposDurationCalledAndReturnValidExisting(String recipeId) {
        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(TestDataRecipeDurationEntity.
                getValidExistingComplete());
    }

    private void verifyRepoDurationCalledAndReturnValidFromAnotherUser() {
        verify(repoDurationMock).getById(
                eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoDurationCallback.capture());

        repoDurationCallback.getValue().onEntityLoaded(
                VALID_COMPLETE_FROM_ANOTHER_USER);
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
            System.out.println(RecipeDurationEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeResponse response) {
            System.out.println(RecipeDurationEditorViewModelTest.TAG + TAG + "onError:" + response);
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