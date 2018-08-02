package com.example.peter.thekitchenmenu.ui.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.ui.catalog.ActivityCatalogProduct;
import com.example.peter.thekitchenmenu.ui.catalog.ActivityCatalogRecipe;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

// Todo - implement network / no network monitor

public class MainActivity extends AppCompatActivity {

    Button productButton;
    Button recipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productButton = findViewById(R.id.activity_main_goto_products);
        recipeButton = findViewById(R.id.activity_main_goto_recipes);

        productButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ActivityCatalogProduct.class);
            startActivity(intent);
        });
        recipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ActivityCatalogRecipe.class);
            startActivity(intent);
        });
    }
}
