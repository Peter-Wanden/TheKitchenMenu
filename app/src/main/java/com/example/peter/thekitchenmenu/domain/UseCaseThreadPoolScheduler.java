package com.example.peter.thekitchenmenu.domain;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executes asynchronous tasks using a {@link ThreadPoolExecutor}.
 * See also {@link Executors} for a list of factory methods to create common
 * {@link java.util.concurrent.ExecutorService}s for different scenarios.
 */
public class UseCaseThreadPoolScheduler implements UseCaseScheduler {

    private final Handler handler = new Handler();

    public static final int POOL_SIZE = 2;
    public static final int MAX_POOL_SIZE = 4;
    public static final int TIMEOUT = 30;

    ThreadPoolExecutor poolExecutor;

    public UseCaseThreadPoolScheduler() {
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
    public <V extends UseCase.Response> void notifyResponse(
            final V response,
            final UseCase.Callback<V> callback) {

        handler.post(() -> callback.onSuccess(response));
    }

    @Override
    public <V extends UseCase.Response> void onError(
            final V response,
            final UseCase.Callback<V> callback) {
        handler.post(() -> callback.onError(response));
    }
}
