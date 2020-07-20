package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryIngredient;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientEditorActivity;

import javax.annotation.Nonnull;

public class IngredientEditorActivity
        extends AppCompatActivity
        implements AddEditIngredientNavigator {

    public static final int REQUEST_ADD_INGREDIENT = 1;
    public static final int RESULT_ADD_INGREDIENT_OK = 2;
    private static final String EXTRA_INGREDIENT_ID = "EXTRA_INGREDIENT_ID";
//    private IngredientEditorBinding binding;
    private IngredientEditorViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        setUpActionBar();
        setupViewModels();
        setViewModelObservers();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().hasExtra(EXTRA_INGREDIENT_ID)) {
            viewModel.start(getIntent().getStringExtra(EXTRA_INGREDIENT_ID));
        } else {
            viewModel.start();
        }
    }

    @Override
    public void setActivityTitle(int titleResourceId) {

    }

    private void initialiseBindings() {
//        binding = DataBindingUtil.setContentView(this, R.layout.ingredient_editor);
//        binding.setLifecycleOwner(this);
    }

    private void setupToolbar() {
//        setSupportActionBar(binding.ingredientEditorToolbar);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra(EXTRA_INGREDIENT_ID)) {
            actionBar.setTitle(R.string.activity_title_edit_ingredient);
        }
        else {
            actionBar.setTitle(R.string.activity_title_add_new_ingredient);
        }
    }

    private void setupViewModels() {
        viewModel = obtainIdentityEditorViewModel(this);
        viewModel.setNavigator(this);
//        binding.setViewModel(viewModel);
    }

    private static IngredientEditorViewModel obtainIdentityEditorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryIngredient factoryIngredient =
                ViewModelFactoryIngredient.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factoryIngredient).get(
                IngredientEditorViewModel.class);
    }

    private void setViewModelObservers() {
        viewModel.showUseButton.observe(this,
                isShowButton -> invalidateOptionsMenu());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ingredient_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_ingredient_editor_action_done).setVisible(
                viewModel.showUseButton.getValue());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item) {
        if (item.getItemId() == R.id.menu_ingredient_editor_action_done) {
            viewModel.useButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishActivity(String ingredientId) {
        Intent intentThatLaunchedActivity = getIntent();
        Intent intent = new Intent(this, RecipeIngredientEditorActivity.class);

        if (intentThatLaunchedActivity.hasExtra(RecipeIngredientEditorActivity.EXTRA_RECIPE_ID)) {
            intent.putExtra(RecipeIngredientEditorActivity.EXTRA_RECIPE_ID,
                    intentThatLaunchedActivity.getStringExtra(
                            RecipeIngredientEditorActivity.EXTRA_RECIPE_ID));
        }

        intent.putExtra(RecipeIngredientEditorActivity.EXTRA_INGREDIENT_ID, ingredientId);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        viewModel.onActivityDestroyed();
        super.onDestroy();
    }
}
