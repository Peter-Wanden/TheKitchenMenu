package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.MessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList.*;

public class RecipeListRequest
        extends
        MessageModelDataId<RecipeListRequest.Model>
        implements
        UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeListRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeListRequest() {}

    public static class Builder
            extends
            MessageModelDataIdBuilder<Builder, RecipeListRequest, Model> {

        public Builder() {
            message = new RecipeListRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {
        private RecipeListFilter filter;

        public RecipeListFilter getFilter() {
            return filter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return filter == model.filter;
        }

        @Override
        public int hashCode() {
            return Objects.hash(filter);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "filter=" + filter +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                domainModel = new Model();
            }

            @Override
            public Builder getDefault() {
                domainModel.filter = RecipeListFilter.ALL_RECIPES;
                return self();
            }

            public Builder setFilter(RecipeListFilter filter) {
                domainModel.filter = filter;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
