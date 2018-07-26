package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.IngredientAndProductDAO;
import com.example.peter.thekitchenmenu.model.Ingredient;
import com.example.peter.thekitchenmenu.model.IngredientAndProduct;
import com.example.peter.thekitchenmenu.utils.Converters;

import java.util.List;

public class AdapterCatalogIngredients
        extends
        RecyclerView.Adapter<AdapterCatalogIngredients.ViewHolderIngredient>{

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /* List storing the database query result for ingredients */
    private List<IngredientAndProduct> mIngredients;

    /* Click handler */
    private final AdapterCatalogIngredients.AdapterCatalogIngredientsOnClickHandler mClickHandler;

    /* Click interface that receives on click messages */
    public interface AdapterCatalogIngredientsOnClickHandler {
        void onClick(IngredientAndProduct ingredient);
    }

    public AdapterCatalogIngredients(
            Context context,
            AdapterCatalogIngredients.AdapterCatalogIngredientsOnClickHandler handler ) {

        mContext = context;
        mClickHandler = handler;
    }



    @NonNull
    @Override
    public ViewHolderIngredient onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_ingredient, parent, false);

        return new ViewHolderIngredient(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderIngredient holder, int position) {

        // Todo check to see if we need getters
        /* Get the ingredient at the passed in position */
        IngredientAndProduct ingredient = mIngredients.get(position);
        /* Set the description */
        holder.descriptionTV.setText(ingredient.getDescription());
        // Todo - This field should be calculated
        holder.quantityTV.setText(String.valueOf(ingredient.getQuantityPerServing()));
        holder.measureTV.setText(Converters.getStringUnitOfMeasure(mContext, ingredient.getUoM()));
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    /* Getter for the current list */
    public List<IngredientAndProduct> getIngredients() {
        return mIngredients;
    }

    /* When the data changes, this method updates the list of products and notifies the adapter to
       use the new values in it */
    public void setProducts(List<IngredientAndProduct> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    class ViewHolderIngredient
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descriptionTV;
        final TextView quantityTV;
        final TextView measureTV;

        ViewHolderIngredient(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_ingredient_tv_description);
            quantityTV = itemView.findViewById(R.id.list_item_ingredient_tv_quantity);
            measureTV = itemView.findViewById(R.id.list_item_ingredient_tv_unit_of_measure);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {

            IngredientAndProduct ingredient;
            ingredient = mIngredients.get(getAdapterPosition());

            mClickHandler.onClick(ingredient);
        }
    }
}
