package com.example.peter.thekitchenmenu.ui.utils;

import javax.annotation.Nonnull;

public final class CountFractionsItemModel {

    @Nonnull
    private final String fractionAsHtml;

    public CountFractionsItemModel(@Nonnull String fractionAsHtml) {
        this.fractionAsHtml = fractionAsHtml;
    }

    @Nonnull
    public String getFractionAsHtml() {
        return fractionAsHtml;
    }
}
