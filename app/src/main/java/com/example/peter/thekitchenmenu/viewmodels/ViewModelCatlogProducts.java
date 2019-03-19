package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.entity.UsersProductData;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ViewModelCatlogProducts extends AndroidViewModel {

    private static final String TAG = "ViewModelCatlogProducts";

    // View models.
    private MediatorLiveData<List<UsersProductData>> observableProdMys;
    private MediatorLiveData<List<Product>> observableProducts;
    private MediatorLiveData<List<ObservableProductModel>> observeMatchMergeCommunityAndMyProducts;

    // Mutable's.
    private MutableLiveData<String> observableUserId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();
    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ObservableProductModel> selectedProduct = new MutableLiveData<>();

    public ViewModelCatlogProducts(Application application) {
        super(application);
        setupObservables(application);
        initialiseDataSource();
    }

    private void setupObservables(Application application) {

        Repository repository = ((Singletons) application).getRepository();

        // Data models.
        observableProdMys = new MediatorLiveData<>();
        observableProdMys.setValue(null);
        observableProducts = new MediatorLiveData<>();
        observableProducts.setValue(null);

        // View models.
        observeMatchMergeCommunityAndMyProducts = new MediatorLiveData<>();
        observeMatchMergeCommunityAndMyProducts.setValue(null);

        // Request data
        observableProdMys.addSource(repository.getAllUserProductData(), observableProdMys::setValue);
        observableProducts.addSource(repository.getAllProducts(), observableProducts::setValue);

        // Retrieve constants
        observableUserId = Constants.getUserId();
    }

    private void initialiseDataSource() {

        List<UsersProductData> mListProdMy = new ArrayList<>();
        List<Product> mListProdComm = new ArrayList<>();

        // Adds the data models to the mediator. Ensures all data sets have returned results
        // before processing data.
        observeMatchMergeCommunityAndMyProducts.addSource(observableProducts, dmProdComm -> {
            if (dmProdComm != null) {
                mListProdComm.addAll(dmProdComm);
                if (!mListProdMy.isEmpty()) {
                    observeMatchMergeCommunityAndMyProducts.setValue(
                            mergeMatchMyComm(mListProdComm, mListProdMy));
                }
            }
        });

        observeMatchMergeCommunityAndMyProducts.addSource(observableProdMys, dmProdMy -> {
            if (dmProdMy != null) {
                mListProdMy.addAll(dmProdMy);
                if (!mListProdComm.isEmpty()) {
                    observeMatchMergeCommunityAndMyProducts.setValue(
                            mergeMatchMyComm(mListProdComm, mListProdMy));
                }
            }
        });
    }

    // Converts data models into view models.
    public MediatorLiveData<List<ObservableProductModel>> getMatchVmProds() {
        return observeMatchMergeCommunityAndMyProducts;
    }

    // TODO - Keeping these lists in virtual memory cannot be good - get all from the database
    // TODO - Will become clearer when Recipes functionality is added!!!
    // Matches and merges Products with UserProductData into ObservableProductModel.
    // TODO - Use collections for a better sort algorithm here
    private List<ObservableProductModel> mergeMatchMyComm(List<Product> listDmPc, List<UsersProductData> listDmPm) {

        List<ObservableProductModel> listVmP = new ArrayList<>();

        for (Product dmPc : listDmPc) {
            ObservableProductModel vmp = null;

            for(UsersProductData dmPm : listDmPm) {
                if (dmPc.getId() == dmPm.getProductId()) {
                    vmp = new ObservableProductModel(dmPm, dmPc);
                    listVmP.add(vmp);
                }
            }

            if(vmp == null) {
                vmp = new ObservableProductModel(dmPc);
                listVmP.add(vmp);
            }
        }
        return listVmP;
    }

    // Filters through only the UsersProductData data in the ObservableProductModel view model (My Products).
    public LiveData<List<ObservableProductModel>> getAllVmProdMy() {
        return Transformations.map(observeMatchMergeCommunityAndMyProducts, this::filterMy);
    }

    // Filters the view model ProdMy data from the view model ProdComm data.
    private List<ObservableProductModel> filterMy(List<ObservableProductModel> allMyCommData) {
        List<ObservableProductModel> listVmP = new ArrayList<>();

        if (allMyCommData != null) {
            for (ObservableProductModel vMp : allMyCommData) {
                if(vMp.getUserProductDataId() != 0) {
                    listVmP.add(vMp);
                }
            }
        }
        return listVmP;
    }

    // Pushes changes to the user ID to observers.
    public MutableLiveData<String> getUserId() {
        return observableUserId;
    }
    // Boolean that tells us if this user created the product_uneditable being used.
    public MutableLiveData<Boolean> getIsCreator() {
        return isCreator;
    }

    // Triggered by selecting an item in the Fragment's RecyclerView.
    public void selectedItem(ObservableProductModel observableProductModel, boolean isCreator) {
        this.isCreator.setValue(isCreator);
        selectedProduct.setValue(observableProductModel);
    }

    // Pushes the selected product_uneditable to observers.
    public LiveData<ObservableProductModel> getSelected() {
        return selectedProduct;
    }
}