package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.recipeIdentity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.recipeIdentity.RecipeIdentityEntityModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

public class RecipeIdentityUseCase
        extends
        UseCaseMetadata<
                RepositoryRecipeIdentity,
                RecipeIdentityPersistenceModel,
                RecipeIdentityEntityModel,
                RecipeIdentityUseCaseModel,
                RecipeIdentityRequestModel,
                RecipeIdentityResponseModel> {

    private RecipeIdentityEntity identityEntity;

    public RecipeIdentityUseCase(RepositoryRecipeIdentity repository,
                                 RecipeIdentityDomainModelConverter modelConverter,
                                 UniqueIdProvider idProvider,
                                 TimeProvider timeProvider,
                                 RecipeIdentityEntity identityEntity) {
        super(repository, modelConverter, idProvider, timeProvider);

        this.identityEntity = identityEntity;
    }

    @Override
    protected RecipeIdentityUseCaseModel createUseCaseModelFromDefaultValues() {
        return new RecipeIdentityUseCaseModel("", "");
    }

    @Override
    protected void validateDomainModelElements() {
        RecipeIdentityEntityModel model = modelConverter.convertUseCaseToEntityModel(useCaseModel);

        identityEntity.execute(new BusinessEntity.Request<>(model), entityResponse -> {
            failReasons = entityResponse.getFailReasons();
            useCaseModel = modelConverter.convertEntityToUseCaseModel(entityResponse.getModel());

            processResult();
        });
    }

    private void processResult() {
        if (isChanged && isDomainModelValid()) {
            archivePreviousPersistenceModel();
            createNewPersistenceModel();
        }
        buildResponse();
    }

    private void buildResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(modelConverter.convertUseCaseToResponseModel(useCaseModel));

        sendResponse(builder.build());
    }
}