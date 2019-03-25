package com.example.peter.thekitchenmenu.ui.detail;

import android.os.Bundle;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.viewmodels.ProductMainViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ProductMain extends AppCompatActivity {

    private static final String TAG = "ProductMain";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO - see if there is a bundle containing a reference key sent with the intent.
        // If there is launch in edit mode, if not launch in new product mode

        initialiseViews();
    }

    private void initialiseViews() {

        ProductMainViewModel viewModel = ViewModelProviders.of(
                this).get(ProductMainViewModel.class);
        viewModel.getTitle().observe(this, this::setTitle);
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
