package com.example.tkmapplibrary.dataValidation;

/**
 * This class holds data validation rules for all user input
 *
 * Return values:
 *      0. Validated OK
 *   true. Validated OK
 *      1. Value missing
 *      2. String is too long
 *  false. Value is out of range
 */
public class InputValidation {

    /* Validation rules for products description */
    public static int validateProductDescription(String description) {
        // Check if not null and contains more than 1 character
        if (description == null || description.length() < 1) {
            return 1;
            /* Limit length to 120 chars */
        } else if(description.length() > 120) {
            return 2;
        }
        // A zero response is OK
        return 0;
    }

    /* Validation rules for retailers name */
    public static int validateRetailer(String retailer) {
        // Check if not null and contains more than 1 character
        if(retailer == null || retailer.length() < 1) {
            return 0;
            /* Limit length to 120 chars */
        } else if(retailer.length() > 120) {
            return 1;
        }
        return 100;
    }

    /* Validation rules for unit of measure */
    public static boolean validateUoM(int uom) {
        // Check if value is between 0 and 2
        return !(uom < 0 || uom > 2);
    }

    /* Validation rules for pack size */
    public static boolean validatePackSize(int packSize) {
        // Check to see if the value is greater than 1 and less then 1000
        return packSize >= 1 && packSize <= 1000;
    }

    /* Validation rules for shelf life */
    public static boolean validateShelfLife (int shelfLife) {
        // Check value is between 0 and 12
        return shelfLife >= 0 && shelfLife <=12;
    }

    /* Validation rules for location room */
    public static int validateLocRoom (String locRoom){
        // Check if not null and contains more than 1 character
        if (locRoom == null || locRoom.length() < 1) {
            return 0;
            /* Limit length to 60 chars */
        } else if(locRoom.length() > 60) {
            return 1;
        }
        return 100;
    }

    /* Validation rules for location in room */
    public static int validateLocInRoom (String locInRoom){
        // Check if not null and contains more than 1 character
        if (locInRoom == null || locInRoom.length() < 1) {
            return 0;
            /* Limit length to 60 chars */
        } else if(locInRoom.length() > 60) {
            return 1;
        }
        return 100;
    }

    /* Validation rules for packs per month */
    public static boolean validatePacksPerMonth(int packsPerMonth) {
        // Check to ensure vlue is between 1 and 1000
        return packsPerMonth >= 1 && packsPerMonth <= 1000;
    }

    /* Validation rules for category */
    public static boolean validateCategory(int category) {
        // There are currently only three categories 0-2
        return category >= 0 && category <= 2;
    }
}
