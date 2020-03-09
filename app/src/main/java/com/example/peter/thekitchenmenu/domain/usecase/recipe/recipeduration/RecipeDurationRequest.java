package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationRequest extends RecipeRequestAbstract {
    @Nonnull
    private final RecipeDurationRequest.Model model;

    private RecipeDurationRequest(@Nonnull String id,
                                  @Nonnull String cloneToId,
                                  @Nonnull RecipeDurationRequest.Model model) {
        this.id = id;
        this.cloneToId = cloneToId;
        this.model = model;
    }

    @Nonnull
    public RecipeDurationRequest.Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationRequest request = (RecipeDurationRequest) o;
        return id.equals(request.id) &&
                cloneToId.equals(request.cloneToId) &&
                model.equals(request.model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationRequest{" +
                "id='" + id + '\'' +
                ", cloneToId='" + cloneToId + '\'' +
                ", model=" + model +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cloneToId, model);
    }

    public static class Builder {
        private String id;
        private String cloneToId;
        private RecipeDurationRequest.Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setCloneToId("").
                    setModel(RecipeDurationRequest.Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCloneToId(String cloneToId) {
            this.cloneToId = cloneToId;
            return this;
        }

        public Builder setModel(RecipeDurationRequest.Model model) {
            this.model = model;
            return this;
        }

        public RecipeDurationRequest build() {
            return new RecipeDurationRequest(
                    id,
                    cloneToId,
                    model
            );
        }
    }

    public static final class Model {
        private final int prepHours;
        private final int prepMinutes;
        private final int cookHours;
        private final int cookMinutes;

        private Model(int prepHours, int prepMinutes, int cookHours, int cookMinutes) {
            this.prepHours = prepHours;
            this.prepMinutes = prepMinutes;
            this.cookHours = cookHours;
            this.cookMinutes = cookMinutes;
        }

        public int getPrepHours() {
            return prepHours;
        }

        public int getPrepMinutes() {
            return prepMinutes;
        }

        public int getCookHours() {
            return cookHours;
        }

        public int getCookMinutes() {
            return cookMinutes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return prepHours == model.prepHours &&
                    prepMinutes == model.prepMinutes &&
                    cookHours == model.cookHours &&
                    cookMinutes == model.cookMinutes;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prepHours, prepMinutes, cookHours, cookMinutes);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "prepHours=" + prepHours +
                    ", prepMinutes=" + prepMinutes +
                    ", cookHours=" + cookHours +
                    ", cookMinutes=" + cookMinutes +
                    '}';
        }

        public static class Builder {
            private int prepHours;
            private int prepMinutes;
            private int cookHours;
            private int cookMinutes;

            public static Builder getDefault() {
                return new Builder().
                        setPrepHours(0).
                        setPrepMinutes(0).
                        setCookHours(0).
                        setCookMinutes(0);
            }

            public static Builder basedOnDurationResponseModel(RecipeDurationResponse.Model model) {
                return new Builder().
                        setPrepHours(model.getPrepHours()).
                        setPrepMinutes(model.getPrepMinutes()).
                        setCookHours(model.getCookHours()).
                        setCookMinutes(model.getCookMinutes());
            }

            public Builder setPrepHours(int prepHours) {
                this.prepHours = prepHours;
                return this;
            }

            public Builder setPrepMinutes(int prepMinutes) {
                this.prepMinutes = prepMinutes;
                return this;
            }

            public Builder setCookHours(int cookHours) {
                this.cookHours = cookHours;
                return this;
            }

            public Builder setCookMinutes(int cookMinutes) {
                this.cookMinutes = cookMinutes;
                return this;
            }

            public RecipeDurationRequest.Model build() {
                return new RecipeDurationRequest.Model(
                        prepHours,
                        prepMinutes,
                        cookHours,
                        cookMinutes
                );
            }
        }
    }
}