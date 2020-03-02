package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseAbstract;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipePortionsResponse extends RecipeResponseAbstract {
    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final RecipePortionsResponse.Model model;

    private RecipePortionsResponse(@Nonnull String id,
                                   @Nonnull ComponentState state,
                                   @Nonnull List<FailReasons> failReasons,
                                   @Nonnull RecipePortionsResponse.Model model) {
        this.id = id;
        this.state = state;
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public ComponentState getState() {
        return state;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public RecipePortionsResponse.Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsResponse that = (RecipePortionsResponse) o;
        return id.equals(that.id) &&
                state == that.state &&
                failReasons.equals(that.failReasons) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, failReasons, model);
    }

    @Override
    public String toString() {
        return "RecipePortionsResponse{" +
                "id=" + id +
                ", state=" + state +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String id;
        private ComponentState state;
        private List<FailReasons> failReasons;
        private RecipePortionsResponse.Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setState(ComponentState.INVALID_UNCHANGED).
                    setModel(RecipePortionsResponse.Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setState(ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(RecipePortionsResponse.Model model) {
            this.model = model;
            return this;
        }

        public RecipePortionsResponse build() {
            return new RecipePortionsResponse(
                    id,
                    state,
                    failReasons,
                    model
            );
        }
    }

    public static final class Model {
        private final int servings;
        private final int sittings;
        private final int portions;
        private final long createDate;
        private final long lasUpdate;

        private Model(int servings,
                      int sittings,
                      int portions,
                      long createDate,
                      long lasUpdate) {
            this.servings = servings;
            this.sittings = sittings;
            this.portions = portions;
            this.createDate = createDate;
            this.lasUpdate = lasUpdate;
        }

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        public int getPortions() {
            return portions;
        }

        public long getCreateDate() {
            return createDate;
        }

        public long getLasUpdate() {
            return lasUpdate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings &&
                    portions == model.portions &&
                    createDate == model.createDate &&
                    lasUpdate == model.lasUpdate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings, portions, createDate, lasUpdate);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    ", portions=" + portions +
                    ", createDate=" + createDate +
                    ", lasUpdate=" + lasUpdate +
                    '}';
        }

        public static class Builder {
            private int servings;
            private int sittings;
            private int portions;
            private long createDate;
            private long lasUpdate;

            public static Builder getDefault() {
                return new Builder().
                        setServings(1).
                        setSittings(1).
                        setPortions(1).
                        setCreateDate(0L).
                        setLasUpdate(0L);
            }

            public Builder setServings(int servings) {
                this.servings = servings;
                return this;
            }

            public Builder setSittings(int sittings) {
                this.sittings = sittings;
                return this;
            }

            public Builder setPortions(int portions) {
                this.portions = portions;
                return this;
            }

            public Builder setCreateDate(long createDate) {
                this.createDate = createDate;
                return this;
            }

            public Builder setLasUpdate(long lasUpdate) {
                this.lasUpdate = lasUpdate;
                return this;
            }

            public Model build() {
                return new Model(
                        servings,
                        sittings,
                        portions,
                        createDate,
                        lasUpdate
                );
            }
        }
    }
}