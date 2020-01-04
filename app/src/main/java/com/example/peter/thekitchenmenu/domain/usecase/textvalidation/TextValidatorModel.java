package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class TextValidatorModel {
    @Nonnull
    private final String text;

    public TextValidatorModel(@Nonnull String text) {
        this.text = text;
    }

    @Nonnull
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextValidatorModel model = (TextValidatorModel) o;
        return text.equals(model.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Nonnull
    @Override
    public String toString() {
        return "TextValidatorModel{" +
                "text='" + text + '\'' +
                '}';
    }
}
