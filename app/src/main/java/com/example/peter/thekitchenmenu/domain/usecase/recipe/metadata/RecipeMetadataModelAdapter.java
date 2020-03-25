package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.DataModelAdapter;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeFailReasons;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class RecipeMetadataModelAdapter implements DataModelAdapter<RecipeMetadataPersistenceModel> {

    @Nonnull
    private final RepositoryRecipeMetadata recipeMetadataRepository;
    @Nonnull
    private final RepositoryRecipeComponentState recipeComponentStateRepository;
    @Nonnull
    private final RepositoryRecipeFailReasons recipeFailReasonsRepository;
    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    UniqueIdProvider idProvider;

    public RecipeMetadataModelAdapter(
            @Nonnull RepositoryRecipeMetadata recipeMetadataRepository,
            @Nonnull RepositoryRecipeComponentState recipeComponentStateRepository,
            @Nonnull RepositoryRecipeFailReasons recipeFailReasonsRepository,
            @Nonnull TimeProvider timeProvider,
            @Nonnull UniqueIdProvider idProvider) {
        this.recipeMetadataRepository = recipeMetadataRepository;
        this.recipeComponentStateRepository = recipeComponentStateRepository;
        this.recipeFailReasonsRepository = recipeFailReasonsRepository;
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;

        // todo         /////////////////////////////////////
        //              // Built using the Adapter Pattern //
        //              /////////////////////////////////////
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeMetadataPersistenceModel> callback) {

    }

    @Override
    public void getById(@Nonnull String id, @Nonnull GetModelCallback<RecipeMetadataPersistenceModel> callback) {

    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@Nonnull String id) {

    }

    private RecipeMetadataPersistenceModel convertPrimitiveToPersistenceModel(RecipeMetadataEntity entity) {
        return new RecipeMetadataPersistenceModel.Builder().
                setId(entity.getId()).
                setParentId(entity.getParentId()).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

//    private RecipeMetadataEntity convertPersistenceModelPrimitive(RecipeMetadataPersistenceModel model) {
//        return new RecipeMetadataEntity(
//                model.getId(),
//                model.getRecipeId(),
//                model.getParentId(),
//                model.getCreatedBy(),
//                model.getCreateDate(),
//                model.getLastUpdate()
//        );
//    }
}
