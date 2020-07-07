package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends
        Repository<RecipeCoursePersistenceDomainModel> {

    protected static RepositoryRecipeCourse INSTANCE = null;

    private RepositoryRecipeCourse(
            @Nonnull DomainDataAccess<RecipeCoursePersistenceDomainModel> remoteDataSource,
            @Nonnull DomainDataAccess<RecipeCoursePersistenceDomainModel> localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(
            DomainDataAccess<RecipeCoursePersistenceDomainModel> remoteDataSource,
            DomainDataAccess<RecipeCoursePersistenceDomainModel> localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}