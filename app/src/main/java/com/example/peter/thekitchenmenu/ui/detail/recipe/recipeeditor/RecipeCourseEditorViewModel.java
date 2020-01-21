package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

public class RecipeCourseEditorViewModel
        extends
        ObservableViewModel
        implements
        RecipeModelObserver.RecipeModelActions,  UseCaseCommand.Callback<RecipeCourseResponse>{

    private UseCaseHandler handler;
    private RecipeCourse useCase;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    private String recipeId = "";
    private RecipeCourseResponse response;
    private RecipeCourseRequest request;

    private boolean updatingUi;
    private final ObservableBoolean dataLoading = new ObservableBoolean();

    public RecipeCourseEditorViewModel(UseCaseHandler handler,
                                       RecipeCourse useCase) {
        this.handler = handler;
        this.useCase = useCase;
        request = RecipeCourseRequest.Builder.getDefault().build();
        response = RecipeCourseResponse.Builder.getDefault().build();
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            dataLoading.set(true);

            request = RecipeCourseRequest.Builder.getDefault().setRecipeId(recipeId).build();
            handler.execute(useCase, request, this);
        }
    }

    @Override
    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            recipeId = cloneToRecipeId;
            dataLoading.set(true);

            request = RecipeCourseRequest.Builder.getDefault().
                    setRecipeId(cloneFromRecipeId).
                    setCloneToRecipeId(cloneToRecipeId).
                    build();

            handler.execute(useCase, request, this);
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return this.recipeId.isEmpty() || !this.recipeId.equals(recipeId);
    }

    @Override
    public void onSuccess(RecipeCourseResponse response) {
        processResponse(response);
    }

    @Override
    public void onError(RecipeCourseResponse response) {
        processResponse(response);
    }

    private void processResponse(RecipeCourseResponse response) {
        dataLoading.set(false);
        this.response = response;
        RecipeState.ComponentState status = response.getStatus();

        if (status.equals(RecipeState.ComponentState.VALID_CHANGED) ||
                status.equals(RecipeState.ComponentState.INVALID_CHANGED)) {
            setRecipeCoursesToObservables();
        }

        submitModelStatus(status);
    }

    private void submitModelStatus(RecipeState.ComponentState componentState) {
        RecipeComponentStateModel model = new RecipeComponentStateModel(
                RecipeState.ComponentName.COURSE,
                componentState
        );
        modelSubmitter.submitRecipeComponentStatus(model);
    }

    private void setRecipeCoursesToObservables() {
        updatingUi = true;
        notifyChange();
        updatingUi = false;
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

        request = RecipeCourseRequest.Builder.getDefault().
                setRecipeId(recipeId).
                setCourse(course).
                setAddCourse(addCourse).
                build();
        handler.execute(useCase, request, this);
    }
}