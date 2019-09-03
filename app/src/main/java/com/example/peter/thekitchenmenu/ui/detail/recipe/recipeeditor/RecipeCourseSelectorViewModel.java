package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeCourse;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import java.util.List;

public class RecipeCourseSelectorViewModel
        extends ViewModel
        implements DataSource.GetAllCallback<RecipeCourseEntity> {

    private DataSourceRecipeCourse dataSourceRecipeCourse;
    private UniqueIdProvider idProvider;

    private String recipeId;
    private List<RecipeCourseEntity> recipeCourseEntities;

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
                boolean addCourse = courseZeroObservable.get();
                if (addCourse)
                    addCourse(0);
                else
                    removeCourse(0);
            }
        });
        courseOneObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseOneObservable.get();
                if (addCourse)
                    addCourse(1);
                else
                    removeCourse(1);
            }
        });
        courseTwoObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseTwoObservable.get();
                if (addCourse)
                    addCourse(2);
                else
                    removeCourse(2);
            }
        });
        courseThreeObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseThreeObservable.get();
                if (addCourse)
                    addCourse(3);
                else
                    removeCourse(3);
            }
        });
        courseFourObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseFourObservable.get();
                if (addCourse)
                    addCourse(4);
                else
                    removeCourse(4);
            }
        });
        courseFiveObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseFiveObservable.get();
                if (addCourse)
                    addCourse(5);
                else
                    removeCourse(5);
            }
        });
        courseSixObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseSixObservable.get();
                if (addCourse)
                    addCourse(6);
                else
                    removeCourse(6);
            }
        });
        courseSevenObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean addCourse = courseSevenObservable.get();
                if (addCourse)
                    addCourse(7);
                else
                    removeCourse(7);
            }
        });
    }

    public void onStart(@NonNull String recipeId) {
        this.recipeId = recipeId;
        getCoursesForRecipe(recipeId);
    }

    private void getCoursesForRecipe(String recipeId) {
        dataSourceRecipeCourse.getCoursesForRecipe(recipeId, this);
    }

    @Override
    public void onAllLoaded(List<RecipeCourseEntity> recipeCourseEntities) {
        this.recipeCourseEntities = recipeCourseEntities;
        setRecipeCoursesToObservables();
    }

    private void setRecipeCoursesToObservables() {
        for (RecipeCourseEntity courseEntity : recipeCourseEntities) {
            switch (courseEntity.getCourseNo()) {
                case 0:
                    courseZeroObservable.set(true);
                    break;
                case 1:
                    courseOneObservable.set(true);
                    break;
                case 2:
                    courseTwoObservable.set(true);
                    break;
                case 3:
                    courseThreeObservable.set(true);
                    break;
                case 4:
                    courseFourObservable.set(true);
                    break;
                case 5:
                    courseFiveObservable.set(true);
                    break;
                case 6:
                    courseSixObservable.set(true);
                    break;
                case 7:
                    courseSevenObservable.set(true);
                    break;
            }
        }
    }

    @Override
    public void onDataNotAvailable() {

    }

    private void addCourse(int course) {
        if (recipeId != null)
            dataSourceRecipeCourse.save(new RecipeCourseEntity(
                    idProvider.getUId(),
                    course,
                    recipeId));
        else
            throwException();
    }

    private void removeCourse(int course) {
        for (RecipeCourseEntity courseEntity : recipeCourseEntities) {
            if (courseEntity.getCourseNo() == course) {
                deleteCourseFromDatabase(courseEntity.getId());
            }
        }
    }

    private void throwException() {
        throw new RuntimeException("Recipe id cannot be null");
    }

    private void deleteCourseFromDatabase(String recipeCourseEntityEntryId) {
        dataSourceRecipeCourse.deleteById(recipeCourseEntityEntryId);
    }

}
