package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public final class RecipeDurationDomainModelConverter
        extends
        DomainModelConverter<
                        RecipeDurationUseCaseModel,
                        RecipeDurationUseCasePersistenceModel,
                        RecipeDurationUseCaseRequestModel,
                        RecipeDurationUseCaseResponseModel> {

    private static final String TAG = "tkm-" + RecipeDurationDomainModelConverter.class
            .getSimpleName() + ": ";

    public RecipeDurationDomainModelConverter(@Nonnull TimeProvider timeProvider,
                                              @Nonnull UniqueIdProvider idProvider) {
        super(timeProvider, idProvider);
    }

    @Override
    public RecipeDurationUseCaseModel convertPersistenceToUseCaseModel(
            @Nonnull RecipeDurationUseCasePersistenceModel persistenceModel) {

        return new RecipeDurationUseCaseModel.Builder()
                .setPrepTime(persistenceModel.getPrepTime())
                .setCookTime(persistenceModel.getCookTime())
                .build();
    }

    @Override
    public RecipeDurationUseCaseModel convertRequestToUseCaseModel(
            @Nonnull RecipeDurationUseCaseRequestModel requestModel) {

        int prepTime = RecipeDurationTimeHelper.getTotalMinutes(
                requestModel.getPrepHours(), requestModel.getPrepMinutes()
        );
        int cookTime = RecipeDurationTimeHelper.getTotalMinutes(
                requestModel.getCookHours(), requestModel.getCookMinutes()
        );

        return new RecipeDurationUseCaseModel.Builder()
                .setPrepTime(prepTime)
                .setCookTime(cookTime)
                .build();
    }

    @Override
    public RecipeDurationUseCasePersistenceModel convertUseCaseToPersistenceModel(
            @Nonnull String domainId,
            @Nonnull RecipeDurationUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeDurationUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(domainId)
                .setPrepTime(useCaseModel.getPrepTime())
                .setCookTime(useCaseModel.getCookTime())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeDurationUseCasePersistenceModel createArchivedPersistenceModel(
            @Nonnull RecipeDurationUseCasePersistenceModel persistenceModel) {
        return new RecipeDurationUseCasePersistenceModel.Builder()
                .basedOnModel(persistenceModel)
                .setLastUpdate(timeProvider.getCurrentTimeInMills())
                .build();
    }

    @Override
    public RecipeDurationUseCaseResponseModel convertUseCaseToResponseModel(
            @Nonnull RecipeDurationUseCaseModel useCaseModel) {
        return new RecipeDurationUseCaseResponseModel.Builder()
                .setPrepHours(RecipeDurationTimeHelper.getHours(useCaseModel.getPrepTime()))
                .setPrepMinutes(RecipeDurationTimeHelper.getMinutes(useCaseModel.getPrepTime()))
                .setTotalPrepTime(useCaseModel.getPrepTime())
                .setCookHours(RecipeDurationTimeHelper.getHours(useCaseModel.getCookTime()))
                .setCookMinutes(RecipeDurationTimeHelper.getMinutes(useCaseModel.getCookTime()))
                .setTotalCookTime(useCaseModel.getCookTime())
                .setTotalTime(useCaseModel.getPrepTime() + useCaseModel.getCookTime())
                .build();
    }

    @Override
    public RecipeDurationUseCasePersistenceModel updatePersistenceModel(
            @Nonnull RecipeDurationUseCasePersistenceModel persistenceModel,
            @Nonnull RecipeDurationUseCaseModel useCaseModel) {

        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeDurationUseCasePersistenceModel.Builder()
                .setDataId(idProvider.getUId())
                .setDomainId(persistenceModel.getDomainId())
                .setPrepTime(useCaseModel.getPrepTime())
                .setCookTime(useCaseModel.getCookTime())
                .setCreateDate(currentTime)
                .setLastUpdate(currentTime)
                .build();
    }

    @Override
    public RecipeDurationUseCaseModel getDefault() {
        return new RecipeDurationUseCaseModel.Builder()
                .getDefault()
                .build();
    }
}
