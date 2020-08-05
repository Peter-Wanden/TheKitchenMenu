package com.example.peter.thekitchenmenu.domain.usecasenew.common.message;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class UseCaseResponse
        <RESPONSE_MODEL extends DomainModel.UseCaseResponseModel> {

    private String dataId;
    private String domainId;
    private UseCaseMetadataModel useCaseMetadataModel;
    private RESPONSE_MODEL responseModel;

    private UseCaseResponse() {}

    private UseCaseResponse(String dataId,
                           String domainId,
                           UseCaseMetadataModel useCaseMetadataModel,
                           RESPONSE_MODEL responseModel) {
        this.dataId = dataId;
        this.domainId = domainId;
        this.useCaseMetadataModel = useCaseMetadataModel;
        this.responseModel = responseModel;
    }

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    public UseCaseMetadataModel getUseCaseMetadataModel() {
        return useCaseMetadataModel;
    }

    public RESPONSE_MODEL getResponseModel() {
        return responseModel;
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", useCaseMetadataModel=" + useCaseMetadataModel +
                ", responseModel=" + responseModel +
                '}';
    }

    public static class Builder
            <RESPONSE_MODEL extends DomainModel.UseCaseResponseModel> {

        private UseCaseResponse<RESPONSE_MODEL> useCaseResponse;

        public Builder() {
            useCaseResponse = new UseCaseResponse<>();
        }

        public Builder<RESPONSE_MODEL> setDataId(String dataId) {
            useCaseResponse.dataId = dataId;
            return this;
        }

        public Builder<RESPONSE_MODEL> setDomainId(String domainId) {
            useCaseResponse.domainId = domainId;
            return this;
        }

        public Builder<RESPONSE_MODEL> setMetadataModel(UseCaseMetadataModel useCaseMetadataModel) {
            useCaseResponse.useCaseMetadataModel = useCaseMetadataModel;
            return this;
        }

        public Builder<RESPONSE_MODEL> setResponseModel(RESPONSE_MODEL responseModel) {
            useCaseResponse.responseModel = responseModel;
            return this;
        }

        public UseCaseResponse<RESPONSE_MODEL> build() {
            return new UseCaseResponse<>(
                    useCaseResponse.dataId,
                    useCaseResponse.domainId,
                    useCaseResponse.useCaseMetadataModel,
                    useCaseResponse.responseModel
            );
        }
    }
}
