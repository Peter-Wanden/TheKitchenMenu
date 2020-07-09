package com.example.peter.thekitchenmenu.domain.entity;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class BusinessEntity<ENTITY_MODEL extends DomainModel.EntityModel> {

    public static class Request<ENTITY_MODEL extends DomainModel.EntityModel> {
        @Nonnull
        private final ENTITY_MODEL model;

        public Request(@Nonnull ENTITY_MODEL model) {
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

    protected List<FailReasons> failReasons = new ArrayList<>();
    protected EntityCallback<EntityResponse<ENTITY_MODEL>> entityCallback;
    protected ENTITY_MODEL model;

    public void execute(Request<ENTITY_MODEL> request,
                        EntityCallback<EntityResponse<ENTITY_MODEL>> entityCallback) {
        this.entityCallback = entityCallback;
        this.model = request.getModel();

        processElements();
    }

    protected abstract void processElements();

    protected abstract void sendResponse();
}
