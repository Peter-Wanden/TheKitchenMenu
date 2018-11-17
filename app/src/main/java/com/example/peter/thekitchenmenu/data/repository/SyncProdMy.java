package com.example.peter.thekitchenmenu.data.repository;

import android.util.Log;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.DmProdMy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class SyncProdMy {

    private static final String LOG_TAG = SyncProdMy.class.getSimpleName();

    private RepositoryLocal mRepoLocal;
    private Future<DmProdMy> mFutureProdMy;
    private Future<DmProdComm> mFutureProdComm;
    private DmProdMy[] mArrayDmProdMy = new DmProdMy[2];

    SyncProdMy(RepositoryLocal repoLocal, DmProdMy remoteDmProdMy) {

        mRepoLocal = repoLocal;
        mArrayDmProdMy[0] = remoteDmProdMy;
        ExecutorService xs = Executors.newSingleThreadExecutor();
        CallableProdMy callableProdMy = new CallableProdMy();
        CallableProdComm cPc = new CallableProdComm();
        mFutureProdMy = xs.submit(callableProdMy);
        mFutureProdComm = xs.submit(cPc);
    }

    void matchAndSync() {
        try {
            mArrayDmProdMy[1] = mFutureProdMy.get();
            compareAndSync();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void compareAndSync() {

        // If the remote product is not found locally, store it.
        if (mArrayDmProdMy[1] == null) {
            mRepoLocal.insertProdMy(mArrayDmProdMy[0]);
        } else {
            // Perform data ID integrity check and match the products
            try {
                DmProdComm dmProdComm = mFutureProdComm.get();
                match(dmProdComm);
                // If the products do not match, sync with remote.
                if (!mArrayDmProdMy[0].toString().equals(mArrayDmProdMy[1].toString())) {
                    sync();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Places the local ID's into the remote product ready for comparison.
    private void match(DmProdComm dmProdComm) {
        mArrayDmProdMy[0].setId(mArrayDmProdMy[1].getId());
        mArrayDmProdMy[0].setCommunityProductId(dmProdComm.getId());
    }

    // Updates the local product with the remote product.
    private void sync() {
        Log.e(LOG_TAG, "Updating product: " + mArrayDmProdMy[0].getFbProductReferenceKey());

        AppExecutors.getInstance().diskIO().execute(()
                -> mRepoLocal.mProdMyDAO.updateProdMy(mArrayDmProdMy[0]));
    }

    /**
     *
     * Creates and returns a callable {@link DmProdMy} object for submission to the executor
     * service. Remote and local products are matched by comparing the remote product ID, which
     * is stored in both locations.
     */
    private class CallableProdMy implements Callable<DmProdMy> {

        @Override
        public DmProdMy call() {

            return mRepoLocal.mProdMyDAO.
                    getProdMyByRemoteId(mArrayDmProdMy[0].getFbProductReferenceKey());
        }
    }

    /**
     * Creates and returns a callable {@link DmProdComm} object for submission to the
     * executor service. Remote and local products are matched by comparing the remote product ID,
     * which is stored in both locations.
     */
    private class CallableProdComm implements Callable<DmProdComm> {

        @Override
        public DmProdComm call() {

            return mRepoLocal.mProdCommDAO.
                    getProdCommByRemoteId(mArrayDmProdMy[0].getFbProductReferenceKey());
        }
    }
}
