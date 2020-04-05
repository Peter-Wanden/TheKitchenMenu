package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeDuration extends Repository<RecipeDurationPersistenceModel> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(@Nonnull DataAccess<RecipeDurationPersistenceModel> remoteDataAccess,
                                     @Nonnull DataAccess<RecipeDurationPersistenceModel> localDataAccess) {
        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static  RepositoryRecipeDuration getInstance(
            DataAccess<RecipeDurationPersistenceModel> remoteDataAccess,
            DataAccess<RecipeDurationPersistenceModel> localDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDataAccess, localDataAccess);
        return INSTANCE;
    }
}
