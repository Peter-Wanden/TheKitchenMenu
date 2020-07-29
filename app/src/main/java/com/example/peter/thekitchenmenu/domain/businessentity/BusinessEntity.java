package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

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

    public static class Response<ENTITY_MODEL extends DomainModel.EntityModel> {
        @Nonnull
        private final ENTITY_MODEL model;
        @Nonnull
        private final List<FailReasons> failReasons;

        public Response(@Nonnull ENTITY_MODEL model, @Nonnull List<FailReasons> failReasons) {
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

    public interface Callback<RESPONSE> {
        void onProcessed(RESPONSE response);
    }

    protected Request<ENTITY_MODEL> request;
    protected ENTITY_MODEL model;
    protected List<FailReasons> failReasons;
    protected Callback<Response<ENTITY_MODEL>> callback;

    public void execute(Request<ENTITY_MODEL> request,
                        Callback<Response<ENTITY_MODEL>> callback) {
        this.request = request;
        this.model = request.getModel();
        this.failReasons = new ArrayList<>();
        this.callback = callback;

        processDataElements();
    }

    protected abstract void processDataElements();

    protected abstract void sendResponse();
}
