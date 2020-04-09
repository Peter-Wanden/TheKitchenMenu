package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.List;

import javax.annotation.Nonnull;

public interface DomainModelConverter<
        P extends BasePersistence,
        E extends PrimitiveModel> {

    interface ActiveList<
            P extends BasePersistence,
            E extends PrimitiveModel> {
        List<P> convertToActiveModels(@Nonnull List<E> entities);
    }

    interface ActiveModel<
            P extends BasePersistence,
            E extends PrimitiveModel> {
        P convertToActiveModel(@Nonnull List<E> entities);
    }

    P convertToModel(@Nonnull E entity);

    E convertToPrimitive(@Nonnull P model);

    List<P> convertToModels(@Nonnull List<E> entities);


}
