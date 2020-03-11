package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipePortionsResponse extends RecipeResponseBase {
    @Nonnull
    private final Metadata metadata;
    @Nonnull
    private final RecipePortionsResponse.Model model;

    private RecipePortionsResponse(@Nonnull String id,
                                   @Nonnull Metadata metadata,
                                   @Nonnull RecipePortionsResponse.Model model) {
        this.id = id;
        this.metadata = metadata;
        this.model = model;
    }

    @Nonnull
    public Metadata getMetadata() {
        return metadata;
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
                metadata.equals(that.metadata) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metadata, model);
    }

    @Override
    public String toString() {
        return "RecipePortionsResponse{" +
                "id=" + id +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String id;
        private Metadata metadata;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(Metadata.Builder.
                            getDefault().
                            build()).
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setMetadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder setModel(RecipePortionsResponse.Model model) {
            this.model = model;
            return this;
        }

        public RecipePortionsResponse build() {
            return new RecipePortionsResponse(
                    id,
                    metadata,
                    model
            );
        }
    }

    public static final class Metadata {
        @Nonnull
        private final ComponentState state;
        @Nonnull
        private final List<FailReasons> failReasons;
        private final long createDate;
        private final long lasUpdate;

        private Metadata(@Nonnull ComponentState state,
                         @Nonnull List<FailReasons> failReasons,
                         long createDate,
                         long lasUpdate) {
            this.state = state;
            this.failReasons = failReasons;
            this.createDate = createDate;
            this.lasUpdate = lasUpdate;
        }

        @Nonnull
        public ComponentState getState() {
            return state;
        }

        @Nonnull
        public List<FailReasons> getFailReasons() {
            return failReasons;
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
            Metadata that = (Metadata) o;
            return state == that.state &&
                    failReasons.equals(that.failReasons) &&
                    createDate == that.createDate &&
                    lasUpdate == that.lasUpdate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, failReasons, createDate, lasUpdate);
        }

        @Override
        public String toString() {
            return "MetadataModel{" +
                    "state=" + state +
                    ", failReasons=" + failReasons +
                    ", createDate=" + createDate +
                    ", lasUpdate=" + lasUpdate +
                    '}';
        }

        public static class Builder {
            private ComponentState state;
            private List<FailReasons> failReasons;
            private long createDate;
            private long lasUpdate;

            public static Builder getDefault() {
                return new Builder().
                        setState(ComponentState.INVALID_UNCHANGED).
                        setFailReasons(getDefaultFailReasons()).
                        setCreateDate(0L).
                        setLasUpdate(0L);
            }

            public Builder setState(ComponentState state) {
                this.state = state;
                return this;
            }

            public Builder setFailReasons(List<FailReasons> failReasons) {
                this.failReasons = failReasons;
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

            private static List<FailReasons> getDefaultFailReasons() {
                List<FailReasons> failReasons = new ArrayList<>();
                failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
                return failReasons;
            }

            public Metadata build() {
                return new Metadata(
                        state,
                        failReasons,
                        createDate,
                        lasUpdate
                );
            }
        }
    }

    public static final class Model {
        private final int servings;
        private final int sittings;
        private final int portions;

        private Model(int servings,
                      int sittings,
                      int portions) {
            this.servings = servings;
            this.sittings = sittings;
            this.portions = portions;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings &&
                    portions == model.portions;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings, portions);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    ", portions=" + portions +
                    '}';
        }

        public static class Builder {
            private int servings;
            private int sittings;
            private int portions;

            public static Builder getDefault() {
                return new Builder().
                        setServings(RecipePortions.MIN_SERVINGS).
                        setSittings(RecipePortions.MIN_SITTINGS).
                        setPortions(RecipePortions.MIN_SERVINGS * RecipePortions.MIN_SITTINGS);
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

            public Model build() {
                return new Model(
                        servings,
                        sittings,
                        portions
                );
            }
        }
    }
}