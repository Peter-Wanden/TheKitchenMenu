package com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseMetadataModel {

    @Nonnull
    private final ComponentState componentState;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final String createdBy;
    private final long createDate;
    private final long lastUpdate;

    private UseCaseMetadataModel(@Nonnull ComponentState componentState,
                                 @Nonnull List<FailReasons> failReasons,
                                 @Nonnull String createdBy,
                                 long createDate,
                                 long lastUpdate) {
        this.componentState = componentState;
        this.failReasons = failReasons;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Nonnull
    public ComponentState getComponentState() {
        return componentState;
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
                componentState == that.componentState &&
                failReasons.equals(that.failReasons) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentState, failReasons, createdBy, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "MetadataModel{" +
                "componentState=" + componentState +
                ", failReasons=" + failReasons +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {

        private ComponentState componentState;
        private List<FailReasons> failReasons;
        private String createdBy;
        private long createDate;
        private long lastUpdate;

        public Builder getDefault() {
            return new Builder()
                    .setComponentState(ComponentState.INVALID_UNCHANGED)
                    .setFailReasons(new ArrayList<>())
                    .setCreatedBy("")
                    .setCreateDate(0L)
                    .setLastUpdate(0L);
        }

        public Builder setComponentState(ComponentState componentState) {
            this.componentState = componentState;
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

        public Builder setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public UseCaseMetadataModel build() {
            return new UseCaseMetadataModel(
                    componentState,
                    failReasons,
                    createdBy,
                    createDate,
                    lastUpdate
            );
        }
    }
}
