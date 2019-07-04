package com.example.peter.thekitchenmenu.ui.detail.recipe;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;

public class RecipeIdentityViewModel extends AndroidViewModel {

    private static final String TAG = "tkm-RecipeIdentityVM";

    private Application application;
    private Resources resources;

    MediatorLiveData <RecipeIdentityModel> recipeIdentityModel;
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<Integer> prepHours = new ObservableField<>(0);
    public final ObservableField<Integer> prepMinutes = new ObservableField<>(0);
    public final ObservableField<Integer> cookHours = new ObservableField<>(0);
    public final ObservableField<Integer> cookMinutes = new ObservableField<>(0);

    private final SingleLiveEvent<String> titleErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> descriptionErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> prepTimeErrorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> cookTimeErrorEvent = new SingleLiveEvent<>();

    private boolean titleValidated;
    private boolean descriptionValidated = true;
    private boolean prepTimeValidated = true;
    private boolean cookTimeValidated = true;
    public final ObservableBoolean allFieldsValidated = new ObservableBoolean();

    public RecipeIdentityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        resources = application.getResources();

        title.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                titleChanged();
            }
        });
        description.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                descriptionChanged();
            }
        });
        prepHours.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                prepTimeChanged();
            }
        });
        prepMinutes.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                prepTimeChanged();
            }
        });
        cookHours.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                cookTimeChanged();
            }
        });
        cookMinutes.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                cookTimeChanged();
            }
        });
    }

    void start(RecipeIdentityModel identityModel) {
        if (identityModel != null) {
            title.set(identityModel.getTitle());
            description.set(identityModel.getDescription());
            prepHours.set(getHours(identityModel.getPrepTime()));
            prepMinutes.set(getMinutes(identityModel.getPrepTime()));
            cookHours.set(getHours(identityModel.getCookTime()));
            cookMinutes.set(getMinutes(identityModel.getCookTime()));
        }
    }

    private void titleChanged() {
        String validationResponse = validateShortText(title.get());
        if (validationResponse.equals(TextValidationHandler.VALIDATED))
            titleValidated = true;
        else {
            titleValidated = false;
            titleErrorEvent.setValue(validationResponse);
        }
        checkAllFieldsValidated();
    }

    private void descriptionChanged() {
        String validationResponse = validateLongText(description.get());
        if (validationResponse.equals(TextValidationHandler.VALIDATED))
            descriptionValidated = true;
        else {
            descriptionValidated = false;
            descriptionErrorEvent.setValue(validationResponse);
        }
        checkAllFieldsValidated();
    }

    private String validateShortText(String textToValidate) {
        return TextValidationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return TextValidationHandler.validateLongText(resources, textToValidate);
    }

    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    private void prepTimeChanged() {
        int maxPrepTime = resources.getInteger(R.integer.recipe_max_prep_time_in_minutes);
        String prepTimeErrorMessage = resources.getString(
                R.string.input_error_recipe_prep_time_too_long);

        if ((prepHours.get() * 60) + prepMinutes.get() > maxPrepTime) {
            prepTimeValidated = false;
            prepTimeErrorEvent.setValue(prepTimeErrorMessage);
        } else {
            prepTimeValidated = true;
        }
    }

    private void cookTimeChanged() {
        int maxCookTime = application.getResources().
                getInteger(R.integer.recipe_max_cook_time_in_minutes);
        String cookTimeErrorMessage = resources.getString(
                R.string.input_error_recipe_cook_time_too_long);

        if ((cookHours.get() * 60) + cookMinutes.get() > maxCookTime) {
            cookTimeValidated = false;
            cookTimeErrorEvent.setValue(cookTimeErrorMessage);
        } else {
            cookTimeValidated = true;
        }
    }

    private void checkAllFieldsValidated() {
        if (titleValidated && descriptionValidated && prepTimeValidated && cookTimeValidated)
            allFieldsValidated.set(true);
        else
            allFieldsValidated.set(false);
    }

    SingleLiveEvent<String> getTitleErrorEvent() {
        return titleErrorEvent;
    }

    SingleLiveEvent<String> getDescriptionErrorEvent() {
        return descriptionErrorEvent;
    }

    SingleLiveEvent<String> getPrepTimeErrorEvent() {
        return prepTimeErrorEvent;
    }

    SingleLiveEvent<String> getCookTimeErrorEvent() {
        return cookTimeErrorEvent;
    }

    void updateRecipeWithIdentityModel() {

    }

}
