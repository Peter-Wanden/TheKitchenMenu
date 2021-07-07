package com.example.peter.thekitchenmenu.domain.usecasenew.common.message;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.componentname.UseCaseComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel.UseCaseRequestModel;

import javax.annotation.Nonnull;

public class UseCaseRequestProto {

    protected UseCaseComponentName useCaseComponentName;
    protected String dataId;
    protected String domainId;
    protected UseCaseRequestModel requestModel;

    private UseCaseRequestProto(UseCaseComponentName useCaseComponentName,
                                String dataId,
                                String domainId,
                                UseCaseRequestModel requestModel) {
        this.useCaseComponentName = useCaseComponentName;
        this.dataId = dataId;
        this.domainId = domainId;
        this.requestModel = requestModel;
    }

    public UseCaseComponentName getUseCaseComponentName() {
        return useCaseComponentName;
    }

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    public UseCaseRequestModel getRequestModel() {
        return requestModel;
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseRequestProto{" +
                "componentName=" + useCaseComponentName +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", requestModel=" + requestModel +
                '}';
    }

    public static class Builder {

        protected UseCaseComponentName useCaseComponentName;
        protected String dataId;
        protected String domainId;
        protected UseCaseRequestModel requestModel;

        public Builder setUseCaseComponentName(
                UseCaseComponentName useCaseComponentName) {
            this.useCaseComponentName = useCaseComponentName;
            return this;
        }

        public Builder setDataId(String dataId) {
            this.dataId = dataId;
            return this;
        }

        public Builder setDomainId(String domainId) {
            this.domainId = domainId;
            return this;
        }

        public Builder setRequestModel(UseCaseRequestModel requestModel) {
            this.requestModel = requestModel;
            return this;
        }

        public Builder getDefault() {
            useCaseComponentName = null;
            dataId = "";
            domainId = "";
            requestModel = null;
            return this;
        }

        public UseCaseRequestProto build() {
            return new UseCaseRequestProto(
                    useCaseComponentName,
                    dataId,
                    domainId,
                    requestModel
            );
        }
    }
}
