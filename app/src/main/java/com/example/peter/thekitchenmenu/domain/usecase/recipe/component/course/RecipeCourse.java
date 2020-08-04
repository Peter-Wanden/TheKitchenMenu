package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeCourseUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel.UseCaseModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.Course;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCaseFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends
        UseCaseElement<
                RecipeCourseUseCaseDataAccess,
                RecipeCourseUseCasePersistenceModel,
                RecipeCourse.DomainModel> {

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

    protected static final class DomainModel
            extends
            ArrayList<Course>
            implements
            UseCaseModel {

        private DomainModel(@NonNull Collection<? extends Course> c) {
            super(c);
        }
    }

    public RecipeCourse(@Nonnull RecipeCourseUseCaseDataAccess repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        useCaseModel = createUseCaseModelFromDefaultValues();
    }

    @Override
    protected DomainModel createUseCaseModelFromPersistenceModel(
            @Nonnull RecipeCourseUseCasePersistenceModel persistenceModel) {

        return new DomainModel(persistenceModel.getCourses());
    }

    @Override
    protected DomainModel createUseCaseModelFromDefaultValues() {
        return new DomainModel(new ArrayList<>());
    }

    @Override
    protected DomainModel createUseCaseModelFromRequestModel() {
        RecipeCourseRequest.DomainModel requestModel = ((RecipeCourseRequest) getRequest()).
                getDomainModel();

        return new DomainModel(requestModel.getCourses());
    }

    @Override
    protected void validateDomainModelElements() {
        if (useCaseModel.isEmpty()) {
            failReasons.add(RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED);
        }
        save();
    }

    @Override
    protected void save() {
        if ((isChanged && isDomainModelValid())) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archivePreviousState(currentTime);
            }

            persistenceModel = new RecipeCourseUseCasePersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setCourses(new ArrayList<>(useCaseModel)).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }

        buildResponse();
    }

    @Override
    protected void archivePreviousState(long currentTime) {
        RecipeCourseUseCasePersistenceModel archivedModel = new RecipeCourseUseCasePersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        repository.save(archivedModel);
    }

    @Override
    protected void buildResponse() {
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        sendResponse(response);
    }

    private RecipeCourseResponse.DomainModel getResponseModel() {
        return new RecipeCourseResponse.DomainModel.Builder().
                setCourseList(new ArrayList<>(useCaseModel)).
                build();
    }
}