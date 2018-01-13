package com.mahausch.caketime.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mahausch.caketime.IdlingResource.SimpleIdlingResource;
import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.adapters.RecipeAdapter;
import com.mahausch.caketime.utils.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

// Activity to show a recyclerView with recipes

public class RecipeOverviewActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recycler_view)
    RecyclerView recycler;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgress;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipe;

    RecyclerView.LayoutManager mManager;
    static Parcelable mListState;
    public static ArrayList<Recipe> mList;

    private RecipeAdapter mAdapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        ButterKnife.bind(this);

        //Decide whether to display recipes in grid or list depending on display size
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (dpWidth >= 600) {
            mManager = new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        } else {
            mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
        recycler.setLayoutManager(mManager);

        mAdapter = new RecipeAdapter(this);
        mAdapter.setRecipesList(null);
        recycler.setAdapter(mAdapter);

        startTaskLoader();

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                startTaskLoader();
                mSwipe.setRefreshing(false);
            }
        });
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

    private LoaderManager.LoaderCallbacks<ArrayList<Recipe>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {

        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<ArrayList<Recipe>>(getBaseContext()) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    mProgress.setVisibility(View.VISIBLE);
                    forceLoad();
                }

                @Nullable
                @Override
                public ArrayList<Recipe> loadInBackground() {

                    try {
                        ArrayList<Recipe> list = JsonUtils.getRecipesFromJson(getApplicationContext());
                        return list;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {

            mProgress.setVisibility(View.INVISIBLE);
            if (data != null) {
                mList = data;
                mAdapter.setRecipesList(mList);
                if (mListState != null) {
                    mManager.onRestoreInstanceState(mListState);
                }
            } else {
                Toast toast = Toast.makeText(RecipeOverviewActivity.this, R.string.error, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

        }
    };

    public void startTaskLoader() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mProgress.setVisibility(View.VISIBLE);

            getSupportLoaderManager().restartLoader(1, null, mLoaderCallbacks);

        } else {
            mProgress.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
