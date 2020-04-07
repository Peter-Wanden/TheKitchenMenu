package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalUpdateAdapter;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCourseLocalDomainDataAccess implements DataAccessRecipeCourse {

    private static volatile RecipeCourseLocalDomainDataAccess INSTANCE;

    @Nonnull
    private final CourseLocalGetAdapter getAdapter;
    @Nonnull
    private final CourseLocalUpdateAdapter updateAdapter;
    @Nonnull
    private final CourseLocalSaveAdapter saveAdapter;
    @Nonnull
    private final CourseLocalDeleteAdapter deleteAdapter;

    private RecipeCourseLocalDomainDataAccess(
            @Nonnull CourseLocalGetAdapter getAdapter,
            @Nonnull CourseLocalUpdateAdapter updateAdapter,
            @Nonnull CourseLocalSaveAdapter saveAdapter,
            @Nonnull CourseLocalDeleteAdapter deleteAdapter) {

        this.getAdapter = getAdapter;
        this.updateAdapter = updateAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RecipeCourseLocalDomainDataAccess getInstance(
            @Nonnull CourseLocalGetAdapter getAdapter,
            @Nonnull CourseLocalUpdateAdapter updateAdapter,
            @Nonnull CourseLocalSaveAdapter saveAdapter,
            @Nonnull CourseLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RecipeCourseLocalDomainDataAccess.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeCourseLocalDomainDataAccess(
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
    public void getAllByCourse(
            RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        getAdapter.getAllByCourse(
                c,
                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        getAdapter.getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void update(@Nonnull RecipeCourseModelPersistence model) {
        updateAdapter.update(model);
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onModelLoaded(RecipeCourseModelPersistence model) {
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onModelUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        // TODO - implement? or move to?
        callback.onModelUnavailable();
    }

    @Override
    public void getAllActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        getAdapter.getAllActiveByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void save(@Nonnull RecipeCourseModelPersistence model) {
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
