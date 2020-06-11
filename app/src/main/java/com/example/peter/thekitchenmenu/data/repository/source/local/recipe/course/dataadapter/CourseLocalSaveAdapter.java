package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import javax.annotation.Nonnull;

public class CourseLocalSaveAdapter {

    @Nonnull
    private final RecipeCourseParentLocalDataSource parentLocalDataSource;
    @Nonnull
    private final RecipeCourseItemLocalDataSource itemLocalDataSource;
    @Nonnull
    private final CourseModelConverter converter;

    public CourseLocalSaveAdapter(@Nonnull RecipeCourseParentLocalDataSource parentLocalDataSource,
                                  @Nonnull RecipeCourseItemLocalDataSource itemLocalDataSource) {
        this.parentLocalDataSource = parentLocalDataSource;
        this.itemLocalDataSource = itemLocalDataSource;

        converter = new CourseModelConverter();
    }

    public void save(RecipeCoursePersistenceModel model) {
        saveParentEntity(model);
        saveCourseItems(model);
    }

    public void saveParentEntity(RecipeCoursePersistenceModel domainModel) {
        parentLocalDataSource.save(converter.convertParentDomainModelToEntity(domainModel));
    }

    public void saveCourseItems(RecipeCoursePersistenceModel domainModel) {
        itemLocalDataSource.save(
                converter.convertDomainCourseItemsToEntities(
                        domainModel.getPersistenceModelItems(),
                        domainModel.getDataId())
        );
    }
}
