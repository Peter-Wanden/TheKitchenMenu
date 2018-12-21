package com.example.peter.thekitchenmenu.utils;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;


public class Converters {
    /* Helper method to convert the unit of measure from an integer to a String value */
    public static String getUnitOfMeasureString(Context context, int requestUnitOfMeasure) {

        String unitOfMeasure;

        switch (requestUnitOfMeasure) {
            case 1:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_1);
                break;
            case 2:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_2);
                break;
            case 3:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_3);
                break;
            default:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_0);
                break;
        }
        return unitOfMeasure;
    }

    /* Helper method to convert the shelf life from an integer to a String value */
    public static String getShelfLifeString(Context context, int requestShelfLife) {

        String shelfLife;

        switch (requestShelfLife) {
            case 1:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_1);
                break;
            case 2:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_2);
                break;
            case 3:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_3);
                break;
            case 4:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_4);
                break;
            case 5:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_5);
                break;
            case 6:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_6);
                break;
            case 7:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_7);
                break;
            case 8:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_8);
                break;
            case 9:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_9);
                break;
            case 10:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_10);
                break;
            case 11:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_11);
                break;
            case 12:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_12);
                break;
            case 13:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_13);
                break;
            case 14:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_14);
                break;
            case 15:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_15);
                break;
            case 16:
                shelfLife = context.getResources().getString(R.string.shelf_life_option_16);
                break;
            default: shelfLife = context.getResources().getString(R.string.shelf_life_option_0);

        }
        return shelfLife;
    }

    /* Helper method to convert the category from an integer to a String value */
    public static String getCategoryString (Context context, int requestCategory) {

        String category;

        switch (requestCategory) {
            case 1:
                category = context.getResources().getString(R.string.product_category_option_1);
                break;
            case 2:
                category = context.getResources().getString(R.string.product_category_option_2);
                break;
            case 3:
                category = context.getResources().getString(R.string.product_category_option_3);
                break;
            default: category = context.getResources().getString(R.string.shelf_life_option_0);
        }
        return category;
    }
}
