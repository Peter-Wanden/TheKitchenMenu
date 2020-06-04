package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public class RecipeCourse
        extends UseCaseElement<RecipeCourseRequest.DomainModel>
        implements DomainDataAccess.GetAllDomainModelsCallback<RecipeCoursePersistenceModel> {

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

    @Nonnull
    private RepositoryRecipeCourse repository;
    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    private long createDate;
    private long lastUpdate;
    private boolean isChanged;

    private final HashMap<Course, RecipeCoursePersistenceModel> activeCourseList =
            new LinkedHashMap<>();
    private final List<Course> updatedCourseList = new ArrayList<>();

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
    }

    @Override
    protected void loadDomainModelByDomainId() {
        repository.getAllActiveByDomainId(useCaseDomainId, this);
    }

    private void setupUseCase() {
        isChanged = false;
        updatedCourseList.clear();
        updatedCourseList.addAll(((RecipeCourseRequest) getRequest()).
                getDomainModel().
                getCourseList());
    }

    @Override
    public void onAllLoaded(List<RecipeCoursePersistenceModel> courseModels) {
        addCoursesToLists(courseModels);
    }

    private void addCoursesToLists(List<RecipeCoursePersistenceModel> courseModels) {
        createDate = courseModels.isEmpty() ? 0L : Long.MAX_VALUE;
        lastUpdate = 0L;

        for (RecipeCoursePersistenceModel m : courseModels) {

            Course course = m.getCourse();
            activeCourseList.put(course, m);
            updatedCourseList.add(course);

            createDate = Math.min(createDate, m.getCreateDate());
            lastUpdate = Math.max(lastUpdate, m.getLastUpdate());
        }
        buildResponse();
    }

    @Override
    public void onModelsUnavailable() {
        buildResponse();
    }

    @Override
    protected void processRequestDomainModel() {

    }

    @Override
    protected void reprocessCurrentDomainModel() {
        setupUseCase();
        processCourseAdditions();
        processCourseSubtractions();
        buildResponse();
    }

    private void processCourseAdditions() {
        for (Course c : updatedCourseList) {
            if (isCourseAdded(c)) {
                addCourseToCurrentList(c);
            }
        }
    }

    private boolean isCourseAdded(Course c) {
        return !activeCourseList.containsKey(c);
    }

    private void addCourseToCurrentList(Course course) {
        isChanged = true;
        RecipeCoursePersistenceModel model = createNewPersistenceModel(course);
        activeCourseList.put(course, model);
        save(model);
    }

    private void save(RecipeCoursePersistenceModel model) {
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
        Iterator<RecipeCoursePersistenceModel> iterator = activeCourseList.values().iterator();
        while (iterator.hasNext()) {
            RecipeCoursePersistenceModel m = iterator.next();

            if (isCourseRemoved(m.getCourse())) {
                isChanged = true;
                updateRepository(m);
                iterator.remove();
            }
        }
    }

    private boolean isCourseRemoved(Course c) {
        return !updatedCourseList.contains(c);
    }

    private void updateRepository(RecipeCoursePersistenceModel m) {
        repository.update(getDeactivatedPersistenceModel(m));
    }

    private RecipeCoursePersistenceModel getDeactivatedPersistenceModel(
            RecipeCoursePersistenceModel m) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCoursePersistenceModel.Builder().
                setDataId(m.getDataId()).
                setDomainId(m.getDomainId()).
                setCourse(m.getCourse()).setIsActive(false).
                setCreateDate(m.getCreateDate()).
                setLastUpdate(lastUpdate).
                build();
    }

    protected void buildResponse() {
        RecipeCourseResponse r = new RecipeCourseResponse.Builder().
                setDataId("").
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        System.out.println(TAG + "Response No:" + accessCount + " - " + r);

        sendResponse(r);
    }

    private void sendResponse(RecipeCourseResponse response) {
        if (isValid()) {
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
    }

    private UseCaseMetadataModel getMetadata() {
        return new UseCaseMetadataModel.Builder().
                setState(getComponentState()).
                setFailReasons(getFailReasons()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(createDate).
                setLasUpdate(lastUpdate).
                build();
    }

    private RecipeCourseResponse.Model getResponseModel() {
        return new RecipeCourseResponse.Model.Builder().
                setCourseList(new HashMap<>(activeCourseList)).
                build();
    }

    private boolean isValid() {
        return activeCourseList.size() >= MINIMUM_COURSE_LIST_SIZE;
    }

    protected boolean isDomainModelChanged() {
        return isChanged;
    }
}
