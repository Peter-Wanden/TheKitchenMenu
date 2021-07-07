package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = RecipeIdentityEntity.TABLE_RECIPE_IDENTITY)
public final class RecipeIdentityEntity implements EntityModel {

    public static final String TABLE_RECIPE_IDENTITY = "recipeIdentity";
    public static final String DATA_ID = "dataId";
    public static final String DOMAIN_ID = "domainId";
    public static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DATA_ID)
    private final String dataId;

    @Nonnull
    @ColumnInfo(name = DOMAIN_ID)
    private final String domainId;

    @Nonnull
    @ColumnInfo(name = TITLE)
    private final String title;

    @Nonnull
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeIdentityEntity(@Nonnull String dataId,
                                @Nonnull String domainId,
                                @Nonnull String title,
                                @Nonnull String description,
                                long createDate,
                                long lastUpdate) {
        this.dataId = dataId;
        this.domainId = domainId;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityEntity that = (RecipeIdentityEntity) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                title.equals(that.title) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, title, description, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityEntity{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    @NonNull
    public String getDataId() {
        return dataId;
    }

    @Nonnull
    public String getDomainId() {
        return domainId;
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
}
