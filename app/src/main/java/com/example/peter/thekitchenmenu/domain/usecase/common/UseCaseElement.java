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
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class UseCaseElement<
        PERSISTENCE_MODEL extends BaseDomainPersistenceModel,
        USE_CASE_DOMAIN_MODEL extends UseCaseDomainModel,
        REPOSITORY extends Repository<PERSISTENCE_MODEL>
        >
        extends
        UseCaseBase
        implements DomainDataAccess.GetDomainModelCallback<PERSISTENCE_MODEL>{

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    protected String useCaseDataId = UseCaseMessageModelDataId.NO_ID;
    protected String useCaseDomainId = UseCaseMessageModelDataId.NO_ID;

    protected REPOSITORY repository;
    protected PERSISTENCE_MODEL persistenceModel;

    protected USE_CASE_DOMAIN_MODEL defaultDomainModel;
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

        boolean requestHasDataId = !UseCaseMessageModelDataId.NO_ID.equals(requestDataId);
        boolean requestHasDomainId = !UseCaseMessageModelDataId.NO_ID.equals(requestDomainId);
        boolean requestHasNoIdentifiers = !requestHasDataId && !requestHasDomainId;
        boolean dataIdsAreNotEqual = requestHasDataId &&
                !requestDataId.equals(useCaseDataId);
        boolean domainIdIsAreNotEqual = requestHasDomainId &&
                !requestDomainId.equals(useCaseDomainId);

        if (requestHasNoIdentifiers) {
            reprocessDomainModel();
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
        isDomainDataUnavailable = true;
        System.out.println(TAG + "onDomainModelUnavailable: updatedDomainModel=" + updatedDomainModel);
        reprocessDomainModel();
    }

    @Override
    public void onDomainModelLoaded(PERSISTENCE_MODEL persistenceModel) {
        System.out.println(TAG + "onAllDomainModelsLoaded=" + persistenceModel);
        isDomainDataUnavailable = false;

        createUpdatedDomainModelFromPersistenceModel(persistenceModel);

        activeDomainModel = updatedDomainModel;

        initialiseUseCaseForNewDomainModelProcessing();
    }

    protected void reprocessDomainModel() {
        System.out.println(TAG + "reprocessDomainModel");
        initialiseUseCaseForNewDomainModelProcessing();
    }

    protected void processRequestDomainModel() {
        System.out.println(TAG + "processRequestDomainModel");
        createUpdatedDomainModelFromRequestModel();
        initialiseUseCaseForNewDomainModelProcessing();
    }

    protected abstract void createUpdatedDomainModelFromRequestModel();

    protected abstract void createUpdatedDomainModelFromPersistenceModel(
            @Nonnull PERSISTENCE_MODEL persistenceModel);

    protected abstract void initialiseUseCaseForNewDomainModelProcessing();

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

    protected boolean isDomainModelValid() {
        FailReasons[] noFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] currentFailReasons = failReasons.toArray(new FailReasons[0]);
        return Arrays.equals(
                noFailReasons,
                currentFailReasons
        );
    }

    protected UseCaseMetadataModel getMetadata() {
        return new UseCaseMetadataModel.Builder().
                setFailReasons(new ArrayList<>(getFailReasons())).
                setState(getComponentState()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(persistenceModel == null ? 0L : persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel == null ? 0L : persistenceModel.getLastUpdate()).
                build();
    }

    protected RecipeMetadata.ComponentState getComponentState() {
        return isDomainModelValid() ?
                (isChanged ?
                        RecipeMetadata.ComponentState.VALID_CHANGED:
                        RecipeMetadata.ComponentState.VALID_UNCHANGED):
                (isChanged ?
                        RecipeMetadata.ComponentState.INVALID_CHANGED:
                        RecipeMetadata.ComponentState.INVALID_UNCHANGED);
    }

    protected List<FailReasons> getFailReasons() {
        if (isDomainDataUnavailable) {
            if (!failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
                failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
            }
        }
        return failReasons;
    }
}
