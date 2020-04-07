package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationModelPersistence;

import javax.annotation.Nonnull;

public class RepositoryRecipeDuration extends Repository<RecipeDurationModelPersistence> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(@Nonnull DataAccess<RecipeDurationModelPersistence> remoteDataAccess,
                                     @Nonnull DataAccess<RecipeDurationModelPersistence> localDataAccess) {
        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static  RepositoryRecipeDuration getInstance(
            DataAccess<RecipeDurationModelPersistence> remoteDataAccess,
            DataAccess<RecipeDurationModelPersistence> localDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDataAccess, localDataAccess);
        return INSTANCE;
    }
}
