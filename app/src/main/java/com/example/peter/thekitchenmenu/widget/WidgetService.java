package com.example.peter.thekitchenmenu.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class WidgetService extends IntentService {

    /* Define the actions the intent service can handle */
    public static final String ACTION_UPDATE_WIDGET = "com.example.peter.thekitchenmenu.update_widget";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public WidgetService() {
        super("WidgetService");
    }

    /* Updates any and all widgets */
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    /* To handle the intent we need to extract the action and handle each action separately */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_UPDATE_WIDGET.equals(action)){
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = widgetManager.getAppWidgetIds(
                new ComponentName(this, TKMWidgetProvider.class));

        TKMWidgetProvider.updateAppWidgets(this, widgetManager, appWidgetIds);
    }
}
