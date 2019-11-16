package com.example.peter.thekitchenmenu.ui.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnitOfMeasureSpinnerAdapter extends ArrayAdapter<UnitOfMeasureSpinnerItem> {

    private LayoutInflater inflater;
    private ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureList;

    public UnitOfMeasureSpinnerAdapter(
            @NonNull Context context,
            ArrayList<UnitOfMeasureSpinnerItem> unitOfMeasureList) {

        super(context, 0, unitOfMeasureList);
        this.unitOfMeasureList = unitOfMeasureList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position,
                                @Nullable View convertView,
                                @NonNull ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        UnitOfMeasureSpinnerItem model = unitOfMeasureList.get(position);
        SpinnerItemType type = model.getType();

        switch (type) {

            case SECTION_HEADER:
                view = inflater.inflate(
                        R.layout.list_item_spinner_header,
                        parent,
                        false);

                TextView headerView = view.findViewById(R.id.list_item_spinner_header);

                if (headerView != null) {
                    headerView.setText(model.getMeasurementUnit());
                    headerView.setEnabled(false); // TODO - should not be able to select a header. Why is this not working?
                }
                break;

            case LIST_ITEM:
                view = inflater.inflate(
                        R.layout.list_item_spinner,
                        parent,
                        false);

                TextView measurementView = view.findViewById(R.id.list_item_spinner);
                if (measurementView != null) measurementView.setText(model.getMeasurementUnit());
                break;
        }
        return view;
    }
}
