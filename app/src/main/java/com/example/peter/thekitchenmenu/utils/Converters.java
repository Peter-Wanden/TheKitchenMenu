package com.example.peter.thekitchenmenu.utils;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;


public class Converters {
    /* Helper method to convert the unit of measure from an integer to a String value */
    public static String getUnitOfMeasureString(Context context, int requestUnitOfMeasure) {

        String unitOfMeasure;

        switch (requestUnitOfMeasure) {
            case 1:
                unitOfMeasure = context.getResources().getString(R.string.grams);
                break;
            case 2:
                unitOfMeasure = context.getResources().getString(R.string.millilitres);
                break;
            case 3:
                unitOfMeasure = context.getResources().getString(R.string.count);
                break;
            default:
                unitOfMeasure = context.getResources().getString(R.string.unit_of_measure_default);
                break;
        }
        return unitOfMeasure;
    }

    /* Helper method to convert the shelf life from an integer to a String value */
    public static String getShelfLifeString(Context context, int requestShelfLife) {

        String shelfLife;

        switch (requestShelfLife) {
            case 1:
                shelfLife = context.getResources().getString(R.string.one_day);
                break;
            case 2:
                shelfLife = context.getResources().getString(R.string.three_days);
                break;
            case 3:
                shelfLife = context.getResources().getString(R.string.five_days);
                break;
            case 4:
                shelfLife = context.getResources().getString(R.string.one_week);
                break;
            case 5:
                shelfLife = context.getResources().getString(R.string.two_weeks);
                break;
            case 6:
                shelfLife = context.getResources().getString(R.string.three_weeks);
                break;
            case 7:
                shelfLife = context.getResources().getString(R.string.four_weeks);
                break;
            case 8:
                shelfLife = context.getResources().getString(R.string.frozen);
                break;
            case 9:
                shelfLife = context.getResources().getString(R.string.dried);
                break;
            case 10:
                shelfLife = context.getResources().getString(R.string.sealed);
                break;
            case 11:
                shelfLife = context.getResources().getString(R.string.tin);
                break;
            case 12:
                shelfLife = context.getResources().getString(R.string.jar);
                break;
            case 13:
                shelfLife = context.getResources().getString(R.string.bottle);
                break;
            case 14:
                shelfLife = context.getResources().getString(R.string.box);
                break;
            case 15:
                shelfLife = context.getResources().getString(R.string.packet);
                break;
            case 16:
                shelfLife = context.getResources().getString(R.string.not_applicable);
                break;
            default: shelfLife = context.getResources().getString(R.string.shelf_life_default);

        }
        return shelfLife;
    }

    /* Helper method to convert the category from an integer to a String value */
    public static String getCategoryString (Context context, int requestCategory) {

        String category;

        switch (requestCategory) {
            case 1:
                category = context.getResources().getString(R.string.food);
                break;
            case 2:
                category = context.getResources().getString(R.string.non_food);
                break;
            default: category = context.getResources().getString(R.string.shelf_life_default);
        }
        return category;
    }
}
