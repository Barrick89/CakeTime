package com.mahausch.caketime.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mahausch.caketime.Ingredient;
import com.mahausch.caketime.R;
import com.mahausch.caketime.Recipe;
import com.mahausch.caketime.RecipeStep;
import com.mahausch.caketime.fragments.IngredientsFragment;
import com.mahausch.caketime.fragments.RecipeDetailFragment;
import com.mahausch.caketime.fragments.StepFragment;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    private Recipe mRecipe;
    public static boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (findViewById(R.id.recipe_step_fragment) == null) {
            mTwoPane = false;
        }

        if (savedInstanceState == null) {

            Bundle intentBundle = getIntent().getExtras();
            mRecipe = intentBundle.getParcelable("recipe");

            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", mRecipe);

            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            detailFragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();

            manager.beginTransaction().add(R.id.recipe_detail_fragment, detailFragment).commit();

            if (mTwoPane) {
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                bundle = new Bundle();
                bundle.putParcelableArrayList("ingredients", mRecipe.getIngredients());
                ingredientsFragment.setArguments(bundle);
                manager.beginTransaction().add(R.id.recipe_step_fragment, ingredientsFragment).commit();
            }
        }

    }

    @Override
    public void onStepSelected(RecipeStep step) {

        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        stepFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();

        int view;
        if (mTwoPane) {
            view = R.id.recipe_step_fragment;
        } else {
            view = R.id.recipe_detail_fragment;
        }

        manager.beginTransaction().replace(view, stepFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onIngredientsSelected(ArrayList<Ingredient> ingredients) {

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", ingredients);
        ingredientsFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();

        int view;
        if (mTwoPane) {
            view = R.id.recipe_step_fragment;
        } else {
            view = R.id.recipe_detail_fragment;
        }

        manager.beginTransaction().replace(view, ingredientsFragment)
                .addToBackStack(null)
                .commit();
    }
}
