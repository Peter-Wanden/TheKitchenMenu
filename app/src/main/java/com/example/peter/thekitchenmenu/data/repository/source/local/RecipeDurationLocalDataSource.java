package com.example.peter.thekitchenmenu.data.repository.source.local;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

import java.util.List;

import static androidx.core.util.Preconditions.checkNotNull;

public class RecipeDurationLocalDataSource implements DataSource<RecipeDurationEntity> {

    private static volatile RecipeDurationLocalDataSource INSTANCE;
    private RecipeDurationEntityDao dao;
    private AppExecutors appExecutors;

    private RecipeDurationLocalDataSource(@NonNull AppExecutors appExecutors,
                                          @NonNull RecipeDurationEntityDao dao) {
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public static RecipeDurationLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                            @NonNull RecipeDurationEntityDao dao) {
        if (INSTANCE == null) {
            synchronized (RecipeDurationLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeDurationLocalDataSource(appExecutors, dao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final List<RecipeDurationEntity> list = dao.getAll();
            appExecutors.mainThread().execute(() -> {
                if (list.isEmpty())
                    callback.onDataNotAvailable();
                else
                    callback.onAllLoaded(list);
            });
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getById(@NonNull String id,
                        @NonNull GetEntityCallback<RecipeDurationEntity> callback) {
        Runnable runnable = () -> {
            final RecipeDurationEntity entity = dao.getById(id);
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
    public void save(@NonNull RecipeDurationEntity recipeDurationEntity) {
        checkNotNull(recipeDurationEntity);
        Runnable runnable = () -> dao.insert(recipeDurationEntity);
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteAll() {
        Runnable runnable = () -> dao.deleteAll();
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        Runnable runnable = () -> dao.deleteById(id);
        appExecutors.diskIO().execute(runnable);
    }
}
