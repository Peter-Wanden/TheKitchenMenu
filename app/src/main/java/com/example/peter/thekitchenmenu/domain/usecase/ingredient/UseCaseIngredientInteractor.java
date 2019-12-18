package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;

import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseIngredientInteractor
        extends
        UseCaseInteractor<UseCaseIngredientInteractor.Request, UseCaseIngredientInteractor.Response> {

    private static final String TAG = "tkm-" + UseCaseIngredientInteractor.class.getSimpleName() +
            ":";

    private UseCaseIngredient ingredientEditor;
    private UseCaseTextValidator textValidator;

    private Model requestModel;
    private Model responseModel;

    public UseCaseIngredientInteractor(UseCaseIngredient ingredientEditor,
                                       UseCaseTextValidator textValidator) {
        this.ingredientEditor = ingredientEditor;
        this.textValidator = textValidator;
    }

    @Override
    protected void execute(Request request) {
        if (isNewRequest()) {
            // todo, send request to IngredientEditor
            requestModel = request.getModel();
        } else if (nameHasChanged(request.getModel().getName())) {
            // todo, send request to textValidator
        } else if (descriptionHasChanged(request.getModel().getDescription())) {
            // todo, send request to textValidator
        }
    }

    private boolean isNewRequest() {
        return requestModel == null;
    }

    private boolean nameHasChanged(String name) {
        if (responseModel != null) {
            return !name.trim().equals(responseModel.getName().trim());
        } else {
            return true;
        }
    }

    private boolean descriptionHasChanged(String description) {
        if (responseModel != null) {
            return !description.trim().equals(responseModel.getDescription().trim());
        } else {
            return true;
        }
    }

    private void sendRequestToIngredientEditor() {
        UseCaseIngredient.Model model = new UseCaseIngredient.Model.Builder().
                setIngredientId(requestModel.getIngredientId()).
                setName(requestModel.getName()).
                setDescription(requestModel.getDescription()).
    }

    public static final class Model {
        @Nonnull
        private final String ingredientId;
        @Nonnull
        private final String name;
        @Nonnull
        private final String description;

        public Model(@Nonnull String ingredientId,
                     @Nonnull String name,
                     @Nonnull String description) {
            this.ingredientId = ingredientId;
            this.name = name;
            this.description = description;
        }

        @Nonnull
        public String getIngredientId() {
            return ingredientId;
        }

        @Nonnull
        public String getName() {
            return name;
        }

        @Nonnull
        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return ingredientId.equals(model.ingredientId) &&
                    name.equals(model.name) &&
                    description.equals(model.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ingredientId, name, description);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "ingredientId='" + ingredientId + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        public static class Builder {
            private String ingredientId;
            private String name;
            private String description;

            public static Builder basedOn(@Nonnull Model model) {
                return new Builder().
                        setIngredientId(model.getIngredientId()).
                        setName(model.getName()).
                        setDescription(model.getDescription());
            }

            public Builder setIngredientId(String ingredientId) {
                this.ingredientId = ingredientId;
                return this;
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setDescription(String description) {
                this.description = description;
                return this;
            }

            public Model build() {
                return new Model(
                        ingredientId,
                        name,
                        description
                );
            }
        }
    }

    public static final class Request implements UseCaseInteractor.Request {
        @Nonnull
        private final Model model;

        public Request(@Nonnull Model model) {
            this.model = model;
        }

        @Nonnull
        public Model getModel() {
            return model;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "model=" + model +
                    '}';
        }
    }

    public static final class Response implements UseCaseInteractor.Response {
        private final Model model;
        private final UseCaseTextValidator.Result nameValidationResult;
        private final UseCaseTextValidator.Result descriptionValidationResult;
        private final UseCaseIngredient.Result ingredientModelResult;
    }
}
