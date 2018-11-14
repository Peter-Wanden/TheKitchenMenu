package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Synchronises a single remote and local product. Giving preference to the remote product.
 */
class SyncProdComm {

    // private static final String LOG_TAG = SyncProdComm.class.getSimpleName();

    private RepositoryLocal mRepoLocal;
    private Future<ProductCommunity> mFuturePC;
    private ProductCommunity[] mPcArray = new ProductCommunity[2];

    /**
     * Constructor
     * @param repositoryLocal an instance of the local database repository
     * @param pcRemote       the remote object to sync
     */
    SyncProdComm(RepositoryLocal repositoryLocal, ProductCommunity pcRemote) {

        mRepoLocal = repositoryLocal;
        mPcArray[0] = pcRemote;
        ExecutorService xs = Executors.newSingleThreadExecutor();
        CallableProductCommunity cPc = new CallableProductCommunity();
        mFuturePC = xs.submit(cPc);
    }

    /**
     * Finds and matches a remote product with (if available) a local product.
     * Position 0 always contains the remote product. Position 1 the local product.
     */
    void matchAndSync() {

        try {
            mPcArray[1] = mFuturePC.get();
            compareAndSync();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compares the products in the array, if they are different, stores or updates the local with
     * remote.
     */
    private void compareAndSync() {

        // If position 1 is null, this is a new product, save to local.
        if (mPcArray[1] == null) {
            mRepoLocal.insertProdComm(mPcArray[0]);

        } else {
            // There is a local and remote product with the same remote reference. Add the local ID
            // to the remote product, so they are the same
            match();
            // Compare the two, if they are not equal, update the local with the remote.
            if (!mPcArray[0].toString().equals(mPcArray[1].toString())) {

                AppExecutors.getInstance().diskIO().execute(()
                        -> mRepoLocal.updateProdComm(mPcArray[0]));
            }
        }
    }

    // Places the local ID's into the remote product ready for comparison
    private void match() {
        mPcArray[0].setId(mPcArray[1].getId());
    }

    /**
     * Creates and returns a callable {@link ProductCommunity} object for submission to the
     * executor service. Remote and local products are matched by comparing the remote product ID,
     * which is stored in both locations.
     */
    private class CallableProductCommunity implements Callable<ProductCommunity> {

        @Override
        public ProductCommunity call() {

            return mRepoLocal.getProdCommByRemoteId(mPcArray[0].getFbProductReferenceKey());
        }
    }
}
