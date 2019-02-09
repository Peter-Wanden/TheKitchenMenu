package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public abstract class UnitOfMeasureClassSelector {

    public static UnitOfMeasure getUnitOfMeasureClass(Context appContext, int unitOfMeasure) {

        switch (unitOfMeasure) {
            case UNIT_GRAMS:
                return new Grams(appContext);

            case UNIT_KILOGRAMS:
                return new KiloGrams(appContext);

            case UNIT_OUNCES:
                return new Ounces(appContext);

            default:
                // TODO - default cannot return null
                return new Grams(appContext);
        }
    }
}
