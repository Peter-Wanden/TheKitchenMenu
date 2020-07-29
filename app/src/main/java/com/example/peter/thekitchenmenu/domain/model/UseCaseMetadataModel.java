package com.example.peter.thekitchenmenu.domain.model;


import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;

/**
 * A data structure for storing a use case's metadata. Typically generated within a use case
 * as it processes a {@link UseCaseMessageModelDataId} and sent as a member in its respective
 * {@link UseCaseBase.Response}.
 */
public class UseCaseMetadataModel {

    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final String createdBy;
    private final long createDate;
    private final long lastUpdate;

    private UseCaseMetadataModel(@Nonnull ComponentState state,
                                 @Nonnull List<FailReasons> failReasons,
                                 @Nonnull String createdBy,
                                 long createDate,
                                 long lastUpdate) {
        this.state = state;
        this.failReasons = failReasons;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Nonnull
    public ComponentState getComponentState() {
        return state;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public String getCreatedBy() {
        return createdBy;
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
        UseCaseMetadataModel that = (UseCaseMetadataModel) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                state == that.state &&
                failReasons.equals(that.failReasons) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, createdBy, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseMetadata{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lasUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private ComponentState state;
        private List<FailReasons> failReasons;
        private String createdBy;
        private long createDate;
        private long lasUpdate;

        public Builder getDefault() {
            return new Builder().
                    setComponentState(ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReasons()).
                    setCreatedBy(Constants.getUserId()).
                    setCreateDate(0L).
                    setLastUpdate(0L);
        }

        public Builder setComponentState(ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder setCreateDate(long createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdate(long lasUpdate) {
            this.lasUpdate = lasUpdate;
            return this;
        }

        private static List<FailReasons> getDefaultFailReasons() {
            List<FailReasons> failReasons = new ArrayList<>();
            failReasons.add(RecipeMetadata.FailReason.MISSING_REQUIRED_COMPONENTS);
            return failReasons;
        }

        public UseCaseMetadataModel build() {
            return new UseCaseMetadataModel(
                    state,
                    failReasons,
                    createdBy,
                    createDate,
                    lasUpdate
            );
        }
    }
}
