package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientModelPersistence;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientModelPersistence> {

    private RepositoryIngredient(
            @Nonnull DataAccess<IngredientModelPersistence> remoteDataAccess,
            @Nonnull DataAccess<IngredientModelPersistence> localDataAccess) {
        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static RepositoryIngredient getInstance(
            @Nonnull DataAccess<IngredientModelPersistence> remoteDataAccess,
            @Nonnull DataAccess<IngredientModelPersistence> localDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDataAccess, localDataAccess);
        return (RepositoryIngredient) INSTANCE;
    }
}
