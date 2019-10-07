package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

public class RecipeIngredientMeasurementViewModel extends ViewModel {

    private RepositoryRecipePortions repositoryRecipePortions;
    private RepositoryRecipeIngredient repositoryRecipeIngredient;

    private Resources resources;
    private UniqueIdProvider idProvider;
    private TimeProvider timeProvider;

    private static final int MEASUREMENT_ERROR = -1;

    public final ObservableInt unitOfMeasureSpinnerInt = new ObservableInt();
    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>();
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt();
    public final ObservableField<String> measurementOne = new ObservableField<>();
    public final ObservableField<String> measurementTwo = new ObservableField<>();
    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();

    private String recipeId;
    private String ingredientId;
    private int numberOfPortions;
    private RecipeIngredientEntity recipeIngredientEntity;
    private UnitOfMeasure unitOfMeasure;

    public RecipeIngredientMeasurementViewModel(
            RepositoryRecipePortions repositoryRecipePortions,
            RepositoryRecipeIngredient repositoryRecipeIngredient,
            Resources resources,
            UniqueIdProvider idProvider,
            TimeProvider timeProvider) {

        this.repositoryRecipePortions = repositoryRecipePortions;
        this.repositoryRecipeIngredient = repositoryRecipeIngredient;
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
    }

    public void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        recipeIngredientEntity = createNewRecipeIngredientEntity();
        updateUnitOfMeasureFromRecipeIngredient();
        updateUi();
    }

    public void start(String recipeIngredientId) {
        loadExistingRecipeIngredient(recipeIngredientId);
    }

    private void updateUnitOfMeasureFromRecipeIngredient() {
        // Always add measurement in this order: 1.subType 2.baseUnits 3.numberOfItems/portions
        unitOfMeasure = MeasurementSubtype.fromInt(
                recipeIngredientEntity.getUnitOfMeasureSubtype()).getMeasurementClass();
        unitOfMeasure.itemBaseUnitsAreSet(recipeIngredientEntity.getItemBaseUnits());
        getPortions();
    }

    private void loadExistingRecipeIngredient(String recipeIngredientId) {
        repositoryRecipeIngredient.getById(
                recipeIngredientId,
                new DataSource.GetEntityCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientEntity recipeIngredientEntity) {
                        recipeId = recipeIngredientEntity.getRecipeId();
                        ingredientId = recipeIngredientEntity.getIngredientId();
                        RecipeIngredientMeasurementViewModel.this.recipeIngredientEntity =
                                recipeIngredientEntity;
                        getPortions();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void getPortions() {
        repositoryRecipePortions.getPortionsForRecipe(
                recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
                        int portions = portionsEntity.getServings() * portionsEntity.getSittings();
                        numberOfPortions = portions;
                        unitOfMeasure = MeasurementSubtype.fromInt(
                                recipeIngredientEntity.getUnitOfMeasureSubtype()).getMeasurementClass();
                        unitOfMeasure.numberOfItemsIsSet(portions);
                        unitOfMeasure.itemBaseUnitsAreSet(recipeIngredientEntity.getItemBaseUnits());
                        updateUi();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void subTypeUpdated() {
        MeasurementSubtype newSubtype = getSubtypeFromSpinnerPosition();
        if (unitOfMeasure == null || unitOfMeasure.getMeasurementSubtype() != newSubtype) {
            unitOfMeasure = newSubtype.getMeasurementClass();
            if (numberOfPortions > 1)
                unitOfMeasure.numberOfItemsIsSet(numberOfPortions);
            updateUi();
        }
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
        double decimalMeasurement = parseDecimalFromString();
        if (decimalMeasurement == 0) {
            measurementOne.set("");
            return;
        }
        if (decimalMeasurement != MEASUREMENT_ERROR)
            if (decimalMeasurementHasChanged(decimalMeasurement))
                updateDecimalMeasurement(decimalMeasurement);
    }

    private void updateDecimalMeasurement(double decimalMeasurement) {
        boolean measurementIsSet = unitOfMeasure.totalMeasurementOneIsSet(decimalMeasurement);
        if (measurementIsSet)
            updateUi();
        else
            updateUi();
    }

    private boolean decimalMeasurementHasChanged(double newMeasurement) {
        double oldMeasurement = unitOfMeasure.getTotalMeasurementOne();
        return oldMeasurement != newMeasurement;
    }

    private double parseDecimalFromString() {
        String rawMeasurement = measurementOne.get();
        if (rawMeasurement.isEmpty() || rawMeasurement.equals("."))
            measurementOne.set("");
        try {
            return Double.parseDouble(rawMeasurement);
        } catch (NumberFormatException e) {
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

    private boolean integerMeasurementHasChanged(ObservableField<String> measurement, int newMeasurement) {
        int oldMeasurement = 0;
        if (measurement == measurementOne)
            oldMeasurement = (int) unitOfMeasure.getTotalMeasurementOne();
        if (measurement == measurementTwo)
            oldMeasurement = unitOfMeasure.getTotalMeasurementTwo();
        return oldMeasurement != newMeasurement;
    }

    private void updateIntegerMeasurement(ObservableField<String> measurement, int newMeasurement) {
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

        if (isChanged()) {
            save(updatedRecipeIngredientEntity());
        }
    }

    private int unitsAfterDecimal() {
        return (int) unitOfMeasure.getMeasurementUnitsDigitWidths()[0].second;
    }

    private RecipeIngredientEntity updatedRecipeIngredientEntity() {
        return new RecipeIngredientEntity(
                recipeIngredientEntity.getId(),
                recipeIngredientEntity.getRecipeId(),
                recipeIngredientEntity.getIngredientId(),
                recipeIngredientEntity.getProductId(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getMeasurementSubtype().asInt(),
                recipeIngredientEntity.getCreatedBy(),
                recipeIngredientEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private RecipeIngredientEntity createNewRecipeIngredientEntity() {
        long currentTime = timeProvider.getCurrentTimestamp();
        return new RecipeIngredientEntity(
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

    private boolean isChanged() {
        if (recipeIngredientEntity != null) {
            RecipeIngredientEntity updatedEntity = new RecipeIngredientEntity(
                    recipeIngredientEntity.getId(),
                    recipeIngredientEntity.getRecipeId(),
                    recipeIngredientEntity.getIngredientId(),
                    recipeIngredientEntity.getProductId(),
                    unitOfMeasure.getItemBaseUnits(),
                    unitOfMeasure.getMeasurementSubtype().asInt(),
                    Constants.getUserId().getValue(),
                    recipeIngredientEntity.getCreateDate(),
                    recipeIngredientEntity.getLastUpdate()
            );
            return !recipeIngredientEntity.equals(updatedEntity);

        } else
            return false;
    }

    private void save(RecipeIngredientEntity recipeIngredientEntity) {
        repositoryRecipeIngredient.save(recipeIngredientEntity);
        this.recipeIngredientEntity = recipeIngredientEntity;
    }
}
