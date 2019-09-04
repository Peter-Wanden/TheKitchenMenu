package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.core.util.Pair;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class RecipeEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity VALID_RECIPE_ENTITY = getValidExistingRecipeEntity();
    private static final String VALID_RECIPE_ID = VALID_RECIPE_ENTITY.getId();
    private static final RecipeEntity VALID_RECIPE_ENTITY_FROM_ANOTHER_USER =
            getValidRecipeEntityFromAnotherUser();
    private static final String VALID_RECIPE_ID_FROM_ANOTHER_USER =
            VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId();
    private static final String NEW_ID = "newId";

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
    DataSource<RecipeEntity> recipeEntityDataSource;
    @Mock
    UniqueIdProvider uniqueIdProviderMock;
    @Mock
    Resources resourcesMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> getEntityCallbackArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeEditorViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        SUT = new RecipeEditorViewModel(
                timeProviderMock,
                recipeEntityDataSource,
                uniqueIdProviderMock,
                resourcesMock);

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
    public void onStart_recipeIdSupplied_titleEventCalledWithEditRecipeResourceId() throws Exception {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserveMock);
        // Act
        SUT.start(VALID_RECIPE_ID);
        // Assert
        returnValidRecipeDatabaseCall();
        verify(integerObserveMock).onChanged(R.string.activity_title_edit_recipe);
    }

    @Test
    public void onStart_recipeIdSupplied_recipeIdSetToLiveData() throws Exception {
        // Arrange
        observeRecipeIdLiveDataWithMockStringObserver();
        // Act
        SUT.start(VALID_RECIPE_ID);
        returnValidRecipeDatabaseCall();
        // Assert
        verify(stringObserverMock).onChanged(VALID_RECIPE_ID);
    }

    @Test
    public void reportIdentityEntityChanges_recipeInvalid_enableReviewButtonEventCalledIsShowReviewButtonFalse() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.reportIdentityEntityChanges(new Pair<>(false, true));
        // Assert
        verify(voidEventObserverMock).onChanged(any());
        assertFalse(SUT.isShowReviewButton());
    }

    @Test
    public void reportIdentityEntityChanges_recipeInvalid_setVisibilityShowIngredientsButtonObservableCalledWithFalse() throws Exception {
        // Arrange
        // Act
        SUT.start(VALID_RECIPE_ID);
        returnValidRecipeDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(false, true));
        // Assert
        assertFalse(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void reportIdentityEntityChanges_validRecipe_setVisibilityShowIngredientsButtonObservableCalledWithTrue() throws Exception {
        // Arrange
        // Act
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        // Assert
        assertTrue(SUT.showIngredientsButtonObservable.get());
    }

    @Test
    public void reportIdentityEntityChanges_recipeValid_enableReviewEventCalledIsShowReviewTrue() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        // Assert
        assertTrue(SUT.isShowReviewButton());
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void setRecipeModels_nothingChanged_enableReviewEventCalledIsShowReviewFalse() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.reportIdentityEntityChanges(new Pair<>(true, false));
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
        SUT.reportIdentityEntityChanges(new Pair<>(false, true));
        // Act
        SUT.upOrBackPressed();
        // Assert
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void upOrBackPressed_validRecipeChanged_showUnsavedChangesDialogEventCalled() throws Exception {
        // Arrange
        SUT.getShowUnsavedChangesDialogEvent().observeForever(voidEventObserverMock);
        SUT.start(VALID_RECIPE_ID);
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
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
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        // Act
        SUT.reviewButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewNewRecipe(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewRecipe_newRecipe_recipeSavedWithIdAndParentSameValues() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> recipeEntityCapture =
                ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(VALID_RECIPE_ENTITY.getId());
        // Act
        SUT.start();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.reviewButtonPressed();
        // Assert
        verify(recipeEntityDataSource).save(recipeEntityCapture.capture());
        RecipeEntity recipeEntity = recipeEntityCapture.getValue();
        assertEquals(recipeEntity.getId(), recipeEntity.getParentId());
    }

    @Test
    public void reviewRecipe_existingRecipeEditedByOwner_navigatorReviewEditedRecipeCalledWithExpectedId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        returnValidRecipeDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, false));
        SUT.reviewButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewEditedRecipe(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewRecipe_existingRecipeEditedByOwner_recipeSavedWithNewLastUpdatedDate() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(100L);
        // Act
        SUT.start(VALID_RECIPE_ID);
        returnValidRecipeDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.reviewButtonPressed();
        // Assert
        verify(recipeEntityDataSource).save(ac.capture());
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
        returnValidRecipeFromAnotherUserDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.reviewButtonPressed();
        // Assert
        verify(recipeEntityDataSource).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(NEW_ID, recipeEntity.getId());
        assertEquals(VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId(), recipeEntity.getParentId());
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
        returnValidRecipeDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
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
        returnValidRecipeFromAnotherUserDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        // Assert
        assertEquals(reviewIngredients, SUT.ingredientsButtonTextObservable.get());
    }

    @Test
    public void addIngredients_newRecipe_recipeSavedWithIdAndParentIdSameValues() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.ingredientsButtonPressed();
        // Assert
        verify(recipeEntityDataSource).save(ac.capture());
        assertEquals(ac.getValue().getId(), ac.getValue().getParentId());
    }

    @Test
    public void ingredientsButtonPressed_newRecipe_navigatorAddIngredientsRecipeId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.ingredientsButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).addIngredients(ac.capture());
        assertEquals(NEW_ID, ac.getValue());
    }

    @Test
    public void editIngredients_existingValidRecipe_recipeSavedWithIdAndParentIdSameValues() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        returnValidRecipeDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.ingredientsButtonPressed();
        // Assert
        verify(recipeEntityDataSource).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(recipeEntity.getId(), recipeEntity.getParentId());
    }

    @Test
    public void editIngredients_existingValidRecipe_navigatorEditIngredients() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(VALID_RECIPE_ID);
        returnValidRecipeDatabaseCall();
        // Update the entity with new valid data
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        // verify the add ingredients button is visible
        assertTrue(SUT.showIngredientsButtonObservable.get());
        // press the button
        SUT.ingredientsButtonPressed();
        // verify recipe is saved
        verify(recipeEntityDataSource).save(anyObject());
        // verify navigator is called
        verify(addEditRecipeNavigatorMock).editIngredients(ac.capture());
        assertEquals(VALID_RECIPE_ENTITY.getId(), ac.getValue());
    }

    @Test
    public void reviewIngredients_clonedRecipe_recipeSavedWithNewIdParentIdOriginal() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        returnValidRecipeFromAnotherUserDatabaseCall();
        // update the entity data
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        // save recipe
        SUT.ingredientsButtonPressed();
        // Confirm recipe cloned with new Id
        verify(recipeEntityDataSource).save(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(NEW_ID, recipeEntity.getId());
        assertEquals(VALID_RECIPE_ENTITY_FROM_ANOTHER_USER.getId(), recipeEntity.getParentId());
    }

    @Test
    public void reviewIngredients_clonedRecipe_navigatorReviewIngredientsRecipeId() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_ID);
        // Act
        SUT.start(VALID_RECIPE_ID_FROM_ANOTHER_USER);
        returnValidRecipeFromAnotherUserDatabaseCall();
        SUT.reportIdentityEntityChanges(new Pair<>(true, true));
        SUT.ingredientsButtonPressed();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewIngredients(ac.capture());
        assertEquals(NEW_ID, ac.getValue());
    }

    // TODO add test for loading indicator observable boolean
    // TODO add in tests for recipe course testing
    // TODO add tests for throwUnknownEditingModeException()

    // region for helper methods -------------------------------------------------------------------
    private void returnValidRecipeDatabaseCall() {
        // verify database called
        verify(recipeEntityDataSource).getById(eq(VALID_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate return value from database
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(VALID_RECIPE_ENTITY);
    }

    private void returnValidRecipeFromAnotherUserDatabaseCall() {
        // get database call
        verify(recipeEntityDataSource).getById(eq(
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