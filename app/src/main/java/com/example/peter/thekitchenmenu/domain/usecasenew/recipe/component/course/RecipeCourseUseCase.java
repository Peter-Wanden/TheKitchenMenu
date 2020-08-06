package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeCourseUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;

public class RecipeCourseUseCase
        extends
        UseCaseResult<
                RecipeCourseUseCaseDataAccess,
                RecipeCourseUseCasePersistenceModel,
                RecipeCourseUseCaseModel,
                RecipeCourseUseCaseRequestModel,
                RecipeCourseUseCaseResponseModel> {

    public RecipeCourseUseCase(RecipeCourseUseCaseDataAccess dataAccess,
                               RecipeCourseDomainModelConverter converter) {
        super(dataAccess, converter);
    }

    @Override
    protected void beginProcessingDomainModel() {
        if (useCaseModel.getCourses().isEmpty()) {
            useCaseFailReasons.add(RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED);
        }
        domainModelProcessingComplete();
    }
}
