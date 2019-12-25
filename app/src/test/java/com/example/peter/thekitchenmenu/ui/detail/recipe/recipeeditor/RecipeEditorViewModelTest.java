package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity VALID_RECIPE_ENTITY = getValidExisting();
    private static final String VALID_RECIPE_ID = VALID_RECIPE_ENTITY.getId();
    private static final RecipeEntity VALID_RECIPE_ENTITY_FROM_ANOTHER_USER =
            getValidFromAnotherUser();
    private static final String VALID_RECIPE_ID_FROM_ANOTHER_USER =
            VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId();

    private static final RecipeEntity NEW_EMPTY_DRAFT_RECIPE_ENTITY = getNewInvalid();
    private static final String NEW_ID = getNewInvalid().getId();
    private static final RecipeEntity INVALID_DRAFT_RECIPE_ENTITY = getInvalidExisting();
    private static final String INVALID_DRAFT_RECIPE_ID = INVALID_DRAFT_RECIPE_ENTITY.getId();

    private static final long CURRENT_TIME = 10L;

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Observer<Integer> integerObserveMock;
    @Mock
    Observer<Void> voidEventObserverMock;
    @Mock
    AddEditRecipeNavigator navigatorMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RepositoryRecipe recipeEntityDataSourceMock;
    @Mock
    UniqueIdProvider uniqueIdProviderMock;
    @Mock
    Resources resourcesMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    RecipeValidator recipeValidatorMock;
    @Mock
    RecipeModelObserver recipeModelObserverMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        SUT = new RecipeEditorViewModel(
                timeProviderMock,
                recipeEntityDataSourceMock,
                uniqueIdProviderMock,
                resourcesMock,
                recipeValidatorMock);

        SUT.setNavigator(navigatorMock);
        SUT.setRecipeModelComposite(recipeModelObserverMock);
    }

    @Test
    public void onStart_noRecipeIdSupplied_titleEventCalledWithAddNewRecipeResourceId() {
        // Arrange
        // Act
        SUT.start();
        // Assert
        verify(navigatorMock).setActivityTitle(R.string.activity_title_add_new_recipe);
    }

    @Test
    public void onStart_noRecipeIdSupplied_startRecipeComponentsNewRecipeId() {
        // Arrange
        returnNewIdFromUniqueIdProviderMock();
        // Act
        SUT.start();
        // Assert
        verify(recipeModelObserverMock).start(eq(NEW_ID));
    }

    @Test
    public void onStart_noRecipeId_newRecipeSavedToDatabaseAsDraft() {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(CURRENT_TIME);
        // Act
        SUT.start();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity newEmptyDraftRecipeEntity = ac.getValue();
        assertEquals(NEW_EMPTY_DRAFT_RECIPE_ENTITY, newEmptyDraftRecipeEntity);
    }

    @Test
    public void onStart_recipeIdSupplied_titleEventCalledWithEditRecipeResourceId() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        // Assert
        simulateReturnValidRecipeDatabaseCall();
        verify(navigatorMock).setActivityTitle(R.string.activity_title_edit_recipe);
    }

    @Test
    public void onStart_recipeIdSupplied_startRecipeComponentsValidRecipeId() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        // Assert
        verify(recipeModelObserverMock).start(eq(VALID_RECIPE_ID));
    }

    @Test
    public void setRecipeValidationStatus_recipeInvalid_enableReviewButtonEventCalledIsShowReviewButtonFalse() {
        // Arrange
        // Act
        SUT.start();
        SUT.setValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        verify(navigatorMock).refreshOptionsMenu();
        assertFalse(SUT.isShowReviewButton());
    }

    @Test
    public void setRecipeValidationStatus_recipeInvalid_setVisibilityShowIngredientsButtonObservableCalledWithFalse() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        assertFalse(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void setRecipeValidationStatus_validRecipe_setVisibilityShowIngredientsButtonObservableCalledWithTrue() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_CHANGED);
        // Assert
        assertTrue(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void setRecipeValidationStatus_recipeValid_enableReviewEventCalledIsShowReviewTrue() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_CHANGED);
        // Assert
        assertTrue(SUT.isShowReviewButton());
        verify(navigatorMock).refreshOptionsMenu();
    }

    @Test
    public void setRecipeValidationStatus_validNothingChanged_enableReviewEventCalledIsShowReviewFalse() {
        // Arrange
        // Act
        SUT.setValidationStatus(VALID_UNCHANGED);
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
        SUT.start(INVALID_DRAFT_RECIPE_ID);
        returnInvalidDraftRecipeFromDatabaseCall();
        SUT.setValidationStatus(INVALID_CHANGED);
        SUT.upOrBackPressed();
        // Assert
        verify(navigatorMock).showUnsavedChangedDialog();
    }

    @Test
    public void reviewRecipe_newRecipe_navigatorReviewNewRecipeCalledWithRecipeExpectedId() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(VALID_RECIPE_ENTITY.getId());
        SUT.start();
        SUT.setValidationStatus(VALID_CHANGED);
        // Act
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(navigatorMock).reviewNewRecipe(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewRecipe_existingRecipeEditedByOwner_navigatorReviewEditedRecipeCalledWithExpectedId() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_CHANGED);
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(navigatorMock).reviewEditedRecipe(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void getIngredientButtonText_newRecipe_addIngredients() {
        String addIngredients = "add ingredients";
        when(resourcesMock.getString(R.string.add_ingredients)).thenReturn(addIngredients);
        // Arrange
        // Act
        SUT.start();
        // Assert
        assertEquals(addIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void getIngredientButtonText_existingRecipe_editIngredients() {
        // Arrange
        String editIngredients = "edit ingredients";
        when(resourcesMock.getString(R.string.edit_ingredients)).thenReturn(editIngredients);
        // Act
        SUT.start(VALID_RECIPE_ID);
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
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // Assert
        assertEquals(reviewIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void ingredientsButtonPressed_newRecipe_navigatorAddIngredientsRecipeId() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        SUT.setValidationStatus(VALID_CHANGED);
        assertTrue(SUT.showIngredientsButtonObservable.get()); // Verify button is shown
        SUT.ingredientsButtonPressed();
        // Assert
        verify(navigatorMock).addIngredients(ac.capture());
        assertEquals(NEW_ID, ac.getValue());
    }

    @Test
    public void editIngredients_existingValidRecipe_navigatorEditIngredients() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_CHANGED);
        // verify the add ingredients button is visible
        assertTrue(SUT.showIngredientsButtonObservable.get());
        // press the button
        SUT.ingredientsButtonPressed();
        // verify navigator is called
        verify(navigatorMock).editIngredients(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewIngredients_clonedRecipe_navigatorReviewIngredientsRecipeId() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        SUT.setValidationStatus(VALID_UNCHANGED);
        assertTrue(SUT.showIngredientsButtonObservable.get()); // ensure ingredients button is visible
        SUT.ingredientsButtonPressed();
        // Assert
        verify(navigatorMock).reviewIngredients(ac.capture());
        assertEquals(NEW_ID, ac.getValue());
    }

    @Test
    public void onStart_validRecipeLoading_loadingIndicatorShownThenOffWhenLoaded() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        verify(recipeEntityDataSourceMock).getById(eq(VALID_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // Assert
        assertTrue(SUT.dataIsLoadingObservable.get());
        // Act
        // simulate return value from database
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(VALID_RECIPE_ENTITY);
        assertFalse(SUT.dataIsLoadingObservable.get());
    }

    @Test
    public void onStart_newRecipeIdSupplied_loadingIndicatorShownThenOffWhenDataNotAvailable() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        verify(recipeEntityDataSourceMock).getById(eq(VALID_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // Assert
        assertTrue(SUT.dataIsLoadingObservable.get());
        // Act
        // simulate onDataNotAvailable return value from database
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
        assertFalse(SUT.dataIsLoadingObservable.get());
    }

    @Test
    public void onStart_noRecipeIdSupplied_recipeSavedAsDraft_recipeIdAndParentIdSame() {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        assertEquals(ac.getValue().getId(), ac.getValue().getParentId());
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsRecipeOwner_recipeIsNotSaved() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_UNCHANGED);
        // Assert
        verifyNoMoreInteractions(recipeEntityDataSourceMock);
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsNotOwner_recipeClonedAndSaved_recipeIdAndParentIdNotSame() {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(NEW_ID, recipeEntity.getId());
        assertEquals(VALID_RECIPE_ID_FROM_ANOTHER_USER, recipeEntity.getParentId());
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsNotOwner_recipeModelsStartedInClonedMode() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // Assert
        verify(recipeModelObserverMock).startWithClonedModel(eq(VALID_RECIPE_ID_FROM_ANOTHER_USER),
                eq(NEW_ID));
    }

    @Test
    public void recipeValidationStatusINVALID_MISSING_MODELS_recipeIsSavedAsDraft() {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        SUT.setValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        verify(recipeEntityDataSourceMock, times(2)).save(ac.capture());
        RecipeEntity recipeEntity = ac.getAllValues().get(1);
        assertTrue(recipeEntity.isDraft());
    }

    @Test
    public void recipeValidationStatusINVALID_NO_CHANGES_recipeIsNotSaved() {
        // Arrange
        // Act
        SUT.start(INVALID_DRAFT_RECIPE_ID);
        returnInvalidDraftRecipeFromDatabaseCall();
        SUT.setValidationStatus(INVALID_UNCHANGED);
        // Assert
        verifyNoMoreInteractions(recipeEntityDataSourceMock);
    }

    @Test
    public void recipeValidationStatusINVALID_HAS_CHANGES_recipeIsSavedAsDraft() {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(INVALID_CHANGED);
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertTrue(recipeEntity.isDraft());
    }

    @Test
    public void recipeValidationStatusVALID_NO_CHANGES_recipeIsNotSaved() {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_UNCHANGED);
        // Assert
        verifyNoMoreInteractions(recipeEntityDataSourceMock);
    }

    @Test
    public void recipeValidationStatusVALID_HAS_CHANGES_recipeIsSavedDraftFlagFalse() {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setValidationStatus(VALID_CHANGED);
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertFalse(recipeEntity.isDraft());
    }

    // region for helper methods -------------------------------------------------------------------
    private void simulateReturnValidRecipeDatabaseCall() {
        // verify database called
        verify(recipeEntityDataSourceMock).getById(eq(VALID_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate return value from database
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(VALID_RECIPE_ENTITY);
    }

    private void simulateReturnValidRecipeFromAnotherUserDatabaseCall() {
        // get database call
        verify(recipeEntityDataSourceMock).getById(eq(
                VALID_RECIPE_ID_FROM_ANOTHER_USER),
                getEntityCallbackArgumentCaptor.capture());
        // return database call
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_RECIPE_ENTITY_FROM_ANOTHER_USER);
    }

    private void returnNewIdFromUniqueIdProviderMock() {
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
    }

    private void returnInvalidDraftRecipeFromDatabaseCall() {
        verify(recipeEntityDataSourceMock).getById(eq(
                INVALID_DRAFT_RECIPE_ENTITY.getId()), getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(INVALID_DRAFT_RECIPE_ENTITY);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------

}