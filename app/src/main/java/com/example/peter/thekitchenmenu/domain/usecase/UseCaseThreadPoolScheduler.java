package com.example.peter.thekitchenmenu.domain.usecase;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executes asynchronous use cases using a {@link ThreadPoolExecutor}.
 * See also {@link Executors} for a list of factory methods to create common
 * {@link java.util.concurrent.ExecutorService}s for different scenarios.
 */
public class UseCaseThreadPoolScheduler
        implements
        UseCaseScheduler {

    private final Handler handler = new Handler();

    private static final int POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int TIMEOUT = 30;

    private ThreadPoolExecutor poolExecutor;

    UseCaseThreadPoolScheduler() {
        poolExecutor = new ThreadPoolExecutor(
                POOL_SIZE,
                MAX_POOL_SIZE,
                TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(POOL_SIZE));
    }

    @Override
    public void execute(Runnable runnable) {
        poolExecutor.execute(runnable);
    }

    @Override
    public <RESPONSE extends UseCaseBase.Response> void onSuccessResponse(
            final RESPONSE response,
            final UseCaseBase.Callback<RESPONSE> callback) {

        handler.post(() -> callback.onSuccessResponse(response));
    }

    @Override
    public <RESPONSE extends UseCaseBase.Response> void onErrorResponse(
            final RESPONSE response,
            final UseCaseBase.Callback<RESPONSE> callback) {

        handler.post(() -> callback.onErrorResponse(response));
    }
}
