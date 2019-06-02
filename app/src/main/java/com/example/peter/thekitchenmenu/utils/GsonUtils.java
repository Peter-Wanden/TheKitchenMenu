package com.example.peter.thekitchenmenu.utils;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {

    /* Turns a list of product_uneditable objects into a JSON string */
    public static String productsToJson(List<ProductEntity> products) {
        return new Gson().toJson(products);
    }

    /* Turns a JSON string into a list of JSON objects */
    public static List<ProductEntity> productsJsonToList (String productsJson){

        Type listType = new TypeToken<List<ProductEntity>>(){}.getType();
        return new Gson().fromJson(productsJson, listType);
    }
}
