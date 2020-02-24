package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeCourse
        extends UseCase<RecipeCourseRequest, RecipeCourseResponse>
        implements DataSource.GetAllCallback<RecipeCourseEntity> {

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

    private String recipeId = "";
    private boolean isCloned;
    private final HashMap<Course, RecipeCourseModel> oldCourseList = new LinkedHashMap<>();
    private final HashMap<Course, RecipeCourseModel> newCourseList = new LinkedHashMap<>();

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
        if (isNewRequest(request.getId())) {
            extractIds(request);
        } else {
            addOrRemoveCourse(request.isAddCourse(), request.getCourse());
        }
    }

    private boolean isNewRequest(String recipeId) {
        return !this.recipeId.equals(recipeId);
    }

    private void extractIds(RecipeCourseRequest request) {
        if (isCloneRequest(request)) {
            recipeId = request.getCloneToId();
        } else {
            recipeId = request.getId();
        }
        loadData(request.getId());
    }

    private boolean isCloneRequest(RecipeCourseRequest request) {
        return isCloned = !request.getCloneToId().equals(DO_NOT_CLONE);
    }

    private void loadData(String recipeId) {
        repository.getCoursesForRecipe(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> courseEntities) {
        if (isCloned) {
            cloneCourses(courseEntities);
        } else {
            addCoursesToCourseList(courseEntities);
        }
    }

    private void cloneCourses(List<RecipeCourseEntity> courseEntities) {
        for (RecipeCourseEntity recipeCourseEntity : courseEntities) {
            addOrRemoveCourse(true, Course.fromInt(recipeCourseEntity.getCourseNo()));
        }
        isCloned = false;
        compareCourseLists();
    }

    @Override
    public void onDataNotAvailable() {
        RecipeCourseResponse response = RecipeCourseResponse.Builder.getDefault().
                setStatus(ComponentState.DATA_UNAVAILABLE).
                setFailReasons(getFailReasons()).
                build();
        System.out.println(TAG + response);
        getUseCaseCallback().onError(response);
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

    private void addCoursesToCourseList(List<RecipeCourseEntity> courseEntities) {
        for (RecipeCourseEntity courseEntity : courseEntities) {
            RecipeCourseModel model = convertEntityToModel(courseEntity);
            Course course = model.getCourse();

            newCourseList.put(course, model);
            oldCourseList.put(course, model);
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
        RecipeCourseModel model = createCourseModel(course);
        newCourseList.put(course, model);

        repository.save(convertModelToEntity(model));
        if (!isCloned) {
            compareCourseLists();
        }
    }

    private RecipeCourseModel createCourseModel(Course course) {
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

        equaliseCourseLists();

        sendResponse(isChanged);
    }

    private boolean isChanged() {
        return !oldCourseList.keySet().equals(newCourseList.keySet());
    }

    private void equaliseCourseLists() {
        oldCourseList.clear();
        oldCourseList.putAll(newCourseList);
    }

    private void sendResponse(boolean isChanged) {
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setStatus(getComponentState(isChanged)).
                setFailReasons(getFailReasons()).
                setCourseList(newCourseList).
                build();

        System.out.println(TAG + response);

        if (response.getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private ComponentState getComponentState(boolean isChanged) {
        boolean isValid = !newCourseList.isEmpty();
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
