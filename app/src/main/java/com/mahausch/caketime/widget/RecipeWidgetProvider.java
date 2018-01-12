package com.mahausch.caketime.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.utils.JsonUtils;

import java.util.ArrayList;

public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "extraItem";
    public static final String PREVIOUS_ARROW_CLICKED = "PREVIOUS_ARROW_CLICKED";
    public static final String NEXT_ARROW_CLICKED = "NEXT_ARROW_CLICKED";

    public static int mRecipeId;
    public static ArrayList<Recipe> mRecipeList;
    private Context mContext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        mContext = context;

        if (mRecipeList == null || mRecipeList.isEmpty()) {
            new RecipeTask().execute();
        }

        if (mRecipeList != null || !mRecipeList.isEmpty()) {
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

    class RecipeTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            if (mRecipeList == null || mRecipeList.isEmpty()) {
                try {
                    mRecipeList = JsonUtils.getRecipesFromJson(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}
