package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.List;

import javax.annotation.Nonnull;

public interface DomainModelConverterParent<
        P extends BasePersistence,
        E extends PrimitiveModel> {

    P convertToModel(@Nonnull E e);

    E convertToPrimitive(@Nonnull P m);

    List<P> convertToModels(@Nonnull List<E> es);
}
