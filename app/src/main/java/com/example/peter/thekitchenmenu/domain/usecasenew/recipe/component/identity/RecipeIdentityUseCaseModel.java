package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityUseCaseModel
        implements
        DomainModel.UseCaseModel {

    @Nonnull
    private final String title;
    @Nonnull
    private final String description;

    RecipeIdentityUseCaseModel(@Nonnull String title, @Nonnull String description) {
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
        if (!(o instanceof RecipeIdentityUseCaseModel)) return false;

        RecipeIdentityUseCaseModel that = (RecipeIdentityUseCaseModel) o;

        if (!Objects.equals(title, that.title)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityUseCaseModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
