package com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.message;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.RequestModelBase;

public class UseCaseRequestWithId<REQUEST_MODEL extends DomainModel.RequestModel>
        extends
        RequestModelBase<REQUEST_MODEL>
        implements
        UseCaseBase.Request {

    public static final String NO_ID = "";

    protected String dataId = NO_ID;
    protected String domainId = NO_ID;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    protected static abstract class IdBuilder<
            SELF extends IdBuilder<SELF, REQUEST_TYPE, REQUEST_MODEL>,
            REQUEST_TYPE extends UseCaseRequestWithId<REQUEST_MODEL>,
            REQUEST_MODEL extends DomainModel.RequestModel>
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
