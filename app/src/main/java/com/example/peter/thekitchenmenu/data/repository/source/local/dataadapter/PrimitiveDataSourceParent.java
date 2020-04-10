package com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import javax.annotation.Nonnull;

public interface PrimitiveDataSourceParent<T extends PrimitiveModel>
        extends PrimitiveDataSource<T> {

    void getAllByDomainId(@Nonnull String domainId, @Nonnull GetAllPrimitiveCallback<T> callback);

    void deleteAllByDomainId(@Nonnull String domainId);
}
