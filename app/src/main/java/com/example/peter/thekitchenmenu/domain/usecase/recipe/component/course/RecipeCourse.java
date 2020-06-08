package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourses;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends
        UseCaseElement<RecipeCoursePersistenceModel, RecipeCourse.DomainModel>
        implements
        DomainDataAccess.GetDomainModelCallback<RecipeCoursePersistenceModel> {

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

    protected static final class DomainModel implements UseCaseDomainModel {
        private final Set<Course> courses;

        public DomainModel(Set<Course> courses) {
            this.courses = courses;
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    "courses=" + courses +
                    '}';
        }
    }

    @Nonnull
    private RepositoryRecipeCourses repository;
    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    private Set<RecipeCoursePersistenceModelItem> persistenceModelItems;
    private long createDate;
    private long lastUpdate;

    public RecipeCourse(@Nonnull RepositoryRecipeCourses repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void loadDomainModelByDataId() {
        System.out.println(TAG + "loadDomainModelByDataId called, this shouldn't be called");
        // only uses domain id's, this shouldn't be called
        reprocessCurrentDomainModel();
    }

    @Override
    protected void loadDomainModelByDomainId() {
        System.out.println(TAG + "loadDomainModelByDomainId called");
        repository.getActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void onDomainModelUnavailable() {
        System.out.println(TAG + "onDomainModelsUnavailable");
        isDomainDataUnavailable = true;

        persistenceModelItems = new HashSet<>();
        activeDomainModel = new DomainModel(new HashSet<>());
        updatedDomainModel = activeDomainModel;

        reprocessCurrentDomainModel();
    }

    @Override
    public void onDomainModelLoaded(RecipeCoursePersistenceModel persistenceModel) {
        System.out.println(TAG + "onAllDomainModelsLoaded=" + persistenceModel);
        isDomainDataUnavailable = false;

        this.persistenceModelItems = new HashSet<>(persistenceModel.getPersistenceModelItems());

        getDomainModelFromPersistenceModel();
        getDatesFromPersistenceModels();

        activeDomainModel = updatedDomainModel;
        processUpdatedDomainModel();
    }

    private void getDomainModelFromPersistenceModel() {
        Set<Course> domainItemModels = new HashSet<>();
        persistenceModelItems.forEach(model -> domainItemModels.add(model.getCourse()));
        updatedDomainModel = new DomainModel(domainItemModels);

        System.out.println(TAG + "getDomainModelFromPersistenceModel: updatedDomainModel=" +
                updatedDomainModel);
    }

    private void getDatesFromPersistenceModels() {
        createDate = persistenceModelItems.isEmpty() ? 0L : Long.MAX_VALUE;
        lastUpdate = 0L;

        persistenceModelItems.forEach(model -> {
            createDate = Math.min(createDate, model.getCreateDate());
            lastUpdate = Math.max(lastUpdate, model.getLastUpdate());
        });
        System.out.println(TAG + "getDatesFromPersistenceModels: createDate=" + createDate +
                " lastUpdate=" + lastUpdate);
    }

    @Override
    protected void processRequestDomainModel() {
        System.out.println(TAG + "processRequestDomainModel");
        getDomainModelFromRequestModel();
        processUpdatedDomainModel();
    }

    private void getDomainModelFromRequestModel() {
        Set<Course> domainModelItems = ((RecipeCourseRequest) getRequest()).
                getDomainModel().
                getCourseList();
        updatedDomainModel = new DomainModel(domainModelItems);

        System.out.println(TAG + "getDomainModelFromRequestModel: updatedDomainModel=" +
                updatedDomainModel);
    }

    @Override
    protected void reprocessCurrentDomainModel() {
        System.out.println(TAG + "reprocessCurrentDomainModel");
        processUpdatedDomainModel();
    }

    private void processUpdatedDomainModel() {
        isChanged = !activeDomainModel.equals(updatedDomainModel);
        System.out.println(TAG + "processNewDomainModel: isChanged=" + isChanged);
        if (!isChanged) {
            updatedDomainModel = activeDomainModel;
        }
        validateNewDomainModelElements();
    }

    private void validateNewDomainModelElements() {
        System.out.println(TAG + "validateNewDomainModelElements");
        setupDomainModelProcessing();
        processCourseAdditions();
        processCourseSubtractions();
        buildResponse();
    }

    private void setupDomainModelProcessing() {
        System.out.println(TAG + "setupDomainModelProcessing");
        failReasons.clear();
        isChanged = false;
    }

    private void processCourseAdditions() {
        System.out.println(TAG + "processCourseAdditions");
        for (Course c : updatedDomainModel.courses) {
            if (isCourseAdded(c)) {
                addCourseActiveDomainModelList(c);
            }
        }
    }

    private boolean isCourseAdded(Course course) {
        System.out.println(TAG + "isCourseAdded=" + !activeDomainModel.courses.contains(course));
        return !activeDomainModel.courses.contains(course);
    }

    private void addCourseActiveDomainModelList(Course course) {
        System.out.println(TAG + "addCourseToCurrentList: adding course=" + course);
        isChanged = true;

        Set<Course> courses = new HashSet<>(activeDomainModel.courses);
        courses.add(course);
        activeDomainModel = new DomainModel(courses);

        failReasons.add(CommonFailReason.NONE);

        addCourseToPersistenceModel(course);
    }

    private void addCourseToPersistenceModel(Course course) {
        isDomainDataUnavailable = false; // domain persistence models now exist
        RecipeCoursePersistenceModelItem model = createNewPersistenceModelItem(course);
        persistenceModelItems.add(model);
        System.out.println(TAG + "addCourseToPersistenceModel: persistence model=" + model);
        save();
    }

    private void save() {
        persistenceModel = new RecipeCoursePersistenceModel.Builder().
                getDefault().
                baseOnModel(persistenceModel).
                setPersistenceModelItems(persistenceModelItems).
                setCreateDate(createDate).
                setLastUpdate(lastUpdate).
                build();

        System.out.println(TAG + "save: saving persistence model=" + persistenceModel);
        repository.save(persistenceModel);
    }

    private RecipeCoursePersistenceModelItem createNewPersistenceModelItem(Course course) {
        System.out.println(TAG + "createNewPersistenceModel for: " + course);
        long currentTime = timeProvider.getCurrentTimeInMills();
        lastUpdate = currentTime;
        if (createDate == 0L) { // Create date has never been set
            createDate = currentTime;
        }
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId(idProvider.getUId()).
                setDomainId(useCaseDomainId).
                setCourse(course).
                setIsActive(true).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
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
            }
        }
        if (activeDomainModel.courses.size() < MINIMUM_COURSE_LIST_SIZE) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        }
        System.out.println(TAG + "processCourseSubtractions: courses=" + activeDomainModel.courses);
    }

    private boolean isCourseRemoved(Course course) {
        System.out.println(TAG + "isCourseRemoved: " + course + " " +
                !updatedDomainModel.courses.contains(course));
        return !updatedDomainModel.courses.contains(course);
    }

    private void archiveExistingPersistenceModel(Course course) {
        System.out.println(TAG + "archiveExistingPersistenceModel");
        RecipeCoursePersistenceModelItem modelToArchive = null;
        for (RecipeCoursePersistenceModelItem model : persistenceModelItems) {
            if (course.equals(model.getCourse())) {
                modelToArchive = model;
            }
        }
        if (modelToArchive != null) {
            archivePersistenceModelItem(modelToArchive);
        }
    }

    private void archivePersistenceModelItem(RecipeCoursePersistenceModelItem model) {
        System.out.println(TAG + "archivePersistenceModel: " + model);
        lastUpdate = timeProvider.getCurrentTimeInMills();
        persistenceModelItems.remove(model);

        RecipeCoursePersistenceModelItem archivedPersistenceModelItem =
                new RecipeCoursePersistenceModelItem.Builder().
                        basedOnModel(model).
                        setIsActive(false).
                        setLastUpdate(lastUpdate).
                        build();

        persistenceModelItems.add(archivedPersistenceModelItem);
        save();
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

    private RecipeCourseResponse.Model getResponseModel() {
        System.out.println(TAG + "getResponseModel");
        return new RecipeCourseResponse.Model.Builder().
                setCourseList(new ArrayList<>(activeDomainModel.courses)).
                build();
    }
}
