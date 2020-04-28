package com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import javax.annotation.Nonnull;

public interface PrimitiveDataSourceChild<T extends PrimitiveModel>
        extends PrimitiveDataSource<T> {

    void getAllByParentDataId(@Nonnull String parentDataId, GetAllPrimitiveCallback<T> callback);

    void save(@Nonnull T[] entity);

    void deleteAllByParentId(@Nonnull String parentId);
}
