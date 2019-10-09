package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.utils.TextValidationHandler.VALIDATED;

public class IngredientEditorViewModel
        extends ViewModel
        implements
        DataSource.GetEntityCallback<IngredientEntity>,
        IngredientDuplicateChecker.DuplicateCallback {

    private Resources resources;
    private DataSource<IngredientEntity> dataSource;
    private TextValidationHandler textValidationHandler;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;
    private AddEditIngredientNavigator navigator;
    private IngredientDuplicateChecker duplicateChecker;

    private final SingleLiveEvent<Integer> setActivityTitleEvent = new SingleLiveEvent<>();

    public final ObservableField<String> nameObservable = new ObservableField<>();
    public final ObservableField<String> descriptionObservable = new ObservableField<>();
    public final ObservableField<String> nameErrorMessageObservable = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessageObservable = new ObservableField<>();

    final MutableLiveData<Boolean> showUseButtonLiveData = new MutableLiveData<>(false);

    private IngredientEntity ingredientEntity;

    private boolean observablesUpdating;
    private boolean nameValid;
    private boolean descriptionValid = true;

    public IngredientEditorViewModel(Resources resources,
                                     DataSource<IngredientEntity> dataSource,
                                     TextValidationHandler textValidationHandler,
                                     UniqueIdProvider idProvider,
                                     TimeProvider timeProvider,
                                     IngredientDuplicateChecker duplicateChecker) {
        this.resources = resources;
        this.dataSource = dataSource;
        this.textValidationHandler = textValidationHandler;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.duplicateChecker = duplicateChecker;

        nameObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!nameObservable.get().isEmpty())
                    nameUpdated();
            }
        });
        descriptionObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!descriptionObservable.get().isEmpty())
                    descriptionUpdated();
            }
        });
    }

    void setNavigator(AddEditIngredientNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start() {
        setActivityTitleEvent.setValue(R.string.activity_title_add_new_ingredient);
        ingredientEntity = createNewIngredientEntity();
        updateObservables();
    }

    void start(String ingredientId) {
        dataSource.getById(ingredientId, this);
    }

    SingleLiveEvent<Integer> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
    }

    @Override
    public void onEntityLoaded(IngredientEntity ingredientEntity) {
        if (editorIsCreator(ingredientEntity.getCreatedBy())) {
            setActivityTitleEvent.setValue(R.string.activity_title_edit_ingredient);
            this.ingredientEntity = ingredientEntity;
            updateObservables();
        } else
            navigator.finishActivity(null);
    }

    private boolean editorIsCreator(String createdBy) {
        return Constants.getUserId().getValue().equals(createdBy);
    }

    @Override
    public void onDataNotAvailable() {
        start();
    }

    private void updateObservables() {
        observablesUpdating = true;
        nameObservable.set(ingredientEntity.getName());
        descriptionObservable.set(ingredientEntity.getDescription());
        observablesUpdating = false;
        updateUseButtonVisibility();
    }

    private void nameUpdated() {
        nameErrorMessageObservable.set(null);
        String validationResponse = validateShortText(nameObservable.get());

        nameValid = validationResponse.equals(VALIDATED);

        if (!nameValid)
            nameErrorMessageObservable.set(validationResponse);

        else if (!observablesUpdating && nameHasChanged())
            duplicateChecker.checkForDuplicatesAndNotify(
                    nameObservable.get(),
                    ingredientEntity.getId(),
                    this);
        updateUseButtonVisibility();
    }

    private boolean nameHasChanged() {
        return !ingredientEntity.getName().trim().equals(nameObservable.get().trim());
    }

    @Override
    public void duplicateCheckResult(String duplicateId) {
        if (duplicateId.equals(IngredientDuplicateChecker.NO_DUPLICATE_FOUND))
            updateUseButtonVisibility();
        else {
            nameErrorMessageObservable.set(
                    resources.getString(R.string.ingredient_name_duplicate_error_message));
            showUseButtonLiveData.setValue(false);
        }
    }

    private void descriptionUpdated() {
        descriptionErrorMessageObservable.set(null);
        String validationResponse = validateLongText(descriptionObservable.get());

        descriptionValid = validationResponse.equals(VALIDATED);

        if (!descriptionValid)
            descriptionErrorMessageObservable.set(validationResponse);

        updateUseButtonVisibility();
    }

    private String validateShortText(String textToValidate) {
        return textValidationHandler.validateShortText(resources, textToValidate);
    }

    private String validateLongText(String textToValidate) {
        return textValidationHandler.validateLongText(resources, textToValidate);
    }

    private void updateUseButtonVisibility() {
        if (!observablesUpdating) {
            if (isModelValid() && modelHasChanged()) {
                showUseButtonLiveData.setValue(true);
            } else
                showUseButtonLiveData.setValue(false);
        }
    }

    private boolean isModelValid() {
        return nameValid && descriptionValid;
    }

    private boolean modelHasChanged() {
        if (ingredientEntity != null) {
            IngredientEntity updatedEntity = new IngredientEntity(
                    ingredientEntity.getId(),
                    nameObservable.get().trim(),
                    descriptionObservable.get().trim(),
                    0,
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
        String id = idProvider.getUId();
        return new IngredientEntity(
                id,
                "",
                "",
                0,
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
                0,
                ingredientEntity.getCreatedBy(),
                ingredientEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private void saveModel() {
        dataSource.save(ingredientEntity);
    }

    void useButtonPressed() {
        if (isModelValid() && modelHasChanged()) {
            ingredientEntity = getUpdatedIngredientEntity();
            saveModel();
        }
        navigator.finishActivity(ingredientEntity.getId());
    }
}
