package com.example.peter.thekitchenmenu.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.catalog.product.ProductCatalogActivity;

/**
 * Implementation of App Widget functionality.
 */
public class TKMWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = getRemoteViews(context);

        /* Remove the empty view */
        remoteViews.setViewVisibility(R.id.tmkwidget_provider_tv_empty_view, View.GONE);

        // Set the title in the widget
        remoteViews.setTextViewText(
                R.id.tmkwidget_provider_product_title,
                context.getString(R.string.widget_title_latest_products));

        // Runs onDataSetChanged() in the WidgetServiceList.class (the adapter for the Ingredients)
        appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId, R.id.tkmwidget_provider_lv_products);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    // Cycles through available widgets and updates them
    public static void updateAppWidgets(Context context, AppWidgetManager widgetManager,
                                        int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, widgetManager, appWidgetId);
        }
    }

    private static RemoteViews getRemoteViews (Context context) {

        // Construct the RemoteViews object to update the widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.tkmwidget_provider);

        // Set the WidgetServiceList intent to act as the adapter for the ListView
        Intent intentAdapter = new Intent(context, WidgetServiceList.class);
        remoteViews.setRemoteAdapter(R.id.tkmwidget_provider_lv_products, intentAdapter);

        // An intent that launches the MainActivity when the widget button is clicked.
        Intent intentActivity = new Intent(context, ProductCatalogActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intentActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.tmkwidget_provider_add_button, pendingIntent);

        return remoteViews;
    }

    /*
     * Called each time a new widget is created and each update period as set in
     * recipe_widget_provider_info.xml - android:updatePeriodMillis="86400000"
     *
     * The AppWidgetManager class gives information regarding all existing widgets, it's also
     * how you access and force an update to available widgets.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // To update a widget you need to pass in its ID along with a RemoteViews object
        // describing the widget.
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // There is no more widget so deleteByFavoriteProductId the recipe info from shared preferences
        for (int widgetId : appWidgetIds) {
            context.getSharedPreferences(String.valueOf(widgetId), Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
        }
    }
}

