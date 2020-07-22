package com.example.peter.thekitchenmenu.domain.businessentity.textvalidation;

import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity.TextLength;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import javax.annotation.Nonnull;

public final class TextValidationModel
        implements
        DomainModel.EntityModel {

    @Nonnull
    private final TextLength textLength;
    @Nonnull
    private final String text;

    public TextValidationModel(@Nonnull TextLength textLength, @Nonnull String text) {
        this.textLength = textLength;
        this.text = text;
    }

    @Nonnull
    public TextLength getTextLength() {
        return textLength;
    }

    @Nonnull
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextValidationModel)) return false;

        TextValidationModel that = (TextValidationModel) o;

        if (textLength != that.textLength) return false;
        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        int result = textLength.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "TextValidationEntityModel{" +
                "textLength=" + textLength +
                ", text='" + text + '\'' +
                '}';
    }
}
