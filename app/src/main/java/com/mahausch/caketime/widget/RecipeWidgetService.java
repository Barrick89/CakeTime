package com.mahausch.caketime.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mahausch.caketime.Ingredient;
import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<Ingredient> mWidgetItems = new ArrayList<>();
        private String mRecipeName;
        private Context mContext;
        private int mAppWidgetId;
        private int mRecipeId;
        private ArrayList<Recipe> recipeList;


        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mRecipeId = intent.getIntExtra("recipeId", 0);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            recipeList = JsonUtils.getRecipesFromJson(mContext);
            if (recipeList != null) {
                mRecipeName = recipeList.get(mRecipeId).getName();
                mWidgetItems = recipeList.get(mRecipeId).getIngredients();
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            if (mWidgetItems != null) {
                return mWidgetItems.size() + 1;
            } else {
                return 0;
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {


            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            if (i == 0) {
                rv.setViewVisibility(R.id.widget_recipe_name, View.VISIBLE);
                rv.setTextViewText(R.id.widget_recipe_name, mRecipeName);
                rv.setViewVisibility(R.id.widget_ingredient, View.GONE);
            } else {
                rv.setViewVisibility(R.id.widget_ingredient, View.VISIBLE);
                rv.setTextViewText(R.id.widget_ingredient, mWidgetItems.get(i - 1).getIngredient());
                rv.setViewVisibility(R.id.widget_recipe_name, View.GONE);
            }

            Bundle extras = new Bundle();
            extras.putInt(RecipeWidgetProvider.EXTRA_ITEM, i);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widget_ingredients_list, fillInIntent);

            return rv;
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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
