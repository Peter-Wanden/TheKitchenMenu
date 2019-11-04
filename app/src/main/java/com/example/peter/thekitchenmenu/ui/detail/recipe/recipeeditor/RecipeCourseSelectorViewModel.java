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

public class RecipeCourseSelectorViewModel
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
        private static Map<Integer, Course> map = new HashMap<>();

        Course(int courseNo) {
            this.courseNo = courseNo;
        }

        static {
            for (Course course : Course.values())
                map.put(course.courseNo, course);
        }

        public static Course valueOf(int courseNo) {
            return map.get(courseNo);
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
    private boolean modelUpdating;
    private boolean cloneModel;

    public RecipeCourseSelectorViewModel(RepositoryRecipeCourse repository,
                                         UniqueIdProvider idProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        this.recipeId = recipeId;
        getCoursesForRecipe(recipeId);
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String newRecipeId) {
        cloneModel = true;
        recipeId = newRecipeId;
        getCoursesForRecipe(oldRecipeId);
    }

    private void getCoursesForRecipe(String recipeId) {
        repository.getCoursesForRecipe(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> recipeCourseEntities) {
        if (cloneModel)
            cloneRecipeCourseEntities(recipeCourseEntities);

        modelUpdating = true;

        for (RecipeCourseEntity courseEntity : recipeCourseEntities)
            oldCourseList.put(Course.valueOf(courseEntity.getCourseNo()), courseEntity);

        if (!cloneModel)
            newCourseList.putAll(oldCourseList);

        setRecipeCoursesToObservables();
    }

    private void cloneRecipeCourseEntities(List<RecipeCourseEntity> recipeCourseEntities) {
        for (RecipeCourseEntity recipeCourseEntity : recipeCourseEntities)
            addCourse(Course.valueOf(recipeCourseEntity.getCourseNo()));
    }

    private void setRecipeCoursesToObservables() {
        for (Course courseName : newCourseList.keySet()) {
            switch (courseName) {
                case COURSE_ZERO:
                    addCourse(Course.COURSE_ZERO);
                    notifyPropertyChanged(BR.courseZero);
                    break;
                case COURSE_ONE:
                    addCourse(Course.COURSE_ONE);
                    notifyPropertyChanged(BR.courseOne);
                    break;
                case COURSE_TWO:
                    addCourse(Course.COURSE_TWO);
                    notifyPropertyChanged(BR.courseTwo);
                    break;
                case COURSE_THREE:
                    addCourse(Course.COURSE_THREE);
                    notifyPropertyChanged(BR.courseThree);
                    break;
                case COURSE_FOUR:
                    addCourse(Course.COURSE_FOUR);
                    notifyPropertyChanged(BR.courseFour);
                    break;
                case COURSE_FIVE:
                    addCourse(Course.COURSE_FIVE);
                    notifyPropertyChanged(BR.courseFive);
                    break;
                case COURSE_SIX:
                    addCourse(Course.COURSE_SIX);
                    notifyPropertyChanged(BR.courseSix);
                    break;
                case COURSE_SEVEN:
                    addCourse(Course.COURSE_SEVEN);
                    notifyPropertyChanged(BR.courseSeven);
                    break;
            }
        }
        modelUpdating = false;
        compareCourseLists();
    }

    @Override
    public void onDataNotAvailable() {
        reportModelValidationStatus(false, false);
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
        if (addCourse && !courseInList(course))
            addCourse(course);
        else if (!addCourse && courseInList(course))
            removeCourse(course);
    }

    private boolean courseInList(Course course) {
        return !(newCourseList.get(course) == null);
    }

    private void addCourse(Course course) {
        if (newCourseList.get(course) == null) {
            RecipeCourseEntity newCourseEntity = createNewCourseEntity(course.getCourseNo());
            newCourseList.put(course, newCourseEntity);
            repository.save(newCourseEntity);
        }
        if (!modelUpdating)
            compareCourseLists();
    }

    private RecipeCourseEntity createNewCourseEntity(int courseNo) {
        return new RecipeCourseEntity(
                idProvider.getUId(),
                courseNo,
                recipeId);
    }

    private void removeCourse(Course course) {
        deleteCourseFromDatabase(newCourseList.get(course).getId());
        newCourseList.remove(course);
        compareCourseLists();
    }

    private void deleteCourseFromDatabase(String recipeCourseEntityId) {
        repository.deleteById(recipeCourseEntityId);
    }

    private void compareCourseLists() {
        boolean isChanged;
        boolean isValid;

        isChanged = !oldCourseList.keySet().equals(newCourseList.keySet());
        isValid = !newCourseList.isEmpty();

        makeOldListEquivalentToNew();

        reportModelValidationStatus(isChanged, isValid);
    }

    private void makeOldListEquivalentToNew() {
        oldCourseList.clear();
        oldCourseList.putAll(newCourseList);
    }

    private void reportModelValidationStatus(boolean isChanged, boolean isValid) {
        modelSubmitter.submitModelStatus(new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                isChanged,
                isValid
        ));
    }
}