package com.example.peter.thekitchenmenu.ui.utils;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.R;

import java.util.ArrayList;

public class CountFractionsAdapter extends ArrayAdapter<CountFractionsItemModel> {

    private static final String TAG = "tkm-" + CountFractionsAdapter.class.getSimpleName() + " ";

    private LayoutInflater inflater;
    private ArrayList<CountFractionsItemModel> fractionsList;

    public CountFractionsAdapter(
            @NonNull Context context,
            ArrayList<CountFractionsItemModel> fractionsList) {

        super(context, 0, fractionsList);
        this.fractionsList = fractionsList;
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

    private View initView(int position, View view, ViewGroup parent) {

        CountFractionsItemModel model = fractionsList.get(position);

        view = inflater.inflate(
                R.layout.list_item_spinner_count_fractions,
                parent,
                false);

        SpannableStringBuilder spannable = new SpannableStringBuilder();
        spannable.append(Html.fromHtml(
                model.getFractionAsHtml(), Html.FROM_HTML_MODE_LEGACY));

        System.out.println(TAG + model.getFractionAsHtml());

        TextView textView = view.findViewById(R.id.list_item_count_fraction);
        textView.setText(spannable);

        return view;
    }
}
