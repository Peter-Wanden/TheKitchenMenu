package com.example.peter.thekitchenmenu.domain.usecasenew.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModelConverter;

import javax.annotation.Nonnull;

public abstract class UseCaseData<
        DATA_ACCESS extends DomainDataAccess<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        REQUEST_MODEL extends DomainModel.UseCaseRequestModel,
        RESPONSE_MODEL extends DomainModel.UseCaseResponseModel>
        extends
        UseCase<REQUEST_MODEL, RESPONSE_MODEL>
        implements
        DomainDataAccess.GetDomainModelCallback<PERSISTENCE_MODEL> {

    private static final String TAG = "tkm-" + "UseCaseData" + ": ";

    public static final String NO_ID = "";

    protected String useCaseDataId = NO_ID;
    protected String useCaseDomainId = NO_ID;

    protected DATA_ACCESS dataAccess;
    protected PERSISTENCE_MODEL persistenceModel;
    protected USE_CASE_MODEL useCaseModel;

    protected DomainModelConverter<
                USE_CASE_MODEL,
                PERSISTENCE_MODEL,
                REQUEST_MODEL,
                RESPONSE_MODEL> modelConverter;

    protected boolean isChanged;
    protected int accessCount;

    public UseCaseData(@Nonnull DATA_ACCESS dataAccess,
                       @Nonnull DomainModelConverter<
                                                      USE_CASE_MODEL,
                                                      PERSISTENCE_MODEL,
                                                      REQUEST_MODEL,
                                                      RESPONSE_MODEL> modelConverter) {
        this.dataAccess = dataAccess;
        this.modelConverter = modelConverter;
    }

    @Override
    protected void execute(UseCaseRequest<REQUEST_MODEL> useCaseRequest) {
        accessCount++;
        System.out.println(TAG + "Request No:" + accessCount + " " + useCaseRequest);

        // extract id's
        String requestDataId = useCaseRequest.getDataId() == null ?
                UseCaseMessageModelDataId.NO_ID :
                useCaseRequest.getDataId();

        String requestDomainId = useCaseRequest.getDomainId() == null ?
                UseCaseMessageModelDataId.NO_ID :
                useCaseRequest.getDomainId();

        // Explaining variables
        boolean requestHasDataId = !UseCaseMessageModelDataId.NO_ID.equals(requestDataId);
        boolean requestHasDomainId = !UseCaseMessageModelDataId.NO_ID.equals(requestDomainId);
        boolean requestHasNoIds = !requestHasDataId && !requestHasDomainId;
        boolean isDataIdChanged = requestHasDataId && !requestDataId.equals(useCaseDataId);
        boolean isDomainIdChanged = requestHasDomainId && !requestDomainId.equals(useCaseDomainId);

        if (requestHasNoIds) {
            if (useCaseModel == null) {
                useCaseModel = createUseCaseModelFromDefaultValues();
            }
            initialiseUseCase();
        } else {
            useCaseDataId = requestDataId;
            useCaseDomainId = requestDomainId;

            if (isDataIdChanged) {
                getPersistenceModelByDataId();
            } else if (isDomainIdChanged) {
                getPersistenceModelByDomainId();
            } else {
                setupForRequestModelProcessing();
            }
        }
    }

    protected void getPersistenceModelByDataId() {
        dataAccess.getByDataId(useCaseDataId, this);
    }

    protected void getPersistenceModelByDomainId() {
        dataAccess.getByDomainId(useCaseDomainId, this);
    }

    @Override
    public void onPersistenceModelUnavailable() {
        useCaseModel = createUseCaseModelFromDefaultValues();
        initialiseUseCase();
    }

    @Override
    public void onPersistenceModelLoaded(PERSISTENCE_MODEL model) {
        isChanged = false;

        useCaseDataId = model.getDataId();
        useCaseDomainId = model.getDomainId();

        persistenceModel = model;
        useCaseModel = modelConverter.convertPersistenceToUseCaseModel(model);
        initialiseUseCase();
    }

    private void setupForRequestModelProcessing() {
        USE_CASE_MODEL updateFromRequestModel = modelConverter.convertRequestToUseCaseModel(
                useCaseRequest.getRequestModel());

        isChanged = !useCaseModel.equals(updateFromRequestModel);
        useCaseModel = updateFromRequestModel;

        initialiseUseCase();
    }

    protected void saveChanges() {
        if (persistenceModel != null) {
            archivePreviousPersistenceModel();
            persistenceModel = modelConverter.updatePersistenceModel(persistenceModel, useCaseModel);
        } else {
            persistenceModel = modelConverter.convertUseCaseToPersistenceModel(useCaseDomainId, useCaseModel);
        }
        useCaseDataId = persistenceModel.getDataId();
        dataAccess.save(persistenceModel);
    }

    protected void archivePreviousPersistenceModel() {
        dataAccess.save(modelConverter.createArchivedPersistenceModel(persistenceModel));
    }

    protected boolean isDefaultDomainModel() {
        return useCaseModel.equals(createUseCaseModelFromDefaultValues());
    }

    protected RESPONSE_MODEL getResponseModel() {
        return modelConverter.convertUseCaseToResponseModel(useCaseModel);
    }

    protected USE_CASE_MODEL createUseCaseModelFromDefaultValues() {

        return modelConverter.getDefault();
    }

    protected abstract void initialiseUseCase();
}
