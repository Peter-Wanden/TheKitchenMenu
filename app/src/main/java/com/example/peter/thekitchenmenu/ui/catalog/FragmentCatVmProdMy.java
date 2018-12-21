package com.example.peter.thekitchenmenu.ui.catalog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;

import com.example.peter.thekitchenmenu.data.model.VmProd;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatProd;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentCatVmProdMy
        extends Fragment
        implements OnClickVmProd {

    private static final String TAG = "FragmentCatVmProdMy";

    private AdapterCatProdComm mAdapterCatProd;
    private ViewModelCatProd mViewModelProdCommMy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapterCatProd = new AdapterCatProdComm(getActivity(), this);

        mViewModelProdCommMy = ViewModelProviders.of(getActivity()).get(ViewModelCatProd.class);

        // Observes changes to view model ProdMy list and passes them to the adaptor.
        final Observer<List<VmProd>> viewModelProd = vmProds
                -> mAdapterCatProd.setProducts(vmProds);

        mViewModelProdCommMy.getAllVmProdMy().observe(this, viewModelProd);

        // Observes changes to the user ID state and passes them to the adaptor.
        final Observer<String> userIdObserver = newUserId
                -> {
            if (newUserId !=null && !newUserId.equals(ANONYMOUS)) {
                mAdapterCatProd.setUserId(newUserId);
            }
        };
        mViewModelProdCommMy.getUserId().observe(this, userIdObserver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentCatalogProductsBinding mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new
                    GridLayoutManager(getActivity().
                    getApplicationContext(), columnCalculator());

            mBinding.fragmentCatalogProductsRv.
                    setLayoutManager(gridManager);
        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(getActivity().getApplicationContext(),
                    RecyclerView.VERTICAL, false);

            mBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(linearManager);
        }

        mBinding.fragmentCatalogProductsRv.setHasFixedSize(true);

        mBinding.fragmentCatalogProductsRv.setAdapter(mAdapterCatProd);

        return mBinding.getRoot();
    }

    /**
     * Screen width column calculator
     */
    private int columnCalculator() {

        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity())
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;

        return columns;
    }

    @Override
    public void onClick(VmProd vmProd, boolean isCreator) {
        // TODO - update ViewModel to take these values
        mViewModelProdCommMy.selectedItem(vmProd, isCreator);
    }
}
