package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.app.Singletons;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ViewModelCatlogProducts extends AndroidViewModel {

    private static final String TAG = "ViewModelCatlogProducts";

    // View models.
    private MediatorLiveData<List<ProductUserDataEntity>> observableProdMys;
    private MediatorLiveData<List<ProductEntity>> observableProducts;
    private MediatorLiveData<List<ProductModel>> observeMatchMergeCommunityAndMyProducts;

    // Mutable's.
    private MutableLiveData<String> observableUserId;
    private MutableLiveData<Boolean> isCreator = new MutableLiveData<>();
    // The item selected in the adapter, passed through the click interface of the fragment.
    private final MutableLiveData<ProductModel> selectedProduct = new MutableLiveData<>();

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

        List<ProductUserDataEntity> mListProdMy = new ArrayList<>();
        List<ProductEntity> mListProdComm = new ArrayList<>();

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
    public MediatorLiveData<List<ProductModel>> getMatchVmProds() {
        return observeMatchMergeCommunityAndMyProducts;
    }

    // TODO - Keeping these lists in virtual memory cannot be good - get all from the database
    // TODO - Will become clearer when Recipes functionality is added!!!
    // Matches and merges Products with UserProductData into ProductModel.
    // TODO - Use collections for a better sort algorithm here
    private List<ProductModel> mergeMatchMyComm(List<ProductEntity> listDmPc, List<ProductUserDataEntity> listDmPm) {

        List<ProductModel> listVmP = new ArrayList<>();

        for (ProductEntity dmPc : listDmPc) {
            ProductModel vmp = null;

            for(ProductUserDataEntity dmPm : listDmPm) {
                if (dmPc.getId() == dmPm.getProductId()) {
                    vmp = new ProductModel(dmPm, dmPc);
                    listVmP.add(vmp);
                }
            }

            if(vmp == null) {
                vmp = new ProductModel(dmPc);
                listVmP.add(vmp);
            }
        }
        return listVmP;
    }

    // Filters through only the ProductUserDataEntity data in the ProductModel view model (My Products).
    public LiveData<List<ProductModel>> getAllVmProdMy() {
        return Transformations.map(observeMatchMergeCommunityAndMyProducts, this::filterMy);
    }

    // Filters the view model ProdMy data from the view model ProdComm data.
    private List<ProductModel> filterMy(List<ProductModel> allMyCommData) {
        List<ProductModel> listVmP = new ArrayList<>();

        if (allMyCommData != null) {
            for (ProductModel vMp : allMyCommData) {
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
    public void selectedItem(ProductModel productModel, boolean isCreator) {
        this.isCreator.setValue(isCreator);
        selectedProduct.setValue(productModel);
    }

    // Pushes the selected product_uneditable to observers.
    public LiveData<ProductModel> getSelected() {
        return selectedProduct;
    }
}