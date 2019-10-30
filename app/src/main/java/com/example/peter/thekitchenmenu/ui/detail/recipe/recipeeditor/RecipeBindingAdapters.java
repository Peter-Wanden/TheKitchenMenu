package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;

public class RecipeBindingAdapters {

    @BindingAdapter(value = {"setupEditTextForRecipeTimes"})
    public static void setupEditTextForRecipeDurationInput(EditText editText, String time) {
        int viewId = editText.getId();
        int maxValue = 0;
        Resources resources = editText.getResources();

        if (viewId == R.id.editable_recipe_duration_prep_time_hours)
            maxValue = resources.getInteger(R.integer.recipe_max_prep_time_in_minutes) / 60;

        else if (viewId == R.id.editable_recipe_duration_cook_time_hours)
            maxValue = resources.getInteger(R.integer.recipe_max_cook_time_in_minutes) / 60;

        else if (viewId == R.id.editable_recipe_duration_prep_time_minutes ||
                viewId == R.id.editable_recipe_duration_cook_time_minutes)
            maxValue = 60;

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (viewId == R.id.editable_recipe_duration_prep_time_hours ||
                viewId == R.id.editable_recipe_duration_cook_time_hours) {
            editText.setFilters(new InputFilter[]{
                    new DecimalDigitsInputFilter(getMaxDigitsWidth(maxValue), 0)});
            returnBlankIfZero(editText);

        } else if (viewId == R.id.editable_recipe_duration_prep_time_minutes ||
                viewId == R.id.editable_recipe_duration_cook_time_minutes) {
            editText.setFilters(new InputFilter[]{
                    new DecimalDigitsInputFilter(getMaxDigitsWidth(maxValue), 0)});
            returnBlankIfZero(editText);
        }
    }

    @BindingAdapter(value = {"setUpEditTextForRecipePortions"})
    public static void setupEditTextForRecipePortions(EditText editText, String portion) {
        int viewId = editText.getId();
        int maxValue = 0;
        Resources resources = editText.getResources();

        if (viewId == R.id.editable_servings) {
            maxValue = resources.getInteger(R.integer.recipe_max_servings);

        } else if (viewId == R.id.editable_sittings) {
            maxValue = resources.getInteger(R.integer.recipe_max_sittings);
        }

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{
                new DecimalDigitsInputFilter(getMaxDigitsWidth(maxValue), 0)});
        returnBlankIfZero(editText);
    }

    private static void returnBlankIfZero(EditText editText) {
        if (editText.getText().toString().equals("0"))
            editText.setText("");
    }

    private static int getMaxDigitsWidth(int maxValue) {
        int digits = 0;
        while (maxValue > 0) {
            digits ++;
            maxValue = maxValue / 10;
        }
        return digits;
    }
}
