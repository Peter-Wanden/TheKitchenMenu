package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class UseCaseResult<
        DATA_ACCESS extends DomainDataAccess<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        USE_CASE_REQUEST_MODEL extends DomainModel.UseCaseRequestModel,
        USE_CASE_RESPONSE_MODEL extends DomainModel.UseCaseResponseModel>

        extends
        UseCaseData<
                DATA_ACCESS,
                PERSISTENCE_MODEL,
                USE_CASE_MODEL,
                USE_CASE_REQUEST_MODEL,
                USE_CASE_RESPONSE_MODEL> {

    private static final String TAG = "tkm-" + UseCaseResult.class.getSimpleName() + ": ";

    protected List<FailReasons> failReasons = new ArrayList<>();

    public UseCaseResult(DATA_ACCESS dataAccess,
                         DomainModel.Converter<
                                 USE_CASE_MODEL,
                                 PERSISTENCE_MODEL,
                                 USE_CASE_REQUEST_MODEL,
                                 USE_CASE_RESPONSE_MODEL> modelConverter) {
        super(dataAccess, modelConverter);
    }

    @Override
    protected void initialiseUseCase() {
        failReasons.clear();

        if (isDomainDataElementsProcessed()) {
            if (isChanged && isValid()) {
                System.out.println(TAG + "saving changes");
                saveChanges();
            }
            buildResponse();
        }
    }

    protected abstract boolean isDomainDataElementsProcessed();

    protected abstract void buildResponse();

    protected UseCaseMetadataModel getMetadata() {

        UseCaseMetadataModel.Builder builder = new UseCaseMetadataModel.Builder();
        builder.setComponentState(getComponentState());

        addCommonFailReasons();

        builder.setFailReasons(new ArrayList<>(failReasons));
        builder.setCreatedBy(Constants.getUserId());
        builder.setCreateDate(persistenceModel == null ? 0L : persistenceModel.getCreateDate());
        builder.setLastUpdate(persistenceModel == null ? 0L : persistenceModel.getLastUpdate());

        return builder.build();
    }

    protected void sendResponse(UseCaseBase.Response response) {
        if (failReasons.equals(Collections.singletonList(CommonFailReason.NONE))) {
            System.out.println(TAG + "onSuccess: " + response);
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            System.out.println(TAG + "onError: " + response);
            getUseCaseCallback().onUseCaseError(response);
        }
    }

    protected ComponentState getComponentState() {
        ComponentState componentState;

        if (isDefaultDomainModel()) {
            componentState = isValid() ?
                    ComponentState.VALID_DEFAULT :
                    ComponentState.INVALID_DEFAULT;
        } else {
            componentState = isValid() ?
                    (isChanged ? ComponentState.VALID_CHANGED : ComponentState.VALID_UNCHANGED) :
                    (isChanged ? ComponentState.INVALID_CHANGED : ComponentState.INVALID_UNCHANGED);
        }
        return componentState;
    }

    protected boolean isValid() {
        return failReasons.isEmpty();
    }

    private void addCommonFailReasons() {
        if (persistenceModel == null) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        }
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }
}
