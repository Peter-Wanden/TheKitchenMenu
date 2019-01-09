package com.example.peter.thekitchenmenu.utils;

import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {

    /* Turns a list of product objects into a JSON string */
    public static String productsToJson(List<ProductModel> products) {
        return new Gson().toJson(products);
    }

    /* Turns a JSON string into a list of JSON objects */
    public static List<ProductModel> productsJsonToList (String productsJson){

        Type listType = new TypeToken<List<ProductModel>>(){}.getType();
        return new Gson().fromJson(productsJson, listType);
    }
}
