package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends
        UseCaseElement<
                RepositoryRecipeCourse,
                RecipeCoursePersistenceModel,
                RecipeCourse.DomainModel> {

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

    protected static final class DomainModel
            extends
            ArrayList<Course>
            implements
            UseCaseDomainModel {

        private DomainModel(@NonNull Collection<? extends Course> c) {
            super(c);
        }
    }

    public enum FailReason
            implements
            FailReasons {
        NO_COURSE_SELECTED(200);

        private final int id;
        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> failReasonMap = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason failReason : FailReason.values()) {
                failReasonMap.put(failReason.id, failReason);
            }
        }

        public static FailReasons fromId(int failReasonId) {
            return failReasonMap.get(failReasonId);
        }

        public int getId() {
            return id;
        }
    }

    public enum Course {
        COURSE_ZERO(0),
        COURSE_ONE(1),
        COURSE_TWO(2),
        COURSE_THREE(3),
        COURSE_FOUR(4),
        COURSE_FIVE(5),
        COURSE_SIX(6),
        COURSE_SEVEN(7),
        COURSE_EIGHT(8);

        private final int id;
        @SuppressLint("UseSparseArrays")
        private static Map<Integer, Course> courseMap = new HashMap<>();

        Course(int id) {
            this.id = id;
        }

        static {
            for (Course course : Course.values()) courseMap.put(course.id, course);
        }

        public static Course fromId(int courseId) {
            return courseMap.get(courseId);
        }

        public int getId() {
            return id;
        }
    }

    public RecipeCourse(@Nonnull RepositoryRecipeCourse repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        domainModel = createDomainModelFromDefaultValues();
    }

    @Override
    protected DomainModel createDomainModelFromPersistenceModel(
            @Nonnull RecipeCoursePersistenceModel persistenceModel) {

        return new DomainModel(persistenceModel.getCourses());
    }

    @Override
    protected DomainModel createDomainModelFromDefaultValues() {
        return new DomainModel(new ArrayList<>());
    }

    @Override
    protected DomainModel createDomainModelFromRequestModel() {
        RecipeCourseRequest.DomainModel requestModel = ((RecipeCourseRequest) getRequest()).
                getDomainModel();

        return new DomainModel(requestModel.getCourses());
    }

    @Override
    protected void validateDomainModelElements() {
        if (domainModel.isEmpty()) {
            failReasons.add(FailReason.NO_COURSE_SELECTED);
        }
        save();
    }

    @Override
    protected void save() {
        if ((isChanged && isDomainModelValid())) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archiveExistingPersistenceModel(currentTime);
            }

            persistenceModel = new RecipeCoursePersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setCourses(new ArrayList<>(domainModel)).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }

        buildResponse();
    }

    @Override
    protected void archiveExistingPersistenceModel(long currentTime) {
        RecipeCoursePersistenceModel archivedModel = new RecipeCoursePersistenceModel.Builder().
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

    private RecipeCourseResponse.Model getResponseModel() {
        return new RecipeCourseResponse.Model.Builder().
                setCourseList(new ArrayList<>(domainModel)).
                build();
    }
}