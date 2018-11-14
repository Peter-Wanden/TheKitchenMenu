package com.example.peter.thekitchenmenu.data.repository;

import android.util.Log;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.data.model.ProductMy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class SyncProdMy {

    private static final String LOG_TAG = SyncProdMy.class.getSimpleName();

    private RepositoryLocal mRepoLocal;
    private Future<ProductMy> mFutureProdMy;
    private Future<ProductCommunity> mFutureProdComm;
    private ProductMy[] mArrayProdMy = new ProductMy[2];

    SyncProdMy(RepositoryLocal repoLocal, ProductMy remoteProdMy) {

        mRepoLocal = repoLocal;
        mArrayProdMy[0] = remoteProdMy;
        ExecutorService xs = Executors.newSingleThreadExecutor();
        CallableProdMy callableProdMy = new CallableProdMy();
        CallableProdComm cPc = new CallableProdComm();
        mFutureProdMy = xs.submit(callableProdMy);
        mFutureProdComm = xs.submit(cPc);
    }

    void matchAndSync() {
        try {
            mArrayProdMy[1] = mFutureProdMy.get();
            compareAndSync();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void compareAndSync() {

        // If the remote product is not found locally, store it.
        if (mArrayProdMy[1] == null) {
            mRepoLocal.insertProdMy(mArrayProdMy[0]);
        } else {
            // Perform data ID integrity check and match the products
            try {
                ProductCommunity prodComm = mFutureProdComm.get();
                match(prodComm);
                // If the products do not match, sync with remote.
                if (!mArrayProdMy[0].toString().equals(mArrayProdMy[1].toString())) {
                    sync();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Places the local ID's into the remote product ready for comparison.
    private void match(ProductCommunity prodComm) {
        mArrayProdMy[0].setId(mArrayProdMy[1].getId());
        mArrayProdMy[0].setCommunityProductId(prodComm.getId());
    }

    // Updates the local product with the remote product.
    private void sync() {
        Log.e(LOG_TAG, "Updating product: " + mArrayProdMy[0].getFbProductReferenceKey());

        AppExecutors.getInstance().diskIO().execute(()
                -> mRepoLocal.mProdMyDAO.updateProdMy(mArrayProdMy[0]));
    }

    /**
     *
     * Creates and returns a callable {@link ProductMy} object for submission to the executor
     * service. Remote and local products are matched by comparing the remote product ID, which
     * is stored in both locations.
     */
    private class CallableProdMy implements Callable<ProductMy> {

        @Override
        public ProductMy call() {

            return mRepoLocal.mProdMyDAO.
                    getProdMyByRemoteId(mArrayProdMy[0].getFbProductReferenceKey());
        }
    }

    /**
     * Creates and returns a callable {@link ProductCommunity} object for submission to the
     * executor service. Remote and local products are matched by comparing the remote product ID,
     * which is stored in both locations.
     */
    private class CallableProdComm implements Callable<ProductCommunity> {

        @Override
        public ProductCommunity call() {

            return mRepoLocal.mProdCommDAO.
                    getProdCommByRemoteId(mArrayProdMy[0].getFbProductReferenceKey());
        }
    }
}
