package com.example.peter.thekitchenmenu.domain.businessentity.recipeIdentity;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipeIdentityEntityModel
        implements
        DomainModel.EntityModel {
    @Nonnull
    private final String title;
    @Nonnull
    private final String description;

    public RecipeIdentityEntityModel(@Nonnull String title, @Nonnull String description) {
        this.title = title;
        this.description = description;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIdentityEntityModel)) return false;

        RecipeIdentityEntityModel that = (RecipeIdentityEntityModel) o;

        if (!title.equals(that.title)) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityEntityModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
