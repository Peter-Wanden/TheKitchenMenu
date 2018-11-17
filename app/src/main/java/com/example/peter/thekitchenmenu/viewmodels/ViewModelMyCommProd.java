package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.DmProdComm;
import com.example.peter.thekitchenmenu.data.model.DmProdMy;
import com.example.peter.thekitchenmenu.data.model.VmProd;
import com.example.peter.thekitchenmenu.data.repository.Repository;

/**
 * 'DM' in a field's name means data model whereas 'VM' means view model.
 */
public class ViewModelMyCommProd extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelMyCommProd.class.getSimpleName();

    private Repository mRepository;

    // Data models.
    private LiveData<List<DmProdComm>> mDmGetAllProdComm;
    private LiveData<List<DmProdMy>> mDmGetAllProdMy;

    // View models.
    private MediatorLiveData<List<VmProd>> mVmLiveDataMatchMergeMyComm = new MediatorLiveData<>();

    // Mutable primitives.
    private MutableLiveData<String> mUserId;
    private boolean isCreator;

    // TODO - Might be better using Queue as tha data is live.
    private List<DmProdMy> mProdMys = new ArrayList<>();
    private List<DmProdComm> mProdComms = new ArrayList<>();
    private List<VmProd> mVmProdMyComm = new ArrayList<>();

    // The item in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<VmProd> mSelectedVmProd = new MutableLiveData<>();

    public ViewModelMyCommProd(Application application) {
        super(application);
        mRepository = new Repository(application);
        mUserId = Constants.getUserId();
        mDmGetAllProdComm = mRepository.getAllProdComms();
        mDmGetAllProdMy = mRepository.getAllProdMys();
    }

    // Converts the DmProdComm and DmProdMy data streams into a VmProd data stream.
    // Where a DmProdComm and DmProdMy match, they are merged.
    // TODO - DELIVERING TWO SETS OF RESULTS - When getAllVmProdMy() and getMatchVmProdsMyComm() attached.
    public MediatorLiveData<List<VmProd>> getMatchVmProdsMyComm() {

        mVmLiveDataMatchMergeMyComm.addSource(mDmGetAllProdComm, dmProdComms -> {
            if (dmProdComms != null) {
                mProdComms.addAll(dmProdComms);
            }
            mergeMatchMyComm();
        });

        mVmLiveDataMatchMergeMyComm.addSource(mDmGetAllProdMy, dmProdMys -> {
            if (dmProdMys != null) {
                mProdMys.addAll(dmProdMys);
            }
            mergeMatchMyComm();
        });

        return mVmLiveDataMatchMergeMyComm;
    }

    // Converts Matches and merges DmProdComms with DmProdMys into VmProds.
    private void mergeMatchMyComm() {
        mVmProdMyComm.clear();
        for (DmProdComm dmPc : mProdComms) {
            VmProd vmp = null;
            for(DmProdMy dmPm : mProdMys) {
                if (dmPc.getId() == dmPm.getCommunityProductId()) {
                    vmp = new VmProd(dmPm, dmPc);
                    mVmProdMyComm.add(vmp);
                }
            }
            if(vmp == null) {
                vmp = new VmProd(dmPc);
                mVmProdMyComm.add(vmp);
            }
        }
        mVmLiveDataMatchMergeMyComm.setValue(mVmProdMyComm);
    }

    // Returns only the DmProdMy data in a VmProd view model.
    public LiveData<List<VmProd>> getAllVmProdMy() {

        return Transformations.map(mVmLiveDataMatchMergeMyComm, this::filterMy);
    }

    // Filters the view model ProdMy data from the view model ProdComm data
    private List<VmProd> filterMy(List<VmProd> allMyCommData) {

        List<VmProd> listVmP = new ArrayList<>();

        for (VmProd vMp : allMyCommData) {

            if(vMp.getProductMyId() != 0) {
                listVmP.add(vMp);
            }
        }
        return listVmP;
    }

    // Pushes the selected item to observers.
    public LiveData<VmProd> getSelected() {
        return mSelectedVmProd;
    }

    // Turns remote data sync on for all data model objects used by this class
    public void setRemoteSyncEnabled(boolean syncEnabled) {

        mRepository.isLiveProdComm(syncEnabled);
        mRepository.IsLiveProdMy(syncEnabled, this.mUserId.getValue());
    }

    // Pushes changes to the user ID to observers
    public MutableLiveData<String> getUserId() {

        return mUserId;
    }

    // Triggered by selecting an item in the Fragment's RecyclerView.
    public void selectedItem(VmProd vmProd, boolean isCreator) {

        Log.i(LOG_TAG, "--- ITEM SELECTED: " + vmProd.getDescription());
        this.isCreator = isCreator;
        mSelectedVmProd.setValue(vmProd);
    }
}