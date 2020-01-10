package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.domain.FailReasons;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIdentityMediator
        extends UseCase<RecipeIdentityRequest, RecipeIdentityResponse>
        implements UseCase.Callback<RecipeIdentityResponse> {

    private static final String TAG = "tkm-" + RecipeIdentityMediator.class.getSimpleName() + ": ";

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private RecipeIdentity recipeIdentity;
    @Nonnull
    private TextValidator textValidator;

    private String recipeId = "";
    private RecipeIdentityResponse response;

    private List<FailReasons> failReasons = new ArrayList<>();
    private RecipeIdentityModel.Builder modelBuilder = new RecipeIdentityModel.Builder();

    public RecipeIdentityMediator(@Nonnull UseCaseHandler handler,
                                  @Nonnull RecipeIdentity recipeIdentity,
                                  @Nonnull TextValidator textValidator) {
        this.handler = handler;
        this.recipeIdentity = recipeIdentity;
        this.textValidator = textValidator;
    }

    @Override
    protected void execute(RecipeIdentityRequest request) {
        System.out.println(TAG + request);
        failReasons.clear();
        if (isNewRequest()) {
            this.recipeId = getRequest().getRecipeId();
            executeUseCaseRecipeIdentity();
        } else {
            validateText();
        }
    }

    private boolean isNewRequest() {
        return !recipeId.equals(getRequest().getRecipeId());
    }

    private void validateText() {
        validateTitle();
    }

    private void validateTitle() {
        String title = getRequest().getModel().getTitle();

        TextValidatorRequest request = new TextValidatorRequest(
                RecipeIdentity.TITLE_TEXT_TYPE,
                new TextValidatorModel(title)
        );
        handler.execute(textValidator, request, new Callback<TextValidatorResponse>() {

            @Override
            public void onSuccess(TextValidatorResponse response) {
                processTitleValidationResponse(response);
            }

            @Override
            public void onError(TextValidatorResponse response) {
                modelBuilder.setTitle(response.getModel().getText());
                processTitleValidationResponse(response);
            }
        });
    }

    private void processTitleValidationResponse(TextValidatorResponse response) {
        TextValidator.FailReason failReason = response.getFailReason();

        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_LONG);

        } else if (failReason == TextValidator.FailReason.NONE) {
            failReasons.add(RecipeIdentity.FailReason.TITLE_OK);
        }
        validateDescription();
    }

    private void validateDescription() {
        String description = getRequest().getModel().getDescription();

        TextValidatorRequest request = new TextValidatorRequest(
                RecipeIdentity.DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(description)
        );
        handler.execute(textValidator, request, new Callback<TextValidatorResponse>() {

            @Override
            public void onSuccess(TextValidatorResponse response) {
                processDescriptionValidationResponse(response);
            }

            @Override
            public void onError(TextValidatorResponse response) {
                modelBuilder.setTitle(response.getModel().getText());
                processDescriptionValidationResponse(response);
            }
        });
    }

    private void processDescriptionValidationResponse(TextValidatorResponse response) {
        TextValidator.FailReason failReason = response.getFailReason();

        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_SHORT);
        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG);
        } else if (failReason == TextValidator.FailReason.NONE) {
            failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_OK);
        }
        processIdentityRequest();
    }

    private void processIdentityRequest() {
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_OK) &&
                failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_OK)) {
            executeUseCaseRecipeIdentity();
        } else {
            buildResponse();
        }
    }

    private void executeUseCaseRecipeIdentity() {
        handler.execute(recipeIdentity, getRequest(), this);
    }

    @Override
    public void onSuccess(RecipeIdentityResponse response) {
        this.response = response;
        failReasons.addAll(this.response.getFailReasons());
        buildResponse();
        getUseCaseCallback().onSuccess(this.response);
    }

    @Override
    public void onError(RecipeIdentityResponse response) {
        this.response = response;
        failReasons.addAll(this.response.getFailReasons());
        buildResponse();
        getUseCaseCallback().onError(this.response);
    }

    private void buildResponse() {

        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder();
        builder.setState(response.getState());
        builder.setModel(response.getModel());
        builder.setFailReasons(failReasons);



        this.response = builder.build();
        System.out.println(TAG + this.response);
    }
}
