package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;


public final class RecipeResponse implements UseCase.Response {
    protected String id;
    protected Model model;

    private RecipeResponse() {}

    public String getId() {
        return id;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse that = (RecipeResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipeResponse response;

        public Builder() {
            response = new RecipeResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(
                    new Model.Builder().
                            getDefault().
                            build()
            );
        }

        public Builder setId(String id) {
            response.id = id;
            return this;
        }

        public Builder setModel(Model model) {
            response.model = model;
            return this;
        }

        public RecipeResponse build() {
            return response;
        }
    }

    public static final class Model extends UseCaseDomainModel {
        private HashMap<RecipeMetadata.ComponentName, UseCaseResponse> componentResponses;

        public Model() {}

        public HashMap<RecipeMetadata.ComponentName, UseCaseResponse> getComponentResponses() {
            return componentResponses;
        }

        public static class Builder extends DomainModelBuilder<
                                Builder,
                                Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Model.Builder().
                        setComponentResponses(getDefaultComponentResponses());
            }

            public Builder setComponentResponses(
                    HashMap<RecipeMetadata.ComponentName, UseCaseResponse> componentResponses) {
                model.componentResponses = componentResponses;
                return self();
            }

            private static HashMap<RecipeMetadata.ComponentName, UseCaseResponse>
            getDefaultComponentResponses() {
                return new LinkedHashMap<>();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
