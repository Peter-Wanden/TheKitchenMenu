package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;

import java.util.Objects;

public final class RecipePortionsRequest
        extends UseCaseMessageModelDataId<RecipePortionsRequest.DomainModel>
        implements UseCaseBase.Request {

    @Override
    public String toString() {
        return "RecipePortionsRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipePortionsRequest() {}

    public static class Builder
            extends UseCaseMessageModelDataIdBuilder<Builder, RecipePortionsRequest, DomainModel> {

        public Builder() {
            message = new RecipePortionsRequest();
            message.model = new DomainModel();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new DomainModel.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipePortionsResponse r) {
            message.dataId = r.getDataId();
            message.domainId = r.getDomainId();
            message.model.servings = r.getDomainModel().getServings();
            message.model.sittings = r.getDomainModel().getSittings();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class DomainModel extends BaseDomainModel {
        private int servings;
        private int sittings;

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DomainModel model = (DomainModel) o;
            return servings == model.servings &&
                    sittings == model.sittings;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                domainModel = new DomainModel();
            }

            public Builder getDefault() {
                domainModel.servings = RecipePortions.MIN_SERVINGS;
                domainModel.sittings = RecipePortions.MIN_SITTINGS;
                return self();
            }

            public Builder basedResponseModel(RecipePortionsResponse.Model m) {
                domainModel.sittings = m.getSittings();
                domainModel.servings = m.getServings();
                return self();
            }

            public Builder setServings(int servings) {
                domainModel.servings = servings;
                return self();
            }

            public Builder setSittings(int sittings) {
                domainModel.sittings = sittings;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
