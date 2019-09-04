package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;

import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;

public class RecipeIdentityTestData {

    public static RecipeIdentityEntity getValidRecipeIdentityEntity() {
        return new RecipeIdentityEntity(
                "id",
                getValidExistingRecipeEntity().getId(),
                "title",
                "description",
                90,
                90,
                10L,
                10L
        );
    }

}
