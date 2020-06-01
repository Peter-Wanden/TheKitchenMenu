package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;

import java.util.List;

import javax.annotation.Nullable;

public class RecipeIdentityEditorViewImpl
        extends
        BaseObservableViewMvc<RecipeIdentityEditorView.Listener>
        implements RecipeIdentityEditorView {

    private final Resources resources;

    private final TextView titleLabelTextView;
    private final EditText titleEditText;

    private final TextView descriptionLabelTextView;
    private final EditText descriptionEditText;

    private boolean isUpdatingUi;
    private boolean isDataLoadingError;

    public RecipeIdentityEditorViewImpl(LayoutInflater inflater,
                                        @Nullable ViewGroup parent) {
        setRootView(inflater.inflate(
                R.layout.recipe_identity_editor_fragment, parent, false)
        );

        resources = getContext().getResources();

        titleLabelTextView = findViewById(R.id.recipe_editor_title_label);
        titleEditText = findViewById(R.id.editable_recipe_title);

        descriptionLabelTextView = findViewById(R.id.recipe_description_label);
        descriptionEditText = findViewById(R.id.editable_recipe_description);

        setLabels();
        attachTextChangedListeners();
    }

    private void setLabels() {
        titleLabelTextView.setText(getString(R.string.recipe_title_label));
        descriptionLabelTextView.setText(getString(R.string.recipe_description_label));
    }

    private void attachTextChangedListeners() {
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdatingUi) {
                    String title = s.toString();
                    if (!title.isEmpty()) {
                        getListeners().forEach(listener ->
                                listener.identityViewOnTitleChanged(title));
                    }
                }
            }
        });

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdatingUi) {
                    String description = s.toString();
                    if (!description.isEmpty()) {
                        getListeners().forEach(listener ->
                                listener.identityViewOnDescriptionChanged(description));
                    }
                }
            }
        });
    }

    @Override
    public void updateViewWithOnSuccessResponse(RecipeIdentityResponse response) {
        isUpdatingUi = true;

        titleEditText.setError(null);
        descriptionEditText.setError(null);

        RecipeIdentityResponse.DomainModel domainModel = response.getDomainModel();
        titleEditText.setText(domainModel.getTitle());
        descriptionEditText.setText(domainModel.getDescription());

        isUpdatingUi = false;
    }

    @Override
    public void updateViewWithOnErrorResponse(RecipeIdentityResponse response) {
        isUpdatingUi = true;

        List<FailReasons> failReasons = response.getMetadata().getFailReasons();

        if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
            isDataLoadingError = true;
        }

        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT)) {
            titleEditText.setError(
                    resources.getString(
                            R.string.input_error_text_too_short,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_max_length))));
        }

        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG)) {
            titleEditText.setError(
                    resources.getString(R.string.input_error_text_too_long,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_max_length))));
        }

        if (failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG)) {
            descriptionEditText.setError(
                    resources.getString(R.string.input_error_text_too_long,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_long_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_long_text_max_length))));
        }

        RecipeIdentityResponse.DomainModel domainModel = response.getDomainModel();
        titleEditText.setText(domainModel.getTitle());
        descriptionEditText.setText(domainModel.getDescription());

        isUpdatingUi = false;
    }

    @Override
    public void showDataLoadingError() {

    }

    @Override
    public void hideDataLoadingError() {

    }
}
