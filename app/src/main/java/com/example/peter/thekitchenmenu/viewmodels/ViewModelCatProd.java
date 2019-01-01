package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.DmProdComm;
import com.example.peter.thekitchenmenu.data.entity.DmProdMy;
import com.example.peter.thekitchenmenu.data.model.VmProd;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * 'DM' in a field's name means data model whereas 'VM' means view model.
 */
public class ViewModelCatProd extends AndroidViewModel {

    private static final String TAG = "ViewModelCatProd";

    // View models.
    private MediatorLiveData<List<DmProdMy>> mObservableProdMys;
    private MediatorLiveData<List<DmProdComm>> mObservableProdComms;
    private MediatorLiveData<List<VmProd>> mVmLiveDataMatchMergeMyComm;

    // Mutable's.
    private MutableLiveData<String> mUserId;
    private MutableLiveData<Boolean> mIsCreator = new MutableLiveData<>();
    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<VmProd> mSelectedVmProd = new MutableLiveData<>();

    public ViewModelCatProd(Application application) {
        super(application);

        Repository repository = ((Singletons) application).getRepository();

        // Data models, set by default to null until we get data from the database.
        mObservableProdMys = new MediatorLiveData<>();
        mObservableProdMys.setValue(null);
        mObservableProdComms = new MediatorLiveData<>();
        mObservableProdComms.setValue(null);

        // View models
        mVmLiveDataMatchMergeMyComm = new MediatorLiveData<>();
        mVmLiveDataMatchMergeMyComm.setValue(null);

        // Request data
        mObservableProdMys.addSource(
                repository.getAllProdMys(), mObservableProdMys::setValue);

        mObservableProdComms.addSource(
                repository.getAllProdComm(), mObservableProdComms::setValue);

        // Retrieve constants
        mUserId = Constants.getUserId();

        initialiseDataSource();
    }

    // Initialises view model data source
    private void initialiseDataSource() {

        List<DmProdMy> mListProdMy = new ArrayList<>();
        List<DmProdComm> mListProdComm = new ArrayList<>();

        // Adds the data models to the mediator. Ensures all data sets have returned results
        // before processing data.
        mVmLiveDataMatchMergeMyComm.addSource(mObservableProdComms, dmProdComm -> {
            if (dmProdComm != null) {
                mListProdComm.addAll(dmProdComm);
                if (!mListProdMy.isEmpty()) {
                    mVmLiveDataMatchMergeMyComm.setValue(
                            mergeMatchMyComm(mListProdComm, mListProdMy));
                }
            }
        });

        mVmLiveDataMatchMergeMyComm.addSource(mObservableProdMys, dmProdMy -> {
            if (dmProdMy != null) {
                mListProdMy.addAll(dmProdMy);
                if (!mListProdComm.isEmpty()) {
                    mVmLiveDataMatchMergeMyComm.setValue(
                            mergeMatchMyComm(mListProdComm, mListProdMy));
                }
            }
        });
    }

    // Converts the data models into a VmProd view model data stream.
    public MediatorLiveData<List<VmProd>> getMatchVmProds() {
        return mVmLiveDataMatchMergeMyComm;
    }

    // TODO - Keeping these lists in virtual memory cannot be good - get all from the database
    // TODO - Make prodMy relational to prod comm
    // TODO - Will become clearer when Recipes functionality is added!!!
    // Matches and merges DmProdComms with DmProdMys into VmProds.
    // TODO - Use collections for a better sort algorithm here
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

    // Filters through only the DmProdMy data in the VmProd view model (My Products).
    public LiveData<List<VmProd>> getAllVmProdMy() {
        return Transformations.map(mVmLiveDataMatchMergeMyComm, this::filterMy);
    }

    // Filters the view model ProdMy data from the view model ProdComm data.
    private List<VmProd> filterMy(List<VmProd> allMyCommData) {
        List<VmProd> listVmP = new ArrayList<>();

        if (allMyCommData != null) {
            for (VmProd vMp : allMyCommData) {
                if(vMp.getProductMyId() != 0) {
                    listVmP.add(vMp);
                }
            }
        }
        return listVmP;
    }

    // Pushes the selected item to observers.
    public LiveData<VmProd> getSelected() {
        return mSelectedVmProd;
    }

    // Pushes changes to the user ID to observers.
    public MutableLiveData<String> getUserId() {
        return mUserId;
    }

    // Boolean that tells us if this user created the product being used.
    public MutableLiveData<Boolean> getIsCreator() {
        return mIsCreator;
    }

    // Triggered by selecting an item in the Fragment's RecyclerView.
    public void selectedItem(VmProd vmProd, boolean isCreator) {
        this.mIsCreator.setValue(isCreator);
        mSelectedVmProd.setValue(vmProd);
    }
}