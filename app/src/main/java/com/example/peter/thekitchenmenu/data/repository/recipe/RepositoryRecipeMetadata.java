package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.DataSourceRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.DataSourceRecipeFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadata extends Repository<RecipeMetadataPersistenceModel> {


    private final DataSourceRecipeMetaData remoteMetadataSource;
    private final DataSourceRecipeMetaData localMetadataSource;
    private final DataSourceRecipeComponentState remoteComponentStateDataSource;
    private final DataSourceRecipeComponentState localComponentStateDataSource;
    private final DataSourceRecipeFailReason remoteFailReasonsDataSource;
    private final DataSourceRecipeFailReason localFailReasonsDataSource;

    private RepositoryRecipeMetadata(
            @Nonnull DataSourceRecipeMetaData remoteMetadataSource,
            @Nonnull DataSourceRecipeMetaData localMetadataSource,
            @Nonnull DataSourceRecipeComponentState remoteComponentStateDataSource,
            @Nonnull DataSourceRecipeComponentState localComponentStateDataSource,
            @Nonnull DataSourceRecipeFailReason remoteFailReasonsDataSource,
            @Nonnull DataSourceRecipeFailReason localFailReasonsDataSource) {

    }
}
