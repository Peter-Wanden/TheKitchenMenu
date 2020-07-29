package com.example.peter.thekitchenmenu.domain.usecasenew.common.message;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public final class UseCaseRequest
        <REQUEST_MODEL extends DomainModel.RequestModel>
        implements
        DomainModel.RequestModel {

    private String dataId;
    private String domainId;
    private REQUEST_MODEL requestModel;

    private UseCaseRequest() {}

    private UseCaseRequest(String dataId, String domainId, REQUEST_MODEL requestModel) {
        this.dataId = dataId;
        this.domainId = domainId;
        this.requestModel = requestModel;
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

    public static class Builder
            <REQUEST_MODEL extends DomainModel.RequestModel> {

        private UseCaseRequest<REQUEST_MODEL> useCaseRequest;

        public Builder() {
            useCaseRequest = new UseCaseRequest<>();
        }

        public Builder<REQUEST_MODEL> getDefault() {
            useCaseRequest.dataId = "";
            useCaseRequest.domainId = "";
            useCaseRequest.requestModel = null;
            return this;
        }

        public Builder<REQUEST_MODEL> setDataId(String dataId) {
            useCaseRequest.dataId = dataId;
            return this;
        }

        public Builder<REQUEST_MODEL> setDomainId(String domainId) {
            useCaseRequest.domainId = domainId;
            return this;
        }

        public Builder<REQUEST_MODEL> setRequestModel(REQUEST_MODEL requestModel) {
            useCaseRequest.requestModel = requestModel;
            return this;
        }

        public UseCaseRequest<REQUEST_MODEL> build() {
            return new UseCaseRequest<>(
                    useCaseRequest.dataId,
                    useCaseRequest.domainId,
                    useCaseRequest.requestModel
            );
        }
    }
}
