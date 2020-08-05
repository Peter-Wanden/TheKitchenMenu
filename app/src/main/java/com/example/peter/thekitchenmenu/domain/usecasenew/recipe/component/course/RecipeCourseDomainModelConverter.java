package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public final class RecipeCourseDomainModelConverter
        extends
        DomainModelConverter<
                        RecipeCourseUseCaseModel,
                        RecipeCourseUseCasePersistenceModel,
                        RecipeCourseUseCaseRequestModel,
                        RecipeCourseUseCaseResponseModel> {

    public RecipeCourseDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                            @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipeCourseUseCaseModel convertPersistenceToUseCaseModel(
            @Nonnull RecipeCourseUseCasePersistenceModel persistenceModel) {
        return new RecipeCourseUseCaseModel(persistenceModel.getCourses());
    }

    @Override
    public RecipeCourseUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipeCourseUseCaseRequestModel requestModel) {
        return new RecipeCourseUseCaseModel(requestModel.getCourseList());
    }

    @Override
    public RecipeCourseUseCasePersistenceModel convertUseCaseToPersistenceModel(
            @Nonnull String domainId, @Nonnull RecipeCourseUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeCourseUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(domainId)
                .setCourses(useCaseModel)
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeCourseUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeCourseUseCasePersistenceModel persistenceModel) {
        return new RecipeCourseUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public RecipeCourseUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeCourseUseCaseModel useCaseModel) {
        return new RecipeCourseUseCaseResponseModel.Builder()
                .setCourseList(useCaseModel)
                .build();
    }

    @Override
    public RecipeCourseUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipeCourseUseCasePersistenceModel persistenceModel,
            @Nonnull RecipeCourseUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeCourseUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(persistenceModel.getDomainId())
                .setCourses(useCaseModel)
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeCourseUseCaseModel getDefault() {
        return null;
    }
}
