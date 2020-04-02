package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.DataSourceRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.DataSourceRecipeFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadata extends Repository<RecipeMetadataPersistenceModel> {

    private RepositoryRecipeMetadata() {

    }
}
