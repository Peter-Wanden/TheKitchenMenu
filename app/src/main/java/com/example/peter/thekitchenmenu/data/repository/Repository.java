package com.example.peter.thekitchenmenu.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.DmProdMy;

import java.util.List;

/**
 * The 'single source of truth' for the app data
 */
public class Repository {

    private static final String LOG_TAG = Repository.class.getSimpleName();

    private RepositoryLocal mRepoLocal;
    private RepositoryRemote mRepoRemote;
    private LiveData<List<DmProdComm>> mGetAllProdComms;
    private LiveData<List<DmProdComm>> mGetProdCommsByIdArray;
    private LiveData<List<DmProdMy>> mGetAllProdMys;
    private LiveData<DmProdMy> mGetProdMyById;

    public Repository(Application application) {
        mRepoLocal = new RepositoryLocal(application);
        mRepoRemote = new RepositoryRemote(this);
    }

    /**
     * Informs the remote repository to start and stop listening to {@link DmProdComm}
     * data.
     * @param isLive True, to start listening, false to stop.
     *               If this data is used by a ViewModel, this value should be set to true in
     *               onActive() and false onInactive().
     */
    public void isLiveProdComm(boolean isLive) {
        mRepoRemote.isLiveProdComm(isLive);
    }

    /**
     * Gets a list of all {@link DmProdComm} objects from teh local database
     * @return a list of all {@link DmProdComm} stored in the local database.
     */
    public LiveData<List<DmProdComm>> getAllProdComms() {
        mGetAllProdComms = mRepoLocal.getAllProdComms();
        return mGetAllProdComms;
    }

    /**
     * Gets a list of {@link DmProdComm} objects given an array of ID's
     * @param idList an array of id's to fetch.
     * @return a list of {@link DmProdComm} where id's match the id's in the input array.
     */
    public LiveData<List<DmProdComm>> getProdCommsByIdArray(int[] idList) {
        mGetProdCommsByIdArray = mRepoLocal.getProdCommsByIdArray(idList);
        return mGetProdCommsByIdArray;
    }

    /**
     * Takes a remote {@link DmProdComm} and synchronises it with the local repository.
     * The remote object has priority.
     * @param dmProdComm the remote {@link DmProdComm}
     */
    void syncProdComm(DmProdComm dmProdComm) {
        mRepoLocal.remoteSyncProdComm(dmProdComm);
    }

    /**
     * Gets a single {@link DmProdComm} object given its ID
     * @param id the id of the {@link DmProdComm} object to return.
     * @return the requested object.
     */
    public LiveData<DmProdComm> getProdCommById(int id){
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
     * Gets a list of all {@link DmProdMy} objects from the local database
     * @return a list of {@link DmProdMy} objects.
     */
    public LiveData<List<DmProdMy>> getAllProdMys() {
        mGetAllProdMys = mRepoLocal.getAllProdMys();
        return mGetAllProdMys;
    }

    /**
     * Gets a single {@link DmProdMy} by ID
     * @param id the local database id of the object to return
     * @return the requested DmProdMy object.
     */
    public LiveData<DmProdMy> getProdMyById(int id) {
        mGetProdMyById = mRepoLocal.getProdMyById(id);
        return mGetProdMyById;
    }

    public DmProdMy getProdMyByCommId(int commId) {
        return mRepoLocal.getProdMyByCommId(commId);
    }

    /**
     * Takes an incoming {@link DmProdMy} from the remote repository and synchronises it with the
     * local repository. The remote {@link DmProdMy} has priority.
     * @param dmProdMy the incoming remote product.
     */
    void remoteSyncProdMy(DmProdMy dmProdMy) {
        mRepoLocal.remoteSyncProdMy(dmProdMy);
    }
}
