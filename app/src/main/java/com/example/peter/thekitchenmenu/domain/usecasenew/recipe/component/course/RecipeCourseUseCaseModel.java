package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

public final class RecipeCourseUseCaseModel
        extends ArrayList<Course>
        implements
        DomainModel.UseCaseModel {

    RecipeCourseUseCaseModel(@Nonnull Collection<? extends Course> c) {
        super(c);
    }
}
