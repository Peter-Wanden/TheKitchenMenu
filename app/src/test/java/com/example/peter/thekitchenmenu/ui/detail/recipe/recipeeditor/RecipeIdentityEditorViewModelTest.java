package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
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
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeIdentityEditorViewModelTest {

    private static final String TAG = "tkm-" +
            RecipeIdentityEditorViewModelTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_SHORT =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShort();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooLong();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShortDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            TestDataRecipeIdentityEntity.getValidNewTitleValid();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();

    private static final RecipeIdentityEntity INVALID_EXISTING_INCOMPLETE_INVALID_TITLE =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleTooShort();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();

    private static final String ERROR_MESSAGE_TOO_LONG = "ERROR_MESSAGE_TOO_LONG";
    private static final String ERROR_MESSAGE_TOO_SHORT = "ERROR_MESSAGE_TOO_SHORT";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    Resources resourcesMock;
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
    ArgumentCaptor<PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseItemEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private UseCaseHandler handler;

    private Recipe recipeMacro;
    private RecipeMetadataListener recipeStateListener;
    private int shortTextMaxLength = 70;
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
        handler = new UseCaseHandler(new UseCaseSchedulerMock()
        );

        TextValidator identityTextValidator = new TextValidator.Builder().
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
//                identityTextValidator
//        );

        RecipeCourse courses = new RecipeCourse(
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
//                courses,
//                duration,
//                portions);

        recipeStateListener = new RecipeMetadataListener();
        recipeMacro.registerMetadataListener(recipeStateListener);

        return new RecipeIdentityEditorViewModel(
                handler,
                recipeMacro,
                resourcesMock);
    }

    @Test
    public void startNewRecipeId_requestFromAnotherRecipeMacroClient_identityResponsePushedRegisteredCallback() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String expectedTitle = INVALID_NEW_EMPTY.getTitle();
        String expectedDescription = INVALID_NEW_EMPTY.getDescription();

        // Used for listening to all recipe data as it changes
        RecipeMacroResponseListener macroResponseListener = new RecipeMacroResponseListener();
        recipeMacro.registerRecipeListener(macroResponseListener);

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        assertEquals(expectedTitle, SUT.getTitle());
        assertEquals(expectedDescription, SUT.getDescription());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewId_dataLoadingError_true() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();
        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        // Assert
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        assertTrue(SUT.isDataLoadingError.get());
    }

    @Test
    public void startNewId_invalidTitleTooShort_tooShortErrorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT.getTitle();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();
        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        // Act
        SUT.setTitle(invalidTitle);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_SHORT, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewId_invalidTitleTooLong_tooLongErrorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_LONG.getTitle();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewId_invalidTitle_stateINVALID_CHANGED() {
        // Arrange
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT.getTitle();
        String recipeId = INVALID_NEW_EMPTY.getDataId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_invalidTitleValidDescription_nothingSaved() {
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String invalidTitle = INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle();
        String validDescription = INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(invalidTitle);
        SUT.setDescription(validDescription);
        // Assert
        verifyNoMoreInteractions(repoIdentityMock);
    }

    @Test
    public void startNewRecipeId_validTitle_errorMessageObservableNull() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String validTitle = VALID_NEW_TITLE_VALID.getTitle();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitle_titleSaved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String title = VALID_NEW_TITLE_VALID.getTitle();
        long time = VALID_NEW_TITLE_VALID.getCreateDate();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(title);
        // Assert
//        verify(repoIdentityMock).save(VALID_NEW_TITLE_VALID);
    }

    @Test
    public void startNewRecipeId_validTitle_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String title = VALID_NEW_TITLE_VALID.getTitle();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(title);
        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_errorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();

        String validTitle = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String invalidDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(invalidDescription);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_descriptionNotSaved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();

        String validTitle = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String invalidDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        verify(repoIdentityMock).save(anyObject());
        SUT.setDescription(invalidDescription);
        verifyNoMoreInteractions(repoIdentityMock);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();

        String validTitle = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String invalidDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(invalidDescription);
        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_errorMessageObservableNull() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String title = VALID_NEW_COMPLETE.getTitle();
        String description = VALID_NEW_COMPLETE.getDescription();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(title);
        SUT.setDescription(description);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_saved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();

        String validTitle = VALID_NEW_COMPLETE.getTitle();
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        // Assert
        SUT.setTitle(validTitle);
//        verify(repoIdentityMock).save(eq(VALID_NEW_TITLE_VALID));
        SUT.setDescription(validDescription);
//        verify(repoIdentityMock).save(eq(VALID_NEW_COMPLETE));
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getDataId();
        String validTitle = VALID_NEW_COMPLETE.getTitle();
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(validDescription);
        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startValidExistingRecipeId_textValuesSetToObservables() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        String title = VALID_EXISTING_COMPLETE.getTitle();
        String description = VALID_EXISTING_COMPLETE.getDescription();

        // Act
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        verifyRepoRecipeCalledAndReturnValidExistingComplete(recipeId);
        verifyRepoIdentityCalledAndReturnValidExistingComplete(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        // Assert
        assertEquals(title, SUT.getTitle());
        assertEquals(description, SUT.getDescription());
    }

    @Test
    public void startValidExistingRecipeId_stateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyRepoRecipeCalledAndReturnValidExistingComplete(recipeId);
        verifyRepoIdentityCalledAndReturnValidExistingComplete(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);
        // Assert
        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_UNCHANGED;
        RecipeMetadata.ComponentState actualState = recipeStateListener.
                getResponse().
                getDomainModel().
                getComponentStates().
                get(RecipeMetadata.ComponentName.IDENTITY);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void startValidExistingRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getDataId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
//        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
//        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeMetadataEntity.getInvalidExisting());

//        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(INVALID_EXISTING_INCOMPLETE_INVALID_TITLE);

        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);

        assertEquals(ERROR_MESSAGE_TOO_SHORT, SUT.titleErrorMessage.get());
    }
    // startValidExistingRecipeId_invalidTitle_notSaved

    // startValidExistingRecipeId_invalidTitle_stateINVALID_CHANGED

    // startInvalidExistingRecipeId_stateINVALID_UNCHANGED

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

    private void verifyAllComponentReposCalledAndReturnDataUnavailable(String recipeId) {
        verifyRepoRecipeCalledAndReturnDataUnavailable(recipeId);
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
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

    private void verifyRepoRecipeCalledAndReturnValidExistingComplete(String recipeId) {
//        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
//        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeMetadataEntity.getValidExisting());
    }

    private void verifyRepoIdentityCalledAndReturnValidExistingComplete(String recipeId) {
//        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
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

    private static class RecipeResponseCallback implements UseCaseBase.Callback<RecipeMetadataResponse> {

        private static final String TAG = "tkm-" + RecipeResponseCallback.class.getSimpleName() +
                ": ";

        private RecipeMetadataResponse response;

        @Override
        public void onUseCaseSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeIdentityEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeMetadataResponse response) {
            System.out.println(RecipeIdentityEditorViewModelTest.TAG + TAG + "onError:" + response);
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
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

    private static class RecipeMacroResponseListener implements UseCaseBase.Callback<RecipeResponse> {

        private static final String TAG = "tkm-" + RecipeMacroResponseListener.class.
                getSimpleName() + ": ";

        RecipeResponse response;

        @Override
        public void onUseCaseSuccess(RecipeResponse response) {
            System.out.println(RecipeIdentityEditorViewModelTest.TAG + TAG + "onSuccess:");
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeResponse response) {
            System.out.println(RecipeIdentityEditorViewModelTest.TAG + TAG + "onError:");
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
    // endregion helper classes --------------------------------------------------------------------

}