package com.example.peter.thekitchenmenu.ui.utils;

import android.content.res.Resources;
import android.widget.SpinnerAdapter;

import androidx.fragment.app.FragmentActivity;

import com.example.peter.thekitchenmenu.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class CountFractionsSpinnerAdapterBuilder {

    private FragmentActivity activity;
    private Resources resources;
    private ArrayList<CountFractionsItemModel> items = new ArrayList<>();
    private List<String> countFractionsAsHtmlStrings;

    public static CountFractionsSpinnerAdapterBuilder setActivity(FragmentActivity activity) {
        return new CountFractionsSpinnerAdapterBuilder().
                addActivity(activity).
                setResources(activity.getResources()).
                setStringArray(activity.getResources());
    }

    private CountFractionsSpinnerAdapterBuilder addActivity(FragmentActivity activity) {
        this.activity = activity;
        return this;
    }

    private CountFractionsSpinnerAdapterBuilder setResources(Resources resources) {
        this.resources = resources;
        return this;
    }

    private CountFractionsSpinnerAdapterBuilder setStringArray(Resources resources) {
        countFractionsAsHtmlStrings = Arrays.asList(resources.getStringArray(
                R.array.count_fractions));
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addOneTenth() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.one_tenth)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addOneFifth() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.one_fifth)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addThreeTenths() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.three_tenths)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addTwoFifths() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.two_fifths)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addHalf() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.half)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addThreeFifths() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.three_fifths)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addSevenTenths() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.seven_tenths)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addFourFifths() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.four_fifths)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addNineTenths() {
        CountFractionsItemModel model = new CountFractionsItemModel(
                resources.getString(R.string.nine_tenths)
        );
        items.add(model);
        return this;
    }

    public CountFractionsSpinnerAdapterBuilder addAll() {
        EnumSet.allOf(CountFraction.class).forEach(CountFraction -> {
            CountFractionsItemModel model = new CountFractionsItemModel(
                    countFractionsAsHtmlStrings.get(CountFraction.asInt())
            );
            items.add(model);
        });
        return this;
    }

    public SpinnerAdapter build() {
        return new CountFractionsAdapter(activity, items);
    }


}
