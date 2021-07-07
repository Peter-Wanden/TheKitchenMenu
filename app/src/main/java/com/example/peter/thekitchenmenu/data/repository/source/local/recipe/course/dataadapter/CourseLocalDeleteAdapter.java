package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CourseLocalDeleteAdapter {

    @Nonnull
    private final RecipeCourseParentLocalDataSource parentLocalDataSource;
    @Nonnull
    private final RecipeCourseItemLocalDataSource itemLocalDataSource;

    public CourseLocalDeleteAdapter(
            @Nonnull RecipeCourseParentLocalDataSource parentLocalDataSource,
            @Nonnull RecipeCourseItemLocalDataSource itemLocalDataSource) {
        this.parentLocalDataSource = parentLocalDataSource;
        this.itemLocalDataSource = itemLocalDataSource;
    }

    public void deleteByDataId(@Nonnull String dataId) {
        parentLocalDataSource.deleteByDataId(dataId);
        itemLocalDataSource.deleteAllByParentDataId(dataId);
    }

    public void deleteAllByDomainId(@Nonnull String domainId) {
        createParentDataIdList(domainId);
    }

    public void deleteAll() {
        parentLocalDataSource.deleteAll();
        itemLocalDataSource.deleteAll();
    }

    private void createParentDataIdList(String domainId) {
        List<String> parentDataIds = new ArrayList<>();
        parentLocalDataSource.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeCourseParentEntity> entities) {
                        if (!entities.isEmpty()) {
                            entities.forEach(entity -> parentDataIds.add(entity.getDataId()));
                            deleteRecordsByParentDataIds(parentDataIds);
                        }
                    }

                    @Override
                    public void onDataUnavailable() {

                    }
                });
    }

    private void deleteRecordsByParentDataIds(List<String> parentDataIds) {
        parentDataIds.forEach(parentDataId -> {
            itemLocalDataSource.deleteAllByParentDataId(parentDataId);
            parentLocalDataSource.deleteByDataId(parentDataId);
        });
    }
}
