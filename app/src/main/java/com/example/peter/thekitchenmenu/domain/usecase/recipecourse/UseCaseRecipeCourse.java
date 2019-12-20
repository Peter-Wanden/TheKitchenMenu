package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UseCaseRecipeCourse
        extends
        UseCaseInteractor<UseCaseRecipeCourse.Request, UseCaseRecipeCourse.Response>
        implements DataSource.GetAllCallback<RecipeCourseEntity> {

    private static final String TAG = "tkm-" + UseCaseRecipeCourse.class.getSimpleName() + " ";

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
    private HashMap<Course, Model> oldCourseList = new LinkedHashMap<>();
    private HashMap<Course, Model> newCourseList = new LinkedHashMap<>();

    public UseCaseRecipeCourse(@Nonnull RepositoryRecipeCourse repository,
                               @Nonnull UniqueIdProvider idProvider,
                               @Nonnull TimeProvider timeProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void execute(Request request) {
        if (isNewRequest(request)) {
            loadData(request);
        } else {
            addOrRemoveCourse(request.isAddCourse(), request.getCourse());
        }
    }

    private boolean isNewRequest(Request request) {
        return !recipeId.equals(request.getRecipeId());
    }

    private void loadData(Request request) {
        if (isCloneRequest(request)) {
            isClone = true;
            recipeId = request.getCloneToRecipeId();
        } else {
            recipeId = request.getRecipeId();
        }
        repository.getCoursesForRecipe(request.getRecipeId(), this);
    }

    private boolean isCloneRequest(Request request) {
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
        getUseCaseCallback().onError(new Response(newCourseList, isChanged(), isValid()));
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
            Model model = convertEntityToModel(courseEntity);
            newCourseList.put(model.getCourse(), model);
            oldCourseList.put(model.getCourse(), model);
        }
        compareCourseLists();
    }

    private Model convertEntityToModel(RecipeCourseEntity entity) {
        return new Model(
                entity.getId(),
                Course.fromInt(entity.getCourseNo()),
                entity.getRecipeId(),
                entity.getCreateDate(),
                entity.getLasUpdate()
        );
    }

    private void addCourse(Course course) {
        Model model = createNewCourseModel(course);
        newCourseList.put(course, model);
        repository.save(convertModelToEntity(model));
        if (!isClone) {
            compareCourseLists();
        }
    }

    private Model createNewCourseModel(Course course) {
        long currentTime = timeProvider.getCurrentTimeInMills();
        return new Model(
                idProvider.getUId(),
                course,
                recipeId,
                currentTime,
                currentTime
        );
    }

    private RecipeCourseEntity convertModelToEntity(Model model) {
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
        Response response = new Response(
                newCourseList,
                isChanged,
                isValid
        );
        getUseCaseCallback().onSuccess(response);
    }

    public class Model {

        @Nonnull
        private final String id;
        @Nonnull
        private final Course course;
        @Nonnull
        private final String recipeId;
        private final long createDate;
        private final long lasUpdate;

        public Model(@Nonnull String id,
                     @Nonnull Course course,
                     @Nonnull String recipeId,
                     long createDate,
                     long lasUpdate) {
            this.id = id;
            this.course = course;
            this.recipeId = recipeId;
            this.createDate = createDate;
            this.lasUpdate = lasUpdate;
        }

        @Nonnull
        public String getId() {
            return id;
        }

        @Nonnull
        public Course getCourse() {
            return course;
        }

        @Nonnull
        public String getRecipeId() {
            return recipeId;
        }

        public long getCreateDate() {
            return createDate;
        }

        public long getLasUpdate() {
            return lasUpdate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model that = (Model) o;
            return createDate == that.createDate &&
                    lasUpdate == that.lasUpdate &&
                    id.equals(that.id) &&
                    course == that.course &&
                    recipeId.equals(that.recipeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, course, recipeId, createDate, lasUpdate);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "id='" + id + '\'' +
                    ", course=" + course +
                    ", recipeId='" + recipeId + '\'' +
                    ", createDate=" + createDate +
                    ", lasUpdate=" + lasUpdate +
                    '}';
        }
    }

    public static final class Request implements UseCaseInteractor.Request {
        @Nonnull
        private final String recipeId;
        @Nonnull
        private final String cloneToRecipeId;
        @Nullable
        private final Course course;
        private final boolean addCourse;

        public Request(@Nonnull String recipeId,
                       @Nonnull String cloneToRecipeId,
                       @Nullable Course course,
                       boolean addCourse) {
            this.recipeId = recipeId;
            this.cloneToRecipeId = cloneToRecipeId;
            this.course = course;
            this.addCourse = addCourse;
        }

        @Nonnull
        public String getRecipeId() {
            return recipeId;
        }

        @Nonnull
        public String getCloneToRecipeId() {
            return cloneToRecipeId;
        }

        @Nullable
        public Course getCourse() {
            return course;
        }

        public boolean isAddCourse() {
            return addCourse;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return addCourse == request.addCourse &&
                    recipeId.equals(request.recipeId) &&
                    cloneToRecipeId.equals(request.cloneToRecipeId) &&
                    course == request.course;
        }

        @Override
        public int hashCode() {
            return Objects.hash(recipeId, cloneToRecipeId, course, addCourse);
        }

        @Override
        public String toString() {
            return "Request{" +
                    "recipeId='" + recipeId + '\'' +
                    ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                    ", course=" + course +
                    ", addCourse=" + addCourse +
                    '}';
        }
    }


    public static final class Response implements UseCaseInteractor.Response {
        @Nonnull
        private final HashMap<Course, Model> courseList;
        private final boolean isChanged;
        private final boolean isValid;

        public Response(@Nonnull HashMap<Course, Model> courseList,
                        boolean isChanged,
                        boolean isValid) {
            this.courseList = courseList;
            this.isChanged = isChanged;
            this.isValid = isValid;
        }

        @Nonnull
        public HashMap<Course, Model> getCourseList() {
            return courseList;
        }

        public boolean isChanged() {
            return isChanged;
        }

        public boolean isValid() {
            return isValid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response response = (Response) o;
            return isChanged == response.isChanged &&
                    isValid == response.isValid &&
                    courseList.equals(response.courseList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(courseList, isChanged, isValid);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "courseList=" + courseList +
                    ", isChanged=" + isChanged +
                    ", isModelValid=" + isValid +
                    '}';
        }
    }
}
