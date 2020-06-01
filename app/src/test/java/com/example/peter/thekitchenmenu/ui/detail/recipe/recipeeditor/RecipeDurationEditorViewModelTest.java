package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeDurationEditorViewModelTest {

    private static final String TAG = "tkm-" + RecipeDurationEditorViewModelTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeDurationEntity VALID_NEW_EMPTY =
            TestDataRecipeDurationEntity.getValidNew();
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

    private static final int MAX_PREP_TIME = TestDataRecipeDurationEntity.getMaxPrepTime();
    private static final int MAX_COOK_TIME = TestDataRecipeDurationEntity.getMaxCookTime();
    private static final String ERROR_MESSAGE_TIME_TOO_LONG = "error_message_time_too_long";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
//    @Mock
//    RepositoryRecipeComponentState repoRecipeMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeMetadataParentEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    Resources resourcesMock;
    @Captor
    ArgumentCaptor<RecipeDurationEntity> durationEntityCaptor;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private UseCaseHandler handler;
    private RecipeMetadataListener recipeStateListener;
    private Recipe recipeMacro;
    private RecipeMacroResponseListener macroListener;
    private DurationResponseListener durationListener;
    private UseCaseMetadataModel metadata;
    private RecipeDurationResponse.Model model;

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

//        RecipeMetadata recipeMetadata = new RecipeMetadata(
//                timeProviderMock,
//                repoRecipeMock,
//                RecipeComponents.requiredComponents
//        );

//        RecipeIdentity identity = new RecipeIdentity(
//                repoIdentityMock,
//                timeProviderMock,
//                handler,
//                textValidator
//        );

        RecipeCourse course = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );

//        RecipeDuration duration = new RecipeDuration(
//                repoDurationMock,
//                timeProviderMock,
//                RecipeDurationTest.MAX_PREP_TIME,
//                RecipeDurationTest.MAX_COOK_TIME
//        );

        RecipePortions portions = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );

//        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

//        recipeMacro = new Recipe(
//                handler,
//                stateCalculator,
//                recipeMetadata,
//                identity,
//                course,
//                duration,
//                portions);

        recipeStateListener = new RecipeMetadataListener();
        recipeMacro.registerMetadataListener(recipeStateListener);

        return new RecipeDurationEditorViewModel(
                handler, recipeMacro, resourcesMock
        );
    }

    @Test
    public void newId_newEmptyValuesSetToObservers() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();

        // Act
        givenNewEmptyRecipe(recipeId);

        // Assert
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() / 60), SUT.getPrepHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() % 60), SUT.getPrepMinutesInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() / 60), SUT.getCookHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() % 60), SUT.getCookMinutesInView());
    }

    // TODO
    //  ///////////////////////////////////////////////////////////////////////////////
    //  ///// Move any test that doesn't affect the display to RecipeDurationTest /////
    //  ///////////////////////////////////////////////////////////////////////////////
    @Test
    public void newId_recipeStatusINVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        long dateReturnedIfNoData = 0L;
        int timeReturnedForEmptyModel = 0;

        // Act
        givenNewEmptyRecipe(recipeId);

        // Assert metadata response
        assertEquals(RecipeMetadata.ComponentState.INVALID_UNCHANGED, metadata.getComponentState());
        assertTrue(metadata.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
        assertEquals(dateReturnedIfNoData, metadata.getCreateDate());
        assertEquals(dateReturnedIfNoData, metadata.getLasUpdate());

        // Assert model response
        assertEquals(timeReturnedForEmptyModel, model.getTotalPrepTime());
        assertEquals(timeReturnedForEmptyModel, model.getTotalPrepTime());
        assertEquals(timeReturnedForEmptyModel, model.getTotalTime());

        // Assert state listener response
        RecipeMetadata.ComponentState actualState = recipeStateListener.getResponse().
                getDomainModel().getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);

        assertEquals(RecipeMetadata.ComponentState.INVALID_UNCHANGED, actualState);

        // Assert duration response in macro response equals duration response
        assertEquals(durationListener.onErrorResponse,
                macroListener.getResponse().
                        getDomainModel().
                        getComponentResponses().
                        get(RecipeMetadata.ComponentName.DURATION));
    }

    @Test
    public void newId_invalidPrepHours_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();

        // Act
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void newId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();

        // Act
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60 + 1));
        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void newId_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60 + 1));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);

        assertTrue(durationListener.onErrorResponse.getMetadata().
                getFailReasons().
                contains(RecipeDuration.FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void newId_validPrepHours_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_validPrepHours_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_validPrepHours_prepHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_invalidPrepMinutes_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));

        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void newId_invalidPrepMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));

        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void newId_validPrepMinutes_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));

        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void newId_validPrepMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_validPrepHoursAndMinutes_prepTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_validPrepHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_invalidPrepHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() % 60));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_invalidPrepHoursAndMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60));
        SUT.setPrepMinutesInView(String.valueOf(MAX_PREP_TIME % 60 + 1));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_validCookHours_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_validCookHours_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_validCookHours_cookHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_invalidCookHours_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));

        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_invalidCookHours_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));

        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void newId_validCookMinutes_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_validCookMinutes_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_validCookMinutes_cookTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_invalidCookMinutes_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_invalidCookMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);

    }

    @Test
    public void newId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));

        // Assert
        verifyNoMoreInteractions(repoDurationMock);
    }

    @Test
    public void newId_validCookHoursAndMinutes_errorMessageObservableNull() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));

        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_validCookHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_validCookHoursAndMinutes_cookTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));

        // Assert
//        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void newId_invalidCookHoursAndMinutes_errorMessageSetToObservable() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void newId_invalidCookHoursAndMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void newId_invalidCookHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        givenNewEmptyRecipe(recipeId);

        // Act
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));

        // Assert
//        verify(repoDurationMock).save(VALID_NEW_COOK_TIME_VALID);
    }

    @Test
    public void validExistingId_prepHoursSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        String prepHours = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() / 60);

        // An external request that loads the recipe
//        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
//        handler.execute(recipeMacro, request, new RecipeMacroResponseListener());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(prepHours, SUT.getPrepHoursInView());
    }

    @Test
    public void validExistingId_prepMinutesSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        String prepMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() % 60);

        // An external request that loads the recipe
//        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
//        handler.execute(recipeMacro, request, new RecipeMacroResponseListener());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(prepMinutes, SUT.getPrepMinutesInView());
    }

    @Test
    public void validExistingId_cookHoursSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        String cookHours = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() / 60);

        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
//        handler.execute(recipeMacro, request, new RecipeMacroResponseListener());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(cookHours, SUT.getCookHoursInView());
    }

    @Test
    public void validExistingId_cookMinutesSetToObservable() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        String cookMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() % 60);

        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
//        handler.execute(recipeMacro, request, new RecipeMacroResponseListener());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        assertEquals(cookMinutes, SUT.getCookMinutesInView());
    }

    @Test
    public void validExistingId_RecipeModelStatusVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());

        // An external request that loads the recipe
//        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
//        handler.execute(recipeMacro, request, new RecipeMacroResponseListener());

        // Assert
        verifyAllOtherReposCalledAndReturnValidExisting(recipeId);
        verifyReposDurationCalledAndReturnValidExisting(recipeId);

        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_UNCHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.DURATION);
        assertEquals(expectedState, actualState);
    }


    // region helper methods -----------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
    }

    private void givenNewEmptyRecipe(String recipeId) {
        // Arrange
        // An external request that loads the recipe. This can ba any request type.
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setDomainModel(new RecipeDurationRequest.Model.Builder().
                        getDefault().
                        build()).
                build();

        durationListener = new DurationResponseListener();
        macroListener = new RecipeMacroResponseListener();
        recipeMacro.registerRecipeListener(macroListener);

        // Act
        handler.executeAsync(recipeMacro, request, durationListener);

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
//        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onDataUnavailable();
    }

    private void verifyRepoIdentityCalledAndReturnDataUnavailable(String recipeId) {
//        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataUnavailable();
    }

    private void verifyRepoCoursesCalledAndReturnDataUnavailable(String recipeId) {
//        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataUnavailable();
    }

    private void verifyRepoDurationCalledAndReturnDataUnavailable(String recipeId) {
//        verify(repoDurationMock).getByDataId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataUnavailable();
    }

    private void verifyRepoPortionsCalledAndReturnDataUnavailable(String recipeId) {
//        verify(repoPortionsMock).getAllByDomainId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataUnavailable();
    }

    private void verifyAllOtherReposCalledAndReturnValidExisting(String recipeId) {

//        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
//        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeMetadataEntity.getValidFromAnotherUser());

//        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(TestDataRecipeIdentityEntity.
                getValidExistingTitleValidDescriptionValid());

//        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAllExistingActiveByDomainId(recipeId));

//        verify(repoPortionsMock).getAllByDomainId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(TestDataRecipePortionsEntity.
                getExistingValidNinePortions());
    }

    private void verifyReposDurationCalledAndReturnValidExisting(String recipeId) {
//        verify(repoDurationMock).getByDataId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(TestDataRecipeDurationEntity.
                getValidExistingComplete());
    }

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeMetadataListener implements Recipe.RecipeMetadataListener {

        RecipeMetadataResponse response;

        @Override
        public void onRecipeMetadataChanged(RecipeMetadataResponse response) {
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
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

    private static class RecipeMacroResponseListener implements UseCaseBase.Callback<RecipeResponse> {

        private static final String TAG = "tkm-" + RecipeMacroResponseListener.class.
                getSimpleName() + ": ";

        RecipeResponse response;

        @Override
        public void onUseCaseSuccess(RecipeResponse response) {
            System.out.println(RecipeDurationEditorViewModelTest.TAG + TAG + "onSuccess:");
            this.response = response;

        }

        @Override
        public void onUseCaseError(RecipeResponse response) {
            System.out.println(RecipeDurationEditorViewModelTest.TAG + TAG + "onError:");
            this.response = response;
        }

        public RecipeResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeMacroResponseListener{" +
                    "response=" + response +
                    '}';
        }
    }

    private class DurationResponseListener implements UseCaseBase.Callback<RecipeDurationResponse> {

        private final String TAG = "tkm:" + DurationResponseListener.class.getSimpleName()
                + ": ";

        private RecipeDurationResponse onSuccessResponse;
        private RecipeDurationResponse onErrorResponse;

        @Override
        public void onUseCaseSuccess(RecipeDurationResponse response) {
            System.out.println(RecipeDurationEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            onSuccessResponse = response;
            metadata = response.getMetadata();
            model = response.getDomainModel();
        }

        @Override
        public void onUseCaseError(RecipeDurationResponse response) {
            System.out.println(RecipeDurationEditorViewModelTest.TAG + TAG + "onError:" + response);
            onErrorResponse = response;
            metadata = response.getMetadata();
            model = response.getDomainModel();
        }
    }

    // endregion helper classes --------------------------------------------------------------------
}