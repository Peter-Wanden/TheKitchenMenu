package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeIdentityEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipePortionsEntityTestData;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeIngredientEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private final RecipeIdentityEntity EXISTING_VALID_IDENTITY_MODEL =
            RecipeIdentityEntityTestData.getValidExistingComplete();
    private final RecipePortionsEntity EXISTING_VALID_PORTIONS_MODEL =
            RecipePortionsEntityTestData.getExistingValid();
    private final IngredientEntity EXISTING_VALID_INGREDIENT =
            IngredientEntityTestData.getExistingValidNameValidDescription();

    private final RecipeIngredientEntity EXISTING_VALID_RECIPE_INGREDIENT =
            new RecipeIngredientEntity(
                    "existingId",
                    RecipeEntityTestData.getValidExisting().getId(),
                    IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
                    "validProductId",
                    1234,
                    0
            );
    private final RecipeIngredientEntity NEW_EMPTY_RECIPE_INGREDIENT =
            new RecipeIngredientEntity(
                    "newRecipeIngredientId",
                    RecipeEntityTestData.getValidNew().getId(),
                    IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
                    "",
                    0,
                    0
            );
    private final RecipeIngredientEntity EXISTING_INVALID_RECIPE_INGREDIENT =
            new RecipeIngredientEntity(
                    "existingInvalidRecipeId",
                    RecipeEntityTestData.getInvalidExisting().getId(),
                    IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
                    "",
                    10001,
                    2
            );

    private final List<RecipeIngredientEntity> RECIPE_INGREDIENT_LIST = getIngredientEntities();

    private List<RecipeIngredientEntity> getIngredientEntities() {
        List<RecipeIngredientEntity> ingredientEntities = new ArrayList<>();
        ingredientEntities.add(EXISTING_VALID_RECIPE_INGREDIENT);
        ingredientEntities.add(NEW_EMPTY_RECIPE_INGREDIENT);
        ingredientEntities.add(EXISTING_INVALID_RECIPE_INGREDIENT);
        return ingredientEntities;
    }

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    RepositoryRecipeIdentity repositoryRecipeIdentityMock;
    @Mock
    RepositoryIngredient repositoryIngredientMock;
    @Mock
    RepositoryRecipeIngredient repositoryRecipeIngredientMock;
    @Mock
    RepositoryRecipePortions repositoryRecipePortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>>
            identityCallbackCaptor;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            portionsCallbackCaptor;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>>
            ingredientCallbackCaptor;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeIngredientEntity>>
            recipeIngredientsCallbackCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeIngredientEditorViewModel(
                repositoryRecipeIdentityMock,
                repositoryRecipePortionsMock,
                repositoryIngredientMock,
                repositoryRecipeIngredientMock
        );
    }

    // startExistingValidRecipeIdNewIngredientId_

    @Test
    public void startExistingValidRecipeIdExistingValidIngredientId_recipeIdentityModelDisplayed() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID_IDENTITY_MODEL.getId(), EXISTING_VALID_INGREDIENT.getId());
        simulateValidRecipeIdentityReturnedFromDatabase();
        // Assert
        assertEquals(EXISTING_VALID_IDENTITY_MODEL.getTitle(), SUT.recipeTitleObservable.get());
    }

    @Test
    public void startExistingValidRecipeIdExistingValidIngredientId_recipePortionsModelDisplayed() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID_IDENTITY_MODEL.getId(), EXISTING_VALID_INGREDIENT.getId());
        simulateValidPortionsReturnedFromDatabase();
        // Assert
        assertEquals(String.valueOf(
                EXISTING_VALID_PORTIONS_MODEL.getServings()), SUT.servingsObservable.get());
        assertEquals(String.valueOf(
                EXISTING_VALID_PORTIONS_MODEL.getSittings()), SUT.sittingsObservable.get());
        assertEquals(String.valueOf(
                EXISTING_VALID_PORTIONS_MODEL.getServings() * EXISTING_VALID_PORTIONS_MODEL.getSittings()),
                SUT.portionsObservable.get());
    }

    @Test
    public void startExistingValidRecipeIdExistingValidIngredientId_ingredientModelDisplayed() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID_IDENTITY_MODEL.getId(), EXISTING_VALID_INGREDIENT.getId());
        simulateValidIngredientReturnedFromDatabase();
        // Assert
        assertEquals(EXISTING_VALID_INGREDIENT.getName(), SUT.ingredientObservable.get());
    }

    @Test
    public void startExistingValidRecipeIdExistingValidIngredientId_recipeIngredientModelDisplayed() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID_IDENTITY_MODEL.getId(), EXISTING_VALID_INGREDIENT.getId());
        simulateAllRecipeIngredientsReturnedFromDatabase();
        // Assert
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateValidRecipeIdentityReturnedFromDatabase() {
        verify(repositoryRecipeIdentityMock).getById(
                eq(EXISTING_VALID_IDENTITY_MODEL.getId()),
                identityCallbackCaptor.capture());
        identityCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_IDENTITY_MODEL);
    }

    private void simulateValidPortionsReturnedFromDatabase() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(EXISTING_VALID_IDENTITY_MODEL.getId()),
                portionsCallbackCaptor.capture());
        portionsCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_PORTIONS_MODEL);
    }

    private void simulateValidIngredientReturnedFromDatabase() {
        verify(repositoryIngredientMock).getById(
                eq(EXISTING_VALID_INGREDIENT.getId()),
                ingredientCallbackCaptor.capture());
        ingredientCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_INGREDIENT);
    }

    private void simulateAllRecipeIngredientsReturnedFromDatabase() {
        verify(repositoryRecipeIngredientMock).getByRecipeId(
                eq(EXISTING_VALID_RECIPE_INGREDIENT.getId()),
                recipeIngredientsCallbackCaptor.capture());
        recipeIngredientsCallbackCaptor.getValue().onAllLoaded(RECIPE_INGREDIENT_LIST);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}