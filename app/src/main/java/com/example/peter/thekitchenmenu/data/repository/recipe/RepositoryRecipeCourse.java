package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends
        Repository<RecipeCoursePersistenceModel> {

    protected static RepositoryRecipeCourse INSTANCE = null;

    private RepositoryRecipeCourse(
            @Nonnull DomainDataAccess<RecipeCoursePersistenceModel> remoteDataSource,
            @Nonnull DomainDataAccess<RecipeCoursePersistenceModel> localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(
            DomainDataAccess<RecipeCoursePersistenceModel> remoteDataSource,
            DomainDataAccess<RecipeCoursePersistenceModel> localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}