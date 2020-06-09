package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public interface DomainModelConverterParent<
        PARENT extends BaseDomainPersistenceModel,
        PRIMITIVE_ENTITY extends PrimitiveModel> {

    PARENT convertToModelItem(@Nonnull PRIMITIVE_ENTITY entity);

    PRIMITIVE_ENTITY convertToPrimitive(@Nonnull PARENT parent);

    List<PARENT> convertToModels(@Nonnull List<PRIMITIVE_ENTITY> entities);
}
