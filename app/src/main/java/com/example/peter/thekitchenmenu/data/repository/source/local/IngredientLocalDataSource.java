package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class IngredientLocalDataSource implements DataSource<IngredientEntity> {

    private static volatile IngredientLocalDataSource INSTANCE;
    private IngredientEntityDao entityDao;
    private AppExecutors appExecutors;

    private IngredientLocalDataSource(@NonNull AppExecutors appExecutors,
                                      @NonNull IngredientEntityDao entityDao) {
        this.appExecutors = appExecutors;
        this.entityDao = entityDao;
    }

    public static IngredientLocalDataSource getInstance(
            @NonNull AppExecutors appExecutors,
            @NonNull IngredientEntityDao entityDao) {

        if (INSTANCE == null) {
            synchronized (IngredientLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new IngredientLocalDataSource(appExecutors, entityDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final List<IngredientEntity> entityList = entityDao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (entityList.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(entityList);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<IngredientEntity> callback) {
        Runnable runnable = () -> {
            final IngredientEntity entity = entityDao.getById(id);
            appExecutors.mainThread().execute(() -> {
                if (entity != null)
                    callback.onEntityLoaded(entity);
                else
                    callback.onDataNotAvailable();
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void save(@NonNull IngredientEntity entity) {
        checkNotNull(entity);
        Runnable runnable = () -> entityDao.insert(entity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> entityDao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        Runnable runnable = () -> entityDao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
