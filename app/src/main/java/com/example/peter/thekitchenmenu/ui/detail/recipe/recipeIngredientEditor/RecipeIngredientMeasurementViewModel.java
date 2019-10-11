package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

public class RecipeIngredientMeasurementViewModel extends ViewModel {

    private static final String TAG = "tkm-IngredientMeasureVM";

    private RepositoryRecipePortions portionsRepository;
    private RepositoryRecipeIngredient recipeIngredientRepository;
    private RepositoryIngredient ingredientRepository;
    private MeasurementSubtype defaultSubtype = MeasurementSubtype.METRIC_MASS;

    private Resources resources;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;

    private static final int MEASUREMENT_ERROR = -1;
    private static final int NO_CONVERSION_FACTOR = 0;

    public final ObservableInt unitOfMeasureSpinnerInt = new ObservableInt(defaultSubtype.asInt());
    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>(defaultSubtype);
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt();
    public final ObservableField<String> measurementOne = new ObservableField<>();
    public final ObservableField<String> measurementTwo = new ObservableField<>();
    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();
    public final ObservableBoolean isConversionFactorEnabled = new ObservableBoolean();
    public final ObservableField<String> conversionFactor = new ObservableField<>();

    private String recipeId;
    private String ingredientId;
    private int numberOfPortions;
    private RecipeIngredientQuantityEntity recipeIngredientQuantityEntity;
    private IngredientEntity ingredientEntity;
    private UnitOfMeasure unitOfMeasure = defaultSubtype.getMeasurementClass();

    public RecipeIngredientMeasurementViewModel(
            RepositoryRecipePortions portionsRepository,
            RepositoryRecipeIngredient recipeIngredientRepository,
            RepositoryIngredient ingredientRepository,
            Resources resources,
            UniqueIdProvider idProvider,
            TimeProvider timeProvider) {

        this.portionsRepository = portionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.resources = resources;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        unitOfMeasureSpinnerInt.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                subTypeUpdated();
            }
        });

        measurementOne.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!measurementOne.get().isEmpty())
                    measurementOneUpdated();
            }
        });

        measurementTwo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!measurementTwo.get().isEmpty())
                    measurementTwoUpdated();
            }
        });

        conversionFactor.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!conversionFactor.get().isEmpty())
                    conversionFactorUpdated();
            }
        });
    }

    public void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        recipeIngredientQuantityEntity = createNewRecipeIngredientQuantityEntity();
        loadIngredient();
    }

    private RecipeIngredientQuantityEntity createNewRecipeIngredientQuantityEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIngredientQuantityEntity(
                idProvider.getUId(),
                recipeId,
                ingredientId,
                "",
                0,
                0,
                Constants.getUserId().getValue(),
                currentTime,
                currentTime
        );
    }

    public void start(String recipeIngredientId) {
        loadExistingRecipeIngredient(recipeIngredientId);
    }

    private void loadExistingRecipeIngredient(String recipeIngredientId) {
        recipeIngredientRepository.getById(
                recipeIngredientId,
                new DataSource.GetEntityCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientQuantityEntity recipeIngredientQuantityEntity) {
                        recipeId = recipeIngredientQuantityEntity.getRecipeId();
                        ingredientId = recipeIngredientQuantityEntity.getIngredientId();
                        RecipeIngredientMeasurementViewModel.this.recipeIngredientQuantityEntity =
                                recipeIngredientQuantityEntity;
                        loadIngredient();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void loadIngredient() {
        ingredientRepository.getById(
                ingredientId,
                new DataSource.GetEntityCallback<IngredientEntity>() {
                    @Override
                    public void onEntityLoaded(IngredientEntity ingredientEntity) {
                        RecipeIngredientMeasurementViewModel.this.ingredientEntity = ingredientEntity;
                        loadPortions();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void loadPortions() {
        portionsRepository.getPortionsForRecipe(
                recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
                        numberOfPortions = portionsEntity.getServings() * portionsEntity.getSittings();
                        setupUnitOfMeasure();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void setupUnitOfMeasure() {
        unitOfMeasure = MeasurementSubtype.fromInt(recipeIngredientQuantityEntity.
                getUnitOfMeasureSubtype()).getMeasurementClass();

        if (ingredientEntity.getConversionFactor() != NO_CONVERSION_FACTOR)
            unitOfMeasure.conversionFactorIsSet(ingredientEntity.getConversionFactor());

        unitOfMeasure.itemBaseUnitsAreSet(recipeIngredientQuantityEntity.getItemBaseUnits());
        unitOfMeasure.numberOfItemsIsSet(numberOfPortions);
        updateUi();
    }

    private void subTypeUpdated() {
        MeasurementSubtype newSubtype = getSubtypeFromSpinnerPosition();
        if (unitOfMeasure.getMeasurementSubtype() != newSubtype) {
            unitOfMeasure = newSubtype.getMeasurementClass();
            if (numberOfPortions > 1)
                unitOfMeasure.numberOfItemsIsSet(numberOfPortions);
            updateUi();
        }
    }

    private void conversionFactorUpdated() {
        double conversionFactorDecimal = parseDecimalFromString(conversionFactor);
        if (conversionFactorDecimal == 0 || conversionFactorDecimal == MEASUREMENT_ERROR) {
            conversionFactor.set("");
            return;
        }
        if (conversionFactorHasChanged(conversionFactorDecimal))
            updateConversionFactor(conversionFactorDecimal);
    }

    private boolean conversionFactorHasChanged(double newConversionFactor) {
        return unitOfMeasure.getConversionFactor() != newConversionFactor;
    }

    private void updateConversionFactor(double conversionFactor) {
        boolean conversionFactorIsSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);
        if (conversionFactorIsSet && conversionFactor != ingredientEntity.getConversionFactor()) {
            saveUpdatedConversionFactorToIngredient(conversionFactor);
            if (recipeIngredientQuantityIsChanged())
                save(updatedRecipeIngredientEntity());
        }
    }

    private void saveUpdatedConversionFactorToIngredient(double conversionFactor) {
        IngredientEntity updatedEntity = new IngredientEntity(
                ingredientEntity.getId(),
                ingredientEntity.getName(),
                ingredientEntity.getDescription(),
                conversionFactor,
                ingredientEntity.getCreatedBy(),
                ingredientEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
        ingredientRepository.save(updatedEntity);
    }

    private MeasurementSubtype getSubtypeFromSpinnerPosition() {
        return MeasurementSubtype.fromInt(unitOfMeasureSpinnerInt.get());
    }

    private void measurementOneUpdated() {
        if (unitsAfterDecimal() > 0)
            processDecimalMeasurement();
        else
            processIntegerMeasurement(measurementOne);
    }

    private void processDecimalMeasurement() {
        double decimalMeasurement = parseDecimalFromString(measurementOne);
        if (decimalMeasurement == 0 || decimalMeasurement == MEASUREMENT_ERROR) {
            measurementOne.set("");
            return;
        }
        if (decimalMeasurement != MEASUREMENT_ERROR)
            if (decimalMeasurementHasChanged(decimalMeasurement))
                updateDecimalMeasurement(decimalMeasurement);
    }

    private boolean decimalMeasurementHasChanged(double newMeasurement) {
        double oldMeasurement = unitOfMeasure.getTotalMeasurementOne();
        return oldMeasurement != newMeasurement;
    }

    private void updateDecimalMeasurement(double decimalMeasurement) {
        boolean measurementIsSet = unitOfMeasure.totalMeasurementOneIsSet(decimalMeasurement);
        if (measurementIsSet)
            updateUi();
        else
            updateUi();
    }

    private double parseDecimalFromString(ObservableField<String> observableField) {
        String rawValue = observableField.get();
        if (rawValue.isEmpty() || rawValue.equals("."))
            return MEASUREMENT_ERROR;
        try {
            return Double.parseDouble(rawValue);
        } catch (NumberFormatException e) {
            if (observableField == measurementOne)
                measurementOneErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void measurementTwoUpdated() {
        processIntegerMeasurement(measurementTwo);
    }

    private void processIntegerMeasurement(ObservableField<String> measurement) {
        int integerMeasurement = parseIntegerFromString(measurement);
        if (integerMeasurement == 0) {
            measurement.set("");
            return;
        }
        if (integerMeasurement != MEASUREMENT_ERROR) {
            if (integerMeasurementHasChanged(measurement, integerMeasurement))
                updateIntegerMeasurement(measurement, integerMeasurement);
        }
    }

    private int parseIntegerFromString(ObservableField<String> measurement) {
        String rawMeasurement = measurement.get();
        if (rawMeasurement.isEmpty())
            measurement.set("");
        try {
            return Integer.parseInt(rawMeasurement);
        } catch (NumberFormatException e) {
            if (measurement == measurementOne)
                measurementOneErrorMessage.set(resources.getString(R.string.number_format_exception));
            else if (measurement == measurementTwo)
                measurementTwoErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private boolean integerMeasurementHasChanged(ObservableField<String> measurement,
                                                 int newMeasurement) {
        int oldMeasurement = 0;
        if (measurement == measurementOne)
            oldMeasurement = (int) unitOfMeasure.getTotalMeasurementOne();
        if (measurement == measurementTwo)
            oldMeasurement = unitOfMeasure.getTotalMeasurementTwo();
        return oldMeasurement != newMeasurement;
    }

    private void updateIntegerMeasurement(ObservableField<String> measurement,
                                          int newMeasurement) {
        boolean measurementIsSet = false;
        if (measurement == measurementOne)
            measurementIsSet = unitOfMeasure.totalMeasurementOneIsSet(newMeasurement);
        if (measurement == measurementTwo)
            measurementIsSet = unitOfMeasure.totalMeasurementTwoIsSet(newMeasurement);
        if (measurementIsSet) {
            updateUi();
        } else {
            updateUi();
        }
    }

    private void updateUi() {
        if (unitOfMeasureSpinnerInt.get() != unitOfMeasure.getMeasurementSubtype().asInt())
            unitOfMeasureSpinnerInt.set(unitOfMeasure.getMeasurementSubtype().asInt());

        if (subtype.get() != unitOfMeasure.getMeasurementSubtype())
            subtype.set(unitOfMeasure.getMeasurementSubtype());

        if (numberOfMeasurementUnits.get() != unitOfMeasure.getNumberOfMeasurementUnits())
            numberOfMeasurementUnits.set(unitOfMeasure.getNumberOfMeasurementUnits());

        if (unitsAfterDecimal() > 0)
            measurementOne.set(String.valueOf(unitOfMeasure.getTotalMeasurementOne()));
        else
            measurementOne.set(String.valueOf((int) unitOfMeasure.getTotalMeasurementOne()));

        if (numberOfMeasurementUnits.get() > 1)
            measurementTwo.set(String.valueOf(unitOfMeasure.getTotalMeasurementTwo()));

        updateConversionFactor();

        if (recipeIngredientQuantityIsChanged() && unitOfMeasure.isValidMeasurement()) {
            save(updatedRecipeIngredientEntity());
        }

    }

    private int unitsAfterDecimal() {
        return (int) unitOfMeasure.getMeasurementUnitsDigitWidths()[0].second;
    }

    private void updateConversionFactor() {
        if (unitOfMeasure.getMeasurementSubtype() == MeasurementSubtype.IMPERIAL_SPOON &&
                ingredientEditorIsIngredientOwner()) {
            isConversionFactorEnabled.set(true);
        } else {
            isConversionFactorEnabled.set(false);
        }
    }

    private boolean ingredientEditorIsIngredientOwner() {
        return Constants.getUserId().getValue().equals(ingredientEntity.getCreatedBy());
    }

    private RecipeIngredientQuantityEntity updatedRecipeIngredientEntity() {
        return new RecipeIngredientQuantityEntity(
                recipeIngredientQuantityEntity.getId(),
                recipeIngredientQuantityEntity.getRecipeId(),
                recipeIngredientQuantityEntity.getIngredientId(),
                recipeIngredientQuantityEntity.getProductId(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getMeasurementSubtype().asInt(),
                recipeIngredientQuantityEntity.getCreatedBy(),
                recipeIngredientQuantityEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private boolean recipeIngredientQuantityIsChanged() {
        if (recipeIngredientQuantityEntity != null) {
            RecipeIngredientQuantityEntity updatedEntity = new RecipeIngredientQuantityEntity(
                    recipeIngredientQuantityEntity.getId(),
                    recipeIngredientQuantityEntity.getRecipeId(),
                    recipeIngredientQuantityEntity.getIngredientId(),
                    recipeIngredientQuantityEntity.getProductId(),
                    unitOfMeasure.getItemBaseUnits(),
                    unitOfMeasure.getMeasurementSubtype().asInt(),
                    Constants.getUserId().getValue(),
                    recipeIngredientQuantityEntity.getCreateDate(),
                    recipeIngredientQuantityEntity.getLastUpdate()
            );
            return !recipeIngredientQuantityEntity.equals(updatedEntity);

        } else
            return false;
    }

    private void save(RecipeIngredientQuantityEntity recipeIngredientQuantityEntity) {
        this.recipeIngredientQuantityEntity = recipeIngredientQuantityEntity;
        recipeIngredientRepository.save(recipeIngredientQuantityEntity);
    }
}
