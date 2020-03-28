package com.example.peter.thekitchenmenu.domain.usecase;


import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

/**
 * A data structure for storing a use case's metadata state. Typically generated within a use case
 * as it processes a {@link UseCaseRequestWithDomainModel} and sent as a member var in its respective
 * {@link UseCaseResponse}.
 */
public class UseCaseMetadata {

    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    private final long createDate;
    private final long lasUpdate;

    private UseCaseMetadata(@Nonnull ComponentState state,
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
        UseCaseMetadata that = (UseCaseMetadata) o;
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
        private ComponentState state;
        private List<FailReasons> failReasons;
        private long createDate;
        private long lasUpdate;

        public Builder getDefault() {
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

        public UseCaseMetadata build() {
            return new UseCaseMetadata(
                    state,
                    failReasons,
                    createDate,
                    lasUpdate
            );
        }
    }
}
