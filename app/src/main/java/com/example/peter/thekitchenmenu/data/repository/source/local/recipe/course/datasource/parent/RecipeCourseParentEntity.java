package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeCourseParentEntity.TABLE_RECIPE_COURSE_PARENT)
public final class RecipeCourseParentEntity
        implements
        EntityModel {

    public static final String TABLE_RECIPE_COURSE_PARENT = "tableRecipeCourseParent";
    public static final String DATA_ID = "dataId";
    public static final String DOMAIN_ID = "domainId";
    public static final String CREATE_DATE = "createDate";
    public static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @NonNull
    @ColumnInfo(name = DOMAIN_ID)
    private final String domainId;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeCourseParentEntity(@NonNull String dataId,
                                    @NonNull String domainId,
                                    long createDate,
                                    long lastUpdate) {
        this.dataId = dataId;
        this.domainId = domainId;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCourseParentEntity)) return false;
        RecipeCourseParentEntity that = (RecipeCourseParentEntity) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                dataId.equals(that.dataId) &&
                domainId.equals(that.domainId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseParentEntity{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    @NonNull
    public String getDataId() {
        return dataId;
    }

    @NonNull
    public String getDomainId() {
        return domainId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public static class Builder {
        private String dataId;
        private String domainId;
        private long createDate;
        private long lastUpdate;

        public Builder getDefault() {
            dataId = "";
            domainId = "";
            createDate = 0L;
            lastUpdate = 0L;
            return this;
        }

        public Builder setDataId(String dataId) {
            this.dataId = dataId;
            return this;
        }

        public Builder setDomainId(String domainId) {
            this.domainId = domainId;
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

        public RecipeCourseParentEntity build() {
            return new RecipeCourseParentEntity(
                    dataId,
                    domainId,
                    createDate,
                    lastUpdate
            );
        }
    }
}
