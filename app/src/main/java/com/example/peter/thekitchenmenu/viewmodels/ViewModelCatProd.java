package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

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
public class ViewModelCatProd extends AndroidViewModel {

    private static final String LOG_TAG = ViewModelCatProd.class.getSimpleName();

    private Repository mRepository;

    // View models.
    private MediatorLiveData<List<VmProd>> mVmLiveDataMatchMergeMyComm = new MediatorLiveData<>();
    // Mutable primitives.
    private MutableLiveData<String> mUserId;
    private MutableLiveData<Boolean> mIsCreator;
    // The item in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<VmProd> mSelectedVmProd = new MutableLiveData<>();

    public ViewModelCatProd(Application application) {
        super(application);
        mRepository = new Repository(application);
        mUserId = Constants.getUserId();
        initialiseDataSource();
    }

    // Initialises the data sources for this view model
    private void initialiseDataSource() {

        // Data models.
        LiveData<List<DmProdComm>> mDmGetAllProdComm = mRepository.getAllProdComms();
        LiveData<List<DmProdMy>> mDmGetAllProdMy = mRepository.getAllProdMys();

        List<DmProdMy> mListProdMy = new ArrayList<>();
        List<DmProdComm> mListProdComm = new ArrayList<>();

        // Adds the source objects to the mediator. Ensures all data sets have returned results
        // before processing data.
        mVmLiveDataMatchMergeMyComm.addSource(mDmGetAllProdComm, dmProdComms -> {
            if (dmProdComms != null) {
                mListProdComm.addAll(dmProdComms);
                if (!mListProdMy.isEmpty()) {
                    mVmLiveDataMatchMergeMyComm.setValue(
                            mergeMatchMyComm(mListProdComm, mListProdMy));
                }
            }
        });

        mVmLiveDataMatchMergeMyComm.addSource(mDmGetAllProdMy, dmProdMys -> {
            if (dmProdMys != null) {
                mListProdMy.addAll(dmProdMys);
                if (!mListProdComm.isEmpty()) {
                    mVmLiveDataMatchMergeMyComm.setValue(
                            mergeMatchMyComm(mListProdComm, mListProdMy));
                }
            }
        });
    }

    // Converts the DmProdComm and DmProdMy data models into a VmProd data stream.
    public MediatorLiveData<List<VmProd>> getMatchVmProdsMyComm() {
        return mVmLiveDataMatchMergeMyComm;
    }

    // Converts Matches and merges DmProdComms with DmProdMys into VmProds.
    private List<VmProd> mergeMatchMyComm(List<DmProdComm> listDmPc, List<DmProdMy> listDmPm) {

        List<VmProd> listVmP = new ArrayList<>();

        for (DmProdComm dmPc : listDmPc) {
            VmProd vmp = null;

            for(DmProdMy dmPm : listDmPm) {
                if (dmPc.getId() == dmPm.getCommunityProductId()) {
                    vmp = new VmProd(dmPm, dmPc);
                    listVmP.add(vmp);
                }
            }

            if(vmp == null) {
                vmp = new VmProd(dmPc);
                listVmP.add(vmp);
            }
        }
        return listVmP;
    }

    // Returns only the DmProdMy data in the VmProd view model.
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

    public MutableLiveData<Boolean> getIsCreator() {
        return mIsCreator;
    }

    // Triggered by selecting an item in the Fragment's RecyclerView.
    public void selectedItem(VmProd vmProd, boolean isCreator) {
        this.mIsCreator.setValue(isCreator);
        mSelectedVmProd.setValue(vmProd);
    }
}