package com.example.peter.thekitchenmenu.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.peter.thekitchenmenu.model.ProductCommunity;
import com.example.peter.thekitchenmenu.model.ProductMy;

import java.util.List;

/**
 * The 'single source of truth' for the app data
 * See <a href="https://developer.android.com/jetpack/docs/guide">Guide to app architecture</a>
 */
public class Repository {

    private static final String LOG_TAG = Repository.class.getSimpleName();

    private RepositoryRoom mRepositoryRoom;
    private RepositoryFirebase mRepositoryFirebase;
    private LiveData<List<ProductCommunity>> mListCommunityProducts;
    private LiveData<List<ProductMy>> mListMyProducts;

    public Repository(Application application) {
        mRepositoryRoom = new RepositoryRoom(application, this);
        mRepositoryFirebase = new RepositoryFirebase(this);
    }

    /**
     * Informs the Firebase repository to start and stop listening to {@link ProductCommunity}
     * data.
     * @param isLive True, to start listening, false to stop.
     *               If this data is used by a ViewModel, this value should be set to true in
     *               onActive() and false onInactive().
     */
    public void productCommunityIsLive(boolean isLive) {
        mRepositoryFirebase.isLiveProductCommunityList(isLive);
    }

    /**
     * @return a list of all community products stored in the local database.
     */
    public LiveData<List<ProductCommunity>> listAllCommunityProducts() {
        mListCommunityProducts = mRepositoryRoom.listAllCommunityProducts();
        return mListCommunityProducts;
    }

    /**
     * Takes an incoming product from the remote repository and synchronises it with the local
     * repository. The remote product has priority.
     * @param productCommunity the incoming product.
     */
    void synchroniseCommunityProduct(ProductCommunity productCommunity) {
        mRepositoryRoom.synchroniseCommunityProduct(productCommunity);
    }

    /**
     * Turns on and off remote data sync for the MyProducts location
     * @param isLive Turns remote data sync on if true, off id false.
     * @param userId The current users user ID for the remote data source.
     */
    public void productMyIsLive(boolean isLive, String userId) {
        mRepositoryFirebase.isLiveProductMyList(isLive, userId);
    }

    /**
     * Provides a list of all local data sroted in the MyProducts location
     * @return a list od products stored in the MyProducts location.
     */
    public LiveData<List<ProductMy>> listAllMyProducts() {
        mListMyProducts = mRepositoryRoom.listAllMyProducts();
        return mListMyProducts;
    }

    /**
     * Takes an incoming product from the remote repository and synchronises it with the local
     * repository. The remote product has priority.
     * @param productMy the incoming remote product. Remote has priority.
     */
    void synchroniseProductMy(ProductMy productMy) {
        mRepositoryRoom.synchroniseProductMy(productMy);
    }
}
