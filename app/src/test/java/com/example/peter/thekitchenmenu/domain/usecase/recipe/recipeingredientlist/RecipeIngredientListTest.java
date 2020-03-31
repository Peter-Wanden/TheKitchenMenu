package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientListTest {

    // region constants ----------------------------------------------------------------------------
    private final String RECIPE_ID = "RECIPE_ID";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeIngredient repoRecipeIngredientMock;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Mock
    RepositoryIngredient repoIngredientMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientList SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new RecipeIngredientList(repoRecipeIngredientMock,
                repoIngredientMock,
                repoPortionsMock);
    }

    @Test
    public void recipeIdSupplied_dataLayerCalled() {
        // Arrange

        // Act
        // Assert
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCase.Callback<RecipeIngredientListResponse> responseCallback() {
        return new UseCase.Callback<RecipeIngredientListResponse>() {
            @Override
            public void onSuccess(RecipeIngredientListResponse response) {

            }

            @Override
            public void onError(RecipeIngredientListResponse response) {

            }
        };
    }

    private PrimitiveDataSource.GetAllCallback<RecipeIngredientEntity> quantityCallback() {
        return new PrimitiveDataSource.GetAllCallback<RecipeIngredientEntity>() {
            @Override
            public void onAllLoaded(List<RecipeIngredientEntity> entities) {

            }

            @Override
            public void onDataUnavailable() {

            }
        };
    }

    private List<RecipeIngredientEntity> getRecipeIngredients() {
        RecipeIngredientEntity quantity1 = new RecipeIngredientEntity(
                "id1",
                RECIPE_ID,
                "INGREDIENT_ID_1",
                "",
                100,
                0,
                Constants.getUserId(),
                10,
                10
        );

        RecipeIngredientEntity quantity2 = new RecipeIngredientEntity(
                "id2",
                RECIPE_ID,
                "INGREDIENT_ID_2",
                "",
                150,
                1,
                Constants.getUserId(),
                20,
                20
        );

        RecipeIngredientEntity quantity3 = new RecipeIngredientEntity(
                "id1",
                RECIPE_ID,
                "INGREDIENT_ID_3",
                "",
                100,
                2,
                Constants.getUserId(),
                30,
                30
        );

        RecipeIngredientEntity quantity4 = new RecipeIngredientEntity(
                "id1",
                RECIPE_ID,
                "INGREDIENT_ID_4",
                "",
                100,
                3,
                Constants.getUserId(),
                40,
                40
        );

        List<RecipeIngredientEntity> list = new ArrayList<>();
        list.add(quantity1);
        list.add(quantity2);
        list.add(quantity3);
        list.add(quantity4);

        return list;

    }

    private List<IngredientEntity> getIngredients() {
        IngredientEntity ingredient1 = new IngredientEntity(
                "INGREDIENT_ID_1",
                "NAME_1",
                "DESC_1",
                1,
                Constants.getUserId(),
                10,
                20
        );

        IngredientEntity ingredient2 = new IngredientEntity(
                "INGREDIENT_ID_2",
                "NAME_2",
                "DESC_2",
                1.1,
                Constants.getUserId(),
                20,
                30
        );

        IngredientEntity ingredient3 = new IngredientEntity(
                "INGREDIENT_ID_3",
                "NAME_3",
                "DESC_3",
                1.2,
                Constants.getUserId(),
                40,
                50
        );

        IngredientEntity ingredient4 = new IngredientEntity(
                "INGREDIENT_ID_4",
                "NAME_4",
                "DESC_4",
                1.5,
                Constants.getUserId(),
                10,
                20
        );

        List<IngredientEntity> list = new ArrayList<>();
        list.add(ingredient1);
        list.add(ingredient2);
        list.add(ingredient3);
        list.add(ingredient4);

        return list;
    }

    private RecipePortionsEntity getPortionsEntity() {
        return new RecipePortionsEntity("PORTIONS_ID", RECIPE_ID, 4, 4,
                10, 20);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}