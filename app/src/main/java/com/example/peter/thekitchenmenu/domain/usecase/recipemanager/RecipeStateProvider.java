package com.example.peter.thekitchenmenu.domain.usecase.recipemanager;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

/**
 * A recipe is made up of a complex set of independently changing data structures each controlled by
 * its own use case.
 * The {@link RecipeStateProvider}
 */
public class RecipeStateProvider
        extends UseCaseInteractor<RecipeManagerRequest, RecipeManagerResponse>
        implements DataSource.GetEntityCallback<RecipeEntity> {

    private static final String TAG = "tkm-" + RecipeStateProvider.class.getSimpleName() + ": ";

    public enum RecipeState {
        INVALID_MISSING_MODELS,
        INVALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_UNCHANGED,
        VALID_CHANGED
    }

    private final RepositoryRecipe repository;
    private final TimeProvider timeProvider;
    private final UniqueIdProvider idProvider;

    private String recipeId = "";

    public RecipeStateProvider(RepositoryRecipe repository,
                               TimeProvider timeProvider,
                               UniqueIdProvider idProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
    }

    @Override
    protected void execute(RecipeManagerRequest request) {
        System.out.println(TAG + request);

    }

    @Override
    public void onEntityLoaded(RecipeEntity object) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
