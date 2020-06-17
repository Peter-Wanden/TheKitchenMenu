package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

    static final int MINIMUM_COURSE_LIST_SIZE = 1;

    public enum Course {
        DEFAULT_NO_COURSES(0), // default if nothing selected
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
            UseCaseDomainModel, Cloneable {

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

        defaultDomainModel = new DomainModel(Collections.singletonList(Course.DEFAULT_NO_COURSES));
        activeDomainModel = new DomainModel();
        updatedDomainModel = new DomainModel();
    }

    @Override
    protected void createUpdatedDomainModelFromPersistenceModel(
            RecipeCoursePersistenceModel persistenceModel) {
        updatedDomainModel.addAll(persistenceModel.getCourses());

        System.out.println(TAG + "createUpdatedDomainModelFromPersistenceModel: updatedDomainModel=" +
                updatedDomainModel);
    }

    @Override
    protected void createUpdatedDomainModelFromRequestModel() {
        RecipeCourseRequest request = (RecipeCourseRequest) getRequest();
        updatedDomainModel.clear();
        updatedDomainModel.addAll(request.getDomainModel().getCourseList());

        System.out.println(TAG + "createUpdatedDomainModelFromRequestModel: " +
                "\n  - defaultDomainModel= " + defaultDomainModel +
                "\n  - activeDomainModel= " + activeDomainModel +
                "\n  - updatedDomainModel= " + updatedDomainModel
        );
    }

    @Override
    protected void initialiseUseCaseForNewDomainModelProcessing() {
        if (isDomainDataUnavailable) {
            updatedDomainModel.addAll(defaultDomainModel);
            if (updatedDomainModel.contains(Course.DEFAULT_NO_COURSES) && updatedDomainModel.size() > 1) {
                updatedDomainModel.remove(Course.DEFAULT_NO_COURSES);
                if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
                    failReasons.remove(CommonFailReason.DATA_UNAVAILABLE);
                }
            }
        }
        failReasons.clear();
        isChanged = false;
        System.out.println(TAG + "initialiseUseCaseForNewDomainModelProcessing: " +
                "\n  - defaultDomainModel= " + defaultDomainModel +
                "\n  - activeDomainModel= " + activeDomainModel +
                "\n  - updatedDomainModel= " + updatedDomainModel +
                "\n  - isDomainDataUnavailable= " + isDomainDataUnavailable
        );
        validateUpdatedDomainModelElements();
    }

    protected void processUpdatedDomainModel() {
        isChanged = !activeDomainModel.equals(updatedDomainModel);

        System.out.println(TAG + "processUpdatedDomainModel:" +
                "\n  - isChanged=" + isChanged + "\n" +
                "\n  - defaultDomainModel= " + defaultDomainModel +
                "\n  - activeDomainModel=" + activeDomainModel +
                "\n  - updatedDomainModel= " + updatedDomainModel);
        initialiseUseCaseForNewDomainModelProcessing();
    }

    @Override
    protected void validateUpdatedDomainModelElements() {
        System.out.println(TAG + "validateNewDomainModelElements");
        processCourseAdditions();
        processCourseSubtractions();

        save();
    }

    private void processCourseAdditions() {
        System.out.println(TAG + "processCourseAdditions:" +
                " activeDomainModel=" + activeDomainModel +
                " updatedDomainModel=" + updatedDomainModel
        );
        for (Course course : updatedDomainModel) {
            if (isCourseAdded(course)) {
                activeDomainModel.add(course);

                if (isDefaultDomainModel()) {
                    isChanged = false;
                    failReasons.add(FailReason.NO_COURSE_SELECTED);
                } else {
                    isChanged = true;
                    addCommonFailReasonNone();
                }
            }
        }
        System.out.println(TAG + "processCourseAdditions:" +
                " activeDomainModel=" + activeDomainModel +
                " updatedDomainModel=" + updatedDomainModel
        );
    }

    private boolean isDefaultDomainModel() {
        return activeDomainModel.equals(defaultDomainModel);
    }

    private void addCommonFailReasonNone() {
        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private boolean isCourseAdded(Course course) {
        System.out.println(TAG + "isCourseAdded:" + course + " " + !activeDomainModel.contains(course));
        return !activeDomainModel.contains(course);
    }

    private void processCourseSubtractions() {
        System.out.println(TAG + "processCourseSubtractions:" +
                " activeDomainModel=" + activeDomainModel +
                " updatedDomainModel=" + updatedDomainModel
        );

        Iterator<Course> i = activeDomainModel.iterator();
        while (i.hasNext()) {
            Course course = i.next();

            if (isCourseRemoved(course)) {
                i.remove();
                isChanged = true;
            }
        }
        if (activeDomainModel.size() < MINIMUM_COURSE_LIST_SIZE) {
            addCommonFailReasonDataUnavailable();
        }
        System.out.println(TAG + "processCourseSubtractions:" +
                " activeDomainModel=" + activeDomainModel +
                " updatedDomainModel=" + updatedDomainModel
        );
    }

    private void addCommonFailReasonDataUnavailable() {
        if (!failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        }
    }

    private boolean isCourseRemoved(Course course) {
        System.out.println(TAG + "isCourseRemoved=" + course + " " +
                !updatedDomainModel.contains(course));
        return !updatedDomainModel.contains(course);
    }

    @Override
    protected void save() {
        if (isChanged || isDefaultDomainModel()) {

            long createDate;
            if (isDefaultDomainModel()) {
                createDate = timeProvider.getCurrentTimeInMills();
            } else {
                createDate = persistenceModel.getCreateDate();
            }

            persistenceModel = new RecipeCoursePersistenceModel.Builder().
                    setDataId(idProvider.getUId()).
                    setDomainId(useCaseDomainId).
                    setCourses(activeDomainModel).
                    setCreateDate(createDate). // todo
                    setLastUpdate(timeProvider.getCurrentTimeInMills()).
                    build();

            System.out.println(TAG + "save:" + persistenceModel);
            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void buildResponse() {
        System.out.println(TAG + "buildResponse");
        useCaseDataId = persistenceModel.getDataId();

        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        System.out.println(TAG + "Response No:" + accessCount + " - " + response);

        sendResponse(response);
    }

    private RecipeCourseResponse.Model getResponseModel() {
        RecipeCourseResponse.Model responseModel = new RecipeCourseResponse.Model.Builder().
                setCourseList(new ArrayList<>(activeDomainModel)).
                build();

        System.out.println(TAG + "getResponseModel=" + responseModel);
        return responseModel;
    }
}
