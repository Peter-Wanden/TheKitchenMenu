package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse implements UseCaseCommand.Response {

    @Nonnull
    private final RecipeStateCalculator.ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final Model model;

    private RecipeIdentityResponse(@Nonnull RecipeStateCalculator.ComponentState state,
                                   @Nonnull List<FailReasons> failReasons,
                                   @Nonnull Model model) {
        this.state = state;
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public RecipeStateCalculator.ComponentState getState() {
        return state;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityResponse that = (RecipeIdentityResponse) o;
        return state == that.state &&
                failReasons.equals(that.failReasons) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, model);
    }

    @Override
    public String toString() {
        return "RecipeIdentityResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipeStateCalculator.ComponentState state;
        private List<FailReasons> failReasons;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setState(RecipeStateCalculator.ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReasons()).
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        private static List<FailReasons> getDefaultFailReasons() {
            List<FailReasons> failReasons = new ArrayList<>();
            failReasons.add(RecipeIdentity.FailReason.DATA_UNAVAILABLE);
            return failReasons;
        }

        public Builder setState(RecipeStateCalculator.ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(RecipeIdentityResponse.Model model) {
            this.model = model;
            return this;
        }

        public RecipeIdentityResponse build() {
            return new RecipeIdentityResponse(
                    state,
                    failReasons,
                    model
            );
        }
    }

    public static final class Model {
        @Nonnull
        private final String title;
        @Nonnull
        private final String description;
        private final long createDate;
        private final long lastUpdate;

        public Model(@Nonnull String title,
                     @Nonnull String description,
                     long createDate,
                     long lastUpdate) {
            this.title = title;
            this.description = description;
            this.createDate = createDate;
            this.lastUpdate = lastUpdate;
        }

        @Nonnull
        public String getTitle() {
            return title;
        }

        @Nonnull
        public String getDescription() {
            return description;
        }

        public long getCreateDate() {
            return createDate;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return createDate == model.createDate &&
                    lastUpdate == model.lastUpdate &&
                    title.equals(model.title) &&
                    description.equals(model.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description, createDate, lastUpdate);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder {
            private String title;
            private String description;
            private long createDate;
            private long lastUpdate;

            public static Builder getDefault() {
                return new Builder().
                        setTitle("").
                        setDescription("").
                        setCreateDate(0L).
                        setLastUpdate(0L);
            }

            public static Builder basedOn(RecipeIdentityPersistenceModel model) {
                return new Builder().
                        setTitle(model.getTitle()).
                        setDescription(model.getDescription()).
                        setCreateDate(model.getCreateDate()).
                        setLastUpdate(model.getLastUpdate());
            }

            public static Builder basedOn(RecipeIdentityRequest.Model model) {
                return new Builder().
                        setTitle(model.getTitle()).
                        setDescription(model.getDescription());
            }

            public Builder setTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder setDescription(String description) {
                this.description = description;
                return this;
            }

            public Builder setCreateDate(long createDate) {
                this.createDate = createDate;
                return this;
            }

            public Builder setLastUpdate(long lastUpdate) {
                this.lastUpdate = lastUpdate;
                return this;
            }

            public RecipeIdentityResponse.Model build() {
                return new RecipeIdentityResponse.Model(
                        title,
                        description,
                        createDate,
                        lastUpdate
                );
            }
        }
    }
}