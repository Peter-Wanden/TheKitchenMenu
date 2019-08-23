package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.provider.TimeProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class RecipeEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity RECIPE_ENTITY = getValidExistingRecipeEntity();
    private static final RecipeEntity EMPTY_RECIPE_ENTITY = RecipeEditorViewModel.EMPTY_RECIPE;
    private static final RecipeEntity RECIPE_ENTITY_UPDATED_DATA = getRecipeEntityWithUpdatedData();
    private static final RecipeIdentityModelMetaData INVALID_IDENTITY_MODEL_METADATA =
            getInvalidRecipeIdentityModelMetaData();
    private static final RecipeIdentityModelMetaData VALID_IDENTITY_MODEL_METADATA_EXISTING_DATA =
            getValidExistingRecipeIdentityModelMetadataData();
    private static final RecipeIdentityModelMetaData VALID_IDENTITY_MODEL_METADATA_UPDATED_DATA =
            getValidRecipeIdentityModelMetadataUpdatedData();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Observer<RecipeEntity> recipeEntityObserverMock;
    @Mock
    Observer<Integer> integerObserveMock;
    @Mock
    Observer<Void> voidEventObserverMock;
    @Mock
    AddEditRecipeNavigator addEditRecipeNavigatorMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock Application applicationMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeEditorViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeEditorViewModel(applicationMock, timeProviderMock);
        SUT.setNavigator(addEditRecipeNavigatorMock);
    }

    @Test
    public void onStart_noRecipeSupplied_titleEventCalledWithAddNewRecipeResourceId() throws Exception {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserveMock);
        // Act
        SUT.start();
        // Assert
        verify(integerObserveMock).onChanged(R.string.activity_title_add_new_recipe);
    }

    @Test
    public void onStart_recipeSupplied_titleEventCalledWithEditRecipeResourceId() throws Exception {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserveMock);
        // Act
        SUT.start(RECIPE_ENTITY);
        // Assert
        verify(integerObserveMock).onChanged(R.string.activity_title_edit_recipe);
    }

    @Test
    public void onStart_noRecipeSupplied_emptyRecipeSetToLiveData() throws Exception {
        // Arrange
        SUT.getRecipeEntity().observeForever(recipeEntityObserverMock);
        // Act
        SUT.start();
        // Assert
        verify(recipeEntityObserverMock).onChanged(EMPTY_RECIPE_ENTITY);
    }

    @Test
    public void onStart_recipeSupplied_recipeSetToLiveData() throws Exception {
        // Arrange
        SUT.getRecipeEntity().observeForever(recipeEntityObserverMock);
        // Act
        SUT.start(RECIPE_ENTITY);
        // Assert
        verify(recipeEntityObserverMock).onChanged(RECIPE_ENTITY);
    }

    @Test
    public void setRecipeIdentityModel_recipeInvalid_enableReviewEventCalledIsShowReviewFalse() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.setRecipeIdentityModel(INVALID_IDENTITY_MODEL_METADATA);
        // Assert
        verify(voidEventObserverMock).onChanged(any());
        assertFalse(SUT.isShowReviewButton());
    }

    @Test
    public void setRecipeIdentityModel_recipeValid_enableReviewEventCalledIsShowReviewTrue() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.setRecipeIdentityModel(VALID_IDENTITY_MODEL_METADATA_UPDATED_DATA);
        // Assert
        assertTrue(SUT.isShowReviewButton());
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void setRecipeModels_nothingChanged_enableReviewEventCalledIsShowReviewFalse() throws Exception {
        // Arrange
        SUT.getEnableReviewButtonEvent().observeForever(voidEventObserverMock);
        // Act
        SUT.setRecipeIdentityModel(VALID_IDENTITY_MODEL_METADATA_EXISTING_DATA);
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
        SUT.start(RECIPE_ENTITY);
        SUT.setRecipeIdentityModel(INVALID_IDENTITY_MODEL_METADATA);
        // Act
        SUT.upOrBackPressed();
        // Assert
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void upOrBackPressed_validRecipeChanged_showUnsavedChangesDialogEventCalled() throws Exception {
        // Arrange
        SUT.getShowUnsavedChangesDialogEvent().observeForever(voidEventObserverMock);
        SUT.start(RECIPE_ENTITY);
        SUT.setRecipeIdentityModel(VALID_IDENTITY_MODEL_METADATA_UPDATED_DATA);
        // Act
        SUT.upOrBackPressed();
        // Assert
        verify(voidEventObserverMock).onChanged(any());
    }

    @Test
    public void createOrUpdateRecipe_newRecipe_navigatorReviewNewRecipeCalledWithRecipeEntity() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(10L);
        SUT.start();
        SUT.setRecipeIdentityModel(VALID_IDENTITY_MODEL_METADATA_EXISTING_DATA);
        // Act
        SUT.createOrUpdateRecipe();
        // Assert
        verify(addEditRecipeNavigatorMock).reviewNewRecipe(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        // Can't think how to mock static UUID, so check all other values
        assertEquals(RECIPE_ENTITY.getTitle(), recipeEntity.getTitle());
        assertEquals(RECIPE_ENTITY.getDescription(), recipeEntity.getDescription());
        assertEquals(RECIPE_ENTITY.getPreparationTime(), recipeEntity.getPreparationTime());
        assertEquals(RECIPE_ENTITY.getCookingTime(), recipeEntity.getCookingTime());
        assertEquals(RECIPE_ENTITY.getCreatedBy(), recipeEntity.getCreatedBy());
        assertEquals(RECIPE_ENTITY.getLastUpdate(), recipeEntity.getLastUpdate());
        assertEquals(RECIPE_ENTITY.getCreateDate(), recipeEntity.getCreateDate());
    }

    @Test
    public void createOrUpdateRecipe_updatedRecipe_navigatorReviewEditedRecipeCalledWithRecipeEntity() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeEntity> ac = ArgumentCaptor.forClass(RecipeEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(20L);
        SUT.start(RECIPE_ENTITY);
        SUT.setRecipeIdentityModel(VALID_IDENTITY_MODEL_METADATA_UPDATED_DATA);
        // Act
        SUT.createOrUpdateRecipe();
        // Assert
        verify(addEditRecipeNavigatorMock).updateExistingRecipe(ac.capture());
        RecipeEntity recipeEntity = ac.getValue();
        assertEquals(RECIPE_ENTITY_UPDATED_DATA, recipeEntity);
    }
    // region for helper methods -------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------

}