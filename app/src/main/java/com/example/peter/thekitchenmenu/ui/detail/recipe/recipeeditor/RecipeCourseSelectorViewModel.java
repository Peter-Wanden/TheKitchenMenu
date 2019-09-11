package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeCourse;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecipeCourseSelectorViewModel
        extends
        ViewModel
        implements
        DataSource.GetAllCallback<RecipeCourseEntity>,
        RecipeModelComposite.RecipeModelActions {

    private enum Courses {
        COURSE_ZERO(0),
        COURSE_ONE(1),
        COURSE_TWO(2),
        COURSE_THREE(3),
        COURSE_FOUR(4),
        COURSE_FIVE(5),
        COURSE_SIX(6),
        COURSE_SEVEN(7);

        private final int courseNo;
        private static Map<Integer, Courses> map = new HashMap<>();

        Courses(int courseNo) {
            this.courseNo = courseNo;
        }

        static {
            for (Courses course : Courses.values())
                map.put(course.courseNo, course);
        }

        public static Courses valueOf(int courseNo) {
            return map.get(courseNo);
        }

        public int getCourseNo() {
            return courseNo;
        }
    }

    private DataSourceRecipeCourse dataSourceRecipeCourse;
    private UniqueIdProvider idProvider;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    private String recipeId;
    private HashMap<Courses, RecipeCourseEntity> oldCourseList = new LinkedHashMap<>();
    private HashMap<Courses, RecipeCourseEntity> newCourseList = new LinkedHashMap<>();
    private boolean modelIsUpdating;
    private boolean cloneModel;

    public final ObservableBoolean courseZeroObservable = new ObservableBoolean();
    public final ObservableBoolean courseOneObservable = new ObservableBoolean();
    public final ObservableBoolean courseTwoObservable = new ObservableBoolean();
    public final ObservableBoolean courseThreeObservable = new ObservableBoolean();
    public final ObservableBoolean courseFourObservable = new ObservableBoolean();
    public final ObservableBoolean courseFiveObservable = new ObservableBoolean();
    public final ObservableBoolean courseSixObservable = new ObservableBoolean();
    public final ObservableBoolean courseSevenObservable = new ObservableBoolean();

    public RecipeCourseSelectorViewModel(DataSourceRecipeCourse dataSourceRecipeCourse,
                                         UniqueIdProvider idProvider) {
        this.dataSourceRecipeCourse = dataSourceRecipeCourse;
        this.idProvider = idProvider;

        courseZeroObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseZeroObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_ZERO);
                else
                    removeCourse(Courses.COURSE_ZERO);
            }
        });
        courseOneObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseOneObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_ONE);
                else
                    removeCourse(Courses.COURSE_ONE);
            }
        });
        courseTwoObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseTwoObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_TWO);
                else
                    removeCourse(Courses.COURSE_TWO);
            }
        });
        courseThreeObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseThreeObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_THREE);
                else
                    removeCourse(Courses.COURSE_THREE);
            }
        });
        courseFourObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseFourObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_FOUR);
                else
                    removeCourse(Courses.COURSE_FOUR);
            }
        });
        courseFiveObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseFiveObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_FIVE);
                else
                    removeCourse(Courses.COURSE_FIVE);
            }
        });
        courseSixObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseSixObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_SIX);
                else
                    removeCourse(Courses.COURSE_SIX);
            }
        });
        courseSevenObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean courseAdded = courseSevenObservable.get();
                if (courseAdded)
                    addCourse(Courses.COURSE_SEVEN);
                else
                    removeCourse(Courses.COURSE_SEVEN);
            }
        });
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
        dataSourceRecipeCourse.getCoursesForRecipe(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> recipeCourseEntities) {
        if (cloneModel)
            cloneRecipeCourseEntities(recipeCourseEntities);

        modelIsUpdating = true;

        for (RecipeCourseEntity courseEntity : recipeCourseEntities)
            oldCourseList.put(Courses.valueOf(courseEntity.getCourseNo()), courseEntity);

        newCourseList.putAll(oldCourseList);
        setRecipeCoursesToObservables();
    }

    private void cloneRecipeCourseEntities(List<RecipeCourseEntity> recipeCourseEntities) {
        for (RecipeCourseEntity recipeCourseEntity : recipeCourseEntities)
            dataSourceRecipeCourse.save(createNewCourseEntity(recipeCourseEntity.getCourseNo()));
    }

    private void setRecipeCoursesToObservables() {
        for (Courses courseName : newCourseList.keySet()) {
            switch (courseName) {
                case COURSE_ZERO:
                    courseZeroObservable.set(true);
                    break;
                case COURSE_ONE:
                    courseOneObservable.set(true);
                    break;
                case COURSE_TWO:
                    courseTwoObservable.set(true);
                    break;
                case COURSE_THREE:
                    courseThreeObservable.set(true);
                    break;
                case COURSE_FOUR:
                    courseFourObservable.set(true);
                    break;
                case COURSE_FIVE:
                    courseFiveObservable.set(true);
                    break;
                case COURSE_SIX:
                    courseSixObservable.set(true);
                    break;
                case COURSE_SEVEN:
                    courseSevenObservable.set(true);
                    break;
            }
        }
        modelIsUpdating = false;
        compareCourseLists();
    }

    @Override
    public void onDataNotAvailable() {
        reportModelValidationStatus(false, false);
    }

    private void addCourse(Courses course) {
        if (newCourseList.get(course) == null) {
            RecipeCourseEntity newCourseEntity = createNewCourseEntity(course.getCourseNo());
            newCourseList.put(course, newCourseEntity);
            dataSourceRecipeCourse.save(newCourseEntity);
        }
        if (!modelIsUpdating)
            compareCourseLists();
    }

    private RecipeCourseEntity createNewCourseEntity(int courseNo) {
        return new RecipeCourseEntity(
                idProvider.getUId(),
                courseNo,
                recipeId);
    }

    private void removeCourse(Courses course) {
        deleteCourseFromDatabase(newCourseList.get(course).getId());
        newCourseList.remove(course);
        compareCourseLists();
    }

    private void deleteCourseFromDatabase(String recipeCourseEntityId) {
        dataSourceRecipeCourse.deleteById(recipeCourseEntityId);
    }

    private void compareCourseLists() {
        boolean isChanged;
        boolean isValid;

        isChanged = !oldCourseList.keySet().equals(newCourseList.keySet());
        isValid = !newCourseList.isEmpty();

        reportModelValidationStatus(isChanged, isValid);
    }

    private void reportModelValidationStatus(boolean isChanged, boolean isValid) {
        modelSubmitter.submitModelStatus(new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                isChanged,
                isValid
        ));
    }
}