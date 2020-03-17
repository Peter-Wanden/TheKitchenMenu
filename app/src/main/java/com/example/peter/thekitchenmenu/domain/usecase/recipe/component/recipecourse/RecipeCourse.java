package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentMetadata;
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

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;

public class RecipeCourse extends UseCase implements DataSource.GetAllCallback<RecipeCourseEntity> {

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
    private final HashMap<Course, RecipeCourseModel> oldCourseMap = new LinkedHashMap<>();
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

        if (isNewRequest(courseRequest.getId())) {
            id = courseRequest.getId();
            loadData(id);
        } else {
            processChanges(courseRequest.getModel().getCourseList());
        }
    }

    private boolean isNewRequest(String recipeId) {
        return !this.id.equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getCoursesForRecipe(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> courseEntities) {
        addCoursesToLists(courseEntities);
    }

    private void addCoursesToLists(List<RecipeCourseEntity> courseEntities) {
        createDate = courseEntities.isEmpty() ? 0L : Long.MAX_VALUE;
        lastUpdate = 0L;

        for (RecipeCourseEntity courseEntity : courseEntities) {
            RecipeCourseModel model = convertEntityToModel(courseEntity);
            Course course = model.getCourse();

            oldCourseMap.put(course, model);
            newCourseList.add(course);

            createDate = Math.min(createDate, model.getCreateDate());
            lastUpdate = Math.max(lastUpdate, model.getLasUpdate());
        }

        sendResponse();
    }

    @Override
    public void onDataNotAvailable() {
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
        RecipeCourseModel model = createNewCourseModel(course);
        oldCourseMap.put(course, model);
        save(model);
    }

    private void save(RecipeCourseModel model) {
        repository.save(convertModelToPrimitive(model));
    }

    private void processCourseSubtractions() {
        Iterator<Map.Entry<Course, RecipeCourseModel>> courseIterator =
                oldCourseMap.entrySet().iterator();

        while (courseIterator.hasNext()) {
            Map.Entry<Course, RecipeCourseModel> course = courseIterator.next();
            if (!newCourseList.contains(course.getKey())) {
                subtractCourse(course.getKey());
                courseIterator.remove();
            }
        }
    }

    private void subtractCourse(Course course) {
        isChanged = true;
        lastUpdate = timeProvider.getCurrentTimeInMills();
        repository.deleteById(oldCourseMap.get(course).getId());
    }

    private RecipeCourseModel convertEntityToModel(RecipeCourseEntity entity) {
        return new RecipeCourseModel(
                entity.getId(),
                Course.fromInt(entity.getCourseNo()),
                entity.getRecipeId(),
                entity.getCreateDate(),
                entity.getLasUpdate()
        );
    }

    private RecipeCourseModel createNewCourseModel(Course course) {
        lastUpdate = timeProvider.getCurrentTimeInMills();
        return new RecipeCourseModel(
                idProvider.getUId(),
                course,
                id,
                lastUpdate,
                lastUpdate
        );
    }

    private RecipeCourseEntity convertModelToPrimitive(RecipeCourseModel model) {
        return new RecipeCourseEntity(
                model.getId(),
                model.getCourse().getCourseNo(),
                model.getRecipeId(),
                model.getCreateDate(),
                model.getLasUpdate());
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

    private RecipeComponentMetadata getMetadata() {
        return new RecipeComponentMetadata.Builder().
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

    private ComponentState getComponentState() {
        boolean isValid = !newCourseList.isEmpty();
        if (!isValid) createDate = 0L;

        if (!isValid && !isChanged) {
            return ComponentState.INVALID_UNCHANGED;
        } else if (isValid && !isChanged) {
            return ComponentState.VALID_UNCHANGED;
        } else if (!isValid && isChanged) {
            return ComponentState.INVALID_CHANGED;
        } else {
            return ComponentState.VALID_CHANGED;
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
