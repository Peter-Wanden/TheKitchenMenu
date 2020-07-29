package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.RequestModelBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.message.UseCaseRequestWithId;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

import javax.annotation.Nonnull;

/**
 * Each use case is responsible for a single persistence model.
 * This abstract class provides the functionality required
 *
 * @param <DATA_ACCESS>             the component used to store and retrieve persistence models
 * @param <PERSISTENCE_MODEL>       used for domain data storage
 * @param <USE_CASE_MODEL>          the use case internal domain data structure
 * @param <USE_CASE_REQUEST_MODEL>  domain data model used in requests
 * @param <USE_CASE_RESPONSE_MODEL> domain data model used in responses
 */
public abstract class UseCaseData<
        DATA_ACCESS extends DomainDataAccess<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        USE_CASE_REQUEST_MODEL extends DomainModel.RequestModel,
        USE_CASE_RESPONSE_MODEL extends DomainModel.ResponseModel>
        extends
        UseCaseBase
        implements
        DomainDataAccess.GetDomainModelCallback<PERSISTENCE_MODEL> {

    private static final String TAG = "tkm-" + "UseCaseModel" + ": ";

    protected String useCaseDataId = UseCaseMessageModelDataId.NO_ID;
    protected String useCaseDomainId = UseCaseMessageModelDataId.NO_ID;

    protected DATA_ACCESS dataAccess;

    protected PERSISTENCE_MODEL persistenceModel;
    protected USE_CASE_MODEL useCaseModel;

    protected DomainModel.Converter<
            USE_CASE_MODEL,
            PERSISTENCE_MODEL,
            USE_CASE_REQUEST_MODEL,
            USE_CASE_RESPONSE_MODEL> modelConverter;

    protected boolean isChanged;
    protected int accessCount;

    public UseCaseData(
            DATA_ACCESS dataAccess,
            DomainModel.Converter<
                    USE_CASE_MODEL,
                    PERSISTENCE_MODEL,
                    USE_CASE_REQUEST_MODEL,
                    USE_CASE_RESPONSE_MODEL> modelConverter) {

        this.dataAccess = dataAccess;
        this.modelConverter = modelConverter;
    }

    @Override
    protected <REQUEST extends UseCaseBase.Request> void execute(REQUEST request) {
        accessCount++;

        UseCaseRequestWithId<USE_CASE_REQUEST_MODEL> r = (UseCaseRequestWithId<USE_CASE_REQUEST_MODEL>) request;

        System.out.println(TAG + "Request No:" + accessCount + " " + request);

        String requestDataId = r.getDataId() == null ?
                UseCaseMessageModelDataId.NO_ID :
                r.getDataId();

        String requestDomainId = r.getDomainId() == null ?
                UseCaseMessageModelDataId.NO_ID :
                r.getDomainId();

        // Explaining variables
        boolean requestHasDataId = !UseCaseMessageModelDataId.NO_ID.equals(requestDataId);
        boolean requestHasDomainId = !UseCaseMessageModelDataId.NO_ID.equals(requestDomainId);
        boolean requestHasNoIdentifiers = !requestHasDataId && !requestHasDomainId;

        boolean dataIdsAreNotEqual = requestHasDataId &&
                !requestDataId.equals(useCaseDataId);
        boolean domainIdIsAreNotEqual = requestHasDomainId &&
                !requestDomainId.equals(useCaseDomainId);

        if (requestHasNoIdentifiers) {
            if (useCaseModel == null) {
                useCaseModel = createUseCaseModelFromDefaultValues();
            }
            initialiseUseCase();
        } else {
            useCaseDataId = requestDataId;
            useCaseDomainId = requestDomainId;

            if (dataIdsAreNotEqual) {
                getPersistenceModelByDataId();
            } else if (domainIdIsAreNotEqual) {
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
        useCaseModel = createUseCaseModelFromPersistenceModel(model);
        initialiseUseCase();
    }

    private USE_CASE_MODEL createUseCaseModelFromPersistenceModel(
            @Nonnull PERSISTENCE_MODEL model) {
        return modelConverter.convertPersistenceToDomainModel(model);
    }

    private void setupForRequestModelProcessing() {
        USE_CASE_MODEL updatedModel = createUseCaseModelFromRequestModel();
        isChanged = !useCaseModel.equals(updatedModel);
        useCaseModel = updatedModel;

        initialiseUseCase();
    }

    private USE_CASE_MODEL createUseCaseModelFromRequestModel() {
        USE_CASE_REQUEST_MODEL requestModel =
                ((RequestModelBase<USE_CASE_REQUEST_MODEL>) getRequest()).getModel();
        return modelConverter.convertRequestToUseCaseModel(requestModel);
    }

    protected void createNewPersistenceModel() {
        persistenceModel = modelConverter.createNewPersistenceModel(useCaseDomainId, useCaseModel);
        dataAccess.save(persistenceModel);
    }

    protected void saveChanges() {
        if (persistenceModel != null) {
            archivePreviousPersistenceModel();
            persistenceModel = modelConverter.updatePersistenceModel(persistenceModel, useCaseModel);
        } else {
            persistenceModel = modelConverter.createNewPersistenceModel(useCaseDomainId, useCaseModel);
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

    protected USE_CASE_RESPONSE_MODEL getResponseModel() {
        return modelConverter.convertUseCaseToResponseModel(useCaseModel);
    }

    protected abstract void initialiseUseCase();

    protected abstract USE_CASE_MODEL createUseCaseModelFromDefaultValues();
}
