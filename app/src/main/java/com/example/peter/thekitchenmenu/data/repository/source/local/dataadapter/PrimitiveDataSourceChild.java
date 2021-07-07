package com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import javax.annotation.Nonnull;

public interface PrimitiveDataSourceChild<T extends EntityModel>
        extends PrimitiveDataSource<T> {

    void getAllByParentDataId(@Nonnull String parentDataId,
                              @Nonnull GetAllPrimitiveCallback<T> callback);

    void save(@Nonnull T[] entity);

    void deleteAllByParentDataId(@Nonnull String parentId);
}
