package com.example.peter.thekitchenmenu.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.data.model.ProductMy;

import java.util.List;

/**
 * The 'single source of truth' for the app data
 */
public class Repository {

    private static final String LOG_TAG = Repository.class.getSimpleName();

    private RepositoryLocal mRepoLocal;
    private RepositoryRemote mRepoRemote;
    private LiveData<List<ProductCommunity>> mGetAllProdComms;
    private LiveData<List<ProductCommunity>> mGetProdCommsByIdArray;
    private LiveData<List<ProductMy>> mGetAllProdMys;

    public Repository(Application application) {
        mRepoLocal = new RepositoryLocal(application);
        mRepoRemote = new RepositoryRemote(this);
    }

    /**
     * Informs the remote repository to start and stop listening to {@link ProductCommunity}
     * data.
     * @param isLive True, to start listening, false to stop.
     *               If this data is used by a ViewModel, this value should be set to true in
     *               onActive() and false onInactive().
     */
    public void isLiveProdComm(boolean isLive) {
        mRepoRemote.isLiveProdComm(isLive);
    }

    /**
     * Gets a list of all {@link ProductCommunity} objects from teh local database
     * @return a list of all {@link ProductCommunity} stored in the local database.
     */
    public LiveData<List<ProductCommunity>> getAllProdComms() {
        mGetAllProdComms = mRepoLocal.getAllProdComms();
        return mGetAllProdComms;
    }

    /**
     * Gets a list of {@link ProductCommunity} objects given an array of ID's
     * @param idList an array of id's to fetch.
     * @return a list of {@link ProductCommunity} where id's match the id's in the input array.
     */
    public LiveData<List<ProductCommunity>> getProdCommsByIdArray(int[] idList) {
        mGetProdCommsByIdArray = mRepoLocal.getProdCommsByIdArray(idList);
        return mGetProdCommsByIdArray;
    }

    /**
     * Takes a remote {@link ProductCommunity} and synchronises it with the local repository.
     * The remote object has priority.
     * @param prodComm the remote {@link ProductCommunity}
     */
    void syncProdComm(ProductCommunity prodComm) {
        mRepoLocal.remoteSyncProdComm(prodComm);
    }

    /**
     * Gets a single {@link ProductCommunity} object given its ID
     * @param id the id of the {@link ProductCommunity} object to return.
     * @return the requested object.
     */
    public LiveData<ProductCommunity> getProdCommById(int id){
        return mRepoLocal.getProdCommById(id);
    }

    /**
     * Turns on and off remote data sync for the MyProducts location
     * @param isLive Turns remote data sync on if true, off id false.
     * @param userId The current users user ID for the remote data source.
     */
    public void IsLiveProdMy(boolean isLive, String userId) {
        mRepoRemote.isLiveProdMy(isLive, userId);
    }

    /**
     * Gets a list of all {@link ProductMy} objects from the local database
     * @return a list of {@link ProductMy} objects.
     */
    public LiveData<List<ProductMy>> getAllProdMys() {
        mGetAllProdMys = mRepoLocal.getAllProdMys();
        return mGetAllProdMys;
    }

    /**
     * Takes an incoming {@link ProductMy} from the remote repository and synchronises it with the
     * local repository. The remote {@link ProductMy} has priority.
     * @param productMy the incoming remote product.
     */
    void remoteSyncProdMy(ProductMy productMy) {
        mRepoLocal.remoteSyncProdMy(productMy);
    }
}
