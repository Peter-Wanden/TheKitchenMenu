package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity VALID_RECIPE_ENTITY = getValidExistingRecipeEntity();
    private static final String VALID_RECIPE_ID = VALID_RECIPE_ENTITY.getId();
    private static final RecipeEntity VALID_RECIPE_ENTITY_FROM_ANOTHER_USER =
            getValidRecipeEntityFromAnotherUser();
    private static final String VALID_RECIPE_ID_FROM_ANOTHER_USER =
            VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId();

    private static final RecipeEntity NEW_EMPTY_DRAFT_RECIPE_ENTITY = getNewEmptyDraftRecipeEntity();
    private static final String NEW_ID = getNewEmptyDraftRecipeEntity().getId();

    private static final long CURRENT_TIME = 10L;

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Observer<String> stringObserverMock;
    @Mock
    Observer<Integer> integerObserveMock;
    @Mock
    Observer<Void> voidEventObserverMock;
    @Mock
    AddEditRecipeNavigator addEditRecipeNavigatorMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    DataSource<RecipeEntity> recipeEntityDataSourceMock;
    @Mock
    UniqueIdProvider uniqueIdProviderMock;
    @Mock
    Resources resourcesMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    RecipeValidator recipeValidatorMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeEditorViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        SUT = new RecipeEditorViewModel(
                timeProviderMock,
                recipeEntityDataSourceMock,
                uniqueIdProviderMock,
                resourcesMock,
                recipeValidatorMock);

        SUT.setNavigator(addEditRecipeNavigatorMock);
    }

    @Test
    public void onStart_noRecipeIdSupplied_titleEventCalledWithAddNewRecipeResourceId() throws Exception {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserveMock);
        // Act
        SUT.start();
        // Assert
        verify(integerObserveMock).onChanged(R.string.activity_title_add_new_recipe);
    }

    @Test
    public void onStart_noRecipeIdSupplied_newRecipeIdSetToObservedLiveData() throws Exception {
        // Arrange
        ArgumentCaptor<String> recipeIdSentToLiveData = ArgumentCaptor.forClass(String.class);
        returnNewIdFromUniqueIdProviderMock();
        observeRecipeIdLiveDataWithMockStringObserver();
        // Act
        SUT.start();
        // Assert
        verify(stringObserverMock).onChanged(recipeIdSentToLiveData.capture());
        assertEquals(NEW_ID, recipeIdSentToLiveData.getValue());
    }

    @Test
    public void onStart_noRecipeId_newRecipeSavedToDatabaseAsDraft() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(CURRENT_TIME);
        // Act
        SUT.start();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity newEmptyDraftRecipeEntity = ac.getValue();
        assertEquals(NEW_EMPTY_DRAFT_RECIPE_ENTITY, newEmptyDraftRecipeEntity);
    }

    @Test
    public void onStart_recipeIdSupplied_titleEventCalledWithEditRecipeResourceId() throws Exception {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserveMock);
        // Act
        SUT.start(VALID_RECIPE_ID);
        // Assert
        simulateReturnValidRecipeDatabaseCall();
        verify(integerObserveMock).onChanged(R.string.activity_title_edit_recipe);
    }

    @Test
    public void onStart_recipeIdSupplied_recipeIdSetToLiveData() throws Exception {
        // Arrange
        observeRecipeIdLiveDataWithMockStringObserver();
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        // Assert
        verify(stringObserverMock).onChanged(VALID_RECIPE_ID);
    }

    @Test
    public void setRecipeValidationStatus_recipeInvalid_enableReviewButtonEventCalledIsShowReviewButtonFalse() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.setRecipeValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        verify(voidEventObserverMock).onChanged(any());
        assertFalse(SUT.isShowReviewButton());
    }

    @Test
    public void reportRecipeModelStatus_recipeInvalid_setVisibilityShowIngredientsButtonObservableCalledWithFalse() throws Exception {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setRecipeValidationStatus(INVALID_MISSING_MODELS);
        // Assert
        assertFalse(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void reportRecipeModelStatus_validRecipe_setVisibilityShowIngredientsButtonObservableCalledWithTrue() throws Exception {
        // Arrange
        // Act
        SUT.setRecipeValidationStatus(VALID_HAS_CHANGES);
        // Assert
        assertTrue(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void reportRecipeModelStatus_recipeValid_enableReviewEventCalledIsShowReviewTrue() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.setRecipeValidationStatus(VALID_HAS_CHANGES);
        // Assert
        assertTrue(SUT.isShowReviewButton());
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void setRecipeModels_nothingChanged_enableReviewEventCalledIsShowReviewFalse() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.setRecipeValidationStatus(VALID_NO_CHANGES);
        // Assert
        assertFalse(SUT.isShowReviewButton());
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void onActivityDestroyed_invalidateNavigator_navigatorEqualsNull() throws Exception {
        // Arrange
        // Act
        SUT.onActivityDestroyed();
        // Assert
        verifyNoMoreInteractions(addEditRecipeNavigatorMock);
    }

    @Test
    public void upOrBackPressed_recipeInvalid_navigatorCancelEditingCalled() throws Exception {
        // Arrange
        // Act
        SUT.upOrBackPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).cancelEditing();
    }

    @Test
    public void upOrBackPressed_invalidRecipeChanged_showUnsavedChangesDialogEventCalled() throws Exception {
        // Arrange
        SUT.getShowUnsavedChangesDialogEvent().observeForever(voidEventObserverMock);
        SUT.start(VALID_RECIPE_ID);
        SUT.setRecipeValidationStatus(INVALID_HAS_CHANGES);
        // Act
        SUT.upOrBackPressed();
        // Assert
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void reviewRecipe_newRecipe_navigatorReviewNewRecipeCalledWithRecipeExpectedId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(VALID_RECIPE_ENTITY.getId());
        SUT.start();
        SUT.setRecipeValidationStatus(VALID_HAS_CHANGES);
        // Act
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewNewRecipe(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewRecipe_existingRecipeEditedByOwner_navigatorReviewEditedRecipeCalledWithExpectedId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setRecipeValidationStatus(VALID_HAS_CHANGES);
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewEditedRecipe(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void getIngredientButtonText_newRecipe_addIngredients() throws Exception {
        String addIngredients = "add ingredients";
        when(resourcesMock.getString(R.string.add_ingredients)).thenReturn(addIngredients);
        // Arrange
        // Act
        SUT.start();
        // Assert
        assertEquals(addIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void getIngredientButtonText_existingRecipe_editIngredients() throws Exception {
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
    public void getIngredientButtonText_clonedRecipe_reviewIngredients() throws Exception {
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
    public void ingredientsButtonPressed_newRecipe_navigatorAddIngredientsRecipeId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        SUT.ingredientsButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).addIngredients(ac.capture());
        assertEquals(NEW_ID, ac.getValue());
    }

    @Test
    public void editIngredients_existingValidRecipe_navigatorEditIngredients() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setRecipeValidationStatus(VALID_HAS_CHANGES);
        // verify the add ingredients button is visible
        assertTrue(SUT.showIngredientsButtonObservable.get());
        // press the button
        SUT.ingredientsButtonPressed();
        // verify navigator is called
        verify(addEditRecipeNavigatorMock).editIngredients(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewIngredients_clonedRecipe_navigatorReviewIngredientsRecipeId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        SUT.setRecipeValidationStatus(VALID_NO_CHANGES);
        assertTrue(SUT.showIngredientsButtonObservable.get()); // ensure ingredients button is visible
        SUT.ingredientsButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewIngredients(ac.capture());
        assertEquals(NEW_ID, ac.getValue());
    }

    @Test
    public void onStart_validRecipeLoading_loadingIndicatorShownThenOffWhenLoaded() throws Exception {
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
    public void onStart_newRecipeIdSupplied_loadingIndicatorShownThenOffWhenDataNotAvailable() throws Exception {
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
    public void onStart_noRecipeIdSupplied_recipeSavedAsDraft_recipeIdAndParentIdSame() throws Exception {
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
    public void onStart_recipeIdSuppliedEditorIsRecipeOwner_recipeIsNotSaved() throws Exception {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.setRecipeValidationStatus(VALID_NO_CHANGES);
        // Assert
        verifyNoMoreInteractions(recipeEntityDataSourceMock);
    }

    @Test
    public void onStart_recipeIdSuppliedEditorIsNotOwner_recipeIsClonedAndSaved_recipeIdAndParentIdNotSame() throws Exception {
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

    // recipeValidationStatusINVALID_MISSING_MODELS_recipeIsSavedAsDraft
    // recipeValidationStatusINVALID_NO_CHANGES_recipeIsNotSaved
    // recipeValidationStatusINVALID_HAS_CHANGES_recipeIsSavedAsDraft
    // recipeValidationStatusVALID_NO_CHANGES_recipeIsNotSaved
    // recipeValidationStatusVALID_HAS_CHANGES_recipeIsSavedDraftFlagFalse

    @Test
    public void addIngredients_newRecipe_recipeSavedWithIdAndParentIdSameValues() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        assertTrue(SUT.showIngredientsButtonObservable.get());
        SUT.ingredientsButtonPressed();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        assertEquals(ac.getValue().getId(), ac.getValue().getParentId());
    }

    @Test
    public void reviewRecipe_newRecipe_recipeSavedWithIdAndParentSameValues() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> recipeEntityCapture =
                ArgumentCaptor.forClass(RecipeEntity.class);
        // Act
        SUT.start();
        assertTrue(SUT.isShowReviewButton()); // ensure review button is visible
        SUT.reviewButtonPressed();
        // Assert
        verify(recipeEntityDataSourceMock).save(recipeEntityCapture.capture());
        RecipeEntity recipeEntity = recipeEntityCapture.getValue();
        assertEquals(recipeEntity.getId(), recipeEntity.getParentId());
    }

    @Test
    public void reviewRecipe_existingRecipeEditedByOwner_recipeSavedWithNewLastUpdatedDate() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(100L);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        SUT.reviewButtonPressed();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(100L, recipeEntity.getLastUpdate());
    }

    @Test
    public void reviewRecipe_existingRecipeCloned_newRecipeIdParentIdFromOriginal() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        SUT.reviewButtonPressed();
        // Assert
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(NEW_ID, recipeEntity.getId());
        assertEquals(VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId(), recipeEntity.getParentId());
    }

    @Test
    public void editIngredients_existingValidRecipe_recipeSavedWithIdAndParentIdSameValues() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        simulateReturnValidRecipeDatabaseCall();
        // verify ingredients button visible
        assertTrue(SUT.showIngredientsButtonObservable.get());
        SUT.ingredientsButtonPressed();
        // Assert
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(recipeEntity.getId(), recipeEntity.getParentId());
    }

    @Test
    public void reviewIngredients_clonedRecipe_recipeSavedWithNewIdParentIdOriginal() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        simulateReturnValidRecipeFromAnotherUserDatabaseCall();
        // update the entity data
        // save recipe
        SUT.ingredientsButtonPressed();
        // Confirm recipe cloned with new Id
        verify(recipeEntityDataSourceMock).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(NEW_ID, recipeEntity.getId());
        assertEquals(VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId(), recipeEntity.getParentId());
    }

    // TODO add tests for throwUnknownEditingModeException()
    // TODO add tests for validator return values
    // TODO recipe editor always saves itself in the database regardless of its models states.
    //  A save is triggered:
    //  - 1. As soon as a recipe id is determined
    //  - 2. Every time a new recipe validation status is received.
    //  A new field shall be introduced called draft. A recipe is saved as a draft if its
    //  validation status is anything other than VALID_NO_CHANGES or VALID_HAS_CHANGES


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

    private void observeRecipeIdLiveDataWithMockStringObserver() {
        SUT.getRecipeIdLiveData().observeForever(stringObserverMock);
    }

    private void returnNewIdFromUniqueIdProviderMock() {
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------

}