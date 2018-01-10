package com.mahausch.caketime.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.mahausch.caketime.R;

public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "extraItem";
    public static final String PREVIOUS_ARROW_CLICKED = "PREVIOUS_ARROW_CLICKED";
    public static final String NEXT_ARROW_CLICKED = "NEXT_ARROW_CLICKED";

    public static int mRecipeId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; ++i) {

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.putExtra("recipeId", mRecipeId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            rv.setRemoteAdapter(appWidgetIds[i], R.id.widget_ingredients_list, intent);

            rv.setOnClickPendingIntent(R.id.widget_left_arrow, getPendingSelfIntent(context, PREVIOUS_ARROW_CLICKED, i));
            rv.setOnClickPendingIntent(R.id.widget_right_arrow, getPendingSelfIntent(context, NEXT_ARROW_CLICKED, i));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int widgetId) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("widgetId", widgetId);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
        ComponentName component = new ComponentName(context,
                RecipeWidgetProvider.class);
        int[] widgetId = AppWidgetManager.getInstance(context).getAppWidgetIds(component);

        if (PREVIOUS_ARROW_CLICKED.equals(intent.getAction())) {
            if (mRecipeId == 0) {
                mRecipeId = 3;
            } else {
                mRecipeId -= 1;
            }

        } else if (NEXT_ARROW_CLICKED.equals(intent.getAction())) {

            if (mRecipeId == 3) {
                mRecipeId = 0;
            } else {
                mRecipeId += 1;
            }
        }
        onUpdate(context, AppWidgetManager.getInstance(context), widgetId);
    }
}
