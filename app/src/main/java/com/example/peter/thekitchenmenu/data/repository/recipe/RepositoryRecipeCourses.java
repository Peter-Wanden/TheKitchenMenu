package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourses
        extends
        Repository<RecipeCoursePersistenceModel> {

    protected static RepositoryRecipeCourses INSTANCE = null;

    private RepositoryRecipeCourses(
            @Nonnull DomainDataAccess<RecipeCoursePersistenceModel> remoteDataSource,
            @Nonnull DomainDataAccess<RecipeCoursePersistenceModel> localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourses getInstance(
            DomainDataAccess<RecipeCoursePersistenceModel> remoteDataSource,
            DomainDataAccess<RecipeCoursePersistenceModel> localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourses(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}
