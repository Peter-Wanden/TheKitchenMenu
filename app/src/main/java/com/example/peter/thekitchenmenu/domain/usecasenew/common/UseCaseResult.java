package com.example.peter.thekitchenmenu.domain.usecasenew.common;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class UseCaseResult<
        DATA_ACCESS extends DomainDataAccess<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        REQUEST_MODEL extends DomainModel.RequestModel,
        RESPONSE_MODEL extends DomainModel.ResponseModel>
        extends
        UseCaseData<DATA_ACCESS, PERSISTENCE_MODEL, USE_CASE_MODEL, REQUEST_MODEL, RESPONSE_MODEL> {

    private static final String TAG = "tkm-" + "UseCaseResult" + ": ";

    protected List<FailReasons> failReasons = new ArrayList<>();

    public UseCaseResult(
            DATA_ACCESS dataAccess,
            DomainModel.Converter<
                    USE_CASE_MODEL,
                    PERSISTENCE_MODEL,
                    REQUEST_MODEL,
                    RESPONSE_MODEL> modelConverter) {
        super(dataAccess, modelConverter);
    }

    @Override
    protected void initialiseUseCase() {
        failReasons.clear();
        beginProcessingDomainDataElements();
    }

    protected abstract void beginProcessingDomainDataElements();

    protected void domainDataElementProcessingComplete() {
        if (isChanged && isValid()) {
            saveChanges();
        }
        buildResponse();
    }

    protected void buildResponse() {
        sendResponse(new UseCaseResponse.Builder<RESPONSE_MODEL>()
                .setDataId(useCaseDataId)
                .setDomainId(useCaseDomainId)
                .setMetadataModel(getMetadata())
                .setResponseModel(getResponseModel())
                .build());
    }

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

    protected void sendResponse(UseCaseResponse<RESPONSE_MODEL> response) {
        if (failReasons.equals(Collections.singletonList(CommonFailReason.NONE))) {
            System.out.println(TAG + "onSuccess: " + response);
            getUseCaseCallback().onSuccess(response);
        } else {
            System.out.println(TAG + "onError: " + response);
            getUseCaseCallback().onError(response);
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
