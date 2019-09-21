package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.IngredientEditorBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryIngredient;

public class IngredientEditorActivity
        extends AppCompatActivity
        implements AddEditIngredientNavigator {

    private static final String EXTRA_INGREDIENT_ID = "EXTRA_INGREDIENT_ID";
    private IngredientEditorBinding binding;
    private IngredientViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        setUpActionBar();
        setupViewModels();
        setViewModelObservers();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.ingredient_editor);
        binding.setLifecycleOwner(this);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.ingredientEditorToolbar);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra(EXTRA_INGREDIENT_ID))
            actionBar.setTitle(R.string.activity_title_edit_ingredient);
        else
            actionBar.setTitle(R.string.activity_title_add_new_ingredient);
    }

    private void setupViewModels() {
        viewModel = obtainIdentityEditorViewModel(this);
        viewModel.setNavigator(this);
    }

    private static IngredientViewModel obtainIdentityEditorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryIngredient factoryIngredient =
                ViewModelFactoryIngredient.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factoryIngredient).get(
                IngredientViewModel.class);
    }

    private void setViewModelObservers() {
        viewModel.showDoneButtonLiveData.observe(this,
                isShowButton -> invalidateOptionsMenu());
        viewModel.getSetActivityTitleEvent().observe(this, this::setTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ingredient_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_ingredient_editor_action_done).setVisible(
                viewModel.showDoneButtonLiveData.getValue());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_ingredient_editor_action_done) {
            viewModel.doneButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishActivity() {

    }

    @Override
    protected void onDestroy() {
        viewModel.onActivityDestroyed();
        super.onDestroy();
    }
}
