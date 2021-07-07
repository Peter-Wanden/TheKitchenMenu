package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class RecipeCourseUseCaseDataAccess
        extends
        DataAccess<RecipeCourseUseCasePersistenceModel> {

    protected static RecipeCourseUseCaseDataAccess INSTANCE = null;

    private RecipeCourseUseCaseDataAccess(
            @Nonnull DomainDataAccess<RecipeCourseUseCasePersistenceModel> remoteDataSource,
            @Nonnull DomainDataAccess<RecipeCourseUseCasePersistenceModel> localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static RecipeCourseUseCaseDataAccess getInstance(
            DomainDataAccess<RecipeCourseUseCasePersistenceModel> remoteDataSource,
            DomainDataAccess<RecipeCourseUseCasePersistenceModel> localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipeCourseUseCaseDataAccess(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}