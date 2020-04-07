package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.DataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseModelPersistence;

import javax.annotation.Nonnull;

public class CourseLocalGetByDataIdAdapter {

    @Nonnull
    private final RecipeCourseLocalDataAccess courseLocalDataAccess;
    private GetDomainModelCallback<RecipeCourseModelPersistence> callback;

    public CourseLocalGetByDataIdAdapter(
            @Nonnull RecipeCourseLocalDataAccess courseLocalDataAccess) {
        this.courseLocalDataAccess = courseLocalDataAccess;
    }

    public void adaptToDomainModel(
            String dataId,
            GetDomainModelCallback<RecipeCourseModelPersistence> callback) {
        this.callback = callback;
        getPrimitive(dataId);
    }

    private void getPrimitive(String dataId) {
        courseLocalDataAccess.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeCourseEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeCourseEntity e) {
                        callback.onModelLoaded(CourseConverter.convertToModel(e));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                });
    }
}
