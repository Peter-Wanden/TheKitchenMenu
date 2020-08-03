package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeCourseUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;

import java.util.ArrayList;

public class RecipeCourseUseCase
        extends
        UseCaseResult<
                RecipeCourseUseCaseDataAccess,
                RecipeCourseUseCasePersistenceModel,
                RecipeCourseUseCaseModel,
                RecipeCourseUseCaseRequestModel,
                RecipeCourseUseCaseResponseModel> {

    private static final String TAG = "tkm-" + RecipeCourseUseCase.class.getSimpleName() + ": ";

    public RecipeCourseUseCase(RecipeCourseUseCaseDataAccess dataAccess,
                               RecipeCourseDomainModelConverter converter) {
        super(dataAccess, converter);
    }

    @Override
    protected void beginProcessingDomainModel() {
        if (useCaseModel.isEmpty()) {
            useCaseFailReasons.add(RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED);
        }
        domainModelProcessingComplete();
    }

    @Override
    protected RecipeCourseUseCaseModel createUseCaseModelFromDefaultValues() {
        return new RecipeCourseUseCaseModel(new ArrayList<>());
    }
}
