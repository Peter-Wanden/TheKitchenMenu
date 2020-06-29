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
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

/**
 * Using the state of the data Id's, determines the updatedDomainModel to be processed.
 *
 * @param <PERSISTENCE_MODEL>     the domain model the use case sends to the repository
 * @param <USE_CASE_DOMAIN_MODEL> the use case's internal domain model
 * @param <REPOSITORY>            the repository for the persistence model
 */
public abstract class UseCaseElement<
        REPOSITORY extends Repository<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends BaseDomainPersistenceModel,
        USE_CASE_DOMAIN_MODEL extends UseCaseDomainModel>
        extends
        UseCaseBase
        implements DomainDataAccess.GetDomainModelCallback<PERSISTENCE_MODEL> {

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    protected String useCaseDataId = UseCaseMessageModelDataId.NO_ID;
    protected String useCaseDomainId = UseCaseMessageModelDataId.NO_ID;

    protected REPOSITORY repository;
    protected PERSISTENCE_MODEL persistenceModel;
    protected UniqueIdProvider idProvider;
    protected TimeProvider timeProvider;

    protected USE_CASE_DOMAIN_MODEL domainModel;

    protected List<FailReasons> failReasons = new ArrayList<>();

    protected int accessCount;
    protected boolean isChanged;

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {
        accessCount++;

        UseCaseMessageModelDataId<BaseDomainModel> r = (UseCaseMessageModelDataId<BaseDomainModel>) request;

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
            initialiseUseCaseForDomainModelProcessing();
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
        domainModel = createDomainModelFromDefaultValues();
        System.out.println(TAG + "onDomainModelUnavailable: updatedDomainModel=" + domainModel);
        initialiseUseCaseForDomainModelProcessing();
    }

    @Override
    public void onDomainModelLoaded(PERSISTENCE_MODEL persistenceModel) {
        System.out.println(TAG + "onDomainModelLoaded=" + persistenceModel);

        isChanged = false;
        useCaseDataId = persistenceModel.getDataId();
        useCaseDomainId = persistenceModel.getDomainId();

        this.persistenceModel = persistenceModel;

        domainModel = createDomainModelFromPersistenceModel(persistenceModel);
        initialiseUseCaseForDomainModelProcessing();
    }

    protected void processRequestDomainModel() {
        System.out.println(TAG + "processRequestDomainModel");

        USE_CASE_DOMAIN_MODEL updatedDomainModel = createDomainModelFromRequestModel();
        isChanged = !domainModel.equals(updatedDomainModel);
        domainModel = updatedDomainModel;

        initialiseUseCaseForDomainModelProcessing();
    }

    protected abstract USE_CASE_DOMAIN_MODEL createDomainModelFromPersistenceModel(
            @Nonnull PERSISTENCE_MODEL persistenceModel);

    protected abstract USE_CASE_DOMAIN_MODEL createDomainModelFromRequestModel();

    protected abstract USE_CASE_DOMAIN_MODEL createDomainModelFromDefaultValues();

    protected void initialiseUseCaseForDomainModelProcessing() {
        failReasons.clear();
        validateDomainModelElements();
    }

    protected abstract void validateDomainModelElements();

    protected abstract void save();

    protected abstract void archiveExistingPersistenceModel(long currentTime);

    protected abstract void buildResponse();

    protected void sendResponse(UseCaseBase.Response response) {
        if (failReasons.equals(Collections.singletonList(CommonFailReason.NONE))) {
            System.out.println(TAG + "Response No:" + accessCount + " - " + response);
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            System.out.println(TAG + "Response No:" + accessCount + " - " + response);
            getUseCaseCallback().onUseCaseError(response);
        }
    }

    protected UseCaseMetadataModel getMetadata() {

        UseCaseMetadataModel.Builder builder = new UseCaseMetadataModel.Builder();
        builder.setComponentState(getComponentState());

        addCommonFailReasons();

        builder.setFailReasons(new ArrayList<>(failReasons));
        builder.setCreatedBy(Constants.getUserId());
        builder.setCreateDate(persistenceModel == null ? 0L : persistenceModel.getCreateDate());
        builder.setLastUpdate(persistenceModel == null ? 0L : persistenceModel.getLastUpdate());

        UseCaseMetadataModel metadataModel = builder.build();

        System.out.println(TAG + "metadata= " + metadataModel);

        return metadataModel;
    }

    protected ComponentState getComponentState() {
        ComponentState componentState;

        if (isDefaultDomainModel()) {
            componentState =
                    isDomainModelValid() ?
                            ComponentState.VALID_DEFAULT :
                            ComponentState.INVALID_DEFAULT;
        } else {
            componentState =
                    isDomainModelValid() ?
                            (isChanged ?
                                    ComponentState.VALID_CHANGED :
                                    ComponentState.VALID_UNCHANGED) :
                            (isChanged ?
                                    ComponentState.INVALID_CHANGED :
                                    ComponentState.INVALID_UNCHANGED);
        }

        System.out.println(TAG + "getComponentState= " + componentState);
        return componentState;
    }

    private boolean isDefaultDomainModel() {
        return domainModel.equals(createDomainModelFromDefaultValues());
    }

    protected boolean isDomainModelValid() {
        boolean isValid = failReasons.isEmpty();
        System.out.println(TAG + "isDomainModelValid= " + isValid);
        return isValid;
    }

    protected void addCommonFailReasons() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
        if (persistenceModel == null) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        }
        System.out.println(TAG + "addCommonFailReasons= " + failReasons);
    }
}
