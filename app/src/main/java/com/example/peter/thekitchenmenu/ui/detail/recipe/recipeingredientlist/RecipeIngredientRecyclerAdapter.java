package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.databinding.RecipeIngredientListItemBinding;

public class RecipeIngredientRecyclerAdapter
        extends RecyclerView.Adapter<RecipeIngredientRecyclerAdapter.ViewHolder> {

    private final RecipeIngredientListViewModel viewModel;

    public RecipeIngredientRecyclerAdapter(RecipeIngredientListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecipeIngredientListItemBinding binding;

        RecipeIngredientListItemNavigator navigator = new RecipeIngredientListItemNavigator() {
            @Override
            public void deleteIngredient(String ingredientId) {

            }

            @Override
            public void editIngredient(String ingredientId) {

            }
        };

        ViewHolder(RecipeIngredientListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {

        }
    }
}
