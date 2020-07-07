package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.RequestModelBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.RequestWithId;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public abstract class UseCaseData<
        REPOSITORY extends Repository<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceDomainModel,
        USE_CASE_MODEL extends DomainModel.UseCaseDomainModel,
        REQUEST_MODEL extends DomainModel.RequestDomainModel,
        RESPONSE_MODEL extends DomainModel.ResponseDomainModel>
        extends
        UseCaseBase
        implements
        DomainDataAccess.GetDomainModelCallback<PERSISTENCE_MODEL> {

    private static final String TAG = "tkm-" + "UseCaseIdLayerOne" + ": ";

    protected String useCaseDataId = UseCaseMessageModelDataId.NO_ID;
    protected String useCaseDomainId = UseCaseMessageModelDataId.NO_ID;

    protected REPOSITORY repository;
    protected UniqueIdProvider idProvider;
    protected TimeProvider timeProvider;

    protected PERSISTENCE_MODEL persistenceModel;
    protected USE_CASE_MODEL useCaseModel;

    protected DomainModelConverter<
            USE_CASE_MODEL,
            PERSISTENCE_MODEL,
            REQUEST_MODEL,
            RESPONSE_MODEL> modelConverter;

    protected boolean isChanged;
    protected int accessCount;

    public UseCaseData(DomainModelConverter<
            USE_CASE_MODEL,
            PERSISTENCE_MODEL,
            REQUEST_MODEL,
            RESPONSE_MODEL> modelConverter) {
        this.modelConverter = modelConverter;
    }

    @Override
    protected <REQUEST extends UseCaseBase.Request> void execute(REQUEST request) {
        accessCount++;

        RequestWithId<REQUEST_MODEL> r = (RequestWithId<REQUEST_MODEL>) request;

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

    private void getPersistenceModelByDataId() {
        repository.getByDataId(useCaseDataId, this);
    }

    private void getPersistenceModelByDomainId() {
        repository.getActiveByDomainId(useCaseDomainId, this);
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
        REQUEST_MODEL requestModel = ((RequestModelBase<REQUEST_MODEL>) getRequest()).getDomainModel();
        return modelConverter.convertRequestModelToUseCaseModel(requestModel);
    }

    private void archivePreviousState(long currentTime) {
        repository.save(modelConverter.convertPersistentModelToArchivedModel(currentTime));
    }

    protected abstract USE_CASE_MODEL createUseCaseModelFromDefaultValues();

    protected abstract void initialiseUseCase();
}
