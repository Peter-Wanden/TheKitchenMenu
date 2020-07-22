package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class DataAccessRecipeCourse
        extends
        DataAccess<RecipeCoursePersistenceModel> {

    protected static DataAccessRecipeCourse INSTANCE = null;

    private DataAccessRecipeCourse(
            @Nonnull DomainDataAccess<RecipeCoursePersistenceModel> remoteDataSource,
            @Nonnull DomainDataAccess<RecipeCoursePersistenceModel> localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static DataAccessRecipeCourse getInstance(
            DomainDataAccess<RecipeCoursePersistenceModel> remoteDataSource,
            DomainDataAccess<RecipeCoursePersistenceModel> localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataAccessRecipeCourse(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}