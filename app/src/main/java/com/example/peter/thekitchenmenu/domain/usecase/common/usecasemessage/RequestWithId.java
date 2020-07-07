package com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

public abstract class RequestWithId<
        REQUEST_MODEL extends DomainModel.RequestDomainModel>
        extends RequestModelBase<REQUEST_MODEL>
        implements UseCaseBase.Request {

    public static final String NO_ID = "";

    protected String dataId = NO_ID;
    protected String domainId = NO_ID;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    public static abstract class IdBuilder<
            SELF extends IdBuilder<SELF, REQUEST_TYPE, REQUEST_MODEL>,
            REQUEST_TYPE extends RequestWithId<REQUEST_MODEL>,
            REQUEST_MODEL extends DomainModel.RequestDomainModel>
            extends
            RequestModelBuilder<SELF, REQUEST_TYPE, REQUEST_MODEL> {

        public SELF setDataId(String dataId) {
            message.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            message.domainId = domainId;
            return self();
        }
    }
}
