package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class CourseLocalSaveAdapter {

    @Nonnull
    private final RecipeCourseParentLocalDataSource parentLocalDataSource;
    @Nonnull
    private final RecipeCourseItemLocalDataSource itemLocalDataSource;
    @Nonnull
    private final CourseModelConverter converter;

    public CourseLocalSaveAdapter(@Nonnull RecipeCourseParentLocalDataSource parentLocalDataSource,
                                  @Nonnull RecipeCourseItemLocalDataSource itemLocalDataSource,
                                  @Nonnull UniqueIdProvider idProvider) {
        this.parentLocalDataSource = parentLocalDataSource;
        this.itemLocalDataSource = itemLocalDataSource;

        converter = new CourseModelConverter(idProvider);
    }

    public void save(RecipeCourseUseCasePersistenceModel model) {
        saveParentEntity(model);
        saveCourseItems(model);
    }

    public void saveParentEntity(RecipeCourseUseCasePersistenceModel domainModel) {
        parentLocalDataSource.save(converter.convertParentDomainModelToEntity(domainModel));
    }

    public void saveCourseItems(RecipeCourseUseCasePersistenceModel domainModel) {
        itemLocalDataSource.save(
                converter.convertDomainCoursesToEntities(
                        domainModel.getCourses(),
                        domainModel.getDataId())
        );
    }
}
