package com.example.peter.thekitchenmenu.domain.businessentity.recipeIdentity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeIdentityEntity
        extends
        BusinessEntity<RecipeIdentityEntityModel> {

    public enum FailReason
            implements
            FailReasons {
        TITLE_TOO_SHORT(300),
        TITLE_TOO_LONG(301),
        DESCRIPTION_TOO_SHORT(302),
        DESCRIPTION_TOO_LONG(303);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason s : FailReason.values())
                options.put(s.id, s);
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    private static final TextValidator.TextType TITLE_TEXT_TYPE = TextValidator.TextType.SHORT_TEXT;
    private static final TextValidator.TextType DESCRIPTION_TEXT_TYPE = TextValidator.TextType.LONG_TEXT;

    @Nonnull
    private final TextValidator textValidator;
    private boolean isTitleValidationComplete;
    private boolean isDescriptionValidationComplete;

    public RecipeIdentityEntity(@Nonnull TextValidator textValidator) {
        this.textValidator = textValidator;
    }

    protected void processElements() {
        validateTitle();
        validateDescription();

        if (isTitleValidationComplete && isDescriptionValidationComplete) {
            sendResponse();
        }
    }

    private void validateTitle() {
        isTitleValidationComplete = false;
        TextValidatorRequest request = new TextValidatorRequest(
                TITLE_TEXT_TYPE,
                new TextValidatorModel(model.getTitle())
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                processResults();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addTitleFailReasonFromTextValidator(response.getFailReason());
                processResults();
            }

            private void processResults() {
                isTitleValidationComplete = true;
            }
        });
    }

    private void addTitleFailReasonFromTextValidator(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(FailReason.TITLE_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(FailReason.TITLE_TOO_LONG);
        }
    }

    private void validateDescription() {
        isDescriptionValidationComplete = false;
        TextValidatorRequest request = new TextValidatorRequest(
                DESCRIPTION_TEXT_TYPE,
                new TextValidatorModel(model.getDescription())
        );
        textValidator.execute(request, new UseCaseBase.Callback<TextValidatorResponse>() {
            @Override
            public void onUseCaseSuccess(TextValidatorResponse response) {
                processResults();
            }

            @Override
            public void onUseCaseError(TextValidatorResponse response) {
                addDescriptionFailReasonsFromTextValidator(response.getFailReason());
                processResults();
            }

            private void processResults() {
                isDescriptionValidationComplete = true;
            }
        });
    }

    private void addDescriptionFailReasonsFromTextValidator(FailReasons failReason) {
        if (failReason == TextValidator.FailReason.TOO_SHORT) {
            failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_SHORT);

        } else if (failReason == TextValidator.FailReason.TOO_LONG) {
            failReasons.add(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG);
        }
    }

    @Override
    protected void sendResponse() {
        callback.onProcessed(
                new Response<>(
                        new RecipeIdentityEntityModel(
                                model.getTitle(),
                                model.getDescription()),
                        new ArrayList<>(failReasons))
        );
    }
}
