package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceDomainModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalSaveAdapter {

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource failReasonsDataSource;
    @Nonnull
    private final RecipeMetadataToDatabaseEntityConverter parentConverter;
    @Nonnull
    private final ComponentStateConverter componentStateConverter;
    @Nonnull
    private final UniqueIdProvider idProvider;

    public RecipeMetadataLocalSaveAdapter(
            @Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
            @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
            @Nonnull RecipeFailReasonsLocalDataSource failReasonsDataSource,
            @Nonnull UniqueIdProvider idProvider) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.failReasonsDataSource = failReasonsDataSource;
        this.idProvider = idProvider;

        parentConverter = new RecipeMetadataToDatabaseEntityConverter();
        componentStateConverter = new ComponentStateConverter();
    }

    public void save(@Nonnull RecipeMetadataPersistenceDomainModel model) {
        saveParentEntity(model);
        saveComponentStates(model);
        saveFailReasons(model);
    }

    private void saveParentEntity(RecipeMetadataPersistenceDomainModel model) {
        parentDataSource.save(parentConverter.convertParentDomainModelToEntity(model));
    }

    private void saveComponentStates(RecipeMetadataPersistenceDomainModel model) {
        List<RecipeComponentStateEntity> entities = componentStateConverter.
                convertToEntities(
                        model.getComponentStates(),
                        model.getDataId()
                );

        componentStateDataSource.save(entities.toArray(new RecipeComponentStateEntity[0]));
    }

    private void saveFailReasons(RecipeMetadataPersistenceDomainModel model) {
        List<RecipeFailReasonEntity> entityList = new ArrayList<>();

        for (FailReasons f : model.getFailReasons()) {
            String dataId = idProvider.getUId();
            String parentDataId = model.getDataId();
            int failReasonAsInt = f.getId();

            entityList.add(new RecipeFailReasonEntity(
                    dataId,
                    parentDataId,
                    failReasonAsInt
            ));
        }
        RecipeFailReasonEntity[] saveArray = new RecipeFailReasonEntity[entityList.size()];
        failReasonsDataSource.save(entityList.toArray(saveArray));
    }
}
