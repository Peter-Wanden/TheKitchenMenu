package com.example.peter.thekitchenmenu.domain.usecase.common;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UseCaseMetadata<
        REPOSITORY extends Repository<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        ENTITY_MODEL extends DomainModel.EntityModel,
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        REQUEST_MODEL extends DomainModel.RequestModel,
        RESPONSE_MODEL extends DomainModel.ResponseModel>

        extends UseCaseModel<
        REPOSITORY,
        PERSISTENCE_MODEL,
        ENTITY_MODEL,
        USE_CASE_MODEL,
        REQUEST_MODEL,
        RESPONSE_MODEL> {

    public enum ComponentState {
        INVALID_DEFAULT(1),
        INVALID_UNCHANGED(2),
        INVALID_CHANGED(3),
        VALID_DEFAULT(4),
        VALID_UNCHANGED(5),
        VALID_CHANGED(6);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, ComponentState> options = new HashMap<>();

        ComponentState(int id) {
            this.id = id;
        }

        static {
            for (ComponentState state : ComponentState.values())
                options.put(state.id, state);
        }

        public static ComponentState fromInt(int id) {
            return options.get(id);
        }

        public int id() {
            return id;
        }
    }

    protected List<FailReasons> failReasons = new ArrayList<>();

    public UseCaseMetadata(REPOSITORY repository,
                           DomainModel.ModelConverter<
                                   ENTITY_MODEL,
                                   USE_CASE_MODEL,
                                   PERSISTENCE_MODEL,
                                   REQUEST_MODEL,
                                   RESPONSE_MODEL> modelConverter,
                           UniqueIdProvider idProvider,
                           TimeProvider timeProvider) {
        super(repository, modelConverter, idProvider, timeProvider);
    }

    @Override
    protected void initialiseUseCase() {
        failReasons.clear();
        validateDomainModelElements();
    }

    protected abstract void validateDomainModelElements();

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
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
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

        return componentState;
    }

    private boolean isDefaultDomainModel() {
        return useCaseModel.equals(createUseCaseModelFromDefaultValues());
    }

    protected boolean isDomainModelValid() {
        return failReasons.isEmpty();
    }

    private void addCommonFailReasons() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
        if (persistenceModel == null) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        }
    }
}
