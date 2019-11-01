package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.model.RecipeListItemModel;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

// Creates and returns lists of recipe list item models based on a filter
public class RecipeListDataInteractor {

    public interface RecipeListCallback {
        void recipeList(List<RecipeListItemModel> recipeList);
        void dataLoadingFailed(FailReason reason);
    }

    enum RecipeFilter {
        ALL,
        FAVORITE
    }

    enum FailReason {
        NO_DATA_AVAILABLE,
        DATA_LOADING_ERROR
    }

    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;

    private List<RecipeListCallback> listeners = new ArrayList<>();
    private LinkedHashMap<String, RecipeIdentityEntity> recipeIdentities = new LinkedHashMap<>();
    private LinkedHashMap<String, RecipeDurationEntity> recipeDurations = new LinkedHashMap<>();
    private List<RecipeListItemModel> recipeListItemModels = new ArrayList<>();

    public RecipeListDataInteractor(RepositoryRecipeIdentity recipeIdentityRepository,
                                    RepositoryRecipeDuration recipeDurationRepository) {
        this.recipeIdentityRepository = recipeIdentityRepository;
        this.recipeDurationRepository = recipeDurationRepository;
    }

    void registerListener(RecipeListCallback listener) {
        listeners.add(listener);
    }

    void unregisterListener(RecipeListCallback listener) {
        listeners.remove(listener);
    }

    void getRecipes(RecipeFilter filter) {
        if (filter == RecipeFilter.ALL)
            getAllRecipes();
        else if (filter == RecipeFilter.FAVORITE)
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
                    RecipeListDataInteractor.this.recipeIdentities.put(entity.getId(), entity);
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
                    RecipeListDataInteractor.this.recipeDurations.put(entity.getId(), entity);
                }
                mergeLists();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void getFavorites() {
        // TODO
    }

    private void returnEmptyList() {
        for (RecipeListCallback callback : listeners) {
            callback.recipeList(recipeListItemModels);
        }
    }

    private void mergeLists() {
        recipeIdentities.forEach((recipeId, recipeIdentity) -> {
            RecipeDurationEntity durationEntity = recipeDurations.get(recipeId);
            RecipeListItemModel model = new RecipeListItemModel(
                    recipeId,
                    recipeIdentity.getTitle(),
                    durationEntity.getPrepTime(),
                    durationEntity.getCookTime()
            );
            recipeListItemModels.add(model);
        });
        returnResults();
    }

    private void returnResults() {
        for (RecipeListCallback callback : listeners)
            callback.recipeList(recipeListItemModels);
    }
}
