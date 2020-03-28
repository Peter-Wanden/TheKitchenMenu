package com.example.peter.thekitchenmenu.data.primitivemodel.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeFailReasonPrimitive
        extends UseCaseDomainModel
        implements PrimitiveModel {

    private String id;
    private String recipeId;
    private int failReason;

    protected RecipeFailReasonPrimitive(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeFailReasonPrimitive that = (RecipeFailReasonPrimitive) o;
        return failReason == that.failReason &&
                id.equals(that.id) &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, failReason);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeFailReasonPrimitive{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", failReason=" + failReason +
                '}';
    }

    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    public int getFailReason() {
        return failReason;
    }



    public static class Builder
            extends DomainModelBuilder<Builder, RecipeFailReasonPrimitive> {

        public Builder() {
            model = new RecipeFailReasonPrimitive();
        }

        public Builder setId(String id) {
            model.id = id;
            return self();
        }

        public Builder setRecipeId(String recipeId) {
            model.recipeId = recipeId;
            return self();
        }

        public Builder setFailReason(int failReason) {
            model.failReason = failReason;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
