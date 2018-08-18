package com.example.peter.thekitchenmenu.widget;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.example.peter.thekitchenmenu.utils.GsonUtils;

import java.util.Collections;
import java.util.List;

public class WidgetServiceList
        extends
        RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    class ListRemoteViewsFactory
            implements
            RemoteViewsService.RemoteViewsFactory {

        private final Context mContext;
        private List<Product> mProducts = Collections.emptyList();

        ListRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {
        }

        /*
        Called onStart() and once the remote views factory is created, as well as every time it
        is notified to update its data calling notifyAppWidgetViewDataChanged().
        */
        @Override
        public void onDataSetChanged() {
            // Get the JSON string containing the list of ingredients from shared preferences
            mProducts = GsonUtils.productsJsonToList(PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(Constants.PRODUCT_KEY, null));
        }

        @Override
        public void onDestroy() {
            mProducts = null;
        }

        @Override
        public int getCount() {
            return mProducts == null ? 0 : mProducts.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Product currentProduct = mProducts.get(position);

            RemoteViews views = new RemoteViews(
                    mContext.getPackageName(),
                    R.layout.list_item_product_widget);

            /* Set the TextView text color */
            views.setTextColor(R.id.list_item_product_widget_tv_description,
                    getResources().getColor(R.color.white));

            views.setTextColor(R.id.list_item_product_widget_tv_pack_size,
                    getResources().getColor(R.color.white));

            views.setTextColor(R.id.list_item_product_widget_tv_unit_of_measure,
                    getResources().getColor(R.color.white));

            /* Set the TextView text */
            views.setTextViewText(
                    R.id.list_item_product_widget_tv_description,
                    currentProduct.getDescription());

            views.setTextViewText(
                    R.id.list_item_product_widget_tv_pack_size,
                    String.valueOf(currentProduct.getPackSize()));

            views.setTextViewText(
                    R.id.list_item_product_widget_tv_unit_of_measure,
                    Converters.getUnitOfMeasureString
                            (mContext, currentProduct.getUnitOfMeasure()));

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
