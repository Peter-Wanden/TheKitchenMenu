package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.*;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class RecipeCourseEntitySelectorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final String RECIPE_ID = "recipeId";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    RecipeCourseSelectorViewModel SUT;

    @Before
    public void setup() throws Exception {
        SUT = new RecipeCourseSelectorViewModel();
    }

    // breakfastSelected_true_courseId1AndRecipeIdSavedToDatabase

    // breakfastSelected_false_courseId1AndRecipeIdRemovedFromToDatabase
    // lunchSelected_true_courseId2AndRecipeIdSavedToDatabase
    // lunchSelected_false_courseId2AndRecipeIdRemovedFromDatabase
    // mainCourseSelected_true_courseId3AndRecipeIdSavedToDatabase
    // mainCourseSelected_false_courseId3AndRecipeIdRemovedFromDatabase
    // accompanimentSelected_true_courseId4AndRecipeIdSavedToDatabase
    // accompanimentSelected_false_courseId4AndRecipeIdRemovedFromDatabase
    // desertSelected_true_courseId5AndRecipeIdSavedToDatabase
    // desertSelected_false_courseId5AndRecipeIdRemovedFromDatabase
    // sideSelected_true_courseId6AndRecipeIdSavedToDatabase
    // sideSelected_false_courseId6AndRecipeIdRemovedFromDatabase
    // snackSelected_true_courseId7AndRecipeIdSavedToDatabase
    // snackSelected_false_courseI76AndRecipeIdRemovedFromDatabase
    // drinkSelected_true_courseId8AndRecipeIdSavedToDatabase
    // drinkSelected_false_courseId8AndRecipeIdRemovedFromDatabase

    // listOfThreeCourseObjectsReceived_setToDisplay


    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}