package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientPersistenceModel> {

    private RepositoryIngredient(
            @Nonnull DataAccess<IngredientPersistenceModel> remoteDataAccess,
            @Nonnull DataAccess<IngredientPersistenceModel> localDataAccess) {
        this.remoteDataAccess = remoteDataAccess;
        this.localDataAccess = localDataAccess;
    }

    public static RepositoryIngredient getInstance(
            @Nonnull DataAccess<IngredientPersistenceModel> remoteDataAccess,
            @Nonnull DataAccess<IngredientPersistenceModel> localDataAccess) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDataAccess, localDataAccess);
        return (RepositoryIngredient) INSTANCE;
    }
}
