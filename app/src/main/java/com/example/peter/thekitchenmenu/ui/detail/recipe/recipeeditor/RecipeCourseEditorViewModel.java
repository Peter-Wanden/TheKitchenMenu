package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeCourseEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-" + RecipeCourseEditorViewModel.class.getSimpleName() +
            "; ";

    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private RecipeMacro recipeMacro;
    private RecipeCourseResponse response;

    private boolean updatingUi;
    private final ObservableBoolean dataLoading = new ObservableBoolean(true);

    public RecipeCourseEditorViewModel(@Nonnull UseCaseHandler handler,
                                       @Nonnull RecipeMacro recipeMacro) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;

        response = RecipeCourseResponse.Builder.getDefault().build();

        recipeMacro.registerComponentCallback(new Pair<>(ComponentName.COURSE,
                new CourseCallbackListener())
        );
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link RecipeMacro}
     */
    private class CourseCallbackListener implements UseCase.Callback<RecipeCourseResponse> {
        @Override
        public void onSuccess(RecipeCourseResponse response) {
            System.out.println(TAG + "onSuccess:" + response);
            RecipeCourseEditorViewModel.this.response = response;
            checkState(response);
        }

        @Override
        public void onError(RecipeCourseResponse response) {
            System.out.println(TAG + "onError:" + response);
            RecipeCourseEditorViewModel.this.response = response;
            onUseCaseError(response);
        }
    }

    private void checkState(RecipeCourseResponse response) {
        if (isStateChanged(response)) {
            processResponse(response);
        }
    }

    private boolean isStateChanged(RecipeCourseResponse response) {
        return !this.response.equals(response);
    }

    private void processResponse(RecipeCourseResponse response) {
        dataLoading.set(false);
        this.response = response;
        setRecipeCoursesToObservables();
    }

    private void setRecipeCoursesToObservables() {
        updatingUi = true;
        notifyChange();
        updatingUi = false;
    }

    private void onUseCaseError(RecipeCourseResponse response) {

    }

    @Bindable
    public boolean isCourseZero() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_ZERO) != null;
    }

    public void setCourseZero(boolean isCourseZero) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_ZERO, isCourseZero);
        }
    }

    @Bindable
    public boolean isCourseOne() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_ONE) != null;
    }

    public void setCourseOne(boolean isCourseOne) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_ONE, isCourseOne);
        }
    }

    @Bindable
    public boolean isCourseTwo() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_TWO) != null;
    }

    public void setCourseTwo(boolean isCourseTwo) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_TWO, isCourseTwo);
        }
    }

    @Bindable
    public boolean isCourseThree() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_THREE) != null;
    }

    public void setCourseThree(boolean isCourseThree) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_THREE, isCourseThree);
        }
    }

    @Bindable
    public boolean isCourseFour() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_FOUR) != null;
    }

    public void setCourseFour(boolean isCourseFour) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_FOUR, isCourseFour);
        }
    }

    @Bindable
    public boolean isCourseFive() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_FIVE) != null;
    }

    public void setCourseFive(boolean isCourseFive) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_FIVE, isCourseFive);
        }
    }

    @Bindable
    public boolean isCourseSix() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_SIX) != null;
    }

    public void setCourseSix(boolean isCourseSix) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_SIX, isCourseSix);
        }
    }

    @Bindable
    public boolean isCourseSeven() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_SEVEN) != null;
    }

    public void setCourseSeven(boolean isCourseSeven) {
        if (!updatingUi) {
            sendRequest(RecipeCourse.Course.COURSE_SEVEN, isCourseSeven);
        }
    }

    private void sendRequest(RecipeCourse.Course course, boolean addCourse) {
        dataLoading.set(true);

        RecipeCourseRequest request = RecipeCourseRequest.Builder.getDefault().
                setId(response.getId()).
                setCourse(course).
                setAddCourse(addCourse).
                build();
        handler.execute(recipeMacro, request, new CourseCallbackListener());
    }
}