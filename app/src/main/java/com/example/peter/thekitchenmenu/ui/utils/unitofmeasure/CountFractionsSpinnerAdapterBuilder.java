package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import android.widget.SpinnerAdapter;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;

public class CountFractionsSpinnerAdapterBuilder {

    private FragmentActivity activity;
    private ArrayList<CountFraction> items = new ArrayList<>();

    public static CountFractionsSpinnerAdapterBuilder setActivity(FragmentActivity activity) {
        return new CountFractionsSpinnerAdapterBuilder().
                addActivity(activity).
                addDefaultValue();
    }

    private CountFractionsSpinnerAdapterBuilder addActivity(FragmentActivity activity) {
        this.activity = activity;
        return this;
    }

    private CountFractionsSpinnerAdapterBuilder addDefaultValue() {
        items.add(CountFraction.DEFAULT);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addOneTenth() {
        items.add(CountFraction.ONE_TENTH);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addOneFifth() {
        items.add(CountFraction.ONE_FIFTH);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addThreeTenths() {
        items.add(CountFraction.THREE_TENTHS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addTwoFifths() {
        items.add(CountFraction.TWO_FIFTHS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addHalf() {
        items.add(CountFraction.HALF);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addThreeFifths() {
        items.add(CountFraction.THREE_FIFTHS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addSevenTenths() {
        items.add(CountFraction.SEVEN_TENTHS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addFourFifths() {
        items.add(CountFraction.FOUR_FIFTHS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addNineTenths() {
        items.add(CountFraction.NINE_TENTHS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addOneQuarter() {
        items.add(CountFraction.ONE_QUARTER);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addThreeQuarters() {
        items.add(CountFraction.THREE_QUARTERS);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addAll() {
        items.addAll(EnumSet.allOf(CountFraction.class));
        items.sort(Comparator.comparingInt(CountFraction::toInt));
        return this;
    }

    public SpinnerAdapter build() {
        items.sort(Comparator.comparingDouble(CountFraction::getDecimalValue));
        return new CountFractionsAdapter(activity, items);
    }
}
