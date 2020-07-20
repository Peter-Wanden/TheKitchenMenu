package com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

public abstract class RequestModelBase
        <REQUEST_MODEL extends DomainModel.RequestModel>
        implements UseCaseBase.Request {

    protected REQUEST_MODEL model;

    public REQUEST_MODEL getModel() {
        return model;
    }

    public static abstract class RequestModelBuilder
            <SELF extends RequestModelBuilder<SELF, MESSAGE, REQUEST_MODEL>,
                    MESSAGE extends RequestModelBase<REQUEST_MODEL>,
                    REQUEST_MODEL extends DomainModel.RequestModel> {

        protected MESSAGE message;

        public SELF setModel(REQUEST_MODEL model) {
            message.model = model;
            return self();
        }

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }

        public abstract SELF getDefault();

        public MESSAGE build() {
            return message;
        }
    }
}
