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
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
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

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe.CREATE_NEW_RECIPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeEditorViewModelTest {

    private static final String TAG = "tag-" + RecipeEditorViewModelTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
//    private static final RecipeMetadataParentEntity NEW_EMPTY_RECIPE_ENTITY = null;
//    getNewInvalidParent();
//    private static final RecipeMetadataParentEntity INVALID_DRAFT_RECIPE_ENTITY = null;
//    getInvalidExisting();
//    private static final String INVALID_DRAFT_RECIPE_ID = INVALID_DRAFT_RECIPE_ENTITY.getDataId();

//    private static final RecipeMetadataParentEntity VALID_RECIPE_ENTITY = null;
//    getValidExisting();
//    private static final String VALID_RECIPE_ID = VALID_RECIPE_ENTITY.getDataId();
//    private static final RecipeMetadataParentEntity VALID_RECIPE_ENTITY_FROM_ANOTHER_USER = null;
//            getValidFromAnotherUser();
//    private static final String VALID_RECIPE_ID_FROM_ANOTHER_USER =
//            VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getDataId();

    private static final long CURRENT_TIME = 10L;

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
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    @Mock
    UseCaseFactory useCaseFactoryMock;
    @Mock
    AddEditRecipeNavigator navigatorMock;
    @Mock
    Resources resourcesMock;

    private Recipe recipeMacro;
    private RecipeMacroResponseListener macroResponseListener;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        SUT = givenViewModel();
        SUT.setNavigator(navigatorMock);
    }

    private RecipeEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

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
        macroResponseListener = new RecipeMacroResponseListener();

//        recipeMacro = new Recipe(
//                handler,
//                stateCalculator,
//                recipeMetadata,
//                identity,
//                course,
//                duration,
//                portions);

        recipeMacro.registerRecipeCallback(macroResponseListener);

//        return new RecipeEditorViewModel(
//                handler,
//                recipeMacro,
//                idProviderMock,
//                resourcesMock);

        return null;
    }

    @Test
    public void onStart_CREATE_NEW_RECIPE_titleEventCalledWithAddNewRecipeResourceId() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
//        whenIdProviderReturnId(recipeId);
        // Act
        SUT.start(CREATE_NEW_RECIPE);
//        verifyAllComponentReposCalledAndReturnDataUnavailable(recipeId);
        // Assert
        verify(navigatorMock).setActivityTitle(R.string.activity_title_add_new_recipe);
    }

    @Test
    public void onStart_CREATE_NEW_RECIPE_startRecipeComponentsNewRecipeId() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
//        whenIdProviderReturnId(recipeId);
        // Act
        SUT.start(CREATE_NEW_RECIPE);
        // Assert
//        verify(recipeModelObserverMock).start(eq(NEW_ID));
    }

    @Test
    public void onStart_CREATE_NEW_RECIPE_newRecipeSavedToDatabaseAsDraft() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
//        whenIdProviderReturnId(recipeId);
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(CURRENT_TIME);
        // Act
        SUT.start(CREATE_NEW_RECIPE);
        // Assert
//        verify(repoRecipeMock).save(eq(NEW_EMPTY_RECIPE_ENTITY));
    }

    @Test
    public void onStart_recipeIdSupplied_titleEventCalledWithEditRecipeResourceId() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        // Assert
        simulateReturnValidRecipeDatabaseCall();
        verify(navigatorMock).setActivityTitle(R.string.activity_title_edit_recipe);
    }

    @Test
    public void onStart_recipeIdSupplied_startRecipeComponentsValidRecipeId() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        // Assert
//        verify(recipeModelObserverMock).start(eq(VALID_RECIPE_ID));
    }

    @Test
    public void setRecipeValidationStatus_recipeInvalid_enableReviewButtonEventCalledIsShowReviewButtonFalse() {
        // Arrange
        // Act
//        SUT.start();
//        SUT.setValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        verify(navigatorMock).refreshOptionsMenu();
        assertFalse(SUT.isShowReviewButton());
    }

    @Test
    public void setRecipeValidationStatus_recipeInvalid_setVisibilityShowIngredientsButtonObservableCalledWithFalse() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        assertFalse(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void setRecipeValidationStatus_validRecipe_setVisibilityShowIngredientsButtonObservableCalledWithTrue() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_CHANGED);
        // Assert
        assertTrue(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void setRecipeValidationStatus_recipeValid_enableReviewEventCalledIsShowReviewTrue() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_CHANGED);
        // Assert
        assertTrue(SUT.isShowReviewButton());
        verify(navigatorMock).refreshOptionsMenu();
    }

    @Test
    public void setRecipeValidationStatus_validNothingChanged_enableReviewEventCalledIsShowReviewFalse() {
        // Arrange
        // Act
//        SUT.setValidationStatus(VALID_UNCHANGED);
        // Assert
        assertFalse(SUT.isShowReviewButton());
        verify(navigatorMock).refreshOptionsMenu();
    }

    @Test
    public void onActivityDestroyed_invalidateNavigator_navigatorEqualsNull() {
        // Arrange
        // Act
        SUT.onActivityDestroyed();
        // Assert
        verifyNoMoreInteractions(navigatorMock);
    }

    @Test
    public void upOrBackPressed_recipeInvalid_navigatorCancelEditingCalled() {
        // Arrange
        // Act
        SUT.upOrBackPressed();
        // Assert
        verify(navigatorMock).cancelEditing();
    }

    @Test
    public void upOrBackPressed_invalidRecipeChanged_showUnsavedChangesDialogEventCalled() {
        // Arrange
        // Act
//        SUT.start(INVALID_DRAFT_RECIPE_ID);
        returnInvalidDraftRecipeFromDatabaseCall();
//        SUT.setValidationStatus(INVALID_CHANGED);
        SUT.upOrBackPressed();
        // Assert
        verify(navigatorMock).showUnsavedChangedDialog();
    }

    @Test
    public void reviewRecipe_newRecipe_navigatorReviewNewRecipeCalledWithRecipeExpectedId() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
//        when(idProviderMock.getUId()).thenReturn(VALID_RECIPE_ENTITY.getDataId());
//        SUT.start();
//        SUT.setValidationStatus(VALID_CHANGED);
        // Act
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(navigatorMock).reviewNewRecipe(ac.capture());
//        assertEquals(VALID_RECIPE_ENTITY.getDataId(), ac.getValue());
    }

    @Test
    public void reviewRecipe_existingRecipeEditedByOwner_navigatorReviewEditedRecipeCalledWithExpectedId() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_CHANGED);
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(navigatorMock).reviewEditedRecipe(ac.capture());
//        assertEquals(VALID_RECIPE_ENTITY.getDataId(), ac.getValue());
    }

    @Test
    public void getIngredientButtonText_newRecipe_addIngredients() {
        String addIngredients = "add ingredients";
        when(resourcesMock.getString(R.string.add_ingredients)).thenReturn(addIngredients);
        // Arrange
        // Act
//        SUT.start();
        // Assert
        assertEquals(addIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void getIngredientButtonText_existingRecipe_editIngredients() {
        // Arrange
        String editIngredients = "edit ingredients";
        when(resourcesMock.getString(R.string.edit_ingredients)).thenReturn(editIngredients);
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        // Assert
        assertEquals(editIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void getIngredientButtonText_clonedRecipe_reviewIngredients() {
        // Arrange
        String reviewIngredients = "review ingredients";
        when(resourcesMock.getString(R.string.review_ingredients)).thenReturn(reviewIngredients);
        // Act
//        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // Assert
        assertEquals(reviewIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void ingredientsButtonPressed_newRecipe_navigatorAddIngredientsRecipeId() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
//        when(idProviderMock.getUId()).thenReturn(recipeId);
        // Act
//        SUT.start();
//        SUT.setValidationStatus(VALID_CHANGED);
        assertTrue(SUT.showIngredientsButtonObservable.get()); // Verify button is shown
        SUT.ingredientsButtonPressed();
        // Assert
        verify(navigatorMock).addIngredients(ac.capture());
//        assertEquals(recipeId, ac.getValue());
    }

    @Test
    public void editIngredients_existingValidRecipe_navigatorEditIngredients() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_CHANGED);
        // verify the add ingredients button is visible
        assertTrue(SUT.showIngredientsButtonObservable.get());
        // press the button
        SUT.ingredientsButtonPressed();
        // verify navigator is called
        verify(navigatorMock).editIngredients(ac.capture());
//        assertEquals(VALID_RECIPE_ENTITY.getDataId(), ac.getValue());
    }

    @Test
    public void reviewIngredients_clonedRecipe_navigatorReviewIngredientsRecipeId() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
//        when(idProviderMock.getUId()).thenReturn(recipeId);
        // Act
//        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
//        SUT.setValidationStatus(VALID_UNCHANGED);
        assertTrue(SUT.showIngredientsButtonObservable.get()); // ensure ingredients button is visible
        SUT.ingredientsButtonPressed();
        // Assert
        verify(navigatorMock).reviewIngredients(ac.capture());
//        assertEquals(recipeId, ac.getValue());
    }

    @Test
    public void onStart_validRecipeLoading_loadingIndicatorShownThenOffWhenLoaded() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
//        verify(repoRecipeMock).getById(eq(VALID_RECIPE_ID),
//                repoRecipeCallback.capture());
        // Assert
        assertTrue(SUT.dataIsLoadingObservable.get());
        // Act
        // simulate return value from database
//        repoRecipeCallback.getValue().onEntityLoaded(VALID_RECIPE_ENTITY);
        assertFalse(SUT.dataIsLoadingObservable.get());
    }

    @Test
    public void onStart_newRecipeIdSupplied_loadingIndicatorShownThenOffWhenDataNotAvailable() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
//        verify(repoRecipeMock).getById(eq(VALID_RECIPE_ID),
//                repoRecipeCallback.capture());
        // Assert
        assertTrue(SUT.dataIsLoadingObservable.get());
        // Act
        // simulate onDataNotAvailable return value from database
        repoRecipeCallback.getValue().onDataUnavailable();
        assertFalse(SUT.dataIsLoadingObservable.get());
    }

    @Test
    public void onStart_noRecipeIdSupplied_recipeSavedAsDraft_recipeIdAndParentIdSame() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
        ArgumentCaptor<RecipeMetadataParentEntity> ac = ArgumentCaptor.forClass(RecipeMetadataParentEntity.class);
//        when(idProviderMock.getUId()).thenReturn(recipeId);
        // Act
//        SUT.start();
        // Assert
//        verify(repoRecipeMock).save(ac.capture());
        assertEquals(ac.getValue().getDataId(), ac.getValue().getParentDomainId());
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsRecipeOwner_recipeIsNotSaved() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_UNCHANGED);
        // Assert
//        verifyNoMoreInteractions(repoRecipeMock);
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsNotOwner_recipeClonedAndSaved_recipeIdAndParentIdNotSame() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
        ArgumentCaptor<RecipeMetadataParentEntity> ac = ArgumentCaptor.forClass(RecipeMetadataParentEntity.class);
//        when(idProviderMock.getUId()).thenReturn(recipeId);
        // Act
//        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // Assert
//        verify(repoRecipeMock).save(ac.capture());
        RecipeMetadataParentEntity recipeMetadataEntity = ac.getValue();
//        assertEquals(recipeId, recipeMetadataEntity.getDataId());
//        assertEquals(VALID_RECIPE_ID_FROM_ANOTHER_USER, recipeMetadataEntity.getParentDomainId());
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsNotOwner_recipeModelsStartedInClonedMode() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
//        when(idProviderMock.getUId()).thenReturn(recipeId);
        // Act
//        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // Assert
//        verify(recipeModelObserverMock).startWithClonedModel(eq(VALID_RECIPE_ID_FROM_ANOTHER_USER),
//                eq(NEW_ID));
    }

    @Test
    public void recipeValidationStatusINVALID_MISSING_MODELS_recipeIsSavedAsDraft() {
        // Arrange
//        String recipeId = NEW_EMPTY_RECIPE_ENTITY.getDataId();
        ArgumentCaptor<RecipeMetadataParentEntity> ac = ArgumentCaptor.forClass(RecipeMetadataParentEntity.class);
//        when(idProviderMock.getUId()).thenReturn(recipeId);
        // Act
//        SUT.start();
//        SUT.setValidationStatus(INVALID_MISSING_MODELS);
        // Assert
//        verify(repoRecipeMock, times(2)).save(ac.capture());

    }

    @Test
    public void recipeValidationStatusINVALID_NO_CHANGES_recipeIsNotSaved() {
        // Arrange
        // Act
//        SUT.start(INVALID_DRAFT_RECIPE_ID);
        returnInvalidDraftRecipeFromDatabaseCall();
//        SUT.setValidationStatus(INVALID_UNCHANGED);
        // Assert
//        verifyNoMoreInteractions(repoRecipeMock);
    }

    @Test
    public void recipeValidationStatusINVALID_HAS_CHANGES_recipeIsSavedAsDraft() {
        // Arrange
        ArgumentCaptor<RecipeMetadataParentEntity> ac = ArgumentCaptor.forClass(RecipeMetadataParentEntity.class);
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(INVALID_CHANGED);
        // Assert
//        verify(repoRecipeMock).save(ac.capture());

    }

    @Test
    public void recipeValidationStatusVALID_NO_CHANGES_recipeIsNotSaved() {
        // Arrange
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_UNCHANGED);
        // Assert
//        verifyNoMoreInteractions(repoRecipeMock);
    }

    @Test
    public void recipeValidationStatusVALID_HAS_CHANGES_recipeIsSavedDraftFlagFalse() {
        // Arrange
        ArgumentCaptor<RecipeMetadataParentEntity> ac = ArgumentCaptor.forClass(RecipeMetadataParentEntity.class);
        // Act
//        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
//        SUT.setValidationStatus(VALID_CHANGED);
        // Assert
//        verify(repoRecipeMock).save(ac.capture());

    }

    // region for helper methods -------------------------------------------------------------------
    private void simulateReturnValidRecipeDatabaseCall() {
        // verify database called
//        verify(repoRecipeMock).getById(eq(VALID_RECIPE_ID),
//                repoRecipeCallback.capture());
        // simulate return value from database
//        repoRecipeCallback.getValue().onEntityLoaded(VALID_RECIPE_ENTITY);
    }

    private void simulateReturnValidRecipeFromAnotherUserDatabaseCall() {
        // get database call
//        verify(repoRecipeMock).getById(eq(
//                VALID_RECIPE_ID_FROM_ANOTHER_USER),
//                repoRecipeCallback.capture());
        // return database call
//        repoRecipeCallback.getValue().onEntityLoaded(
//                VALID_RECIPE_ENTITY_FROM_ANOTHER_USER);
    }

    private void whenIdProviderReturnId(String recipeId) {
        when(idProviderMock.getUId()).thenReturn(recipeId);
    }

    private void returnInvalidDraftRecipeFromDatabaseCall() {
//        verify(repoRecipeMock).getById(eq(
//                INVALID_DRAFT_RECIPE_ENTITY.getDataId()), repoRecipeCallback.capture());
//        repoRecipeCallback.getValue().onEntityLoaded(INVALID_DRAFT_RECIPE_ENTITY);
    }

    private void verifyAllComponentReposCalledAndReturnDataUnavailable(String recipeId) {
        verifyRepoRecipeCalledAndReturnDataUnavailable(recipeId);
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);
    }

    private void verifyRepoRecipeCalledAndReturnDataUnavailable(String recipeId) {
//        verify(repoRecipeMock).getById(eq(recipeId),
//                repoRecipeCallback.capture());
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

    private static class RecipeResponseCallback implements UseCase.Callback<RecipeMetadataResponse> {

        private static final String TAG = "tkm-" + RecipeResponseCallback.class.getSimpleName() +
                ": ";

        private RecipeMetadataResponse response;

        @Override
        public void onSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeMetadataResponse response) {
            System.out.println(RecipeEditorViewModelTest.TAG + TAG + "onError:" + response);
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

    private static class RecipeMacroResponseListener implements UseCase.Callback<RecipeResponse> {

        private static final String TAG = "tkm-" + RecipeMacroResponseListener.class.
                getSimpleName() + ": ";

        RecipeResponse response;

        @Override
        public void onSuccess(RecipeResponse response) {
            System.out.println(RecipeEditorViewModelTest.TAG + TAG + "onSuccess:");
            this.response = response;
        }

        @Override
        public void onError(RecipeResponse response) {
            System.out.println(RecipeEditorViewModelTest.TAG + TAG + "onError:");
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