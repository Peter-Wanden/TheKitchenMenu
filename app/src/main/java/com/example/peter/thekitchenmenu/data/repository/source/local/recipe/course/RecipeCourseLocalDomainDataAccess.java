package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalGetActiveAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalGetAllAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalGetAllByCourseAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalGetAllByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalGetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course.CourseLocalUpdateAdapter;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCourseLocalDomainDataAccess implements DataAccessRecipeCourse {

    private static volatile RecipeCourseLocalDomainDataAccess INSTANCE;

    @Nonnull
    private final CourseLocalGetByDataIdAdapter getByDataIdAdapter;
    @Nonnull
    private final CourseLocalGetAllByCourseAdapter getAllByCourseAdapter;
    @Nonnull
    private final CourseLocalGetAllByDomainIdAdapter getAllByDomainIdAdapter;
    @Nonnull
    private final CourseLocalUpdateAdapter updateAdapter;
    @Nonnull
    private final CourseLocalGetAllAdapter getAllAdapter;
    @Nonnull
    private final CourseLocalGetActiveAdapter getActiveAdapter;

    private RecipeCourseLocalDomainDataAccess(
            @Nonnull CourseLocalGetByDataIdAdapter getByDataIdAdapter,
            @Nonnull CourseLocalGetAllByCourseAdapter getAllByCourseAdapter,
            @Nonnull CourseLocalGetAllByDomainIdAdapter getAllByDomainIdAdapter,
            @Nonnull CourseLocalUpdateAdapter updateAdapter,
            @Nonnull CourseLocalGetAllAdapter getAllAdapter,
            @Nonnull CourseLocalGetActiveAdapter getActiveAdapter) {

        this.getByDataIdAdapter = getByDataIdAdapter;
        this.getAllByCourseAdapter = getAllByCourseAdapter;
        this.getAllByDomainIdAdapter = getAllByDomainIdAdapter;
        this.updateAdapter = updateAdapter;
        this.getAllAdapter = getAllAdapter;
        this.getActiveAdapter = getActiveAdapter;
    }

    public static RecipeCourseLocalDomainDataAccess getInstance(
            @Nonnull CourseLocalGetByDataIdAdapter getByDataIdAdapter,
            @Nonnull CourseLocalGetAllByCourseAdapter getAllByCourseAdapter,
            @Nonnull CourseLocalGetAllByDomainIdAdapter getAllByDomainIdAdapter,
            @Nonnull CourseLocalUpdateAdapter updateAdapter,
            @Nonnull CourseLocalGetAllAdapter getAllAdapter,
            @Nonnull CourseLocalGetActiveAdapter getActiveAdapter) {

        if (INSTANCE == null) {
            synchronized (RecipeCourseLocalDomainDataAccess.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeCourseLocalDomainDataAccess(
                            getByDataIdAdapter,
                            getAllByCourseAdapter,
                            getAllByDomainIdAdapter,
                            updateAdapter,
                            getAllAdapter,
                            getActiveAdapter);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllByCourse(
            RecipeCourse.Course c,
            @Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        getAllByCourseAdapter.getAllByCourse(
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
        getAllByDomainIdAdapter.getAllByDomainId(
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
        });
    }

    @Override
    public void update(@Nonnull RecipeCourseModelPersistence model) {
        updateAdapter.update(model);
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeCourseModelPersistence> callback) {
        getAllAdapter.getAll(new GetAllDomainModelsCallback<RecipeCourseModelPersistence>() {
            @Override
            public void onAllLoaded(List<RecipeCourseModelPersistence> models) {
                callback.onAllLoaded(models);
            }

            @Override
            public void onModelsUnavailable() {
                callback.onModelsUnavailable();
            }
        });
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        getByDataIdAdapter.adaptToDomainModel(
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
        getActiveAdapter.getActiveByDomainId(
                domainId, new GetAllDomainModelsCallback<>()
    }

    @Override
    public void save(@Nonnull RecipeCourseModelPersistence model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
