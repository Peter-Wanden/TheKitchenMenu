package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.model.Recipe;

import java.util.List;

public class AdapterCatalogRecipe extends
        RecyclerView.Adapter<AdapterCatalogRecipe.ViewHolderRecipe> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /* List storing the database query result */
    private List<Recipe> mRecipes;

    /* Click handler */
    private final AdapterCatalogRecipe.RecipeCatalogAdapterOnClickHandler mClickHandler;

    /* Click interface that receives on click messages */
    public interface RecipeCatalogAdapterOnClickHandler {
        void onClick(int productId);
    }

    public AdapterCatalogRecipe(Context context,
                                AdapterCatalogRecipe.RecipeCatalogAdapterOnClickHandler listener) {
        mContext = context;
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public ViewHolderRecipe onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_recipe, viewGroup, false);

        return new ViewHolderRecipe(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecipe holder, int position) {

        /* Get the recipe at the passed in position */
        Recipe recipe = mRecipes.get(position);
        /* Set the title */
        holder.titleTV.setText(recipe.getTitle());
        /* Set the retailer */
        holder.descriptionTV.setText(recipe.getDescription());
        /* Set the category */
        holder.categoryTV.setText(getStringCategory(recipe.getCategory()));
    }

    private String getStringCategory(int requestCategory) {

        String category;

        switch (requestCategory) {
            case 1:
                // Breakfast
                category = mContext.getResources().getString(R.string.recipe_category_option_1);
                break;
            case 2:
                // Lunch
                category = mContext.getResources().getString(R.string.recipe_category_option_2);
                break;
            case 3:
                // Dinner
                category = mContext.getResources().getString(R.string.recipe_category_option_3);
                break;
            default:
                // Dinner
                category = mContext.getResources().getString(R.string.recipe_category_option_3);

        }
        return category;
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    /* Getter for the current list of products */
    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    /* When the data changes, this method updates the list of recipes and notifies the adapter. */
    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolderRecipe
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView titleTV;
        final TextView descriptionTV;
        final TextView categoryTV;

        /* Constructor */
        ViewHolderRecipe(View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.list_item_recipe_title);
            descriptionTV = itemView.findViewById(R.id.list_item_recipe_description);
            categoryTV = itemView.findViewById(R.id.list_item_recipe_category);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int recipeId = mRecipes.get(getAdapterPosition()).getRecipeId();
            mClickHandler.onClick(recipeId);
        }
    }
}
