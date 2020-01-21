package com.example.peter.thekitchenmenu.domain.usecase.recipestate;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;

import org.junit.*;
import org.mockito.*;

public class RecipeStateTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeState SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeState();

    }



    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}