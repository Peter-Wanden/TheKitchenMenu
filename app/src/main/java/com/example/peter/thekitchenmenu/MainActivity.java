package com.example.peter.thekitchenmenu;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.peter.thekitchenmenu.databinding.ActivityMainBinding;
import com.example.peter.thekitchenmenu.ui.detail.ProductDetailActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainBinding.activityMainAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
