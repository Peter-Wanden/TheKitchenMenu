package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
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
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeIdentityEditorViewModelTest {

    private static final String TAG = "tkm-" +
            RecipeIdentityEditorViewModelTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_SHORT =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShortDescriptionDefault();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooLongDescriptionDefault();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShortDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            TestDataRecipeIdentityEntity.getValidNewTitleValidDescriptionDefault();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();
    private static final RecipeIdentityEntity INVALID_EXISTING_INCOMPLETE_INVALID_TITLE =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleTooShortDefaultDescription();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipeIdentityEntity VALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getInvalidFromAnotherUser();
    private static final RecipeIdentityEntity VALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidCompleteAfterCloned();
    private static final RecipeIdentityEntity INVALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidAfterInvalidClonedData();
    private static final RecipeIdentityEntity VALID_CLONED_DESCRIPTION_UPDATED =
            TestDataRecipeIdentityEntity.getValidClonedDescriptionUpdated();

    private static final String ERROR_MESSAGE_TOO_LONG = "ERROR_MESSAGE_TOO_LONG";
    private static final String ERROR_MESSAGE_TOO_SHORT = "ERROR_MESSAGE_TOO_SHORT";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    Resources resourcesMock;
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
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private RecipeStateListener recipeClientListener;
    private RecipeStateResponse recipeStateResponse;
    private RecipeResponse recipeResponse;
    private ComponentState actualComponentState;
    private int shortTextMinLength = 3;
    private int shortTextMaxLength = 70;
    private int longTextMinLength = 0;
    private int longTextMaxLength = 500;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = givenViewModel();
    }

    private RecipeIdentityEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock()
        );
        TextValidator identityTextValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();


        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock,
                handler,
                identityTextValidator
        );

        RecipeCourse courses = new RecipeCourse(
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

        Recipe recipe = new Recipe(
                repoRecipeMock,
                handler,
                stateCalculator,
                identity,
                courses,
                duration,
                portions);

        recipeClientListener = new RecipeStateListener();
        recipe.registerRecipeStateListener(recipeClientListener);

        return new RecipeIdentityEditorViewModel(
                handler,
                recipe,
                resourcesMock);
    }

    @Test
    public void startNewRecipeId_nothingSetToObservers() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String expectedTitle = INVALID_NEW_EMPTY.getTitle();
        String expectedDescription = INVALID_NEW_EMPTY.getDescription();
        // Act
        SUT.start(recipeId);
        // Assert
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        assertEquals(expectedTitle, SUT.getTitle());
        assertEquals(expectedDescription, SUT.getDescription());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewId_stateDATA_UNAVAILABLE() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.start(recipeId);
        // Assert
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        ComponentState expectedState = ComponentState.DATA_UNAVAILABLE;
        ComponentState actualState = recipeClientListener.
                getResponse().
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewId_invalidTitleTooShort_tooShortErrorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT.getTitle();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_SHORT, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewId_invalidTitleTooLong_tooLongErrorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_LONG.getTitle();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewId_invalidTitle_stateINVALID_CHANGED() {
        // Arrange
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT.getTitle();
        String recipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeClientListener.
                getResponse().
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidTitleValidDescription_nothingSaved() {
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle();
        String validDescription = INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        SUT.setDescription(validDescription);
        // Assert
        verifyNoMoreInteractions(repoIdentityMock);
    }

    @Test
    public void startNewRecipeId_validTitle_errorMessageObservableNull() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String validTitle = VALID_NEW_TITLE_VALID.getTitle();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);
        SUT.setTitle(validTitle);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitle_titleSaved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_TITLE_VALID.getTitle();
        long time = VALID_NEW_TITLE_VALID.getCreateDate();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);
        SUT.setTitle(title);
        // Assert
        verify(repoIdentityMock).save(VALID_NEW_TITLE_VALID);
    }

    @Test
    public void startNewRecipeId_validTitle_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_TITLE_VALID.getTitle();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(title);
        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeClientListener.
                getResponse().
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_errorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String validTitle = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String invalidDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(invalidDescription);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_descriptionNotSaved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String validTitle = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String invalidDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);
        SUT.setTitle(validTitle);
        verify(repoIdentityMock).save(anyObject());
        SUT.setDescription(invalidDescription);
        verifyNoMoreInteractions(repoIdentityMock);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String validTitle = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String invalidDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(invalidDescription);
        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = recipeClientListener.
                getResponse().
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_errorMessageObservableNull() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_COMPLETE.getTitle();
        String description = VALID_NEW_COMPLETE.getDescription();
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(title);
        SUT.setDescription(description);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_saved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String validTitle = VALID_NEW_COMPLETE.getTitle();
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        // Assert
        SUT.setTitle(validTitle);
        verify(repoIdentityMock).save(eq(VALID_NEW_TITLE_VALID));
        SUT.setDescription(validDescription);
        verify(repoIdentityMock).save(eq(VALID_NEW_COMPLETE));
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String validTitle = VALID_NEW_COMPLETE.getTitle();
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        // Act
        SUT.start(recipeId);
        verifyAllComponentRepoCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(validDescription);
        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = recipeClientListener.
                getResponse().
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startValidExistingRecipeId_textValuesSetToObservables() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        String title = VALID_EXISTING_COMPLETE.getTitle();
        String description = VALID_EXISTING_COMPLETE.getDescription();
        // Act
        SUT.start(recipeId);
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
        // Assert
        assertEquals(title, SUT.getTitle());
        assertEquals(description, SUT.getDescription());
    }

    @Test
    public void startValidExistingRecipeId_stateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        // Act
        SUT.start(recipeId);
        // Assert
        verifyRepoRecipeCalledAndReturnValidExistingComplete(recipeId);
        verifyRepoIdentityCalledAndReturnValidExistingComplete(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);
        // Assert
        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = recipeClientListener.
                getResponse().
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startValidExistingRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getId();
        String title = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getTitle();
        String description = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getDescription();

        // Act
        SUT.start(recipeId);

        // Assert
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeEntity.getInvalidExisting());

        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(INVALID_EXISTING_INCOMPLETE_INVALID_TITLE);

        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        assertEquals(ERROR_MESSAGE_TOO_SHORT, SUT.titleErrorMessage.get());
    }
    // startValidExistingRecipeId_invalidTitle_notSaved

    // startValidExistingRecipeId_invalidTitle_stateINVALID_CHANGED

    // startInvalidExistingRecipeId_stateINVALID_UNCHANGED

    @Test
    public void startWithCloned_existingAndNewId_persistenceCalledWithExistingId() {
        // Arrange
        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.startByCloningModel(cloneFromRecipeId, cloneToRecipeId);
        // Assert
        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), anyObject());
    }

    @Test
    public void startWithCloned_existingAndNewId_existingCopiedAndSavedWithNewId() {
        // Arrange
        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        // Act
        SUT.startByCloningModel(cloneFromRecipeId, cloneToRecipeId);
        simulateGetValidFromAnotherUserFromDatabase();
        // Assert
        verify(repoIdentityMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_validExistingIdAndNewId_descriptionUpdatedCopiedAndSavedWithUpdatedDescription() {
        // Arrange
        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
        long time = VALID_CLONED_DESCRIPTION_UPDATED.getCreateDate();

        whenTimeProviderReturnTime(time);
        // Act
        SUT.startByCloningModel(cloneFromRecipeId, cloneToRecipeId);
        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_FROM_ANOTHER_USER);

        SUT.setDescription(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
        // Assert
        verify(repoIdentityMock).save(VALID_CLONED_DESCRIPTION_UPDATED);
    }

    @Test(expected = RuntimeException.class)
    public void start_nullRecipe_runtimeExceptionThrown() {
        // Arrange
        // Act
        SUT.start(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "Recipe id cannot be null");
    }

    // region helper methods -------------------------------------------------------------------
    private void setupResources() {
        when(resourcesMock.getString(
                eq(R.string.input_error_text_too_short),
                anyString(),
                anyString())).
                thenReturn(ERROR_MESSAGE_TOO_SHORT);
        when(resourcesMock.getString(
                eq(R.string.input_error_text_too_long),
                anyString(),
                anyString())).
                thenReturn(ERROR_MESSAGE_TOO_LONG);
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void verifyAllComponentRepoCalledAndReturnDataUnavailable(String recipeId) {
        verifyRepoRecipeCalledAndReturnDataUnavailable(recipeId);
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
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

    private void verifyRepoRecipeCalledAndReturnValidExistingComplete(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeEntity.getValidExisting());
    }
    private void verifyRepoIdentityCalledAndReturnValidExistingComplete(String recipeId) {
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidFromAnotherUserFromDatabase() {
        verify(repoIdentityMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_FROM_ANOTHER_USER);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class RecipeStateListener implements Recipe.RecipeStateListener {
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
    // endregion helper classes --------------------------------------------------------------------

}