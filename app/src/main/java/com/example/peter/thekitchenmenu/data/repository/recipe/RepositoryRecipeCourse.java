package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourse
        extends
        Repository<RecipeCoursePersistenceModel>
        implements
        DomainDataAccessRecipeCourse {

    protected static RepositoryRecipeCourse INSTANCE = null;

    private RepositoryRecipeCourse(@Nonnull DomainDataAccessRecipeCourse remoteDataSource,
                                   @Nonnull DomainDataAccessRecipeCourse localDataSource) {
        remoteDomainDataAccess = remoteDataSource;
        localDomainDataAccess = localDataSource;
    }

    public static RepositoryRecipeCourse getInstance(DomainDataAccessRecipeCourse remoteDataSource,
                                                     DomainDataAccessRecipeCourse localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeCourse(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void update(@Nonnull RecipeCoursePersistenceModel model) {
        ((DomainDataAccessRecipeCourse)remoteDomainDataAccess).update(model);
        ((DomainDataAccessRecipeCourse)localDomainDataAccess).update(model);
        cache.put(model.getDataId(), model);
    }
}