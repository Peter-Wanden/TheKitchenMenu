package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker;


import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseInvoker;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequestProto;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel.UseCaseRequestModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel.UseCaseResponseModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCaseRequestModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCase;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.RECIPE;

public class RecipeInvoker extends UseCaseInvoker {

    public static final String CREATE_NEW_RECIPE = "CREATE_NEW_RECIPE";

    private UseCaseHandler handler;

    private List<UseCase<? extends UseCaseRequestModel, ? extends UseCaseResponseModel>>
            components = new ArrayList<>();

    private String recipeDomainId;
    private String dataId;

    public RecipeInvoker(UseCaseHandler handler,
                         RecipeMacroMetadataUseCase metadataUseCase,
                         RecipeCourseUseCase courseUseCase,
                         RecipeDurationUseCase durationUseCase,
                         RecipeIdentityUseCase identityUseCase,
                         RecipePortionsUseCase portionsUseCase) {

        this.handler = handler;

        components.add(courseUseCase);
        components.add(durationUseCase);
        components.add(identityUseCase);
        components.add(portionsUseCase);
    }

    @Override
    protected void execute(UseCaseRequest<? extends DomainModel.UseCaseRequestModel> request) {
        String domainId = request.getDomainId();
        boolean isNewRequest = !this.dataId.equals(domainId);

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
        UseCaseRequestProto request =
                new UseCaseRequestProto.Builder<RecipeIdentityUseCaseRequestModel>()
                .setUseCaseComponentName(RecipeComponentName.IDENTITY)
                .setDataId(dataId)
                .setDomainId(recipeDomainId)
                .setRequestModel()
    }

}