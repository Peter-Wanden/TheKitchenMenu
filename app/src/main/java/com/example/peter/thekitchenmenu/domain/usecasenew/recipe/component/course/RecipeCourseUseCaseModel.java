package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCaseModel
        extends
        ArrayList<Course>
        implements
        DomainModel, DomainModel.UseCaseModel {

    RecipeCourseUseCaseModel(@Nonnull Collection<? extends Course> c) {
        super(c);
    }

    public RecipeCourseUseCaseModel() {
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeCourseUseCaseModel> {

        public Builder() {
            super(new RecipeCourseUseCaseModel());
        }

        @Override
        public Builder getDefault() {
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeCourseUseCaseModel model) {
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
