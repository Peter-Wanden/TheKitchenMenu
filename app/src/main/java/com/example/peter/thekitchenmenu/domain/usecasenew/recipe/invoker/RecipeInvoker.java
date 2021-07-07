package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker;


import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseInvoker;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequestProto;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCase;

import java.util.HashMap;
import java.util.Map;

import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.RECIPE;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.RecipeComponentName.RECIPE_INVOKER;

public class RecipeInvoker
        extends
        UseCaseInvoker {

    public static final String CREATE_NEW_RECIPE = "CREATE_NEW_RECIPE";

    private UseCaseHandler handler;

    private Map<RecipeComponentName, ? extends UseCase> components = new HashMap<>();

    private String recipeDomainId;
    private String dataId;

    private RecipeCourseUseCase courseUseCase;

    public RecipeInvoker(UseCaseHandler handler,
                         RecipeMacroMetadataUseCase metadataUseCase,
                         RecipeCourseUseCase courseUseCase,
                         RecipeDurationUseCase durationUseCase,
                         RecipeIdentityUseCase identityUseCase,
                         RecipePortionsUseCase portionsUseCase) {

        this.handler = handler;

        components.put(RecipeComponentName.COURSE, courseUseCase);
        components.put(RecipeComponentName.DURATION, durationUseCase);
        components.put(RecipeComponentName.IDENTITY, identityUseCase);
        components.put(RecipeComponentName.PORTIONS, portionsUseCase);
    }

    @Override
    protected void execute(UseCaseRequest<? extends DomainModel.UseCaseRequestModel> request) {
        String domainId = request.getDomainId();
        if (isNewRequest || RECIPE == requestOriginator) {
            dataId = request.getDataId();
            recipeDomainId = domainId;
            startComponents();
            System.out.println(TAG + "startComponents");
        } else {
            processRequest(request);
            System.out.println(TAG + "processRequest");
        }
    }

    private void startComponents() {
        UseCaseRequestProto request = new UseCaseRequestProto.Builder()
                .setUseCaseComponentName(RECIPE_INVOKER)
                .setDataId(dataId)
                .setDomainId(recipeDomainId)
                .build();

        for (UseCase useCase : components.values()) {
            useCase.execute(
                    request,
                    new UseCaseCallback<UseCaseResponseProto>() {

                        @Override
                        public void onSuccess(UseCaseResponseProto useCaseResponseProto) {

                        }

                        @Override
                        public void onError(UseCaseResponseProto useCaseResponseProto) {

                        }
                    }
            );
        }
    }
}