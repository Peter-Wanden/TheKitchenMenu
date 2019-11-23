package com.example.peter.thekitchenmenu.ui.detail.common;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.ui.utils.NumberFormatter;

public class MeasurementErrorMessageMaker {
    private Resources resources;
    private NumberFormatter numberFormatter;
    private MeasurementModel model;

    public MeasurementErrorMessageMaker(Resources resources, NumberFormatter numberFormatter) {
        this.resources = resources;
        this.numberFormatter = numberFormatter;
    }

    public String getConversionFactorErrorMessage() {
        return resources.getString(
                R.string.conversion_factor_error_message,
                UnitOfMeasureConstants.MIN_CONVERSION_FACTOR,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR);
    }

    public String getMeasurementErrorMessage(MeasurementModel model) {
        this.model = model;
        UnitOfMeasure unitOfMeasure = model.getSubtype().getMeasurementClass();
        return resources.getString(R.string.measurement_input_error,
                getMinUnitOne(),
                resources.getString(unitOfMeasure.getUnitOneLabelResourceId()),
                getMaxUnitTwo(),
                getUnitTwoLabel(unitOfMeasure),
                getMaxUnitOne(),
                getMaxUnitOneLabel(unitOfMeasure)
        );
    }

    private String getMinUnitOne() {
        return String.valueOf(numberFormatter.formatDecimalForDisplay(model.getMinUnitOne()));
    }

    private String getMaxUnitTwo() {
        return String.valueOf(numberFormatter.formatIntegerForDisplay(model.getMaxUnitTwo()));
    }

    private String getUnitTwoLabel(UnitOfMeasure unitOfMeasure) {
        if (model.getSubtype() == MeasurementSubtype.COUNT) {
            return "";
        } else {
            return resources.getString(unitOfMeasure.getUnitTwoLabelResourceId());
        }
    }

    private String getMaxUnitOne() {
        if (model.getMaxUnitOne() > 0) {
            return String.valueOf(numberFormatter.formatDecimalForDisplay(model.getMaxUnitOne()));
        } else {
            return "";
        }
    }

    private String getMaxUnitOneLabel(UnitOfMeasure unitOfMeasure) {
        if (getMaxUnitOne().isEmpty()) {
            return "";
        } else {
            return resources.getString(unitOfMeasure.getUnitOneLabelResourceId());
        }
    }
}
