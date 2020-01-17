package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState.*;

public class Recipe
        <Q extends UseCaseCommand.Request, P extends UseCaseCommand.Response>
        extends UseCase<Q, P> {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    private final UseCaseHandler handler;
    private RecipeIdentity identity;
    private TextValidator textValidator;

    private RecipeState recipeState;

    private HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

    public Recipe(UseCaseHandler handler, UseCaseFactory factory) {
        this.handler = handler;
        createComponents(factory);
    }

    private void createComponents(UseCaseFactory factory) {
        identity = factory.provideRecipeIdentity(this);
        textValidator = factory.provideTextValidator();
    }

    @Override
    protected void execute(Q request) {
        if (request instanceof TextValidatorRequest) {
            handler.execute(textValidator, request, );
        }
    }
}
