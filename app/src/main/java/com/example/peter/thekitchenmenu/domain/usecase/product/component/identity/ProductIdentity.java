package com.example.peter.thekitchenmenu.domain.usecase.product.component.identity;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class ProductIdentity {

    private static final String TAG = "tkm-" + ProductIdentity.class.getSimpleName() + ": ";

    enum Category {
        NON_FOOD(0),
        FOOD(1);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, Category> options = new HashMap<>();

        Category(int id) {
            this.id = id;
        }

        static {
            for (Category c : Category.values())
                options.put(c.id, c);
        }

        public static Category getById(int id) {
            return options.get(id);
        }

        public int getId() {
            return id;
        }
    }
}
