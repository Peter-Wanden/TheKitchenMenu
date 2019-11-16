package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.annotation.SuppressLint;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecipeCourseEditorViewModel
        extends
        ObservableViewModel
        implements
        DataSource.GetAllCallback<RecipeCourseEntity>,
        RecipeModelComposite.RecipeModelActions {

    private enum Course {
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

        public static Course valueOf(int courseNo) {
            return courseMap.get(courseNo);
        }

        public int getCourseNo() {
            return courseNo;
        }
    }

    private RepositoryRecipeCourse repository;
    private UniqueIdProvider idProvider;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    private String recipeId;
    private HashMap<Course, RecipeCourseEntity> oldCourseList = new LinkedHashMap<>();
    private HashMap<Course, RecipeCourseEntity> newCourseList = new LinkedHashMap<>();
    private boolean updatingUi;
    private boolean isCloned;
    private boolean dataLoading;

    public RecipeCourseEditorViewModel(RepositoryRecipeCourse repository,
                                       UniqueIdProvider idProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (this.recipeId == null || !this.recipeId.equals(recipeId)) {
            this.recipeId = recipeId;
            getData(recipeId);
        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        if (recipeId == null || !recipeId.equals(newRecipeId)) {
            isCloned = true;
            recipeId = newRecipeId;
            getData(oldRecipeId);
        }
    }

    private void getData(String recipeId) {
        dataLoading = true;
        repository.getCoursesForRecipe(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> recipeCourseEntities) {
        if (isCloned) {
            cloneRecipeCourseEntities(recipeCourseEntities);
        } else {
            addEntitiesToNewList(recipeCourseEntities);
        }
        equaliseState();
        setRecipeCoursesToObservables();
    }

    private void addEntitiesToNewList(List<RecipeCourseEntity> recipeCourseEntities) {
        for (RecipeCourseEntity courseEntity : recipeCourseEntities) {
            newCourseList.put(Course.valueOf(courseEntity.getCourseNo()), courseEntity);
        }
    }

    private void cloneRecipeCourseEntities(List<RecipeCourseEntity> recipeCourseEntities) {
        for (RecipeCourseEntity recipeCourseEntity : recipeCourseEntities) {
            addOrRemoveCourse(true, Course.valueOf(recipeCourseEntity.getCourseNo()));
        }
    }

    @Override
    public void onDataNotAvailable() {
        submitModelStatus(false, false);
    }

    private void setRecipeCoursesToObservables() {
        updatingUi = true;
        for (Course courseName : newCourseList.keySet()) {
            switch (courseName) {
                case COURSE_ZERO:
                    addOrRemoveCourse(true, Course.COURSE_ZERO);
                    notifyPropertyChanged(BR.courseZero);
                    break;
                case COURSE_ONE:
                    addOrRemoveCourse(true, Course.COURSE_ONE);
                    notifyPropertyChanged(BR.courseOne);
                    break;
                case COURSE_TWO:
                    addOrRemoveCourse(true, Course.COURSE_TWO);
                    notifyPropertyChanged(BR.courseTwo);
                    break;
                case COURSE_THREE:
                    addOrRemoveCourse(true, Course.COURSE_THREE);
                    notifyPropertyChanged(BR.courseThree);
                    break;
                case COURSE_FOUR:
                    addOrRemoveCourse(true, Course.COURSE_FOUR);
                    notifyPropertyChanged(BR.courseFour);
                    break;
                case COURSE_FIVE:
                    addOrRemoveCourse(true, Course.COURSE_FIVE);
                    notifyPropertyChanged(BR.courseFive);
                    break;
                case COURSE_SIX:
                    addOrRemoveCourse(true, Course.COURSE_SIX);
                    notifyPropertyChanged(BR.courseSix);
                    break;
                case COURSE_SEVEN:
                    addOrRemoveCourse(true, Course.COURSE_SEVEN);
                    notifyPropertyChanged(BR.courseSeven);
                    break;
            }
        }
        updatingUi = false;
        dataLoading = false;
        compareCourseLists();
    }

    @Bindable
    public boolean isCourseZero() {
        return newCourseList.get(Course.COURSE_ZERO) != null;
    }

    public void setCourseZero(boolean courseZero) {
        addOrRemoveCourse(courseZero, Course.COURSE_ZERO);
    }

    @Bindable
    public boolean isCourseOne() {
        return newCourseList.get(Course.COURSE_ONE) != null;
    }

    public void setCourseOne(boolean courseOne) {
        addOrRemoveCourse(courseOne, Course.COURSE_ONE);
    }

    @Bindable
    public boolean isCourseTwo() {
        return newCourseList.get(Course.COURSE_TWO) != null;
    }

    public void setCourseTwo(boolean courseTwo) {
        addOrRemoveCourse(courseTwo, Course.COURSE_TWO);
    }

    @Bindable
    public boolean isCourseThree() {
        return newCourseList.get(Course.COURSE_THREE) != null;
    }

    public void setCourseThree(boolean courseThree) {
        addOrRemoveCourse(courseThree, Course.COURSE_THREE);
    }

    @Bindable
    public boolean isCourseFour() {
        return newCourseList.get(Course.COURSE_FOUR) != null;
    }

    public void setCourseFour(boolean courseFour) {
        addOrRemoveCourse(courseFour, Course.COURSE_FOUR);
    }

    @Bindable
    public boolean isCourseFive() {
        return newCourseList.get(Course.COURSE_FIVE) != null;
    }

    public void setCourseFive(boolean courseFive) {
        addOrRemoveCourse(courseFive, Course.COURSE_FIVE);
    }

    @Bindable
    public boolean isCourseSix() {
        return newCourseList.get(Course.COURSE_SIX) != null;
    }

    public void setCourseSix(boolean courseSix) {
        addOrRemoveCourse(courseSix, Course.COURSE_SIX);
    }

    @Bindable
    public boolean isCourseSeven() {
        return newCourseList.get(Course.COURSE_SEVEN) != null;
    }

    public void setCourseSeven(boolean courseSeven) {
        addOrRemoveCourse(courseSeven, Course.COURSE_SEVEN);
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

    private void addCourse(Course course) {
        RecipeCourseEntity newCourseEntity = createNewCourseEntity(course.getCourseNo());
        newCourseList.put(course, newCourseEntity);
        repository.save(newCourseEntity);

        if (!updatingUi) {
            compareCourseLists();
        }
    }

    private RecipeCourseEntity createNewCourseEntity(int courseNo) {
        return new RecipeCourseEntity(
                idProvider.getUId(),
                courseNo,
                recipeId);
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
        submitModelStatus(isChanged, isValid);
    }

    private void equaliseState() {
        oldCourseList.clear();
        oldCourseList.putAll(newCourseList);
    }

    private void submitModelStatus(boolean isChanged, boolean isValid) {
        modelSubmitter.submitModelStatus(new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                isChanged,
                isValid
        ));
    }

    private boolean isChanged() {
        return !oldCourseList.keySet().equals(newCourseList.keySet());
    }

    private boolean isValid() {
        return !newCourseList.isEmpty();
    }
}