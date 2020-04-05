package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse;

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

    private String id = "";
    private String recipeId = "";
    private final HashMap<Course, RecipeCoursePersistenceModel> oldCourseMap = new LinkedHashMap<>();
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
        RecipeCourseRequest courseRequest = (RecipeCourseRequest) request;
        System.out.println(TAG + courseRequest);

        if (isNewRequest(courseRequest.getDomainId())) {
            recipeId = courseRequest.getDomainId();
            loadData(recipeId);
        } else {
            processChanges(courseRequest.getModel().getCourseList());
        }
    }

    private boolean isNewRequest(String recipeId) {
        return !recipeId.equals(this.recipeId);
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
            oldCourseMap.put(course, courseModel);
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

    private void processChanges(List<Course> courseList) {
        isChanged = false;
        newCourseList.clear();
        newCourseList.addAll(courseList);
        updateCourses();
        sendResponse();
    }

    private void updateCourses() {
        processCourseAdditions();
        processCourseSubtractions();
    }

    private void processCourseAdditions() {
        for (Course course : newCourseList) {
            if (!oldCourseMap.containsKey(course)) {
                addCourse(course);
            }
        }
    }

    private void addCourse(Course course) {
        isChanged = true;
        RecipeCoursePersistenceModel model = createNewCourseModel(course);
        oldCourseMap.put(course, model);
        save(model);
    }

    private void save(RecipeCoursePersistenceModel model) {
        repository.save(model);
    }

    private void processCourseSubtractions() {
        Iterator<Map.Entry<Course, RecipeCoursePersistenceModel>> courseIterator =
                oldCourseMap.entrySet().iterator();

        while (courseIterator.hasNext()) {
            Map.Entry<Course, RecipeCoursePersistenceModel> course = courseIterator.next();
            if (!newCourseList.contains(course.getKey())) {
                subtractCourse(course.getKey());
                courseIterator.remove();
            }
        }
    }

    private void subtractCourse(Course course) {
        isChanged = true;
        lastUpdate = timeProvider.getCurrentTimeInMills();
        repository.deleteByDataId(oldCourseMap.get(course).getDataId());
    }

    private RecipeCoursePersistenceModel createNewCourseModel(Course course) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCoursePersistenceModel(
                idProvider.getUId(),
                course,
                id,
                lastUpdate,
                lastUpdate
        );

        RecipeCoursePersistenceModel model = new RecipeCoursePersistenceModel.Builder().
                setDataId(idProvider.getUId()).
                setRecipeId()
    }

    private void sendResponse() {
        System.out.println(TAG + "oldCourseMap" + oldCourseMap);
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setId(id).
                setMetadata(getMetadata()).
                setModel(getResponseModel()).
                build();

        System.out.println(TAG + response);

        if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
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
                setCourseList(new HashMap<>(oldCourseMap)).
                build();
    }

    private RecipeMetadata.ComponentState getComponentState() {
        boolean isValid = !newCourseList.isEmpty();
        if (!isValid) createDate = 0L;

        if (!isValid && !isChanged) {
            return RecipeMetadata.ComponentState.INVALID_UNCHANGED;
        } else if (isValid && !isChanged) {
            return RecipeMetadata.ComponentState.VALID_UNCHANGED;
        } else if (!isValid && isChanged) {
            return RecipeMetadata.ComponentState.INVALID_CHANGED;
        } else {
            return RecipeMetadata.ComponentState.VALID_CHANGED;
        }
    }

    private List<FailReasons> getFailReasons() {
        List<FailReasons> failReasons = new LinkedList<>();
        if (newCourseList.isEmpty()) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        } else {
            failReasons.add(CommonFailReason.NONE);
        }
        return failReasons;
    }
}
