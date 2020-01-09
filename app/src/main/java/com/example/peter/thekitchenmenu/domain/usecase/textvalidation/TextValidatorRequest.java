package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class TextValidatorRequest implements UseCaseCommand.Request {
    @Nonnull
    private final TextValidator.RequestType type;
    @Nonnull
    private final TextValidatorModel model;

    public TextValidatorRequest(@Nonnull TextValidator.RequestType type,
                                @Nonnull TextValidatorModel model) {
        this.type = type;
        this.model = model;
    }

    @Nonnull
    public TextValidator.RequestType getType() {
        return type;
    }

    @Nonnull
    public TextValidatorModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextValidatorRequest request = (TextValidatorRequest) o;
        return type == request.type &&
                model.equals(request.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "TextValidatorRequest{" +
                "type=" + type +
                ", model=" + model +
                '}';
    }
}
