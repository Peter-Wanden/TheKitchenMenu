package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeDuration extends Repository<RecipeDurationPersistenceModel> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(@Nonnull DataSource<RecipeDurationPersistenceModel> remoteDataSource,
                                     @Nonnull DataSource<RecipeDurationPersistenceModel> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static  RepositoryRecipeDuration getInstance(
            DataSource<RecipeDurationPersistenceModel> remoteDataSource,
            DataSource<RecipeDurationPersistenceModel> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
