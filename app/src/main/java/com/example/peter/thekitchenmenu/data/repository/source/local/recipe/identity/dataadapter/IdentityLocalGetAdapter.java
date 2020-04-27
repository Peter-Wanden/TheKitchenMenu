package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.*;

public class IdentityLocalGetAdapter {

    private static final String TAG = "tkm-" + IdentityLocalGetAdapter.class.getSimpleName() + ": ";

    @Nonnull
    private final RecipeIdentityLocalDataSource dataSource;
    @Nonnull
    private final IdentityModelConverterParent converter;

    public IdentityLocalGetAdapter(@Nonnull RecipeIdentityLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new IdentityModelConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIdentityEntity entity) {
                        callback.onModelLoaded(converter.convertToModel(entity));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIdentityEntity> entities) {
                        callback.onAllLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeIdentityPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeIdentityPersistenceModel> models) {
                        callback.onModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    private RecipeIdentityPersistenceModel filterForActiveModel(
            List<RecipeIdentityPersistenceModel> models) {
        long lastUpdate = 0L;
        RecipeIdentityPersistenceModel activeModel = new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                build();

        for (RecipeIdentityPersistenceModel model : models) {
            if (model.getLastUpdate() > lastUpdate) {
                activeModel = model;
            }
        }
        return activeModel;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIdentityEntity> entities) {
                        callback.onAllLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }
}
