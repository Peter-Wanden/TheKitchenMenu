package com.example.peter.thekitchenmenu.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.ProductCommunityDAO;
import com.example.peter.thekitchenmenu.data.ProductMyDAO;
import com.example.peter.thekitchenmenu.data.TKMLocalDatabase;
import com.example.peter.thekitchenmenu.model.ProductCommunity;
import com.example.peter.thekitchenmenu.model.ProductMy;

import java.util.List;

/**
 * Manages data access for the local database
 */
public class RepositoryRoom {

    private static final String LOG_TAG = RepositoryRoom.class.getSimpleName();

    private Repository mRepository;
    private ProductCommunityDAO mProductCommunityDAO;
    private LiveData<List<ProductCommunity>> mListCommunityProducts;
    private ProductMyDAO mProductMyDAO;
    private LiveData<List<ProductMy>> mListMyProducts;


    RepositoryRoom(Application application, Repository repository) {

        mRepository = repository;
        // Gets an instance of the local Room database.
        TKMLocalDatabase mDb = TKMLocalDatabase.getInstance(application);
        // Gets a reference to the 'community products' DAO
        mProductCommunityDAO = mDb.productCommunityDAO();
        // Gets a reference to the 'my products' DAO
        mProductMyDAO = mDb.productMyDAO();
    }

    /**
     * Creates a complete list of all {@link ProductCommunity} objects from the database
     * @return a complete list of all {@link ProductCommunity} objects in a LiveData wrapper.
     */
    LiveData<List<ProductCommunity>> listAllCommunityProducts() {
        mListCommunityProducts = mProductCommunityDAO.listAllProductCommunity();
        return mListCommunityProducts;
    }

    /**
     * Inserts a single {@link ProductCommunity} object into the database
     * @param productCommunity the object to be inserted.
     */
    public void communityProductInsert(ProductCommunity productCommunity) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProductCommunityDAO.insertProductCommunity(productCommunity));
    }

    /**
     * Inserts a List of {@link ProductCommunity} objects into the database
     * @param productCommunity the list of objects to be inserted.
     */
    public void communityProductsInsertAll(List<ProductCommunity> productCommunity) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProductCommunityDAO.insertProductsCommunity(productCommunity));
    }

    public void productCommunityUpdate(ProductCommunity productCommunity) {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProductCommunityDAO.updateProductCommunity(productCommunity));
    }

    /**
     * Deletes an {@link ProductCommunity} object from the database
     */
    public void communityProductsDeleteAll() {
        AppExecutors.getInstance().diskIO().execute(()
                -> mProductCommunityDAO.deleteAllProductCommunity());
    }

    /**
     * Synchronises the remote and local {@link ProductCommunity} objects, with priority given to
     * the remote product.
     */
    void synchroniseCommunityProduct(ProductCommunity remoteProductCommunity) {
        // Sends the product to be processed.
        new GetCommunityProductAsync().execute(remoteProductCommunity);
    }

    /**
     * Takes a remote ProductCommunity object and if available, finds and returns a local
     * ProductCommunity object with the same fbProductReference. If not found, returns the remote
     * ProductCommunity object used as the input.
     * TODO - Use {@link AppExecutors} and generify.
     */
    public class GetCommunityProductAsync extends
            AsyncTask<ProductCommunity, Void, ProductCommunity[]> {

        @Override
        protected ProductCommunity[] doInBackground(ProductCommunity... productCommunities) {
            ProductCommunity[] pc = new ProductCommunity[2];
            pc[0] = productCommunities[0];

            // Finds the remote product if it is stored locally.
            ProductCommunity localPC = mProductCommunityDAO.
                    findByProductReferenceKey(pc[0].getFbProductReferenceKey());

            if (localPC != null) {
                // Product found locally, match the ID's and return both local and remote product.
                pc[1] = localPC;
            }
            return pc;
        }

        @Override
        protected void onPostExecute(ProductCommunity[] pc) {

            // If only one product is returned, no local product was found.
            if (pc[1] == null) {
                // Add the remote product to the local database.
                AppExecutors.getInstance().diskIO().execute(() ->
                        mProductCommunityDAO.insertProductCommunity(pc[0]));
            } else {
                // There is a local and remote product with the same fbProductReference.
                if (pc[0].getFbProductReferenceKey().equals(pc[1].getFbProductReferenceKey())) {
                    // Add the local ID to the remote product, so they are the same
                    pc[0].setId(pc[1].getId());
                    // Compare the two, if they are not equal, update the local with the remote.
                    if (!pc[0].toString().equals(pc[1].toString())) {
                        AppExecutors.getInstance().diskIO().execute(()
                                -> mProductCommunityDAO.updateProductCommunity(pc[0]));
                    }
                }
            }
        }
    }

    /**
     * Creates a complete list of all {@link ProductMy} objects from the database
     * @return a complete list of all {@link ProductMy} objects in a LiveData wrapper.
     */
    LiveData<List<ProductMy>> listAllMyProducts() {
        mListMyProducts = mProductMyDAO.listAllMyProducts();
        return mListMyProducts;
    }

    void synchroniseProductMy(ProductMy productMy) {
        new GetMyProductsAsync().execute(productMy);
    }

    /**
     * TODO - Generify - see {@link GetCommunityProductAsync}
     */
    public class GetMyProductsAsync extends
            AsyncTask<ProductMy, Void, ProductMy[]> {

        @Override
        protected ProductMy[] doInBackground(ProductMy... myProducts) {
            ProductMy[] pm = new ProductMy[2];
            pm[0] = myProducts[0];

            ProductMy localPM = mProductMyDAO.findByFbRefKey(pm[0].getFbProductReferenceKey());
            if (localPM != null) {
                pm[1] = localPM;
            }
            return pm;
        }

        @Override
        protected void onPostExecute(ProductMy[] pm) {
            if (pm[1] == null) {
                ProductMy productMy = pm[0];
                AppExecutors.getInstance().diskIO().execute(()
                        -> mProductMyDAO.insertProductMy(productMy));
            } else {
                if (pm[0].getFbProductReferenceKey().equals(pm[1].getFbProductReferenceKey())) {
                    pm[0].setId(pm[1].getId());
                    if (!pm[0].toString().equals(pm[1].toString())) {
                        AppExecutors.getInstance().diskIO().execute(()
                                -> mProductMyDAO.updateProductMy(pm[0]));
                    }
                }
            }
        }
    }
}
