package com.example.peter.thekitchenmenu.domain.usecase.product.component.measurement;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class ProductMeasurement {

    enum ShelfLife {
        OPTION_1(1),
        OPTION_2(2),
        OPTION_3(3),
        OPTION_4(4),
        OPTION_5(5),
        OPTION_6(6),
        OPTION_7(7),
        OPTION_8(14),
        OPTION_9(21),
        OPTION_10(28),
        OPTION_11(90),
        OPTION_12(365),
        OPTION_13(730),
        OPTION_14(1095);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, ShelfLife> options = new HashMap<>();

        ShelfLife(int id) {
            this.id = id;
        }

        static {
            for (ShelfLife s : ShelfLife.values())
                options.put(s.id, s);
        }

        public static ShelfLife getById(int id) {
            return options.get(id);
        }

        public int getId() {
            return id;
        }
    }
}
