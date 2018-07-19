package com.example.peter.thekitchenmenu.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.ui.detail.ProductDetailActivity;

import java.util.List;

public class ProductCatalogActivity
        extends
        AppCompatActivity
        implements
        ProductCatalogAdapter.ProductCatalogAdapterOnClickHandler {

    public static final String LOG_TAG = ProductCatalogActivity.class.getSimpleName();

    /* Adapter for the product list view */
    public ProductCatalogAdapter mCatalogAdapter;

    /* RecyclerView for the list view */
    private RecyclerView mRecyclerView;

    /* Progress bar shown when loading data */
    private ProgressBar mLoadingIndicator;

    /* Floating action button */
    private FloatingActionButton mFab;

    /* Empty view */
    private View mEmptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        setupViews();

        // Todo - change color of + in fab to white
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(ProductCatalogActivity.this,
                        ProductDetailActivity.class);
                startActivity(addProductIntent);
            }
        });

        /* Create the adapter and pass in the this class context and the listener (which is also
        this class as this class implements the click handler. */
        mCatalogAdapter = new ProductCatalogAdapter(this, this);
        mRecyclerView.setAdapter(mCatalogAdapter);

        // Todo - Implement empty view and loading indicators

        /* Retrieve the content of the products table */
        setupProductsViewModel();
    }

    private void setupViews() {
        /* Get a reference to the views */
        mRecyclerView = findViewById(R.id.activity_product_catalog_rv);
        mLoadingIndicator = findViewById(R.id.activity_product_catalog_loading_indicator_pb);
        mFab = findViewById(R.id.activity_product_catalog_fab);
        mEmptyView = findViewById(R.id.activity_product_catalog_empty_view);

        /* Create and set the layout manager */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * A product has been clicked in the RecyclerView. Add the product ID to an intent and go to
     * the ProductDetailActivity.
     * @param productId - The ID of the selected product
     */
    @Override
    public void onClick(int productId) {
        Intent intent = new Intent(ProductCatalogActivity.this, ProductDetailActivity.class);
        intent.putExtra(Constants.PRODUCT_ID, productId);
        startActivity(intent);
    }

    /* Shows the loading indicator and hides other views */
    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /* Hides the loading indicator and shows other views */
    private void showProducts () {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /* Retrieve products from the database and set an observer to watch for changes */
    private void setupProductsViewModel() {

        /* Call ViewModelProviders */
        ProductCatalogViewModel viewModel =
                ViewModelProviders
                        .of(this)
                        .get(ProductCatalogViewModel.class);

        /* Set observer for any data changes */
        viewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                // Set the list of products to the adapter
                Log.d(LOG_TAG, "Updating list of tasks from LiveData in ViewModel");
                mCatalogAdapter.setProducts(products);
            }
        });
    }

    /* TODO - Delete product here? Or always in detail activity */
//    private void deleteProduct();
}
