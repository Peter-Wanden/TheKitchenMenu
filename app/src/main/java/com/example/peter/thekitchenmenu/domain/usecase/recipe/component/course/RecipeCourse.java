package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends
        UseCaseElement<RecipeCoursePersistenceModel, RecipeCourse.DomainModel>
        implements
        DomainDataAccess.GetAllDomainModelsCallback<RecipeCoursePersistenceModel> {

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

    static final int MINIMUM_COURSE_LIST_SIZE = 1;

    public enum Course {
        COURSE_ZERO(0),
        COURSE_ONE(1),
        COURSE_TWO(2),
        COURSE_THREE(3),
        COURSE_FOUR(4),
        COURSE_FIVE(5),
        COURSE_SIX(6),
        COURSE_SEVEN(7);

        private final int courseNo;
        @SuppressLint("UseSparseArrays")
        private static Map<Integer, Course> courseMap = new HashMap<>();

        Course(int courseNo) {
            this.courseNo = courseNo;
        }

        static {
            for (Course course : Course.values()) courseMap.put(course.courseNo, course);
        }

        public static Course fromInt(int courseNo) {
            return courseMap.get(courseNo);
        }

        public int getCourseNo() {
            return courseNo;
        }
    }

    protected static class DomainModel implements UseCaseDomainModel {
        private final Set<Course> courses;

        public DomainModel(Set<Course> courses) {
            this.courses = courses;
        }
    }

    @Nonnull
    private RepositoryRecipeCourse repository;
    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    private List<RecipeCoursePersistenceModel> persistenceModels;
    private long createDate;
    private long lastUpdate;

    public RecipeCourse(@Nonnull RepositoryRecipeCourse repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void loadDomainModelByDataId() {
        // only uses domain id's
        reprocessCurrentDomainModel();
    }

    @Override
    protected void loadDomainModelByDomainId() {
        repository.getAllActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void onDomainModelsUnavailable() {
        isDomainDataUnavailable = true;

        persistenceModels = new ArrayList<>();
        activeDomainModel = new DomainModel(new HashSet<>());
        updatedDomainModel = activeDomainModel;

        reprocessCurrentDomainModel();
    }

    @Override
    public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceModel> persistenceModels) {
        isDomainDataUnavailable = false;

        this.persistenceModels = persistenceModels;

        getDatesFromPersistenceModels(persistenceModels);

        updatedDomainModel = getDomainModelFromPersistenceModel();
        activeDomainModel = updatedDomainModel;
        processUpdatedDomainModel();
    }

    private DomainModel getDomainModelFromPersistenceModel() {
        Set<Course> courses = new HashSet<>();
        persistenceModels.forEach(model -> courses.add(model.getCourse()));
        return new DomainModel(courses);
    }

    private void getDatesFromPersistenceModels(
            List<RecipeCoursePersistenceModel> persistenceModels) {
        createDate = persistenceModels.isEmpty() ? 0L : Long.MAX_VALUE;
        lastUpdate = 0L;

        persistenceModels.forEach(model -> {
            createDate = Math.min(createDate, model.getCreateDate());
            lastUpdate = Math.max(lastUpdate, model.getLastUpdate());
        });
    }

    @Override
    protected void processRequestDomainModel() {
        updatedDomainModel = getDomainModelFromRequestModel();
        processUpdatedDomainModel();
    }

    private DomainModel getDomainModelFromRequestModel() {
        Set<Course> updatedCourses = ((RecipeCourseRequest) getRequest()).
                getDomainModel().
                getCourseList();
        return new DomainModel(updatedCourses);
    }

    private void processUpdatedDomainModel() {
        isChanged = !activeDomainModel.equals(updatedDomainModel);
        System.out.println(TAG + "processNewDomainModel: isChanged=" + isChanged);
        if (!isChanged) {
            updatedDomainModel = activeDomainModel;
        }
        validateDomainNewDomainModelElements();
    }

    @Override
    protected void reprocessCurrentDomainModel() {
        processUpdatedDomainModel();
    }

    private void validateDomainNewDomainModelElements() {
        setupDomainModelProcessing();
        processCourseAdditions();
        processCourseSubtractions();
        buildResponse();
    }

    private void setupDomainModelProcessing() {
        failReasons.clear();
        isChanged = false;
    }

    private void processCourseAdditions() {
        for (Course c : updatedDomainModel.courses) {
            if (isCourseAdded(c)) {
                addCourseToCurrentList(c);
            }
        }
    }

    private boolean isCourseAdded(Course course) {
        return !activeDomainModel.courses.contains(course);
    }

    private void addCourseToCurrentList(Course course) {
        isChanged = true;

        save(course);

        Set<Course> courses = activeDomainModel.courses;
        courses.add(course);
        activeDomainModel = new DomainModel(courses);
    }

    private void save(Course course) {
        RecipeCoursePersistenceModel model = createNewPersistenceModel(course);
        repository.save(model);
    }

    private RecipeCoursePersistenceModel createNewPersistenceModel(Course course) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCoursePersistenceModel.Builder().
                setDataId(idProvider.getUId()).
                setDomainId(useCaseDomainId).
                setCourse(course).
                setIsActive(true).
                setCreateDate(lastUpdate).
                setLastUpdate(lastUpdate).
                build();
    }

    private void processCourseSubtractions() {
        Iterator<Course> i = activeDomainModel.courses.iterator();
        while (i.hasNext()) {
            Course course = i.next();

            if (isCourseRemoved(course)) {
                isChanged = true;
                archiveExistingPersistenceModel(course);
                i.remove();

                if (activeDomainModel.courses.size() < MINIMUM_COURSE_LIST_SIZE) {
                    failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
                }
            }
        }
    }

    private boolean isCourseRemoved(Course course) {
        return !updatedDomainModel.courses.contains(course);
    }

    private void archiveExistingPersistenceModel(Course course) {
        RecipeCoursePersistenceModel modelToArchive = null;
        for (RecipeCoursePersistenceModel model : persistenceModels) {
            if (course.equals(model.getCourse())) {
                modelToArchive = model;
            }
        }
        if (modelToArchive != null) {
            archivePersistenceModel(modelToArchive);
            persistenceModels.remove(modelToArchive);
        }
    }

    private void archivePersistenceModel(RecipeCoursePersistenceModel model) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        repository.update(new RecipeCoursePersistenceModel.Builder().
                basedOnModel(model).
                setIsActive(false).
                setLastUpdate(lastUpdate).
                build()
        );
    }

    protected void buildResponse() {
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setDataId("").
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        System.out.println(TAG + "Response No:" + accessCount + " - " + response);

        sendResponse(response);
    }

    private void sendResponse(RecipeCourseResponse response) {
        if (isDomainModelValid()) {
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
    }

    private RecipeCourseResponse.Model getResponseModel() {
        return new RecipeCourseResponse.Model.Builder().
                setCourseList(new ArrayList<>(activeDomainModel.courses)).
                build();
    }
}
