package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.domain.FailReasons;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
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
    private boolean isTextValidationError;
    private boolean isNewRequest;

    private List<FailReasons> failReasons = new ArrayList<>();
    private RecipeIdentityResponse.Model.Builder responseModelBuilder;

    public RecipeIdentityMediator(@Nonnull UseCaseHandler handler,
                                  @Nonnull RecipeIdentity recipeIdentity,
                                  @Nonnull TextValidator textValidator) {
        this.handler = handler;
        this.recipeIdentity = recipeIdentity;
        this.textValidator = textValidator;
        initialiseInstanceVariables();
    }

    @Override
    protected void execute(RecipeIdentityRequest request) {
        System.out.println(TAG + request);
        if (isNewRequest()) {
            isNewRequest = true;
            this.recipeId = getRequest().getRecipeId();
            executeUseCaseRecipeIdentity();
        } else {
            isNewRequest = false;
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
                validateDescription();
            }

            @Override
            public void onError(TextValidatorResponse response) {
                isTextValidationError = true;
                responseModelBuilder.setTitle(response.getModel().getText());

                TextValidator.FailReason failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_LONG);
                }
                validateDescription();
            }
        });
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
                if (!isTextValidationError) {
                    executeUseCaseRecipeIdentity();
                } else {
                    responseModelBuilder.setDescription(response.getModel().getText());
                    buildTextValidationErrorResponse();
                }
            }

            @Override
            public void onError(TextValidatorResponse response) {
                responseModelBuilder.setDescription(response.getModel().getText());

                TextValidator.FailReason failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG);
                }
                buildTextValidationErrorResponse();
            }
        });
    }

    private void buildTextValidationErrorResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder();
        builder.setState(response.getState());
        builder.setFailReasons(failReasons);

        responseModelBuilder.
                setId(response.getModel().getId()).
                setCreateDate(response.getModel().getCreateDate()).
                setLastUpdate(response.getModel().getLastUpdate());
        builder.setModel(responseModelBuilder.build());

        response = builder.build();

        isTextValidationError = false;
        responseModelBuilder = RecipeIdentityResponse.Model.Builder.getDefault();

        onError(response);
    }

    private void executeUseCaseRecipeIdentity() {
        handler.execute(recipeIdentity, getRequest(), this);
    }

    @Override
    public void onSuccess(RecipeIdentityResponse response) {
        this.response = response;
        if (isNewRequest) {
            // TODO - Intercept response of a new request and pass through text validation
            System.out.println(TAG + "new response going through text validation:" + response);
            validateTextFromNewResponse();
        } else {
            System.out.println(TAG + this.response);
            getUseCaseCallback().onSuccess(this.response);
        }
    }

    @Override
    public void onError(RecipeIdentityResponse response) {
        this.response = response;
        System.out.println(TAG + this.response);

        getUseCaseCallback().onError(this.response);
    }

    private void validateTextFromNewResponse() {
        isNewRequest = false;
        String title = response.getModel().getTitle();

        TextValidatorRequest request = new TextValidatorRequest(
                RecipeIdentity.TITLE_TEXT_TYPE,
                new TextValidatorModel(title)
        );
        handler.execute(textValidator, request, new Callback<TextValidatorResponse>() {
            @Override
            public void onSuccess(TextValidatorResponse response) {
                validateDescriptionFromNewResponse();
            }

            @Override
            public void onError(TextValidatorResponse response) {
                isTextValidationError = true;
                responseModelBuilder.setTitle(response.getModel().getText());

                TextValidator.FailReason failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(RecipeIdentity.FailReason.TITLE_TOO_LONG);
                }
                validateDescriptionFromNewResponse();
            }
        });
    }

    private void validateDescriptionFromNewResponse() {
        String description = response.getModel().getDescription();

        TextValidatorRequest request = new TextValidatorRequest(
                RecipeIdentity.DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(description)
        );
        handler.execute(textValidator, request, new Callback<TextValidatorResponse>() {
            @Override
            public void onSuccess(TextValidatorResponse response) {
                responseModelBuilder.setDescription(response.getModel().getText());

                if (!isTextValidationError) {
                    buildTextValidationErrorFromNewRequestResponse();
                } else {
                    failReasons.add(RecipeIdentity.FailReason.NONE);

                    RecipeIdentityResponse finalResponse = new RecipeIdentityResponse.Builder().
                            setState(RecipeState.ComponentState.VALID_UNCHANGED).
                            setFailReasons(failReasons).
                            setModel(responseModelBuilder.build()).
                            build();
                    getUseCaseCallback().onSuccess(finalResponse);
                }
            }

            @Override
            public void onError(TextValidatorResponse response) {
                responseModelBuilder.setDescription(response.getModel().getText());

                TextValidator.FailReason failReason = response.getFailReason();
                if (failReason == TextValidator.FailReason.TOO_SHORT) {
                    failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_SHORT);

                } else if (failReason == TextValidator.FailReason.TOO_LONG) {
                    failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG);
                }
                buildTextValidationErrorFromNewRequestResponse();
            }
        });
    }

    private void buildTextValidationErrorFromNewRequestResponse() {
        RecipeIdentityResponse.Builder builder = new RecipeIdentityResponse.Builder();
        builder.setState(RecipeState.ComponentState.INVALID_UNCHANGED).
                setFailReasons(failReasons).
                setModel(responseModelBuilder.build());

        responseModelBuilder.
                setId(response.getModel().getId()).
                setCreateDate(response.getModel().getCreateDate()).
                setLastUpdate(response.getModel().getLastUpdate());

        builder.setModel(responseModelBuilder.build());
        isTextValidationError = false;
        onError(response);
    }
    private void initialiseInstanceVariables() {
        isTextValidationError = false;
        responseModelBuilder = RecipeIdentityResponse.Model.Builder.getDefault();
    }
}
