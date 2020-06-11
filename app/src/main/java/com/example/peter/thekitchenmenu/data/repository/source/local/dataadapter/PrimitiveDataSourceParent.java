package com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import javax.annotation.Nonnull;

public interface PrimitiveDataSourceParent<T extends EntityModel>
        extends PrimitiveDataSource<T> {

    void getAllByDomainId(@Nonnull String domainId, @Nonnull GetAllPrimitiveCallback<T> callback);

    void deleteAllByDomainId(@Nonnull String domainId);
}
