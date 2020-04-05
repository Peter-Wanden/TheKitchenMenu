package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import javax.annotation.Nonnull;

public interface PrimitiveDataSourceParent<T extends PrimitiveModel>
        extends PrimitiveDataSource<T> {

    void getAllByDomainId(@Nonnull String domainId, GetAllCallback<T> callback);

    void deleteAllByDomainId(@Nonnull String domainId);
}
