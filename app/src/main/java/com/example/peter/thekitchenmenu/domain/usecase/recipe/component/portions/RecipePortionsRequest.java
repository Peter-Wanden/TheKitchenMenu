package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModel;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;

import java.util.Objects;

public final class RecipePortionsRequest
        extends BaseDomainMessageModel<RecipePortionsRequest.Model>
        implements UseCase.Request {

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
            extends UseCaseMessageBuilderModel<Builder, RecipePortionsRequest, Model> {

        public Builder() {
            message = new RecipePortionsRequest();
            message.model = new RecipePortionsRequest.Model();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipePortionsResponse r) {
            message.dataId = r.getDataId();
            message.domainId = r.getDomainId();
            message.model.servings = r.getModel().getServings();
            message.model.sittings = r.getModel().getSittings();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {
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
            Model model = (Model) o;
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

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.servings = RecipePortions.MIN_SERVINGS;
                model.sittings = RecipePortions.MIN_SITTINGS;
                return self();
            }

            public Builder basedResponseModel(RecipePortionsResponse.Model m) {
                model.sittings = m.getSittings();
                model.servings = m.getServings();
                return self();
            }

            public Builder setServings(int servings) {
                model.servings = servings;
                return self();
            }

            public Builder setSittings(int sittings) {
                model.sittings = sittings;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
