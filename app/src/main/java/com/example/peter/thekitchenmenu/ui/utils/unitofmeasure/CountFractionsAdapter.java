package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.R;

import java.util.ArrayList;

public class CountFractionsAdapter extends ArrayAdapter<CountFraction> {

    private LayoutInflater inflater;
    private ArrayList<CountFraction> fractionsList;

    CountFractionsAdapter(@NonNull Context context, ArrayList<CountFraction> fractionsList) {
        super(context, 0, fractionsList);
        this.fractionsList = fractionsList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, parent);
    }

    @Override
    public View getDropDownView(int position,
                                @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return initView(position, parent);
    }

    private View initView(int position, ViewGroup parent) {

        View view = inflater.inflate(
                R.layout.list_item_spinner_count_fractions,
                parent,
                false);

        CountFraction fraction = fractionsList.get(position);
        String fractionString = view.getResources().getString(fraction.getStringResourceId());
        FractionToSpannableConverter converter = new FractionToSpannableConverter();

        TextView textView = view.findViewById(R.id.list_item_count_fraction);
        textView.setText(converter.getFractionSpannable(fractionString));

        return view;
    }
}
