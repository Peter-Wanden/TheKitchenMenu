package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourseLocal
        implements
        DomainDataAccess<RecipeCoursePersistenceDomainModel> {

    private static volatile RepositoryRecipeCourseLocal INSTANCE;

    @Nonnull
    private final CourseLocalGetAdapter getAdapter;
    @Nonnull
    private final CourseLocalSaveAdapter saveAdapter;
    @Nonnull
    private final CourseLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeCourseLocal(
            @Nonnull CourseLocalGetAdapter getAdapter,
            @Nonnull CourseLocalSaveAdapter saveAdapter,
            @Nonnull CourseLocalDeleteAdapter deleteAdapter) {

        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeCourseLocal getInstance(
            @Nonnull CourseLocalGetAdapter getAdapter,
            @Nonnull CourseLocalSaveAdapter saveAdapter,
            @Nonnull CourseLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipeCourseLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryRecipeCourseLocal(
                            getAdapter,
                            saveAdapter,
                            deleteAdapter
                    );
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceDomainModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeCoursePersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceDomainModel> models) {
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceDomainModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeCoursePersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeCoursePersistenceDomainModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceDomainModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId,
                new GetDomainModelCallback<RecipeCoursePersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeCoursePersistenceDomainModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void save(@Nonnull RecipeCoursePersistenceDomainModel model) {
        saveAdapter.save(model);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {
        deleteAdapter.deleteByDataId(dataId);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
