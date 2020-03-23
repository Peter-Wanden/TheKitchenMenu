package com.example.peter.thekitchenmenu.domain.usecase.recipe.state;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;

import org.junit.*;
import org.mockito.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class RecipeStateCalculatorTest {

    // region constants ----------------------------------------------------------------------------

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

    private HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> componentStates;

    private RecipeStateResponse onSuccessResponse;
    private RecipeStateResponse onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeStateCalculator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        componentStates = new LinkedHashMap<>();
        SUT = new RecipeStateCalculator();
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCase.Callback<RecipeStateResponse> getCallback() {
        return new UseCase.Callback<RecipeStateResponse>() {
            @Override
            public void onSuccess(RecipeStateResponse response) {
                if (response != null) {
                    onSuccessResponse = response;
                }
            }

            @Override
            public void onError(RecipeStateResponse response) {
                if (response != null) {
                    onErrorResponse = response;
                }
            }
        };
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}