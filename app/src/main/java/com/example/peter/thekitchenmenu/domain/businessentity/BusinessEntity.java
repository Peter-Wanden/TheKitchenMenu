package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class BusinessEntity<ENTITY_MODEL extends DomainModel.EntityModel> {

    public static class EntityRequest<ENTITY_MODEL extends DomainModel.EntityModel> {
        @Nonnull
        private final ENTITY_MODEL model;

        public EntityRequest(@Nonnull ENTITY_MODEL model) {
            this.model = model;
        }

        @Nonnull
        public ENTITY_MODEL getModel() {
            return model;
        }
    }

    public static class EntityResponse<ENTITY_MODEL extends DomainModel.EntityModel> {
        @Nonnull
        private final ENTITY_MODEL model;
        @Nonnull
        private final List<FailReasons> failReasons;

        public EntityResponse(@Nonnull ENTITY_MODEL model, @Nonnull List<FailReasons> failReasons) {
            this.model = model;
            this.failReasons = failReasons;
        }

        @Nonnull
        public ENTITY_MODEL getModel() {
            return model;
        }

        @Nonnull
        public List<FailReasons> getFailReasons() {
            return failReasons;
        }
    }

    public interface EntityCallback<RESPONSE> {
        void onProcessed(RESPONSE response);
    }

    protected EntityRequest<ENTITY_MODEL> request;
    protected ENTITY_MODEL model;
    protected List<FailReasons> failReasons;
    protected EntityCallback<EntityResponse<ENTITY_MODEL>> callback;

    public void execute(EntityRequest<ENTITY_MODEL> request,
                        EntityCallback<EntityResponse<ENTITY_MODEL>> callback) {
        this.request = request;
        this.model = request.getModel();
        this.failReasons = new ArrayList<>();
        this.callback = callback;

        processDataElements();
    }

    protected abstract void processDataElements();

    protected abstract void sendResponse();
}
