package com.example.peter.thekitchenmenu.ui.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.catalog.ActivityCatalogProduct;
import com.example.peter.thekitchenmenu.ui.catalog.ActivityCatalogRecipe;

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

        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ActivityCatalogProduct.class);
                startActivity(intent);
            }
        });
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ActivityCatalogRecipe.class);
                startActivity(intent);
            }
        });
    }
}
