package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.VALIDATED;

public class IngredientViewModel
        extends ViewModel
        implements DataSource.GetEntityCallback<IngredientEntity> {

    private Resources resources;
    private DataSource<IngredientEntity> dataSource;
    private TextValidationHandler textValidationHandler;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;

    public final ObservableField<String> nameObservable = new ObservableField<>("");
    public final ObservableField<String> descriptionObservable = new ObservableField<>("");

    public final ObservableField<String> nameErrorMessageObservable = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessageObservable = new ObservableField<>();
    public final MutableLiveData<Boolean> showDoneButtonLiveData = new MutableLiveData<>();


    private IngredientEntity ingredientEntity;
    private String ingredientId;

    private boolean modelUpdating;
    private boolean nameValid;
    private boolean descriptionValid = true;

    public IngredientViewModel(Resources resources,
                               DataSource<IngredientEntity> dataSource,
                               TextValidationHandler textValidationHandler,
                               UniqueIdProvider idProvider,
                               TimeProvider timeProvider) {
        this.resources = resources;
        this.dataSource = dataSource;
        this.textValidationHandler = textValidationHandler;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        nameObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!nameObservable.get().isEmpty())
                    nameChanged();
            }
        });
        descriptionObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!descriptionObservable.get().isEmpty())
                    descriptionChanged();
            }
        });
    }

    void start() {
        ingredientEntity = createNewIngredientEntity();
    }

    void start(String ingredientId) {
        this.ingredientId = ingredientId;
        dataSource.getById(ingredientId, this);
    }

    @Override
    public void onEntityLoaded(IngredientEntity ingredientEntity) {
        this.ingredientEntity = ingredientEntity;
        updateObservables();
    }

    @Override
    public void onDataNotAvailable() {
        ingredientEntity = createNewIngredientEntity();
    }

    private void updateObservables() {
        modelUpdating = true;
        nameObservable.set(ingredientEntity.getName());
        descriptionObservable.set(ingredientEntity.getDescription());
        modelUpdating = false;
    }

    private void nameChanged() {
        nameErrorMessageObservable.set(null);
        String validationResponse = validateShortText(nameObservable.get());

        nameValid = validationResponse.equals(VALIDATED);

        if (!nameValid)
            nameErrorMessageObservable.set(validationResponse);

        updateModel();
    }

    private void descriptionChanged() {
        descriptionErrorMessageObservable.set(null);
        String validationResponse = validateLongText(descriptionObservable.get());

        descriptionValid = validationResponse.equals(VALIDATED);
        if (!descriptionValid)
            descriptionErrorMessageObservable.set(validationResponse);

        updateModel();
    }

    private String validateShortText(String textToValidate) {
        return textValidationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return textValidationHandler.validateLongText(resources, textToValidate);
    }

    private void updateModel() {
        if (!modelUpdating) {
            if (isModelValid() && modelHasChanged()) {
                ingredientEntity = getUpdatedIngredientEntity();
                saveModel();
            }
            showDoneButtonLiveData.setValue(isModelValid());
        }
    }

    private boolean isModelValid() {
        return nameValid && descriptionValid;
    }

    private boolean modelHasChanged() {
        if (ingredientEntity != null) {
            IngredientEntity updatedEntity = new IngredientEntity(
                    ingredientEntity.getId(),
                    nameObservable.get(),
                    descriptionObservable.get(),
                    ingredientEntity.getCreatedBy(),
                    ingredientEntity.getCreateDate(),
                    ingredientEntity.getLastUpdate()
            );
            return !ingredientEntity.equals(updatedEntity);
        } else
            return false;
    }

    private IngredientEntity createNewIngredientEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new IngredientEntity(
                idProvider.getUId(),
                "",
                "",
                Constants.getUserId().getValue(),
                currentTime,
                currentTime
        );
    }

    private IngredientEntity getUpdatedIngredientEntity() {
        return new IngredientEntity(
                ingredientEntity.getId(),
                nameObservable.get(),
                descriptionObservable.get(),
                ingredientEntity.getCreatedBy(),
                ingredientEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void saveModel() {
        dataSource.save(ingredientEntity);
    }

    void doneButtonPressed() {

    }
}
