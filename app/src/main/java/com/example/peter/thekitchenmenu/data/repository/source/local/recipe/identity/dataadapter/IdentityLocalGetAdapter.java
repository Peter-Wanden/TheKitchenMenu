package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.*;

public class IdentityLocalGetAdapter {

    private static final String TAG = "tkm-" + IdentityLocalGetAdapter.class.getSimpleName() + ": ";

    @Nonnull
    private final RecipeIdentityLocalDataSource dataSource;
    @Nonnull
    private final IdentityModelToDatabaseEntityConverterParent converter;

    public IdentityLocalGetAdapter(@Nonnull RecipeIdentityLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new IdentityModelToDatabaseEntityConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceDomainModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIdentityEntity entity) {
                        callback.onPersistenceModelLoaded(converter.convertParentEntityToDomainModel(entity));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceDomainModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIdentityEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceDomainModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeIdentityPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIdentityPersistenceDomainModel> models) {
                        callback.onPersistenceModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    private RecipeIdentityPersistenceDomainModel filterForActiveModel(
            List<RecipeIdentityPersistenceDomainModel> models) {
        long lastUpdate = 0L;
        RecipeIdentityPersistenceDomainModel activeModel = new RecipeIdentityPersistenceDomainModel.Builder().
                getDefault().
                build();

        for (RecipeIdentityPersistenceDomainModel model : models) {
            if (model.getLastUpdate() > lastUpdate) {
                activeModel = model;
            }
        }
        return activeModel;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceDomainModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipeIdentityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIdentityEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }
}
