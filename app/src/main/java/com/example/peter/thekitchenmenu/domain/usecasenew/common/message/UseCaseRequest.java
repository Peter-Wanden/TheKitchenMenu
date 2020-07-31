package com.example.peter.thekitchenmenu.domain.usecasenew.common.message;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public class UseCaseRequest
        <REQUEST_MODEL extends DomainModel.RequestModel>
        implements
        DomainModel.RequestModel {

    protected String dataId;
    protected String domainId;
    protected REQUEST_MODEL requestModel;

    protected UseCaseRequest() {
    }

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    public REQUEST_MODEL getRequestModel() {
        return requestModel;
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", requestModel=" + requestModel +
                '}';
    }

    public static abstract class Builder<
            SELF extends Builder<SELF, REQUEST, REQUEST_MODEL>,
            REQUEST extends UseCaseRequest<REQUEST_MODEL>,
            REQUEST_MODEL extends DomainModel.RequestModel> {

        protected REQUEST useCaseRequest;

        public Builder(REQUEST useCaseRequest) {
            this.useCaseRequest = useCaseRequest;
        }

        public SELF getDefault() {
            useCaseRequest.dataId = "";
            useCaseRequest.domainId = "";
            useCaseRequest.requestModel = null;
            return self();
        }

        public SELF setDataId(String dataId) {
            useCaseRequest.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            useCaseRequest.domainId = domainId;
            return self();
        }

        public SELF setRequestModel(REQUEST_MODEL requestModel) {
            useCaseRequest.requestModel = requestModel;
            return self();
        }

        public REQUEST build() {
            return useCaseRequest;
        }

        public abstract SELF self(); // when implementing return this
    }
}
