package com.example.peter.thekitchenmenu.ui.catalog;


import android.support.v4.app.Fragment;

import com.example.peter.thekitchenmenu.model.IngredientAndProduct;


public class FragmentCatalogIngredient
        {

//    public static final String LOG_TAG = FragmentCatalogIngredient.class.getSimpleName();
//
//    /* Binding class */
//    FragmentCatalogIngredientsBinding mIngredientsBinding;
//
//    /* Adapter for the list view */
//    AdapterCatalogIngredients mIngredientsAdapter;
//
//    /* The recipe ID passed to this fragment */
//    int mRecipeId;
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // Get the recipe ID passed to this fragment
//        mRecipeId = getArguments() != null ?
//                getArguments().getInt(Constants.RECIPE_KEY) : Constants.DEFAULT_RECIPE_ID;
//
//        mIngredientsAdapter = new AdapterCatalogIngredients(getActivity(), this);
//        mIngredientsBinding.fragmentCatalogIngredientsRv.setAdapter(mIngredientsAdapter);
//
//        LinearLayoutManager mLinearLayoutManager = new
//                LinearLayoutManager(Objects.requireNonNull(getActivity())
//                .getApplicationContext(),
//                LinearLayoutManager.VERTICAL, false);
//
//        mIngredientsBinding.fragmentCatalogIngredientsRv.setLayoutManager(mLinearLayoutManager);
//
//        mIngredientsBinding.fragmentCatalogIngredientsRv.setHasFixedSize(true);
//
//        setupIngredientsViewModel();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater,
//            @Nullable ViewGroup container,
//            @Nullable Bundle savedInstanceState) {
//
//        mIngredientsBinding = DataBindingUtil.inflate(inflater,
//                R.layout.fragment_catalog_ingredients, container, false);
//
//        return mIngredientsBinding.getRoot();
//
//    }

//    @Override
//    public void onClick(IngredientAndProduct ingredient) {
//
//    }

//    /* Retrieve products from the database and set an observer to watch for changes */
//    private void setupIngredientsViewModel() {
//
//        /* Call ViewModelProviders */
//        ViewModelCatalogIngredientsAndProduct viewModel =
//                ViewModelProviders
//                        .of(this)
//                        .get(ViewModelCatalogIngredientsAndProduct.class);
//
//        /* Set observer for any data changes */
//        viewModel.getIngredients().observe(this, ingredients -> {
//            // Set the list to the adapter
//            mIngredientsAdapter.setProducts(ingredients);
//
//            // Set empty view
//            if(ingredients.size() == 0) {
//                mIngredientsBinding.fragmentCatalogContainerIngredientEmptyView
//                        .setVisibility(View.VISIBLE);
//            } else {
//                mIngredientsBinding.fragmentCatalogContainerIngredientEmptyView
//                        .setVisibility(View.GONE);
//            }
//        });
//    }
}
