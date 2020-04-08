package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends UseCase
        implements DomainDataAccess.GetAllDomainModelsCallback<RecipeCourseModelPersistence> {

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

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
            for (Course course : Course.values()) {
                courseMap.put(course.courseNo, course);
            }
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

    private String dataId = "";
    private String recipeId = "";
    private final HashMap<Course, RecipeCourseModelPersistence> activeCourseList =
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
    protected <Q extends Request> void execute(Q request) {
        RecipeCourseRequest r = (RecipeCourseRequest) request;
        System.out.println(TAG + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            setupUseCase();
            processRequest();
        }
    }

    private boolean isNewRequest(RecipeCourseRequest r) {
        return !r.getDomainId().equals(recipeId);
    }

    private void setupUseCase() {
        isChanged = false;
        updatedCourseList.clear();
        updatedCourseList.addAll(((RecipeCourseRequest) getRequest()).
                getModel().
                getCourseList());
    }

    private void loadData(String recipeId) {
        repository.getAllByDomainId(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseModelPersistence> courseModels) {
        addCoursesToLists(courseModels);
    }

    private void addCoursesToLists(List<RecipeCourseModelPersistence> courseModels) {
        createDate = courseModels.isEmpty() ? 0L : Long.MAX_VALUE;
        lastUpdate = 0L;

        for (RecipeCourseModelPersistence m : courseModels) {

            Course course = m.getCourse();
            activeCourseList.put(course, m);
            updatedCourseList.add(course);

            createDate = Math.min(createDate, m.getCreateDate());
            lastUpdate = Math.max(lastUpdate, m.getLasUpdate());
        }
        sendResponse();
    }

    @Override
    public void onModelsUnavailable() {
        sendResponse();
    }

    private void processRequest() {
        processCourseAdditions();
        processCourseSubtractions();
        sendResponse();
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
        RecipeCourseModelPersistence model = createNewPersistenceModel(course);
        activeCourseList.put(course, model);
        save(model);
    }

    private void save(RecipeCourseModelPersistence model) {
        repository.save(model);
    }

    private RecipeCourseModelPersistence createNewPersistenceModel(Course course) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCourseModelPersistence.Builder().
                setDataId(idProvider.getUId()).
                setDomainId(recipeId).
                setCourse(course).
                setIsActive(true).
                setCreateDate(lastUpdate).
                setLastUpdate(lastUpdate).
                build();
    }

    private void processCourseSubtractions() {
        Iterator<RecipeCourseModelPersistence> i = activeCourseList.values().iterator();
        while (i.hasNext()) {
            RecipeCourseModelPersistence m = i.next();

            if (isCourseRemoved(m.getCourse())) {
                isChanged = true;
                updateRepository(m);
                i.remove();
            }
        }
    }

    private boolean isCourseRemoved(Course c) {
        return !updatedCourseList.contains(c);
    }

    private void updateRepository(RecipeCourseModelPersistence m) {
        repository.update(getDeactivatedPersistenceModel(m));
    }

    private RecipeCourseModelPersistence getDeactivatedPersistenceModel(
            RecipeCourseModelPersistence m) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCourseModelPersistence.Builder().
                setDataId(m.getDataId()).
                setDomainId(m.getDomainId()).
                setCourse(m.getCourse()).setIsActive(false).
                setCreateDate(m.getCreateDate()).
                setLastUpdate(lastUpdate).
                build();
    }

    private void sendResponse() {
        System.out.println(TAG + "activeCourseList" + activeCourseList);
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setDataId(dataId).
                setDomainId(recipeId).
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        System.out.println(TAG + response);

        if (isValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private UseCaseMetadata getMetadata() {
        return new UseCaseMetadata.Builder().
                setState(getComponentState()).
                setFailReasons(getFailReasons()).
                setCreateDate(createDate).
                setLasUpdate(lastUpdate).
                build();
    }

    private RecipeCourseResponse.Model getResponseModel() {
        return new RecipeCourseResponse.Model.Builder().
                setCourseList(new HashMap<>(activeCourseList)).
                build();
    }

    private RecipeMetadata.ComponentState getComponentState() {
        if (!isValid()) createDate = 0L;

        if (!isValid() && !isChanged) {
            return RecipeMetadata.ComponentState.INVALID_UNCHANGED;
        } else if (isValid() && !isChanged) {
            return RecipeMetadata.ComponentState.VALID_UNCHANGED;
        } else if (!isValid() && isChanged) {
            return RecipeMetadata.ComponentState.INVALID_CHANGED;
        } else {
            return RecipeMetadata.ComponentState.VALID_CHANGED;
        }
    }

    private boolean isValid() {
        return !activeCourseList.isEmpty();
    }

    private List<FailReasons> getFailReasons() {
        List<FailReasons> failReasons = new LinkedList<>();
        if (isValid()) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        } else {
            failReasons.add(CommonFailReason.NONE);
        }
        return failReasons;
    }
}
