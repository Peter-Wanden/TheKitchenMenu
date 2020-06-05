package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeTestBase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeListTestRecipeHelper {

    // region constants ----------------------------------------------------------------------------
//    private static final String TAG = "tkm-" + RecipeListTestRecipeHelper.class.getSimpleName() +
//            ": ";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private List<RecipeMetadataPersistenceModel> metadataModels;

    private List<RecipeTestBase> recipeTestBases;
    // endregion helper fields ---------------------------------------------------------------------

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeTestBases = new ArrayList<>();
        metadataModels = new ArrayList<>();
    }

    void createRecipeMocksForMetadataModels(List<RecipeMetadataPersistenceModel> metadataModels) {
        this.metadataModels = metadataModels;
        this.metadataModels.forEach((metadataModel) -> {
            RecipeTestBase recipeTestBase = new RecipeTestBase();
            recipeTestBase.setUp();
            recipeTestBases.add(recipeTestBase);
        });
    }

    List<Recipe> getRecipeList() {
        List<Recipe> recipes = new ArrayList<>();
        recipeTestBases.forEach((recipeTestBase -> recipes.add(recipeTestBase.getRecipe())));
        return recipes;
    }

    @Test
    public void requestRecipeComponentsLoadData() {
        for (int i = 0; i < recipeTestBases.size() -1; i++) {

            RecipeTestBase recipeTestBase = recipeTestBases.get(i);
            RecipeMetadataPersistenceModel metadataModel = metadataModels.get(i);
            String recipeDomainId = metadataModel.getDomainId();

            // RecipeMetadata
            verify(recipeTestBase.repoMetadataMock).getActiveByDomainId(eq(recipeDomainId),
                    recipeTestBase.repoMetadataCallback.capture());
            recipeTestBase.repoMetadataCallback.getValue().onDomainModelLoaded(
                    TestDataRecipeMetadata.getActiveByDomainId(recipeDomainId));

            // RecipeIdentity
            verify(recipeTestBase.repoIdentityMock).getActiveByDomainId(eq(recipeDomainId),
                    recipeTestBase.repoIdentityCallback.capture());
            recipeTestBase.repoIdentityCallback.getValue().onDomainModelLoaded(
                    TestDataRecipeIdentity.getActiveByDomainId(recipeDomainId));

            // RecipeCourse
            verify(recipeTestBase.repoCourseMock).getAllActiveByDomainId(eq(recipeDomainId),
                    recipeTestBase.repoCourseCallback.capture());
            recipeTestBase.repoCourseCallback.getValue().onAllDomainModelsLoaded(new ArrayList<>(
                    TestDataRecipeCourse.getAllExistingActiveByDomainId(recipeDomainId)));

            // RecipeDuration
            verify(recipeTestBase.repoDurationMock).getActiveByDomainId(eq(recipeDomainId),
                    recipeTestBase.repoDurationCallback.capture());
            recipeTestBase.repoDurationCallback.getValue().onDomainModelLoaded(
                    TestDataRecipeDuration.getActiveByDomainId(recipeDomainId));

            // RecipePortions
            verify(recipeTestBase.repoPortionsMock).getActiveByDomainId(eq(recipeDomainId),
                    recipeTestBase.repoPortionsCallback.capture());
            recipeTestBase.repoPortionsCallback.getValue().onDomainModelLoaded(
                    TestDataRecipePortions.getActiveByDomainId(recipeDomainId)
            );
        }
    }
}
