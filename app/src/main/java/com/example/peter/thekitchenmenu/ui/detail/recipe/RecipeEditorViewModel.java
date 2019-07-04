package com.example.peter.thekitchenmenu.ui.detail.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.data.repository.RecipeDataSource;
import com.example.peter.thekitchenmenu.data.repository.RecipeRepository;

public class RecipeEditorViewModel
        extends AndroidViewModel
        implements RecipeDataSource.GetItemCallback{

    private static final String TAG = "tkm-RecipeEditorVM";

    private RecipeRepository recipeRepository;
    private RecipeEntity recipeEntity;
    private RecipeEntity tempRecipeEntity;
    MutableLiveData<ImageModel> imageModel = new MutableLiveData<>();
    MutableLiveData <RecipeIdentityModel> recipeIdentityModel = new MutableLiveData<>();

    private final ObservableBoolean dataLoading = new ObservableBoolean();

    @Nullable
    private String recipeId;
    private boolean isNewRecipe;
    private boolean dataHasLoaded = false;

    public RecipeEditorViewModel(@NonNull Application application,
                                 RecipeRepository recipeRepository) {
        super(application);
        this.recipeRepository = recipeRepository;

        RecipeEntity recipeEntity = RecipeEntity.createRecipe(
                "This is the title of the recipe, it can only be 70 chars long",
                "This is the recipes description. It can be up to 2,000, or is it 20,000 characters long. Either way its probably too long. This recipe has a prep time of 250 minutes and a cook time of 160 minutes.",
                250,
                160,
                "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwia24zZr5vjAhVSXhoKHRMNCrQQjRx6BAgBEAU&url=http%3A%2F%2Fwww.desideria.com.au%2Fproduct%2Fcombo-5%2F&psig=AOvVaw13HceFP4oWt8uUdAj09MiF&ust=1562333425506313",
                "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwia24zZr5vjAhVSXhoKHRMNCrQQjRx6BAgBEAU&url=http%3A%2F%2Fwww.desideria.com.au%2Fproduct%2Fcombo-5%2F&psig=AOvVaw13HceFP4oWt8uUdAj09MiF&ust=1562333425506313",
                "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwia24zZr5vjAhVSXhoKHRMNCrQQjRx6BAgBEAU&url=http%3A%2F%2Fwww.desideria.com.au%2Fproduct%2Fcombo-5%2F&psig=AOvVaw13HceFP4oWt8uUdAj09MiF&ust=1562333425506313",
                "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwia24zZr5vjAhVSXhoKHRMNCrQQjRx6BAgBEAU&url=http%3A%2F%2Fwww.desideria.com.au%2Fproduct%2Fcombo-5%2F&psig=AOvVaw13HceFP4oWt8uUdAj09MiF&ust=1562333425506313"
                );
        tempRecipeEntity = recipeEntity;
    }

    void start(String recipeId) {
        if (dataLoading.get()) {
            // Already loading, ignore.
            return;
        }
        this.recipeId = recipeId;
        if (this.recipeId == null) {
            // No need to populate, it's a new recipe
            isNewRecipe = true;
            onItemLoaded(tempRecipeEntity);
            return;
        }
        if (dataHasLoaded) {
            // No need to populate, already have data.
            return;
        }
        isNewRecipe = false;
        dataLoading.set(true);
        recipeRepository.getById(recipeId, this);
    }

    @Override
    public void onItemLoaded(RecipeEntity recipeEntity) {
        this.recipeEntity = recipeEntity;
        dataLoading.set(false);
        dataHasLoaded = true;
        setImageModel();
        setRecipeIdentityModel();
    }

    private void setImageModel() {
        imageModel.setValue(new ImageModel(
                recipeEntity.getRemoteLargeImageUri(),
                null,
                recipeEntity.getRemoteMediumImageUri(),
                null,
                recipeEntity.getRemoteSmallImageUri(),
                null,
                recipeEntity.getWebImageUrl()
                ));
    }

    private void setRecipeIdentityModel() {
        recipeIdentityModel.setValue(new RecipeIdentityModel(
                recipeEntity.getTitle(),
                recipeEntity.getDescription(),
                recipeEntity.getPreparationTime(),
                recipeEntity.getCookingTime()));
    }

    @Override
    public void onDataNotAvailable() {
        dataLoading.set(false);
    }
}
