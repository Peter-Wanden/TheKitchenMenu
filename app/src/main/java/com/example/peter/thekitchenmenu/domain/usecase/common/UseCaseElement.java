package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Using the state of the data Id's, determines the updatedDomainModel to be processed.
 * @param <PERSISTENCE_MODEL> the domain model the use case sends to the repository
 * @param <USE_CASE_DOMAIN_MODEL> the use case's internal domain model
 * @param <REPOSITORY> the repository for the persistence model
 */
public abstract class UseCaseElement<
        PERSISTENCE_MODEL extends BaseDomainPersistenceModel,
        USE_CASE_DOMAIN_MODEL extends UseCaseDomainModel,
        REPOSITORY extends Repository<PERSISTENCE_MODEL>>
        extends
        UseCaseBase
        implements DomainDataAccess.GetDomainModelCallback<PERSISTENCE_MODEL>{

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    protected String useCaseDataId = UseCaseMessageModelDataId.NO_ID;
    protected String useCaseDomainId = UseCaseMessageModelDataId.NO_ID;

    protected REPOSITORY repository;
    protected PERSISTENCE_MODEL persistenceModel;

    protected USE_CASE_DOMAIN_MODEL activeDomainModel;
    protected USE_CASE_DOMAIN_MODEL updatedDomainModel;

    protected List<FailReasons> failReasons = new ArrayList<>();

    protected int accessCount;
    protected boolean isChanged;
    protected boolean isDomainDataUnavailable;

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {
        accessCount++;

        UseCaseMessageModelDataId<BaseDomainModel> r = (UseCaseMessageModelDataId<BaseDomainModel>) request;

        System.out.println(TAG + "Request No:" + accessCount + " " + request);

        String requestDataId = r.getDataId() == null ? UseCaseMessageModelDataId.NO_ID : r.getDataId();
        String requestDomainId = r.getDomainId() == null ? UseCaseMessageModelDataId.NO_ID : r.getDomainId();

        // Explaining variables
        boolean requestHasDataId = !UseCaseMessageModelDataId.NO_ID.equals(requestDataId);
        boolean requestHasDomainId = !UseCaseMessageModelDataId.NO_ID.equals(requestDomainId);
        boolean requestHasNoIdentifiers = !requestHasDataId && !requestHasDomainId;
        boolean dataIdsAreNotEqual = requestHasDataId &&
                !requestDataId.equals(useCaseDataId);
        boolean domainIdIsAreNotEqual = requestHasDomainId &&
                !requestDomainId.equals(useCaseDomainId);

        if (requestHasNoIdentifiers) {
            initialiseUseCaseForUpdatedDomainModelProcessing();
        } else {
            useCaseDataId = requestDataId;
            useCaseDomainId = requestDomainId;

            if (dataIdsAreNotEqual) {
                loadDomainModelByDataId();
            } else if (domainIdIsAreNotEqual) {
                loadDomainModelByDomainId();
            } else {
                processRequestDomainModel();
            }
        }
    }

    protected void loadDomainModelByDataId() {
        System.out.println(TAG + "loadDomainModelByDataId=" + useCaseDataId);
        repository.getByDataId(useCaseDataId, this);
    }

    protected void loadDomainModelByDomainId() {
        System.out.println(TAG + "loadDomainModelByDomainId=" + useCaseDomainId);
        repository.getActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void onDomainModelUnavailable() {
        System.out.println(TAG + "onDomainModelUnavailable: updatedDomainModel=" + updatedDomainModel);

        isDomainDataUnavailable = true;
        createUpdatedDomainModelFromDefaultValues();
        initialiseUseCaseForUpdatedDomainModelProcessing();
    }

    @Override
    public void onDomainModelLoaded(PERSISTENCE_MODEL persistenceModel) {
        System.out.println(TAG + "onAllDomainModelsLoaded=" + persistenceModel);

        isDomainDataUnavailable = false;
        this.persistenceModel = persistenceModel;
        createUpdatedDomainModelFromPersistenceModel(persistenceModel);
        initialiseUseCaseForUpdatedDomainModelProcessing();
    }

    protected void processRequestDomainModel() {
        System.out.println(TAG + "processRequestDomainModel");
        createUpdatedDomainModelFromRequestModel();
        initialiseUseCaseForUpdatedDomainModelProcessing();
    }

    protected abstract void createUpdatedDomainModelFromRequestModel();

    protected abstract void createUpdatedDomainModelFromPersistenceModel(
            @Nonnull PERSISTENCE_MODEL persistenceModel);

    protected abstract void createUpdatedDomainModelFromDefaultValues();

    protected abstract void initialiseUseCaseForUpdatedDomainModelProcessing();

    protected abstract void validateUpdatedDomainModelElements();

    protected abstract void save();

    protected abstract void buildResponse();

    protected void sendResponse(UseCaseBase.Response response) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + response);
        if (isDomainModelValid()) {
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
    }

    protected UseCaseMetadataModel getMetadata() {
        UseCaseMetadataModel metadataModel = new UseCaseMetadataModel.Builder().
                setFailReasons(new ArrayList<>(getFailReasons())).
                setState(getComponentState()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(persistenceModel == null ? 0L : persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel == null ? 0L : persistenceModel.getLastUpdate()).
                build();

        System.out.println(TAG + "getMetadata= " + metadataModel);
        return metadataModel;
    }

    protected RecipeMetadata.ComponentState getComponentState() {
        RecipeMetadata.ComponentState componentState = isDomainModelValid() ?
                (isChanged ?
                        RecipeMetadata.ComponentState.VALID_CHANGED:
                        RecipeMetadata.ComponentState.VALID_UNCHANGED):
                (isChanged ?
                        RecipeMetadata.ComponentState.INVALID_CHANGED:
                        RecipeMetadata.ComponentState.INVALID_UNCHANGED);

        System.out.println(TAG + "getComponentState= " + componentState);
        return componentState;
    }

    protected boolean isDomainModelValid() {
        boolean isValid = failReasons.equals(Collections.singletonList(CommonFailReason.NONE));
        System.out.println(TAG + "isDomainModelValid= " + isValid);
        return isValid;
    }

    protected List<FailReasons> getFailReasons() {
        if (isDomainDataUnavailable) {
            if (!failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
                failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
            }
        } else if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
        System.out.println(TAG + "getFailReasons= " + failReasons);
        return failReasons;
    }
}
