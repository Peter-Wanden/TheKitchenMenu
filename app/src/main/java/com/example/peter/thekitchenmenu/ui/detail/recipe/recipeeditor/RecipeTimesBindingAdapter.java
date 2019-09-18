package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;

public class RecipeTimesBindingAdapter {

    @BindingAdapter(value = {"setupEditTextForRecipeTimes"})
    public static void setupEditTextForRecipeTimesInput(EditText editText, String time) {
        int viewId = editText.getId();
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (viewId == R.id.editable_recipe_duration_prep_time_hours ||
                viewId == R.id.editable_recipe_duration_cook_time_hours) {
            editText.setFilters(new InputFilter[]{
                    new DecimalDigitsInputFilter(3, 0)});
            returnBlankIfZero(editText);

        } else if (viewId == R.id.editable_recipe_duration_prep_time_minutes ||
                viewId == R.id.editable_recipe_duration_cook_time_minutes) {
            editText.setFilters(new InputFilter[]{
                    new DecimalDigitsInputFilter(2, 0)});
            returnBlankIfZero(editText);
        }
    }

    private static void returnBlankIfZero(EditText editText) {
        if (editText.getText().toString().equals("0"))
            editText.setText("");
    }
}
