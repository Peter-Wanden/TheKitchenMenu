package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends UseCase<RecipeCourseRequest, RecipeCourseResponse>
        implements DataSource.GetAllCallback<RecipeCourseEntity> {

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

    public enum FailReason {
        NO_COURSES_SET,
        NONE
    }

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

    public static final String DO_NOT_CLONE = "";

    @Nonnull
    private RepositoryRecipeCourse repository;
    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    private String recipeId = "";
    private boolean isClone;
    private HashMap<Course, RecipeCourseModel> oldCourseList = new LinkedHashMap<>();
    private HashMap<Course, RecipeCourseModel> newCourseList = new LinkedHashMap<>();

    public RecipeCourse(@Nonnull RepositoryRecipeCourse repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void execute(RecipeCourseRequest request) {
        System.out.println(TAG + request);
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            addOrRemoveCourse(request.isAddCourse(), request.getCourse());
        }
    }

    private boolean isNewRequest(RecipeCourseRequest request) {
        return !recipeId.equals(request.getRecipeId());
    }

    private void loadData(RecipeCourseRequest request) {
        if (isCloneRequest(request)) {
            isClone = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getCoursesForRecipe(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(RecipeCourseRequest request) {
        return !request.getCloneToRecipeId().equals(DO_NOT_CLONE);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> courseEntities) {
        if (isClone) {
            cloneEntities(courseEntities);
        } else {
            addEntitiesToNewList(courseEntities);
        }
    }

    @Override
    public void onDataNotAvailable() {
        RecipeCourseResponse response = RecipeCourseResponse.Builder.getDefault().
                setStatus(RecipeState.ComponentState.DATA_UNAVAILABLE).
                setFailReasons(getFailReasons()).
                build();
        System.out.println(TAG + response);
        getUseCaseCallback().onError(response);
    }

    private void cloneEntities(List<RecipeCourseEntity> courseEntities) {
        for (RecipeCourseEntity recipeCourseEntity : courseEntities) {
            addOrRemoveCourse(true, Course.fromInt(recipeCourseEntity.getCourseNo()));
        }
        isClone = false;
        compareCourseLists();
    }

    private void addOrRemoveCourse(boolean addCourse, Course course) {
        if (addCourse && !isCourseInList(course)) {
            addCourse(course);
        } else if (!addCourse && isCourseInList(course)) {
            removeCourse(course);
        }
    }

    private boolean isCourseInList(Course course) {
        return !(newCourseList.get(course) == null);
    }

    private void addEntitiesToNewList(List<RecipeCourseEntity> courseEntities) {
        for (RecipeCourseEntity courseEntity : courseEntities) {
            RecipeCourseModel model = convertEntityToModel(courseEntity);
            newCourseList.put(model.getCourse(), model);
            oldCourseList.put(model.getCourse(), model);
        }
        compareCourseLists();
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

    private void addCourse(Course course) {
        RecipeCourseModel model = createNewCourseModel(course);
        newCourseList.put(course, model);
        repository.save(convertModelToEntity(model));
        if (!isClone) {
            compareCourseLists();
        }
    }

    private RecipeCourseModel createNewCourseModel(Course course) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new RecipeCourseModel(
                idProvider.getUId(),
                course,
                recipeId,
                currentTime,
                currentTime
        );
    }

    private RecipeCourseEntity convertModelToEntity(RecipeCourseModel model) {
        return new RecipeCourseEntity(
                model.getId(),
                model.getCourse().getCourseNo(),
                model.getRecipeId(),
                model.getCreateDate(),
                model.getLasUpdate());
    }

    private void removeCourse(Course course) {
        deleteCourse(newCourseList.get(course).getId());
        newCourseList.remove(course);
        compareCourseLists();
    }

    private void deleteCourse(String Id) {
        repository.deleteById(Id);
    }

    private void compareCourseLists() {
        boolean isChanged = isChanged();
        boolean isValid = isValid();

        equaliseState();

        sendResponse(isChanged, isValid);
    }

    private boolean isChanged() {
        return !oldCourseList.keySet().equals(newCourseList.keySet());
    }

    private boolean isValid() {
        return !newCourseList.isEmpty();
    }

    private void equaliseState() {
        oldCourseList.clear();
        oldCourseList.putAll(newCourseList);
    }

    private void sendResponse(boolean isChanged, boolean isValid) {
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setStatus(getStatus(isChanged, isValid)).
                setFailReasons(getFailReasons()).
                setCourseList(newCourseList).
                build();
        System.out.println(TAG + response);
        getUseCaseCallback().onSuccess(response);
    }

    private RecipeState.ComponentState getStatus(boolean isChanged, boolean isValid) {
        if (!isValid && !isChanged) {
            return RecipeState.ComponentState.INVALID_UNCHANGED;

        } else if (isValid && !isChanged) {
            return RecipeState.ComponentState.VALID_UNCHANGED;

        } else if (!isValid && isChanged) {
            return RecipeState.ComponentState.INVALID_CHANGED;

        } else {
            return RecipeState.ComponentState.VALID_CHANGED;
        }
    }

    private List<FailReason> getFailReasons() {
        List<FailReason> failReasons = new LinkedList<>();
        if (newCourseList.isEmpty()) {
            failReasons.add(FailReason.NO_COURSES_SET);
        } else {
            failReasons.add(FailReason.NONE);
        }
        return failReasons;
    }
}
