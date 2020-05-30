package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

public class RecipeCourseEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-" + RecipeCourseEditorViewModel.class.getSimpleName() +
            "; ";

    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private Recipe recipeMacro;
    private RecipeCourseResponse response;
    private List<Course> courseList = new ArrayList<>();

    private boolean isUpdatingUi;
    private final ObservableBoolean isDataLoading = new ObservableBoolean(true);

    public RecipeCourseEditorViewModel(@Nonnull UseCaseHandler handler,
                                       @Nonnull Recipe recipeMacro) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;

        response = new RecipeCourseResponse.Builder().getDefault().build();

        recipeMacro.registerComponentListener(new Pair<>(RecipeMetadata.ComponentName.COURSE,
                new CourseCallbackListener())
        );
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link Recipe}
     */
    private class CourseCallbackListener implements UseCaseBase.Callback<RecipeCourseResponse> {
        @Override
        public void onUseCaseSuccess(RecipeCourseResponse response) {
            isDataLoading.set(false);
            if (isStateChanged(response)) {
                System.out.println(TAG + "onSuccess:" + response);
                RecipeCourseEditorViewModel.this.response = response;
                setRecipeCoursesToObservables();
            }
        }

        @Override
        public void onUseCaseError(RecipeCourseResponse response) {
            isDataLoading.set(false);
            if (isStateChanged(response)) {
                System.out.println(TAG + "onError:" + response);
                RecipeCourseEditorViewModel.this.response = response;
                this.onUseCaseError(response);
            }
        }
    }

    private boolean isStateChanged(RecipeCourseResponse response) {
        return !this.response.equals(response);
    }

    private void setRecipeCoursesToObservables() {
        courseList.clear();
        courseList.addAll(response.getModel().getCourseList().keySet());

        isUpdatingUi = true;
        notifyChange();
        isUpdatingUi = false;
    }

    private void onUseCaseError(RecipeCourseResponse response) {
        // The only case in which there is an error is DATA_UNAVAILABLE so display warning 'recipe
        // requires at least one course'.
    }

    @Bindable
    public boolean isCourseZero() {
        return courseList.contains(Course.COURSE_ZERO);
    }

    public void setCourseZero(boolean isCourseZero) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_ZERO, isCourseZero);
        }
    }

    @Bindable
    public boolean isCourseOne() {
        return courseList.contains(Course.COURSE_ONE);
    }

    public void setCourseOne(boolean isCourseOne) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_ONE, isCourseOne);
        }
    }

    @Bindable
    public boolean isCourseTwo() {
        return courseList.contains(Course.COURSE_TWO);
    }

    public void setCourseTwo(boolean isCourseTwo) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_TWO, isCourseTwo);
        }
    }

    @Bindable
    public boolean isCourseThree() {
        return courseList.contains(Course.COURSE_THREE);
    }

    public void setCourseThree(boolean isCourseThree) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_THREE, isCourseThree);
        }
    }

    @Bindable
    public boolean isCourseFour() {
        return courseList.contains(Course.COURSE_FOUR);
    }

    public void setCourseFour(boolean isCourseFour) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_FOUR, isCourseFour);
        }
    }

    @Bindable
    public boolean isCourseFive() {
        return courseList.contains(Course.COURSE_FIVE);
    }

    public void setCourseFive(boolean isCourseFive) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_FIVE, isCourseFive);
        }
    }

    @Bindable
    public boolean isCourseSix() {
        return courseList.contains(Course.COURSE_SIX);
    }

    public void setCourseSix(boolean isCourseSix) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_SIX, isCourseSix);
        }
    }

    @Bindable
    public boolean isCourseSeven() {
        return courseList.contains(Course.COURSE_SEVEN);
    }

    public void setCourseSeven(boolean isCourseSeven) {
        if (!isUpdatingUi) {
            sendRequest(Course.COURSE_SEVEN, isCourseSeven);
        }
    }

    private void sendRequest(Course course, boolean isAddCourse) {
        isDataLoading.set(true);

        if (isAddCourse) {
            courseList.add(course);
        } else {
            courseList.remove(course);
        }

        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDataId(response.getDataId()).
                setDomainId(response.getDomainId()).
                setDomainModel(new RecipeCourseRequest.Model.Builder().
                        setCourseList(courseList).
                        build()).
                build();

        handler.executeAsync(recipeMacro, request, new CourseCallbackListener());
    }
}