package com.example.peter.thekitchenmenu.data.repository.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientPersistenceModel> {

    private RepositoryIngredient(
            @Nonnull DataSource<IngredientPersistenceModel> remoteDataSource,
            @Nonnull DataSource<IngredientPersistenceModel> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryIngredient getInstance(
            @Nonnull DataSource<IngredientPersistenceModel> remoteDataSource,
            @Nonnull DataSource<IngredientPersistenceModel> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDataSource, localDataSource);
        return (RepositoryIngredient) INSTANCE;
    }
}
