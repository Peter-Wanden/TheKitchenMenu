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
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RecipeDurationEditorViewModelTest {

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

    private RecipeMacro recipeMacro;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDurationEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = givenViewModel();
    }

    private RecipeDurationEditorViewModel givenViewModel() {
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
                recipe,
                identity,
                course,
                duration,
                portions);

        return new RecipeDurationEditorViewModel(
                handler, recipeMacro, resourcesMock
        );
    }

    @Test
    public void startNewRecipeId_newEmptyValuesSetToObservers() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        // Assert
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() / 60), SUT.getPrepHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() % 60), SUT.getPrepMinutesInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() / 60), SUT.getCookHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() % 60), SUT.getCookMinutesInView());
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        // Assert

    }

    @Test
    public void startNewRecipeId_invalidPrepHours_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert

    }

    @Test
    public void startNewRecipeId_validPrepHours_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepHours_recipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert

    }

    @Test
    public void startNewRecipeId_validPrepHours_prepHoursSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert

    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_invalidValueNotSaved() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert

    }

    @Test
    public void startNewRecipeId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_prepTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(VALID_CHANGED);
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() % 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60));
        SUT.setPrepMinutesInView(String.valueOf(MAX_PREP_TIME % 60 + 1));
        // Assert

    }

    @Test
    public void startNewRecipeId_validCookHours_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHours_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert

    }

    @Test
    public void startNewRecipeId_validCookHours_cookHoursSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert

    }

    @Test
    public void startNewRecipeId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert

    }

    @Test
    public void startNewRecipeId_validCookMinutes_cookTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert

    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_cookTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
//        verify(modelValidationSubmitterMock).submitRecipeComponentStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromRepo();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        verify(repoDurationMock).save(VALID_NEW_EMPTY);
    }

    @Test
    public void start_validExistingRecipeId_prepHoursSetToObservable() {
        // Arrange
        String prepHours = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepHours, SUT.getPrepHoursInView());
    }

    @Test
    public void start_validExistingRecipeId_prepMinutesSetToObservable() {
        // Arrange
        String prepMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepMinutes, SUT.getPrepMinutesInView());
    }

    @Test
    public void start_validExistingRecipeId_cookHoursSetToObservable() {
        // Arrange
        String cookHours = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookHours, SUT.getCookHoursInView());
    }

    @Test
    public void start_validExistingRecipeId_cookMinutesSetToObservable() {
        // Arrange
        String cookMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookMinutes, SUT.getCookMinutesInView());
    }

    @Test
    public void start_validExistingRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert

    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
        // Arrange
        SUT.startByCloningModel(VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        // Assert
        verify(repoDurationMock).getById(eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoDurationCallback.capture());
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_existingFromAnotherUserCopiedAndSavedWithNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        // Act
        SUT.startByCloningModel(VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        // Assert
        verify(repoDurationMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_prepTimeChanged_savedWithUpdatedPrepTime() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
        // Act
        SUT.startByCloningModel(VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60));
        // Assert
        verify(repoDurationMock, times((3))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_CLONED_PREP_TIME_UPDATED, durationEntityCaptor.getValue());
    }

    @Test
    public void startWithCloned_modelFromAnotherUserNotAvailable_newModelCreatedAndSavedWithNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.startByCloningModel(INVALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        verify(repoDurationMock).getById(eq(INVALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataNotAvailable();

        // Assert
        verify(repoDurationMock).save(eq(VALID_NEW_EMPTY));
    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
    }

    private void simulateNothingReturnedFromRepo() {
        verify(repoDurationMock).getById(eq(VALID_NEW_EMPTY.getId()),
                repoDurationCallback.capture());

        repoDurationCallback.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoDurationMock).getById(eq(VALID_EXISTING_COMPLETE.getId()),
                repoDurationCallback.capture());

        repoDurationCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidEntityFromAnotherUserFromDatabase() {
        verify(repoDurationMock).getById(
                eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoDurationCallback.capture());

        repoDurationCallback.getValue().onEntityLoaded(
                VALID_COMPLETE_FROM_ANOTHER_USER);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}