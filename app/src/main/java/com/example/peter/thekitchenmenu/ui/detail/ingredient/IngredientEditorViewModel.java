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
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

public class IngredientEditorViewModel
        extends ViewModel
        implements
        DataSource.GetEntityCallback<IngredientEntity>,
        IngredientDuplicateChecker.DuplicateCallback {

    private static final String TAG = "tkm-" + IngredientEditorViewModel.class.getSimpleName() + ": ";

    private Resources resources;
    private RepositoryIngredient ingredientRepo;
    private UseCaseHandler handler;
    private UseCaseTextValidator textValidator;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;
    private AddEditIngredientNavigator navigator;
    private IngredientDuplicateChecker duplicateChecker;

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
                                     RepositoryIngredient ingredientRepository,
                                     UseCaseHandler handler,
                                     UseCaseTextValidator textValidator,
                                     UniqueIdProvider idProvider,
                                     TimeProvider timeProvider,
                                     IngredientDuplicateChecker duplicateChecker) {
        this.resources = resources;
        this.ingredientRepo = ingredientRepository;
        this.handler = handler;
        this.textValidator = textValidator;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;
        this.duplicateChecker = duplicateChecker;

        nameObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!nameObservable.get().isEmpty() || nameObservable.get() != null) {
                    nameUpdated();
                }
            }
        });
        descriptionObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!descriptionObservable.get().isEmpty() || descriptionObservable.get() != null) {
                    descriptionUpdated();
                }
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
        if (ingredientEntity == null) {
            ingredientEntity = createNewIngredientEntity();
        }
        navigator.setActivityTitle(R.string.activity_title_add_new_ingredient);
        updateObservables();
    }

    void start(String ingredientId) {
        if (isNewInstantiationOrIngredientIdChanged(ingredientId)) {
            loadData(ingredientId);
        } else {
            updateObservables();
        }
    }

    private boolean isNewInstantiationOrIngredientIdChanged(String ingredientId) {
        return ingredientEntity == null || !ingredientEntity.getId().equals(ingredientId);
    }

    private void loadData(String ingredientId) {
        ingredientRepo.getById(ingredientId, this);
    }

    @Override
    public void onEntityLoaded(IngredientEntity ingredientEntity) {
        if (isEditorCreator(ingredientEntity.getCreatedBy())) {
            navigator.setActivityTitle(R.string.activity_title_edit_ingredient);
            this.ingredientEntity = ingredientEntity;
            updateObservables();
        } else
            navigator.finishActivity(null);
    }

    private boolean isEditorCreator(String createdBy) {
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

        UseCaseTextValidator.Request request = getTextValidatorRequest(
                UseCaseTextValidator.RequestType.SHORT_TEXT,
                nameObservable.get()
        );
        handler.execute(textValidator, request, getShortValidatorCallback());
    }

    private UseCaseTextValidator.Callback<UseCaseTextValidator.Response> getShortValidatorCallback() {
        return new UseCaseTextValidator.Callback<UseCaseTextValidator.Response>() {

            @Override
            public void onSuccess(UseCaseTextValidator.Response response) {
                processShortTextValidationResponse(response);
            }

            @Override
            public void onError(UseCaseTextValidator.Response response) {
                processShortTextValidationResponse(response);
            }
        };
    }

    private void processShortTextValidationResponse(UseCaseTextValidator.Response response) {
        if (response.getResult() == UseCaseTextValidator.Result.VALID) {
            nameValid = true;
        } else {
            nameValid = false;
            setError(nameErrorMessageObservable, response);
        }

        if (!observablesUpdating && nameHasChanged()) {
            duplicateChecker.checkForDuplicatesAndNotify(
                    nameObservable.get(),
                    ingredientEntity.getId(),
                    this);
        }
        updateUseButtonVisibility();
    }

    private boolean nameHasChanged() {
        return !ingredientEntity.getName().trim().equals(nameObservable.get().trim());
    }

    @Override
    public void duplicateCheckResult(String duplicateId) {
        if (duplicateId.equals(IngredientDuplicateChecker.NO_DUPLICATE_FOUND)) {
            updateUseButtonVisibility();
        } else {
            nameErrorMessageObservable.set(
                    resources.getString(R.string.ingredient_name_duplicate_error_message));
            showUseButtonLiveData.setValue(false);
        }
    }

    private void descriptionUpdated() {
        descriptionErrorMessageObservable.set(null);

        UseCaseTextValidator.Request request = getTextValidatorRequest(
                UseCaseTextValidator.RequestType.LONG_TEXT,
                descriptionObservable.get());

        handler.execute(textValidator, request, getLongTextValidatorCallback());
    }

    private UseCaseTextValidator.Request getTextValidatorRequest(
            UseCaseTextValidator.RequestType type, String textToValidate) {
        return new UseCaseTextValidator.Request(
                type,
                new UseCaseTextValidator.Model(textToValidate));
    }

    private UseCaseTextValidator.Callback<UseCaseTextValidator.Response> getLongTextValidatorCallback() {
        return new UseCaseTextValidator.Callback<UseCaseTextValidator.Response>() {

            @Override
            public void onSuccess(UseCaseTextValidator.Response response) {
                processLongTextValidationResponse(response);
            }

            @Override
            public void onError(UseCaseTextValidator.Response response) {
                processLongTextValidationResponse(response);
            }
        };
    }

    private void processLongTextValidationResponse(UseCaseTextValidator.Response response) {
        if (response.getResult() == UseCaseTextValidator.Result.VALID) {
            descriptionValid = true;
        } else {
            descriptionValid = false;
            setError(descriptionErrorMessageObservable, response);
        }
        updateUseButtonVisibility();

    }

    private void setError(ObservableField<String> errorObservable,
                          UseCaseTextValidator.Response response) {

        if (response.getResult() == UseCaseTextValidator.Result.TOO_SHORT) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_short,
                    response.getMinLength(),
                    response.getMaxLength()));

        } else if (response.getResult() == UseCaseTextValidator.Result.TOO_LONG) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_long,
                    response.getMinLength(),
                    response.getMaxLength()
            ));
        }
    }

    private void updateUseButtonVisibility() {
        if (!observablesUpdating) {
            if (isValid() && isChanged()) {
                showUseButtonLiveData.setValue(true);
            } else
                showUseButtonLiveData.setValue(false);
        }
    }

    private boolean isValid() {
        return nameValid && descriptionValid;
    }

    private boolean isChanged() {
        if (ingredientEntity != null) {
            IngredientEntity updatedEntity = new IngredientEntity(
                    ingredientEntity.getId(),
                    nameObservable.get().trim(),
                    descriptionObservable.get().trim(),
                    ingredientEntity.getConversionFactor(),
                    ingredientEntity.getCreatedBy(),
                    ingredientEntity.getCreateDate(),
                    ingredientEntity.getLastUpdate()
            );
            return !ingredientEntity.equals(updatedEntity);
        } else
            return false;
    }

    private IngredientEntity createNewIngredientEntity() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        String id = idProvider.getUId();
        return new IngredientEntity(
                id,
                "",
                "",
                UnitOfMeasureConstants.NO_CONVERSION_FACTOR,
                Constants.getUserId().getValue(),
                currentTime,
                currentTime
        );
    }

    private void saveModel() {
        ingredientRepo.save(ingredientEntity);
    }

    void useButtonPressed() {
        if (isValid() && isChanged()) {
            ingredientEntity = getUpdatedIngredientEntity();
            saveModel();
        }
        navigator.finishActivity(ingredientEntity.getId());
    }

    private IngredientEntity getUpdatedIngredientEntity() {
        return new IngredientEntity(
                ingredientEntity.getId(),
                nameObservable.get(),
                descriptionObservable.get(),
                ingredientEntity.getConversionFactor(),
                ingredientEntity.getCreatedBy(),
                ingredientEntity.getCreateDate(),
                timeProvider.getCurrentTimeInMills()
        );
    }
}
