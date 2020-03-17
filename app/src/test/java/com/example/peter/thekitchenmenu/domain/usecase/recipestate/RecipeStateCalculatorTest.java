package com.example.peter.thekitchenmenu.domain.usecase.recipestate;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateResponse;

import org.junit.*;
import org.mockito.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;
import static org.junit.Assert.*;

public class RecipeStateCalculatorTest {

    // region constants ----------------------------------------------------------------------------

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

    HashMap<ComponentName, ComponentState> componentStates;

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

    @Test
    public void requestWithMissingComponent_stateDATA_UNAVAILABLE_failReasonMISSING_MODELS() {
        // Arrange
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        // portions deliberately missing
        // Act
        handler.execute(SUT, new RecipeStateRequest(componentStates), getCallback());
        // Assert
        assertEquals(RecipeState.DATA_UNAVAILABLE, onErrorResponse.getState());
        assertTrue(onErrorResponse.getFailReasons().contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void requestWithInvalidUnchangedComponent_stateINVALID_UNCHANGED_failReasonINVALID_MODELS() {
        // Arrange
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        // Act
        handler.execute(SUT, new RecipeStateRequest(componentStates), getCallback());
        // Assert
        assertEquals(RecipeState.INVALID_UNCHANGED, onErrorResponse.getState());
        assertTrue(onErrorResponse.getFailReasons().contains(FailReason.INVALID_COMPONENTS));
    }

    @Test
    public void requestWithInvalidChangedComponent_stateINVALID_CHANGED_failReasonINVALID_MODELS() {
        // Arrange
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_CHANGED);
        // Act
        handler.execute(SUT, new RecipeStateRequest(componentStates), getCallback());
        // Assert
        assertEquals(RecipeState.INVALID_CHANGED, onErrorResponse.getState());
        assertTrue(onErrorResponse.getFailReasons().contains(FailReason.INVALID_COMPONENTS));
    }

    @Test
    public void requestWithValidUnchangedComponent_stateVALID_UNCHANGED_failReasonNONE() {
        // Arrange
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);
        // Act
        handler.execute(SUT, new RecipeStateRequest(componentStates), getCallback());
        // Assert
        assertEquals(RecipeState.VALID_UNCHANGED, onSuccessResponse.getState());
        assertTrue(onSuccessResponse.getFailReasons().contains(FailReason.NONE));
    }

    @Test
    public void requestWithValidChangedComponent_stateVALID_CHANGED_failReasonNONE() {
        // Arrange
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.VALID_CHANGED);
        // Act
        handler.execute(SUT, new RecipeStateRequest(componentStates), getCallback());
        // Assert
        assertEquals(RecipeState.VALID_CHANGED, onSuccessResponse.getState());
        assertTrue(onSuccessResponse.getFailReasons().contains(FailReason.NONE));
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