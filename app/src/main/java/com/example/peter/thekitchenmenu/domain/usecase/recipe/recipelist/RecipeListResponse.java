package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeListResponse
        extends
        BaseDomainMessageModelMetadata<RecipeListResponse.Model>
        implements
        UseCase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeListResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public RecipeListResponse(){}

    public static class Builder
            extends UseCaseMessageMetadataBuilder<Builder, RecipeListResponse, Model> {

        public Builder() {
            message = new RecipeListResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadata.Builder().getDefault().build();
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {

        private List<Recipe> recipes;

        private Model(){}

        public List<Recipe> getRecipes() {
            return recipes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(recipes, model.recipes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(recipes);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "recipes=" + recipes +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.recipes = new ArrayList<>();
                return self();
            }

            public Builder setRecipes(List<Recipe> recipes) {
                model.recipes = recipes;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
