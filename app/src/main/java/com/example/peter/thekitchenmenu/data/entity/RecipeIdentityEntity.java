package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.UseCaseRecipeIdentity;

import java.util.Objects;

/**
 * Used only for data access. For the corresponding domain model
 * see {@link UseCaseRecipeIdentity.Model}
 */
@Entity(tableName = RecipeIdentityEntity.TABLE_RECIPE_IDENTITY)
public final class RecipeIdentityEntity implements TkmEntity {

    public static final String TABLE_RECIPE_IDENTITY = "recipeIdentity";
    public static final String ID = "id";
    public static final String RECIPE_ID = "recipeId";
    public static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CREATE_DATE = "createDate";
    private static final String LAST_UPDATE = "lastUpdate";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @NonNull
    @ColumnInfo(name = TITLE)
    private final String title;

    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @ColumnInfo(name = CREATE_DATE)
    private final long createDate;

    @ColumnInfo(name = LAST_UPDATE)
    private final long lastUpdate;

    public RecipeIdentityEntity(@NonNull String id,
                                @NonNull String title,
                                String description,
                                long createDate,
                                long lastUpdate) {
        this.id = id;
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
                id.equals(that.id) &&
                title.equals(that.title) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                title,
                description,
                createDate,
                lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeIdentityEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

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
