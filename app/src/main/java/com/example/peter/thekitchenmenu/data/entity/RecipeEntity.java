package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.app.Constants;

import java.util.Calendar;
import java.util.UUID;

@Entity(tableName = RecipeEntity.TABLE_RECIPE)
public class RecipeEntity {

    public static final String TAG = "RecipeEntity";

    public static final String TABLE_RECIPE = "recipe";
    public static final String ID = "id";
    public static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = ID)
    private final String id;

    @Nullable
    @ColumnInfo(name = TITLE)
    private final String title;

    @NonNull
    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    @ColumnInfo(name = "preparationTime")
    private final long preparationTime;

    @ColumnInfo(name = "cookingTime")
    private final long cookingTime;

    @ColumnInfo(name = "createdBy")
    private final String createdBy;

    @Nullable
    @ColumnInfo(name = "webImageUrl")
    private final String webImageUrl;

    @Nullable
    @ColumnInfo(name = "remoteSmallImageUri")
    private final String remoteSmallImageUri;

    @Nullable
    @ColumnInfo(name = "remoteMediumImageUri")
    private final String remoteMediumImageUri;

    @Nullable
    @ColumnInfo(name = "remoteLargeImageUri")
    private final String remoteLargeImageUri;

    @ColumnInfo(name = "createDate")
    private final long createDate;

    @ColumnInfo(name = "lastUpdate")
    private final long lastUpdate;

    public RecipeEntity(@NonNull String id,
                        @Nullable String title,
                        @NonNull String description,
                        long preparationTime,
                        long cookingTime,
                        String createdBy,
                        @Nullable String webImageUrl,
                        @Nullable String remoteSmallImageUri,
                        @Nullable String remoteMediumImageUri,
                        @Nullable String remoteLargeImageUri,
                        long createDate,
                        long lastUpdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.createdBy = createdBy;
        this.webImageUrl = webImageUrl;
        this.remoteSmallImageUri = remoteSmallImageUri;
        this.remoteMediumImageUri = remoteMediumImageUri;
        this.remoteLargeImageUri = remoteLargeImageUri;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Ignore
    public static RecipeEntity createRecipe(String title,
                                            String description,
                                            long preparationTime,
                                            long cookingTime,
                                            String webImageUrl,
                                            String remoteSmallImageUri,
                                            String remoteMediumImageUri,
                                            String remoteLargeImageUri) {
        return new RecipeEntity(UUID.randomUUID().toString(),
                title, description,
                preparationTime,
                cookingTime,
                Constants.getUserId().getValue(),
                webImageUrl,
                remoteSmallImageUri,
                remoteMediumImageUri,
                remoteLargeImageUri,
                getDate(),
                getDate());
    }

    @Ignore
    public static RecipeEntity updateRecipe(String id,
                                            String title,
                                            String description,
                                            long preparationTime,
                                            long cookingTime,
                                            String createdBy,
                                            String webImageUrl,
                                            String remoteSmallImageUri,
                                            String remoteMediumImageUri,
                                            String remoteLargeImageUri,
                                            long createDate) {
        return new RecipeEntity(id,
                title,
                description,
                preparationTime,
                cookingTime,
                createdBy,
                webImageUrl,
                remoteSmallImageUri,
                remoteMediumImageUri,
                remoteLargeImageUri, createDate, getDate());
    }

    private static long getDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public long getPreparationTime() {
        return preparationTime;
    }

    public long getCookingTime() {
        return cookingTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Nullable
    public String getWebImageUrl() {
        return webImageUrl;
    }

    @Nullable
    public String getRemoteSmallImageUri() {
        return remoteSmallImageUri;
    }

    @Nullable
    public String getRemoteMediumImageUri() {
        return remoteMediumImageUri;
    }

    @Nullable
    public String getRemoteLargeImageUri() {
        return remoteLargeImageUri;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
