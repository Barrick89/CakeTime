package com.mahausch.caketime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.adapters.RecipeAdapter;
import com.mahausch.caketime.utils.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

// Activity to show a recyclerView with recipes

public class RecipeOverviewActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recycler_view)
    RecyclerView recycler;

    RecyclerView.LayoutManager mManager;
    static Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        ButterKnife.bind(this);

        ArrayList<Recipe> list = JsonUtils.getRecipesFromJson(this);

        //Decide whether to display recipes in grid or list depending on display size
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (dpWidth >= 600) {
            mManager = new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        } else {
            mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
        recycler.setLayoutManager(mManager);

        RecipeAdapter adapter = new RecipeAdapter(this);
        adapter.setRecipesList(list);
        recycler.setAdapter(adapter);

    }

    //Decide how many columns to display depending on display size
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void onClick(Recipe recipeData) {

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipeData);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = mManager.onSaveInstanceState();
        outState.putParcelable("listState", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mListState = savedInstanceState.getParcelable("listState");
    }
}
