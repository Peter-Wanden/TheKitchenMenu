package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;

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
            @Nonnull GetDomainModelCallback<RecipeIdentityUseCasePersistenceModel> callback) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityUseCasePersistenceModel> callback) {
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
            @Nonnull GetDomainModelCallback<RecipeIdentityUseCasePersistenceModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeIdentityUseCasePersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIdentityUseCasePersistenceModel> models) {
                        callback.onPersistenceModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    private RecipeIdentityUseCasePersistenceModel filterForActiveModel(
            List<RecipeIdentityUseCasePersistenceModel> models) {
        long lastUpdate = 0L;
        RecipeIdentityUseCasePersistenceModel activeModel = new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                build();

        for (RecipeIdentityUseCasePersistenceModel model : models) {
            if (model.getLastUpdate() > lastUpdate) {
                activeModel = model;
            }
        }
        return activeModel;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityUseCasePersistenceModel> callback) {
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
