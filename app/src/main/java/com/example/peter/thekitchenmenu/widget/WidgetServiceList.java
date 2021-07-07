package com.example.peter.thekitchenmenu.widget;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
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

        private final Context context;
        private List<ProductEntity> products = Collections.emptyList();

        ListRemoteViewsFactory(Context applicationContext) {
            context = applicationContext;
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
            products = GsonUtils.productsJsonToList(PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getString(Constants.PRODUCT_KEY, null));
        }

        @Override
        public void onDestroy() {
            products = null;
        }

        @Override
        public int getCount() {
            return products == null ? 0 : products.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            ProductEntity currentProduct = products.get(position);

            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.product_list_item_widget);

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
                    String.valueOf(currentProduct.getBaseUnits()));

            views.setTextViewText(
                    R.id.list_item_product_widget_tv_unit_of_measure,
                    Converters.getUnitOfMeasureString
                            (context, currentProduct.getSubtype()));

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
