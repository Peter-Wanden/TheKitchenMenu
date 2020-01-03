package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;

import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.HashMap;

public class RecipeCourseEditorViewModel
        extends ObservableViewModel
        implements RecipeModelObserver.RecipeModelActions {

    private UseCaseHandler handler;
    private RecipeCourse useCase;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    private String recipeId = "";
    private RecipeCourseResponse response = new RecipeCourseResponse(
            new HashMap<>(),false,false
    );
    private RecipeCourseRequest request = new RecipeCourseRequest(
            recipeId, RecipeCourse.DO_NOT_CLONE,null, false
    );

    private boolean updatingUi;
    private final ObservableBoolean dataLoading = new ObservableBoolean();

    public RecipeCourseEditorViewModel(UseCaseHandler handler,
                                       RecipeCourse useCase) {
        this.handler = handler;
        this.useCase = useCase;
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            dataLoading.set(true);

            request = new RecipeCourseRequest(
                    recipeId,
                    RecipeCourse.DO_NOT_CLONE,
                    null,
                    false
            );
            handler.execute(useCase, request, getCallback());
        }
    }

    @Override
    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            recipeId = cloneToRecipeId;
            dataLoading.set(true);
            request = new RecipeCourseRequest(
                    cloneFromRecipeId,
                    cloneToRecipeId,

                    null,
                    false
            );
            handler.execute(useCase, request, getCallback());
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return this.recipeId.isEmpty() || !this.recipeId.equals(recipeId);
    }

    private UseCaseInteractor.Callback<RecipeCourseResponse> getCallback() {
        return new UseCaseInteractor.Callback<RecipeCourseResponse>() {

            @Override
            public void onSuccess(RecipeCourseResponse response) {
                processResponse(response);
            }

            @Override
            public void onError(RecipeCourseResponse response) {
                processResponse(response);
            }
        };
    }

    private void processResponse(RecipeCourseResponse response) {
        dataLoading.set(false);
        this.response = response;

        if (response.isChanged()) {
            setRecipeCoursesToObservables();
        }
        submitModelStatus(response.isChanged(), response.isValid());
    }

    private void submitModelStatus(boolean isChanged, boolean isValid) {
        modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                isChanged,
                isValid
        ));
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
            processRequest(RecipeCourse.Course.COURSE_ZERO, isCourseZero);
        }
    }

    @Bindable
    public boolean isCourseOne() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_ONE) != null;
    }

    public void setCourseOne(boolean isCourseOne) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_ONE, isCourseOne);
        }
    }

    @Bindable
    public boolean isCourseTwo() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_TWO) != null;
    }

    public void setCourseTwo(boolean isCourseTwo) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_TWO, isCourseTwo);
        }
    }

    @Bindable
    public boolean isCourseThree() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_THREE) != null;
    }

    public void setCourseThree(boolean isCourseThree) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_THREE, isCourseThree);
        }
    }

    @Bindable
    public boolean isCourseFour() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_FOUR) != null;
    }

    public void setCourseFour(boolean isCourseFour) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_FOUR, isCourseFour);
        }
    }

    @Bindable
    public boolean isCourseFive() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_FIVE) != null;
    }

    public void setCourseFive(boolean isCourseFive) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_FIVE, isCourseFive);
        }
    }

    @Bindable
    public boolean isCourseSix() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_SIX) != null;
    }

    public void setCourseSix(boolean isCourseSix) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_SIX, isCourseSix);
        }
    }

    @Bindable
    public boolean isCourseSeven() {
        return response.getCourseList().get(RecipeCourse.Course.COURSE_SEVEN) != null;
    }

    public void setCourseSeven(boolean isCourseSeven) {
        if (!updatingUi) {
            processRequest(RecipeCourse.Course.COURSE_SEVEN, isCourseSeven);
        }
    }

    private void processRequest(RecipeCourse.Course course, boolean addCourse) {
        dataLoading.set(true);
        request = new RecipeCourseRequest(
                recipeId,
                RecipeCourse.DO_NOT_CLONE,
                course,
                addCourse
        );
        handler.execute(useCase, request, getCallback());
    }
}