package com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UseCaseRecipeIdentityAndDurationList extends
        UseCaseInteractor<UseCaseRecipeIdentityAndDurationListRequestModel,
                        UseCaseRecipeIdentityAndDurationListResponseModel> {

    public enum RecipeFilter {
        ALL,
        FAVORITE
    }

    public enum ResultStatus {
        DATA_LOADING_ERROR,
        DATA_NOT_AVAILABLE,
        RESULT_OK
    }
    private final RepositoryRecipeIdentity recipeIdentityRepository;

    private final RepositoryRecipeDuration recipeDurationRepository;
    private LinkedHashMap<String, RecipeIdentityEntity> recipeIdentities = new LinkedHashMap<>();

    private LinkedHashMap<String, RecipeDurationEntity> recipeDurations = new LinkedHashMap<>();
    private List<RecipeListItemModel> recipeListItemModels = new ArrayList<>();

    public UseCaseRecipeIdentityAndDurationList(RepositoryRecipeIdentity identityRepository,
                                                RepositoryRecipeDuration durationRepository) {
        this.recipeIdentityRepository = identityRepository;
        this.recipeDurationRepository = durationRepository;
    }

    @Override
    protected void execute(UseCaseRecipeIdentityAndDurationListRequestModel request) {
        if (request.getFilter() == RecipeFilter.ALL) {
            getAllRecipes();
        } else if (request.getFilter() == RecipeFilter.FAVORITE)
            getFavorites();
    }

    private void getAllRecipes() {
        getRecipeIdentityEntities();
    }

    private void getRecipeIdentityEntities() {
        recipeIdentityRepository.getAll(new DataSource.GetAllCallback<RecipeIdentityEntity>() {
            @Override
            public void onAllLoaded(List<RecipeIdentityEntity> entities) {
                for (RecipeIdentityEntity entity : entities) {
                    UseCaseRecipeIdentityAndDurationList.this.recipeIdentities.put(
                            entity.getId(), entity);
                }
                loadDurationEntities();
            }

            @Override
            public void onDataNotAvailable() {
                returnEmptyList();
            }
        });
    }

    private void loadDurationEntities() {
        recipeDurationRepository.getAll(new DataSource.GetAllCallback<RecipeDurationEntity>() {
            @Override
            public void onAllLoaded(List<RecipeDurationEntity> entities) {
                for (RecipeDurationEntity entity : entities) {
                    UseCaseRecipeIdentityAndDurationList.this.recipeDurations.put(
                            entity.getId(), entity);
                }
                mergeLists();
            }

            @Override
            public void onDataNotAvailable() {
                returnEmptyList();
            }
        });
    }

    private void getFavorites() {
        // TODO
    }

    private void returnEmptyList() {
        UseCaseRecipeIdentityAndDurationListResponseModel model = new
                UseCaseRecipeIdentityAndDurationListResponseModel(
                        ResultStatus.DATA_NOT_AVAILABLE, recipeListItemModels
        );
        getUseCaseCallback().onError(model);
    }

    private void mergeLists() {
        recipeIdentities.forEach((recipeId, recipeIdentity) -> {
            RecipeDurationEntity durationEntity = recipeDurations.get(recipeId);
            RecipeListItemModel model = new RecipeListItemModel(
                    recipeId,
                    recipeIdentity.getTitle(),
                    recipeIdentity.getDescription(),
                    durationEntity.getPrepTime(),
                    durationEntity.getCookTime(),
                    (durationEntity.getPrepTime() + durationEntity.getCookTime())
            );
            recipeListItemModels.add(model);
        });
        returnResults();
    }

    private void returnResults() {
        UseCaseRecipeIdentityAndDurationListResponseModel model = new
                UseCaseRecipeIdentityAndDurationListResponseModel(
                        ResultStatus.RESULT_OK, recipeListItemModels
        );
        getUseCaseCallback().onSuccess(model);
    }
}
