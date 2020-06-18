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
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends
        UseCaseElement<
                RecipeCoursePersistenceModel,
                RecipeCourse.DomainModel,
                RepositoryRecipeCourse> {

//    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

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

    public enum FailReason implements FailReasons {
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

    protected static final class DomainModel
            extends
            ArrayList<Course>
            implements
            UseCaseDomainModel {

        public DomainModel() {
        }

        public DomainModel(@NonNull Collection<? extends Course> c) {
            super(c);
        }
    }

    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    public RecipeCourse(@Nonnull RepositoryRecipeCourse repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {

        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        activeDomainModel = new DomainModel();
        updatedDomainModel = new DomainModel();
    }

    @Override
    protected void createUpdatedDomainModelFromPersistenceModel(
            @Nonnull RecipeCoursePersistenceModel persistenceModel) {
        updatedDomainModel = new DomainModel(persistenceModel.getCourses());
        activeDomainModel = new DomainModel(persistenceModel.getCourses());
    }

    @Override
    protected void createUpdatedDomainModelFromDefaultValues() {
        updatedDomainModel.clear();
    }

    @Override
    protected void createUpdatedDomainModelFromRequestModel() {
        RecipeCourseRequest request = (RecipeCourseRequest) getRequest();
        updatedDomainModel = new DomainModel(request.getDomainModel().getCourses());
    }

    @Override
    protected void initialiseUseCaseForUpdatedDomainModelProcessing() {
        failReasons.clear();
        isChanged = !activeDomainModel.equals(updatedDomainModel);
        validateUpdatedDomainModelElements();
    }

    @Override
    protected void validateUpdatedDomainModelElements() {
        processCourseAdditions();
        processCourseSubtractions();

        if (isNoCourseSelected()) {
            failReasons.add(FailReason.NO_COURSE_SELECTED);
        }
        save();
    }

    private void processCourseAdditions() {
        for (Course course : updatedDomainModel) {
            if (isCourseAdded(course)) {
                activeDomainModel.add(course);
                isChanged = true;
            }
        }
    }

    private boolean isCourseAdded(Course course) {
        return !activeDomainModel.contains(course);
    }

    private void processCourseSubtractions() {
        Iterator<Course> i = activeDomainModel.iterator();
        while (i.hasNext()) {
            Course course = i.next();

            if (isCourseRemoved(course)) {
                i.remove();
                isChanged = true;
            }
        }
    }

    private boolean isCourseRemoved(Course course) {
        return !updatedDomainModel.contains(course);
    }

    @Override
    protected void save() {
        isDomainDataUnavailable = isNoCourseSelected();

        if (isChanged || isNoCourseSelected()) {

            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archivePersistenceModel(currentTime);
            }

            persistenceModel = new RecipeCoursePersistenceModel.Builder().
                    setDataId(idProvider.getUId()).
                    setDomainId(useCaseDomainId).
                    setCourses(new ArrayList<>(activeDomainModel)).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    private boolean isNoCourseSelected() {
        return updatedDomainModel.isEmpty();
    }

    private void archivePersistenceModel(long currentTime) {
        RecipeCoursePersistenceModel archivedModel = new RecipeCoursePersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        repository.save(archivedModel);
    }

    @Override
    protected void buildResponse() {
        useCaseDataId = persistenceModel.getDataId();

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
                setCourseList(new ArrayList<>(activeDomainModel)).
                build();
    }
}
