package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalUpdateAdapter;
import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourseLocal
        implements DomainDataAccessRecipeCourse {

    private static volatile RepositoryRecipeCourseLocal INSTANCE;

    @Nonnull
    private final CourseLocalGetAdapter getAdapter;
    @Nonnull
    private final CourseLocalUpdateAdapter updateAdapter;
    @Nonnull
    private final CourseLocalSaveAdapter saveAdapter;
    @Nonnull
    private final CourseLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeCourseLocal(
            @Nonnull CourseLocalGetAdapter getAdapter,
            @Nonnull CourseLocalUpdateAdapter updateAdapter,
            @Nonnull CourseLocalSaveAdapter saveAdapter,
            @Nonnull CourseLocalDeleteAdapter deleteAdapter) {

        this.getAdapter = getAdapter;
        this.updateAdapter = updateAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeCourseLocal getInstance(
            @Nonnull CourseLocalGetAdapter getAdapter,
            @Nonnull CourseLocalUpdateAdapter updateAdapter,
            @Nonnull CourseLocalSaveAdapter saveAdapter,
            @Nonnull CourseLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipeCourseLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryRecipeCourseLocal(
                            getAdapter,
                            updateAdapter,
                            saveAdapter,
                            deleteAdapter
                    );
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModelItem> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onDomainModelLoaded(RecipeCoursePersistenceModelItem model) {
                        callback.onDomainModelLoaded(model);
                    }

                    @Override
                    public void onDomainModelUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        getAdapter.getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceModelItem> models) {
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
    public void getAllByCourse(
            @Nonnull RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        getAdapter.getAllByCourse(
                c,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceModelItem> models) {
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
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceModelItem> models) {
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
    public void update(@Nonnull RecipeCoursePersistenceModelItem model) {
        updateAdapter.update(model);
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCoursePersistenceModelItem> callback) {
        // TODO - implement? or move to?
        callback.onDomainModelUnavailable();
    }

    @Override
    public void getAllActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem> callback) {
        getAdapter.getAllActiveByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceModelItem> models) {
                        callback.onAllDomainModelsLoaded(filterForActive(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    private List<RecipeCoursePersistenceModelItem> filterForActive(
            List<RecipeCoursePersistenceModelItem> models) {
            long lastUpdated = 0;

            List<RecipeCoursePersistenceModelItem> activeModels = new ArrayList<>();

            for (RecipeCoursePersistenceModelItem m : models) {
                if (m.getLastUpdate() > lastUpdated) {
                    activeModels.add(m);
                }
            }
            return activeModels;
    }

    @Override
    public void save(@Nonnull RecipeCoursePersistenceModelItem model) {
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
