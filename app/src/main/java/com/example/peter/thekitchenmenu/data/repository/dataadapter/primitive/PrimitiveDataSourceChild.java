package com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import javax.annotation.Nonnull;

public interface PrimitiveDataSourceChild<T extends PrimitiveModel>
        extends PrimitiveDataSource<T> {

    void getAllByParentDataId(@Nonnull String parentDataId, GetAllPrimitiveCallback<T> callback);

    void saveAll(@Nonnull T[] entities);

    void deleteAllByParentId(@Nonnull String parentId);
}
