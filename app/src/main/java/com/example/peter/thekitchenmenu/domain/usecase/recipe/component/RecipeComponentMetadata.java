package com.example.peter.thekitchenmenu.domain.usecase.recipe.component;


import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * A data structure for storing a single recipe components state.
 * Recipe components send updated metadata in their respective {@link RecipeComponentResponse}
 * each time the components state changes.
 */
public class RecipeComponentMetadata {

    @Nonnull
    private final RecipeMetadata.ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    private final long createDate;
    private final long lasUpdate;

    private RecipeComponentMetadata(@Nonnull RecipeMetadata.ComponentState state,
                                    @Nonnull List<FailReasons> failReasons,
                                    long createDate,
                                    long lasUpdate) {
        this.state = state;
        this.failReasons = failReasons;
        this.createDate = createDate;
        this.lasUpdate = lasUpdate;
    }

    @Nonnull
    public RecipeMetadata.ComponentState getState() {
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
        RecipeComponentMetadata that = (RecipeComponentMetadata) o;
        return state == that.state &&
                failReasons.equals(that.failReasons) &&
                createDate == that.createDate &&
                lasUpdate == that.lasUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, createDate, lasUpdate);
    }

    @Nonnull
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
        private RecipeMetadata.ComponentState state;
        private List<FailReasons> failReasons;
        private long createDate;
        private long lasUpdate;

        public Builder getDefault() {
            return new Builder().
                    setState(RecipeMetadata.ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReasons()).
                    setCreateDate(0L).
                    setLasUpdate(0L);
        }

        public Builder setState(RecipeMetadata.ComponentState state) {
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

        public RecipeComponentMetadata build() {
            return new RecipeComponentMetadata(
                    state,
                    failReasons,
                    createDate,
                    lasUpdate
            );
        }
    }
}
