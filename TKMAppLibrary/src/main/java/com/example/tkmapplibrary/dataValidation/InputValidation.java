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

    /* Validation rules for a products description */
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

    /* Validation rules for a products manufacturer */
    public static int validateMadeBy(String madeBy) {
        // Check if not null and contains more than 1 character
        if (madeBy == null || madeBy.length() < 1) {
            return 1;
            /* Limit length to 120 chars */
        } else if(madeBy.length() > 120) {
            return 2;
        }
        // A zero response is OK
        return 0;
    }

    /* Validation rules for retailers name */
    public static int validateRetailer(String retailer) {
        // Check if not null and contains more than 1 character
        if(retailer == null || retailer.length() < 1) {
            return 1;
            /* Limit length to 120 chars */
        } else if(retailer.length() > 120) {
            return 2;
        }
        // A zero response is OK
        return 0;
    }

    /* Validation rules for unit of measure */
    // TODO - include all units of measure, implement units of measuretranslation into base measurements here.
    public static boolean validateUoM(int uom) {
        // Check if value is between 0 and 2
        return !(uom < 1 || uom > 3);
    }

    /* Validation rules for pack size */
    public static boolean validatePackSize(int packSize) {
        // Check to see if the value is greater than 1 and less then 10,000 grams.
        return packSize >= 1 && packSize <= 10000;
    }

    /* Validation rules for shelf life */
    public static boolean validateShelfLife (int shelfLife) {
        // Check value is between 0 and 12
        return shelfLife >= 1 && shelfLife <= 16;
    }

    /* Validation rules for location room */
    public static int validateLocRoom (String locRoom){
        // Check if not null and contains more than 1 character
        if (locRoom == null || locRoom.length() < 1) {
            return 1;
            /* Limit length to 60 chars */
        } else if(locRoom.length() > 60) {
            return 2;
        }
        return 0;
    }

    /* Validation rules for location in room */
    public static int validateLocInRoom (String locInRoom){
        // Check if not null and contains more than 1 character
        if (locInRoom == null || locInRoom.length() < 1) {
            return 1;
            /* Limit length to 60 chars */
        } else if(locInRoom.length() > 60) {
            return 2;
        }
        return 0;
    }

    /* Validation rules for packs per month */
    public static boolean validatePacksPerMonth(int packsPerMonth) {
        // Check to ensure vlue is between 1 and 1000
        return packsPerMonth >= 1 && packsPerMonth <= 1000;
    }

    /* Validation rules for product category */
    public static boolean validateProductCategory(int category) {
        // There are currently only three categories:
        // 0 = no category selected, this is an error.
        // 1 = food used outside of recipes (and sometimes in or with recipes).
        // 2 = food only used in recipes.
        return category >= 1 && category <= 3;
    }

    /* Validation rules for a recipes title */
    public static int validateRecipeTitle(String title) {
        // Check if not null and contains more than 1 character
        if (title == null || title.length() < 1) {
            return 1;
            /* Limit length to 120 chars */
        } else if(title.length() > 120) {
            return 2;
        }
        // A zero response is OK
        return 0;
    }

    /* Validation rules for a recipe description */
    public static int validateRecipeDescription(String description) {
        // A null length string is OK.
        if (description == null || description.length() < 1) {
            return 0;
            /* Limit length to 120 chars */
        } else if(description.length() > 120) {
            return 2;
        }
        // A zero response is OK
        return 0;
    }

    /* Validate product price */
    public static boolean validatePrice(double price) {
        // Check to see if the value is greater than 1 and less then 10,000 grams.
        return price >= 0.01 && price <= 10000;
    }
}
