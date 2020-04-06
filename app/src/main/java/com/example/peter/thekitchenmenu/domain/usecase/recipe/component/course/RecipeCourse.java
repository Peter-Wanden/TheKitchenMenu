package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
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
        implements DataAccess.GetAllDomainModelsCallback<RecipeCoursePersistenceModel> {

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
    private final HashMap<Course, RecipeCoursePersistenceModel> currentCourseList =
            new LinkedHashMap<>();
    private final List<Course> newCourseList = new ArrayList<>();

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
        newCourseList.clear();
        newCourseList.addAll(((RecipeCourseRequest)getRequest()).
                getModel().
                getCourseList());
    }

    private void loadData(String recipeId) {
        repository.getAllByRecipeId(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCoursePersistenceModel> courseModels) {
        addCoursesToLists(courseModels);
    }

    private void addCoursesToLists(List<RecipeCoursePersistenceModel> courseModels) {
        createDate = courseModels.isEmpty() ? 0L : Long.MAX_VALUE;
        lastUpdate = 0L;

        for (RecipeCoursePersistenceModel courseModel : courseModels) {

            Course course = courseModel.getCourse();
            currentCourseList.put(course, courseModel);
            newCourseList.add(course);

            createDate = Math.min(createDate, courseModel.getCreateDate());
            lastUpdate = Math.max(lastUpdate, courseModel.getLasUpdate());
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
        for (Course c : newCourseList) {
            if (isCourseAddedToNewList(c)) {
                addCourseToCurrentList(c);
            }
        }
    }

    private boolean isCourseAddedToNewList(Course c) {
        return !currentCourseList.containsKey(c);
    }

    private void addCourseToCurrentList(Course course) {
        isChanged = true;
        RecipeCoursePersistenceModel model = createNewPersistence(course);
        currentCourseList.put(course, model);
        save(model);
    }

    private void save(RecipeCoursePersistenceModel model) {
        repository.save(model);
    }

    private void processCourseSubtractions() {
        Iterator<Course> iterator = currentCourseList.keySet().iterator();
        while (iterator.hasNext()) {
            Course c = iterator.next();

            if (isCourseRemovedFromNewList(c)) {
                removeCourseFromCurrentList(iterator, c);
            }
        }
    }

    private boolean isCourseRemovedFromNewList(Course c) {
        return !newCourseList.contains(c);
    }

    private void removeCourseFromCurrentList(Iterator i, Course course) {
        isChanged = true;
        lastUpdate = timeProvider.getCurrentTimeInMills();
        repository.deleteByDataId(currentCourseList.get(course).getDataId());
        i.remove();
    }

    private RecipeCoursePersistenceModel createNewPersistence(Course course) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCoursePersistenceModel.Builder().
                setDataId(idProvider.getUId()).
                setRecipeId(recipeId).
                setCourse(course).
                setCreateDate(lastUpdate).
                setLastUpdate(lastUpdate).
                build();
    }

    private void sendResponse() {
        System.out.println(TAG + "oldCourseMap" + currentCourseList);
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
                setCourseList(new HashMap<>(currentCourseList)).
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
        return !currentCourseList.isEmpty();
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
