package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public interface DomainModelConverterParent<
        P extends BaseDomainPersistenceModel,
        E extends PrimitiveModel> {

    P convertToModel(@Nonnull E e);

    E convertToPrimitive(@Nonnull P m);

    List<P> convertToModels(@Nonnull List<E> es);
}
